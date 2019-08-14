package it.desimone.gsheetsaccess;

import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.common.Configurator.Environment;
import it.desimone.gsheetsaccess.dto.ScorePlayer;
import it.desimone.gsheetsaccess.dto.TorneoPubblicato;
import it.desimone.gsheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gsheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gsheetsaccess.gsheets.dto.PartitaRow;
import it.desimone.gsheetsaccess.gsheets.dto.RankingRow;
import it.desimone.gsheetsaccess.gsheets.dto.RankingRow.ContributoRanking;
import it.desimone.gsheetsaccess.gsheets.dto.SheetRow;
import it.desimone.gsheetsaccess.gsheets.dto.TorneiRow;
import it.desimone.gsheetsaccess.gsheets.facade.GSheetsInterface;
import it.desimone.gsheetsaccess.utils.TorneiUtils;
import it.desimone.risiko.torneo.dto.SchedaTorneo.TipoTorneo;
import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class RankingCalculator {
	
	public static void main(String[] args) {
		MyLogger.setConsoleLogLevel(Level.FINE);
		MyLogger.getLogger().info("START");
		String year = "2019";
		Configurator.loadConfiguration(Environment.PRODUCTION);
		try{
			calculate(year);
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore di accesso a google drive "+e.getMessage());
		}
		MyLogger.getLogger().info("END");
	}
	
	public static List<ScorePlayer> elaboraTabellini(String year, List<TorneoPubblicato> torneiPubblicati){
		List<ScorePlayer> result = null;
		
		if (torneiPubblicati != null && !torneiPubblicati.isEmpty()){
			List<AnagraficaGiocatoreRow> anagraficheGiocatoriRow = TorneiUtils.getAllAnagraficheGiocatori(year);
			result = new ArrayList<ScorePlayer>();
			for (TorneoPubblicato torneoPubblicato: torneiPubblicati){
				Set<Integer> idPartecipanti = torneoPubblicato.getIdPartecipanti();
				for (Integer idPartecipante: idPartecipanti){
					if (idPartecipante <=0){ //Si tolgono anonimi e ghost
						continue;
					}
					ScorePlayer scorePlayer = null;
					AnagraficaGiocatoreRow anagraficaSonda = new AnagraficaGiocatoreRow(idPartecipante);
					ScorePlayer scorePlayerSonda = new ScorePlayer(anagraficaSonda);
					Integer indexOfScorePlayer = result.indexOf(scorePlayerSonda);
					boolean giaPresente = true;
					if (indexOfScorePlayer >=0){
						scorePlayer = result.get(indexOfScorePlayer);
					}else{
						giaPresente = false;
						scorePlayer = scorePlayerSonda;
						int indexAnagrafica = anagraficheGiocatoriRow.indexOf(anagraficaSonda);
						scorePlayer.setAnagraficaGiocatore(anagraficheGiocatoriRow.get(indexAnagrafica));
					}
					TorneiRow torneoRow = torneoPubblicato.getTorneoRow();
					for (PartitaRow partitaRow: torneoPubblicato.getPartite()){
						if (TorneiUtils.haPartecipato(partitaRow, idPartecipante)){
							scorePlayer.addPartiteGiocate();
							if (TorneiUtils.isVincitore(partitaRow, idPartecipante)){
								scorePlayer.addPartiteVinte();
							}
						}
					}
					if (torneoPubblicato.isConcluso()){
						for (ClassificheRow classificheRow: torneoPubblicato.getClassifica()){
							if (classificheRow.getIdGiocatore().equals(idPartecipante)){
								TipoTorneo tipoTorneo = TipoTorneo.parseTipoTorneo(torneoRow.getTipoTorneo());
								BigDecimal scoreRanking = RankingScorer.calcolaScore(classificheRow.getPosizione(), tipoTorneo, torneoRow.getNumeroTavoli(), torneoRow.getNumeroPartecipanti(), torneoRow.getNumeroTurni());
								scorePlayer.addScoreRanking(scoreRanking);
								scorePlayer.addTabellinoPlayer(torneoPubblicato, classificheRow.getPosizione(), scoreRanking);
								scorePlayer.addTabellinoPerTipoTorneo(scoreRanking, tipoTorneo);
								break;
							}
						}
					}
					if (!giaPresente){
						result.add(scorePlayer);
					}
				}
			}
		}

		Collections.sort(result, new Comparator() {
			public int compare(final Object o1, final Object o2) {
				int compare = 0;
				ScorePlayer scorePlayer1 = (ScorePlayer) o1;
				ScorePlayer scorePlayer2 = (ScorePlayer) o2; 
				if (scorePlayer2 != null){
					compare = scorePlayer2.getScoreRanking().compareTo(scorePlayer1.getScoreRanking());
				}
				return compare;
			}
		});
		return result;
	}
	
	public static void calculate(String year) throws IOException{
		//Map<Integer, List<ElementoRanking>> torneiGiocatore = mapGiocatoreVSTornei();
		List<ScoreGiocatore> scoreGiocatori = getScoreGiocatori(year);
		assignScores(scoreGiocatori);
		List<SheetRow> righeRankingOrdinate = calcolaRankingEOrdina(scoreGiocatori, year);
		String spreadSheetIdRanking = Configurator.getRankingSheetId(); 
		
		GSheetsInterface.clearSheet(spreadSheetIdRanking, year);
		GSheetsInterface.appendRows(spreadSheetIdRanking, year, righeRankingOrdinate);
	}
	
	private static void assignScores(List<ScoreGiocatore> scoreGiocatori){
		MyLogger.getLogger().entering("RankingCalculator", "assignScores");
		for (ScoreGiocatore scoreGiocatore: scoreGiocatori){
			List<ElementoRanking> elementiRanking = scoreGiocatore.getElementiRanking();
			for (ElementoRanking elementoRanking: elementiRanking){
				assignScore(elementoRanking);
			}
		}
	}
	
	private static void assignScore(ElementoRanking elementoRanking){
		BigDecimal assignedScore = BigDecimal.ZERO;
		//in teoria si può invece di implementare questo metodo implementae l'algoritmo direttamente nel getScore di ElementoRanking visto che è autoconsistente
		TorneiRow torneoRow = elementoRanking.getTorneo();
		TipoTorneo tipoTorneo = TipoTorneo.parseTipoTorneo(torneoRow.getTipoTorneo());
		if (tipoTorneo != null){
			int numeroTavoli = torneoRow.getNumeroTavoli();
			int numeroTurni = torneoRow.getNumeroTurni();
			int numeroPartecipanti = torneoRow.getNumeroPartecipanti();
			int posizioneNelTorneo = elementoRanking.getPosizione();
			assignedScore = RankingScorer.calcolaScore(posizioneNelTorneo, tipoTorneo, numeroTavoli, numeroPartecipanti, numeroTurni);
		}else{
			MyLogger.getLogger().severe("Impossibile assegnare il ranking per il torneo "+torneoRow.getIdTorneo()+" perchè ha un tipoTorneo sconosciuto: "+torneoRow.getTipoTorneo());
		}
		elementoRanking.setScore(assignedScore);
	}
	
	
	private static List<SheetRow> calcolaRankingEOrdina(List<ScoreGiocatore> scoreGiocatori, String year){
		MyLogger.getLogger().entering("RankingCalculator", "calcolaRankingEOrdina");
		List<AnagraficaGiocatoreRow> anagraficheGiocatoriRow = TorneiUtils.getAllAnagraficheGiocatori(year);
		List<SheetRow> righeRankingOrdinate = new ArrayList<SheetRow>();
		for (ScoreGiocatore scoreGiocatore: scoreGiocatori){
			RankingRow rankingRow = new RankingRow();
			rankingRow.setIdGiocatore(scoreGiocatore.getIdGiocatore());
			AnagraficaGiocatoreRow anagraficaGiocatoreRow = findGiocatoreById(anagraficheGiocatoriRow, scoreGiocatore.getIdGiocatore());
			if (anagraficheGiocatoriRow != null){
				rankingRow.setNominativoGiocatore(anagraficaGiocatoreRow.getNome()+" "+anagraficaGiocatoreRow.getCognome());
			}
			BigDecimal scoreRanking = BigDecimal.ZERO;
			List<ElementoRanking> elementiRanking = scoreGiocatore.getElementiRanking();
			List<ContributoRanking> contributiRanking = new ArrayList<ContributoRanking>();
			for (ElementoRanking elementoRanking: elementiRanking){
				scoreRanking = scoreRanking.add(elementoRanking.getScore());
				ContributoRanking contributoRanking = rankingRow.new ContributoRanking();
				contributoRanking.setIdTorneo(elementoRanking.getTorneo().getIdTorneo());
				contributoRanking.setPuntiRanking(elementoRanking.getScore().doubleValue());
				
				contributiRanking.add(contributoRanking);
			}
			//TODO Eventualmente sortare i contributi ranking per idTorneo (che equivale alla data)
			rankingRow.setContributiRanking(contributiRanking);
			scoreGiocatore.setScoreRanking(scoreRanking);
			rankingRow.setPuntiRanking(scoreGiocatore.getScoreRanking().doubleValue());
			righeRankingOrdinate.add(rankingRow);
		}
		Collections.sort(righeRankingOrdinate, new Comparator() {

			public int compare(Object o1, Object o2) {
				int compare = 0;
				RankingRow rankingRow1 = (RankingRow) o1;
				RankingRow rankingRow2 = (RankingRow) o2; 
				if (rankingRow2 != null){
					compare = rankingRow2.getPuntiRanking().compareTo(rankingRow1.getPuntiRanking());
				}
				return compare;
			}
		});
		int position = 0;
		for (SheetRow row: righeRankingOrdinate){
			RankingRow rankingRow = (RankingRow) row;
			rankingRow.setPosizioneRanking(++position);
		}
		return righeRankingOrdinate;
	}
	
	private static AnagraficaGiocatoreRow findGiocatoreById(List<AnagraficaGiocatoreRow> anagraficheGiocatoriRow, Integer id){
		AnagraficaGiocatoreRow result = null;
		AnagraficaGiocatoreRow sonda = new AnagraficaGiocatoreRow();
		sonda.setId(id);
		Integer index = anagraficheGiocatoriRow.indexOf(sonda);
		if (index >= 0){
			result = anagraficheGiocatoriRow.get(index);
		}
		return result;
	}
	
	private static Map<Integer, List<ElementoRanking>> mapGiocatoreVSTornei(){
		
		Map<Integer, List<ElementoRanking>> torneiPerGiocatore = new HashMap<Integer, List<ElementoRanking>>();
		String year = "2019";
		List<TorneiRow> tornei = TorneiUtils.getAllTornei(year);
		List<ClassificheRow> classifiche = TorneiUtils.getAllClassifiche(year);
		
		for (ClassificheRow classificaRow: classifiche){
			Integer idGiocatore = classificaRow.getIdGiocatore();
			String idTorneo = classificaRow.getIdTorneo();
			Integer posizione = classificaRow.getPosizione();
			Integer torneoIndex = tornei.indexOf(idTorneo);
			if (torneoIndex >=0){
				TorneiRow torneo = tornei.get(torneoIndex);
				List<ElementoRanking> torneiGiocatore = torneiPerGiocatore.get(idGiocatore);
				if (torneiGiocatore == null){
					torneiGiocatore = new ArrayList<ElementoRanking>();
				}
				ElementoRanking elementoRanking = new ElementoRanking();
				elementoRanking.setTorneo(torneo);
				elementoRanking.setPosizione(posizione);
				torneiGiocatore.add(elementoRanking);
				torneiPerGiocatore.put(idGiocatore, torneiGiocatore);
			}
		}
		return torneiPerGiocatore;
	}
	
	private static List<ScoreGiocatore> getScoreGiocatori(String year){
		
		MyLogger.getLogger().entering("RankingCalculator", "getScoreGiocatori");
		
		List<ScoreGiocatore> torneiPerGiocatore = new ArrayList<ScoreGiocatore>();
		List<TorneiRow> tornei = TorneiUtils.getAllTornei(year);
		List<ClassificheRow> classifiche = TorneiUtils.getAllClassifiche(year);
		
		TorneiRow torneoRowSonda = new TorneiRow();
		for (ClassificheRow classificaRow: classifiche){
			Integer idGiocatore = classificaRow.getIdGiocatore();
			String idTorneo = classificaRow.getIdTorneo();
			Integer posizione = classificaRow.getPosizione();
			torneoRowSonda.setIdTorneo(idTorneo);
			Integer torneoIndex = tornei.indexOf(torneoRowSonda);
			if (torneoIndex >=0){
				TorneiRow torneo = tornei.get(torneoIndex);
				ElementoRanking elementoRanking = new ElementoRanking();
				elementoRanking.setTorneo(torneo);
				elementoRanking.setPosizione(posizione);
				ScoreGiocatore scoreGiocatore = new ScoreGiocatore(idGiocatore);
				int indexGiocatore = torneiPerGiocatore.indexOf(scoreGiocatore);
				if (indexGiocatore > 0){
					scoreGiocatore = torneiPerGiocatore.get(indexGiocatore);
				}else{
					torneiPerGiocatore.add(scoreGiocatore);
				}
				scoreGiocatore.addElementoRanking(elementoRanking);
			}
		}
		ScoreGiocatore scoreAnonimo = new ScoreGiocatore(0);
		torneiPerGiocatore.remove(scoreAnonimo);
		return torneiPerGiocatore;
	}

}
