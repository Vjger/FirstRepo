package it.desimone.gsheetsaccess;

import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gsheetsaccess.gsheets.dto.RankingRow;
import it.desimone.gsheetsaccess.gsheets.dto.SheetRow;
import it.desimone.gsheetsaccess.gsheets.dto.TorneiRow;
import it.desimone.gsheetsaccess.gsheets.dto.RankingRow.ContributoRanking;
import it.desimone.gsheetsaccess.gsheets.dto.SheetRowFactory.SheetRowType;
import it.desimone.gsheetsaccess.gsheets.facade.GSheetsInterface;
import it.desimone.risiko.torneo.dto.SchedaTorneo.TipoTorneo;
import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class RankingCalculator {
	
	public static void main(String[] args) {
		MyLogger.setConsoleLogLevel(Level.ALL);
		try{
			calculate();
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore di accesso a google drive "+e.getMessage());
		}
	}
	
	public static void calculate() throws IOException{
		//Map<Integer, List<ElementoRanking>> torneiGiocatore = mapGiocatoreVSTornei();
		List<ScoreGiocatore> scoreGiocatori = getScoreGiocatori();
		assignScores(scoreGiocatori);
		List<SheetRow> righeRankingOrdinate = calcolaRankingEOrdina(scoreGiocatori);
		String spreadSheetIdRanking = Configurator.getRankingSheetId(); 
		
		GSheetsInterface.clearSheet(spreadSheetIdRanking, RankingRow.SHEET_NAME);
		GSheetsInterface.appendRows(spreadSheetIdRanking, RankingRow.SHEET_NAME, righeRankingOrdinate);
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
		Double assignedScore = 0D;
		//in teoria si può invece di implementare questo metodo implementae l'algoritmo direttamente nel getScore di ElementoRanking visto che è autoconsistente
		TorneiRow torneoRow = elementoRanking.getTorneo();
		TipoTorneo tipoTorneo = TipoTorneo.parseTipoTorneo(torneoRow.getTipoTorneo());
		if (tipoTorneo != null){
			int numeroTavoli = torneoRow.getNumeroTavoli();
			int numeroPartecipanti = torneoRow.getNumeroPartecipanti();
			int posizioneNelTorneo = elementoRanking.getPosizione();
			assignedScore = calcolaScore(posizioneNelTorneo, tipoTorneo, numeroTavoli, numeroPartecipanti);
		}else{
			MyLogger.getLogger().severe("Impossibile assegnare il ranking per il torneo "+torneoRow.getIdTorneo()+" perchè ha un tipoTorneo sconosciuto: "+torneoRow.getTipoTorneo());
		}
		elementoRanking.setScore(assignedScore);
	}
	
	private static Double calcolaScore(int posizioneNelTorneo, TipoTorneo tipoTorneo, int numeroTavoli, int numeroPartecipanti){
		Double score = 0D;
		Integer VT = 100 + (3 * numeroTavoli);
		Integer scoreVT = VT - posizioneNelTorneo; //Questo sicuramente dovrà cambiare
		
		switch (tipoTorneo) {
		case RADUNO_NAZIONALE:
			score = scoreVT * 50D;
			break;
		case MASTER:
			score = scoreVT * 30D;
			break;
		case OPEN:
			score = scoreVT * 15D;
			break;
		case CAMPIONATO:
			score = scoreVT * 5D;
			break;
		default:
			break;
		}
		
		score += numeroPartecipanti/20;
		
		return score;
	}
	
	private static List<SheetRow> calcolaRankingEOrdina(List<ScoreGiocatore> scoreGiocatori){
		MyLogger.getLogger().entering("RankingCalculator", "calcolaRankingEOrdina");
		List<SheetRow> righeRankingOrdinate = new ArrayList<SheetRow>();
		for (ScoreGiocatore scoreGiocatore: scoreGiocatori){
			RankingRow rankingRow = new RankingRow();
			rankingRow.setIdGiocatore(scoreGiocatore.getIdGiocatore());
			Double scoreRanking = 0D;
			List<ElementoRanking> elementiRanking = scoreGiocatore.getElementiRanking();
			List<ContributoRanking> contributiRanking = new ArrayList<ContributoRanking>();
			for (ElementoRanking elementoRanking: elementiRanking){
				scoreRanking += elementoRanking.getScore();
				ContributoRanking contributoRanking = rankingRow.new ContributoRanking();
				contributoRanking.setIdTorneo(elementoRanking.getTorneo().getIdTorneo());
				contributoRanking.setPuntiRanking(elementoRanking.getScore());
				
				contributiRanking.add(contributoRanking);
			}
			//TODO Eventualmente sortare i contributi ranking per idTorneo (che equivale alla data)
			rankingRow.setContributiRanking(contributiRanking);
			scoreGiocatore.setScoreRanking(scoreRanking);
			rankingRow.setPuntiRanking(scoreGiocatore.getScoreRanking());
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
	
	private static Map<Integer, List<ElementoRanking>> mapGiocatoreVSTornei() throws IOException{
		
		Map<Integer, List<ElementoRanking>> torneiPerGiocatore = new HashMap<Integer, List<ElementoRanking>>();
		String year = "2019";
		List<TorneiRow> tornei = getAllTornei(year);
		List<ClassificheRow> classifiche = getAllClassifiche(year);
		
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
	
	private static List<ScoreGiocatore> getScoreGiocatori() throws IOException{
		
		MyLogger.getLogger().entering("RankingCalculator", "getScoreGiocatori");
		
		List<ScoreGiocatore> torneiPerGiocatore = new ArrayList<ScoreGiocatore>();
		String year = "2019";
		List<TorneiRow> tornei = getAllTornei(year);
		List<ClassificheRow> classifiche = getAllClassifiche(year);
		
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
		return torneiPerGiocatore;
	}
	
	private static List<ClassificheRow> getAllClassifiche(String year) throws IOException{
		String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
		List<ClassificheRow> result = GSheetsInterface.getAllRows(spreadSheetIdTornei, SheetRowType.Classifica);	
		return result;
	}
	
	private static List<TorneiRow> getAllTornei(String year) throws IOException{
		String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
		List<TorneiRow> result = GSheetsInterface.getAllRows(spreadSheetIdTornei, SheetRowType.Torneo);	
		return result;
	}
}
