package it.desimone.gsheetsaccess.ranking;

import it.desimone.gsheetsaccess.dto.ScorePlayer;
import it.desimone.gsheetsaccess.dto.ScorePlayer.TabellinoPerTipoTorneo;
import it.desimone.gsheetsaccess.dto.ScorePlayer.TabellinoPlayer;
import it.desimone.gsheetsaccess.dto.ScorePlayer.TabellinoPerTipoTorneo.DatiTabellinoPerTipoTorneo;
import it.desimone.gsheetsaccess.dto.TorneoPubblicato;
import it.desimone.gsheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gsheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gsheetsaccess.gsheets.dto.PartitaRow;
import it.desimone.gsheetsaccess.gsheets.dto.TorneiRow;
import it.desimone.gsheetsaccess.ranking.RankingThresholds.Thresholds;
import it.desimone.gsheetsaccess.utils.TorneiUtils;
import it.desimone.risiko.torneo.dto.SchedaTorneo.TipoTorneo;
import it.desimone.utils.MyLogger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RankingCalculator {
	
//	public static void main(String[] args) {
//		MyLogger.setConsoleLogLevel(Level.FINE);
//		MyLogger.getLogger().info("START");
//		String year = "2019";
//		Configurator.loadConfiguration(Environment.PRODUCTION);
//		try{
//			calculate(year);
//		}catch(Exception e){
//			MyLogger.getLogger().severe("Errore di accesso a google drive "+e.getMessage());
//		}
//		MyLogger.getLogger().info("END");
//	}

	public static RankingData elaboraRanking(String year, List<TorneoPubblicato> torneiPubblicati, List<Integer> whiteList){
		List<ScorePlayer> tabellini = elaboraTabellini(year, torneiPubblicati, whiteList);
		//RankingData rankingData = filtraTabellini(year, torneiPubblicati, tabellini);
		RankingData rankingData = filtraTabelliniNew(year, torneiPubblicati, tabellini);
		
		return rankingData;
	}
	
	public static List<ScorePlayer> elaboraTabellini(String year, List<TorneoPubblicato> torneiPubblicati, List<Integer> whiteList){
		List<ScorePlayer> result = null;
		
		if (torneiPubblicati != null && !torneiPubblicati.isEmpty()){
			List<AnagraficaGiocatoreRow> anagraficheGiocatoriRow = TorneiUtils.getAllAnagraficheGiocatori(year);
			result = new ArrayList<ScorePlayer>();
			for (TorneoPubblicato torneoPubblicato: torneiPubblicati){
				Set<Integer> idPartecipanti = torneoPubblicato.getIdPartecipanti();
				for (Integer idPartecipante: idPartecipanti){
					if (idPartecipante <=0 || (whiteList != null && !whiteList.isEmpty() && !whiteList.contains(idPartecipante))){ //Si tolgono anonimi e ghost
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
						try{
							scorePlayer.setAnagraficaGiocatore(anagraficheGiocatoriRow.get(indexAnagrafica));
						}catch(RuntimeException e){
							MyLogger.getLogger().severe("Problemi con la ricerca in anagrafica del giocatore con id "+idPartecipante+" nel torneo "+torneoPubblicato.getIdTorneo());
							throw e;
						}
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
						if (torneoPubblicato.getClassifica() != null && !torneoPubblicato.getClassifica().isEmpty()){
							for (ClassificheRow classificheRow: torneoPubblicato.getClassifica()){
								if (classificheRow.getIdGiocatore().equals(idPartecipante)){
									TipoTorneo tipoTorneo = TipoTorneo.parseTipoTorneo(torneoRow.getTipoTorneo());
									BigDecimal scoreRanking = RankingScorer.calcolaScore(year, classificheRow.getPosizione(), tipoTorneo, torneoRow.getNumeroTavoli(), torneoRow.getNumeroPartecipanti(), torneoRow.getNumeroTurni());
									scorePlayer.addScoreRanking(scoreRanking);
									scorePlayer.addTabellinoPlayer(torneoPubblicato, classificheRow.getPosizione(), scoreRanking);
									//scorePlayer.addTabellinoPerTipoTorneo(scoreRanking, tipoTorneo);
									scorePlayer.addTabellinoPerTipoTorneo(scoreRanking, tipoTorneo, classificheRow.getPartiteGiocate(), torneoRow.getNumeroTurni());
									break;
								}
							}
						}else{
							//Ha partecipato ma non c'è classifica
							TipoTorneo tipoTorneo = TipoTorneo.parseTipoTorneo(torneoRow.getTipoTorneo());
							scorePlayer.addTabellinoPlayer(torneoPubblicato, null, BigDecimal.ZERO);
							scorePlayer.addTabellinoPerTipoTorneo(BigDecimal.ZERO, tipoTorneo);
							//scorePlayer.addTabellinoPerTipoTorneo(BigDecimal.ZERO, tipoTorneo, classificheRow.getPartiteGiocate(), torneoRow.getNumeroTurni());
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
	
	private static RankingData filtraTabellini(String year, List<TorneoPubblicato> torneiPubblicati, List<ScorePlayer> tabellini){
		RankingData rankingData = new RankingData();
		//Calcolare le soglie
		RankingThresholds rankingThresholds = RankingBuilder.getRankingThreshold(year);
		MyLogger.getLogger().info("Soglie ranking per l'anno "+year+": "+rankingThresholds);
		
		Set<TipoTorneo> managedTournamentsType = rankingThresholds.getManagedTournamentsType();
		Map<TipoTorneo, Integer> mappaConteggiTipoTorneo = new HashMap<TipoTorneo, Integer>();
		for (TipoTorneo tipoTorneo: managedTournamentsType){
			mappaConteggiTipoTorneo.put(tipoTorneo, 0);
		}
		for (TorneoPubblicato torneoPubblicato: torneiPubblicati){
			if (torneoPubblicato.isConcluso() && torneoPubblicato.getClassifica() != null && !torneoPubblicato.getClassifica().isEmpty()){
				TipoTorneo tipoTorneo = TipoTorneo.parseTipoTorneo(torneoPubblicato.getTorneoRow().getTipoTorneo());
				if (mappaConteggiTipoTorneo.containsKey(tipoTorneo) && RankingScorer.hasMinimuNumberTables(year, tipoTorneo, torneoPubblicato.getTorneoRow().getNumeroTavoli())){
					Integer counter = mappaConteggiTipoTorneo.get(tipoTorneo);
					mappaConteggiTipoTorneo.put(tipoTorneo, ++counter);
				}
			}
		}
		MyLogger.getLogger().info("Contatori Tornei valevoli per Ranking: "+mappaConteggiTipoTorneo);
		Map<TipoTorneo, Integer> mappaSoglieTipoTorneo = new HashMap<TipoTorneo, Integer>();
		for (TipoTorneo tipoTorneo: managedTournamentsType){
			Thresholds threshold = rankingThresholds.getThresholds(tipoTorneo);
			Integer counter = mappaConteggiTipoTorneo.get(tipoTorneo);
			Integer numeroTorneiMinimo = threshold.getMinTournaments();
			BigDecimal percentualeMassimaTornei = threshold.getMaxPercentage();
			BigDecimal numeroTorneiMassimoPerPercentuale = new BigDecimal(counter).multiply(percentualeMassimaTornei).divide(new BigDecimal(100)); 
			Integer numeroTorneiMassimo = numeroTorneiMassimoPerPercentuale.toBigInteger().intValue();
			mappaSoglieTipoTorneo.put(tipoTorneo, Math.max(numeroTorneiMinimo, numeroTorneiMassimo));
		}
		MyLogger.getLogger().info("Soglie ranking per l'anno "+year+" in base ai tornei disputati: "+mappaSoglieTipoTorneo);
		
		//Ricalcolare i tabellini in base alle soglie
		
		for (ScorePlayer tabellino: tabellini){
			BigDecimal ranking = BigDecimal.ZERO;
			for (TipoTorneo tipoTorneo: managedTournamentsType){
				TabellinoPerTipoTorneo tabellinoPerTipoTorneo = tabellino.getTabellino(tipoTorneo);
				List<BigDecimal> scoreRankings = tabellinoPerTipoTorneo.getScoreRankings();
				Collections.sort(scoreRankings);
				Collections.reverse(scoreRankings);
				Integer soglia = mappaSoglieTipoTorneo.get(tipoTorneo);
				BigDecimal rankingPerTipoTorneo = BigDecimal.ZERO;
				for (int index = 1; index <= Math.min(soglia, scoreRankings.size()); index++){
					rankingPerTipoTorneo = rankingPerTipoTorneo.add(scoreRankings.get(index -1));
				}
				tabellinoPerTipoTorneo.setScoreRanking(rankingPerTipoTorneo);
				ranking = ranking.add(rankingPerTipoTorneo);
			}
			tabellino.setScoreRanking(ranking);
		}
		
		//Riordinare i tabellini
		Collections.sort(tabellini, new Comparator() {
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
		rankingData.setMappaConteggiTipoTorneo(mappaConteggiTipoTorneo);
		rankingData.setMappaSoglieTipoTorneo(mappaSoglieTipoTorneo);
		rankingData.setTabellini(tabellini);
		return rankingData;
	}
	
	private static RankingData filtraTabelliniNew(String year, List<TorneoPubblicato> torneiPubblicati, List<ScorePlayer> tabellini){
		RankingData rankingData = new RankingData();
		//Calcolare le soglie
		RankingThresholds rankingThresholds = RankingBuilder.getRankingThreshold(year);
		MyLogger.getLogger().info("Soglie ranking per l'anno "+year+": "+rankingThresholds);
		
		Set<TipoTorneo> managedTournamentsType = rankingThresholds.getManagedTournamentsType();

		ConteggiTornei conteggioTornei = new ConteggiTornei();
		for (TorneoPubblicato torneoPubblicato: torneiPubblicati){
			if (torneoPubblicato.isConcluso() && torneoPubblicato.getClassifica() != null && !torneoPubblicato.getClassifica().isEmpty()){
				TipoTorneo tipoTorneo = TipoTorneo.parseTipoTorneo(torneoPubblicato.getTorneoRow().getTipoTorneo());
				if (managedTournamentsType.contains(tipoTorneo) && RankingScorer.hasMinimuNumberTables(year, tipoTorneo, torneoPubblicato.getTorneoRow().getNumeroTavoli())){
					conteggioTornei.addOneToTorneiDisputati(tipoTorneo);
					conteggioTornei.addToTurniDisputati(tipoTorneo, torneoPubblicato.getTorneoRow().getNumeroTurni());
				}
			}
		}
		MyLogger.getLogger().info("Contatori Tornei valevoli per Ranking: "+conteggioTornei);
		Map<TipoTorneo, Integer> mappaSoglieTipoTorneo = new HashMap<TipoTorneo, Integer>();
		for (TipoTorneo tipoTorneo: managedTournamentsType){
			Thresholds threshold = rankingThresholds.getThresholds(tipoTorneo);
			Integer torneiDisputati = conteggioTornei.getNumeroTorneiDisputati(tipoTorneo);
			Integer turniDisputati = conteggioTornei.getNumeroTurniDisputati(tipoTorneo);
			Integer numeroTorneiMinimo = threshold.getMinTournaments();
			BigDecimal percentualeMassimaTornei = threshold.getMaxPercentage();
			BigDecimal numeroTorneiMassimoPerPercentuale = null;
			if (tipoTorneo == TipoTorneo.CAMPIONATO){//TODO correggere aggiungendo un altro parametro nel xml in modo da diversificare gli anni la mappa deve divengtare un oggetto composto a più parametri con un attributo ch indichi il parametro da usare per la soglia in base al tipo torneo
				//numeroTorneiMassimoPerPercentuale = new BigDecimal(turniDisputati).multiply(percentualeMassimaTornei).divide(new BigDecimal(100));
				numeroTorneiMassimoPerPercentuale = percentualeMassimaTornei;
			}else{
				numeroTorneiMassimoPerPercentuale = new BigDecimal(torneiDisputati).multiply(percentualeMassimaTornei).divide(new BigDecimal(100));
			}
			Integer numeroTorneiMassimo = numeroTorneiMassimoPerPercentuale.toBigInteger().intValue();
			mappaSoglieTipoTorneo.put(tipoTorneo, Math.max(numeroTorneiMinimo, numeroTorneiMassimo));
		}
		MyLogger.getLogger().info("Soglie ranking per l'anno "+year+" in base ai tornei disputati: "+mappaSoglieTipoTorneo);
		
		//Ricalcolare i tabellini in base alle soglie
		
		for (ScorePlayer tabellino: tabellini){
			BigDecimal ranking = BigDecimal.ZERO;
			for (TipoTorneo tipoTorneo: managedTournamentsType){
				TabellinoPerTipoTorneo tabellinoPerTipoTorneo = tabellino.getTabellino(tipoTorneo);
				List<DatiTabellinoPerTipoTorneo> datiTabellinoPerTipoTorneo = tabellinoPerTipoTorneo.getDatiTabellinoPerTipoTorneo();
				Collections.sort(datiTabellinoPerTipoTorneo);
				Collections.reverse(datiTabellinoPerTipoTorneo);
				Integer soglia = mappaSoglieTipoTorneo.get(tipoTorneo);
				BigDecimal rankingPerTipoTorneo = BigDecimal.ZERO;
				Integer torneiValevoliPerRanking = 0;
				if (tipoTorneo != TipoTorneo.CAMPIONATO){
					for (int index = 1; index <= Math.min(soglia, datiTabellinoPerTipoTorneo.size()); index++){
						rankingPerTipoTorneo = rankingPerTipoTorneo.add(datiTabellinoPerTipoTorneo.get(index -1).getScoreRanking());
						torneiValevoliPerRanking++;
					}
				}else{
					int roundCounter = 0;
					
					Iterator<DatiTabellinoPerTipoTorneo> iterator = datiTabellinoPerTipoTorneo.iterator();
					while(iterator.hasNext() && roundCounter < soglia){
						DatiTabellinoPerTipoTorneo datoTabellinoPerTipoTorneo = iterator.next();
						roundCounter += datoTabellinoPerTipoTorneo.getNumeroTurniTornei(); 
						//Modificare qui se invece di contare i turni dei tornei si vogliono contare i turni giocati
						//roundCounter += datoTabellinoPerTipoTorneo.getPartiteGiocate();
						rankingPerTipoTorneo = rankingPerTipoTorneo.add(datoTabellinoPerTipoTorneo.getScoreRanking());
						torneiValevoliPerRanking++;
					}
				}
				tabellinoPerTipoTorneo.setScoreRanking(rankingPerTipoTorneo);
				tabellinoPerTipoTorneo.setTorneiValevoliPerRanking(torneiValevoliPerRanking);
				ranking = ranking.add(rankingPerTipoTorneo);
			}
			tabellino.setScoreRanking(ranking);
		}
		
		//Riordinare i tabellini
		Collections.sort(tabellini, new Comparator() {
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
		
		Map<TipoTorneo, Integer> mappaConteggiTipoTorneo = new HashMap<TipoTorneo, Integer>();
		for (TipoTorneo tipoTorneo: managedTournamentsType){
			mappaConteggiTipoTorneo.put(tipoTorneo, 0);
		}
		for (TorneoPubblicato torneoPubblicato: torneiPubblicati){
			if (torneoPubblicato.isConcluso() && torneoPubblicato.getClassifica() != null && !torneoPubblicato.getClassifica().isEmpty()){
				TipoTorneo tipoTorneo = TipoTorneo.parseTipoTorneo(torneoPubblicato.getTorneoRow().getTipoTorneo());
				if (mappaConteggiTipoTorneo.containsKey(tipoTorneo) && RankingScorer.hasMinimuNumberTables(year, tipoTorneo, torneoPubblicato.getTorneoRow().getNumeroTavoli())){
					Integer counter = mappaConteggiTipoTorneo.get(tipoTorneo);
					mappaConteggiTipoTorneo.put(tipoTorneo, ++counter);
				}
			}
		}
		
		rankingData.setMappaConteggiTipoTorneo(mappaConteggiTipoTorneo);
		rankingData.setMappaSoglieTipoTorneo(mappaSoglieTipoTorneo);
		rankingData.setTabellini(tabellini);
		return rankingData;
	}
	
//	public static void calculate(String year) throws IOException{
//		//Map<Integer, List<ElementoRanking>> torneiGiocatore = mapGiocatoreVSTornei();
//		List<ScoreGiocatore> scoreGiocatori = getScoreGiocatori(year);
//		assignScores(scoreGiocatori);
//		List<SheetRow> righeRankingOrdinate = calcolaRankingEOrdina(scoreGiocatori, year);
//		String spreadSheetIdRanking = Configurator.getRankingSheetId(); 
//		
//		GSheetsInterface.clearSheet(spreadSheetIdRanking, year);
//		GSheetsInterface.appendRows(spreadSheetIdRanking, year, righeRankingOrdinate);
//	}
//	
//	private static void assignScores(List<ScoreGiocatore> scoreGiocatori){
//		MyLogger.getLogger().entering("RankingCalculator", "assignScores");
//		for (ScoreGiocatore scoreGiocatore: scoreGiocatori){
//			List<ElementoRanking> elementiRanking = scoreGiocatore.getElementiRanking();
//			for (ElementoRanking elementoRanking: elementiRanking){
//				assignScore(elementoRanking);
//			}
//		}
//	}
//	
//	private static void assignScore(ElementoRanking elementoRanking){
//		BigDecimal assignedScore = BigDecimal.ZERO;
//		//in teoria si può invece di implementare questo metodo implementae l'algoritmo direttamente nel getScore di ElementoRanking visto che è autoconsistente
//		TorneiRow torneoRow = elementoRanking.getTorneo();
//		TipoTorneo tipoTorneo = TipoTorneo.parseTipoTorneo(torneoRow.getTipoTorneo());
//		if (tipoTorneo != null){
//			int numeroTavoli = torneoRow.getNumeroTavoli();
//			int numeroTurni = torneoRow.getNumeroTurni();
//			int numeroPartecipanti = torneoRow.getNumeroPartecipanti();
//			int posizioneNelTorneo = elementoRanking.getPosizione();
//			assignedScore = RankingScorer.calcolaScore(posizioneNelTorneo, tipoTorneo, numeroTavoli, numeroPartecipanti, numeroTurni);
//		}else{
//			MyLogger.getLogger().severe("Impossibile assegnare il ranking per il torneo "+torneoRow.getIdTorneo()+" perchè ha un tipoTorneo sconosciuto: "+torneoRow.getTipoTorneo());
//		}
//		elementoRanking.setScore(assignedScore);
//	}
//	
//	
//	private static List<SheetRow> calcolaRankingEOrdina(List<ScoreGiocatore> scoreGiocatori, String year){
//		MyLogger.getLogger().entering("RankingCalculator", "calcolaRankingEOrdina");
//		List<AnagraficaGiocatoreRow> anagraficheGiocatoriRow = TorneiUtils.getAllAnagraficheGiocatori(year);
//		List<SheetRow> righeRankingOrdinate = new ArrayList<SheetRow>();
//		for (ScoreGiocatore scoreGiocatore: scoreGiocatori){
//			RankingRow rankingRow = new RankingRow();
//			rankingRow.setIdGiocatore(scoreGiocatore.getIdGiocatore());
//			AnagraficaGiocatoreRow anagraficaGiocatoreRow = findGiocatoreById(anagraficheGiocatoriRow, scoreGiocatore.getIdGiocatore());
//			if (anagraficheGiocatoriRow != null){
//				rankingRow.setNominativoGiocatore(anagraficaGiocatoreRow.getNome()+" "+anagraficaGiocatoreRow.getCognome());
//			}
//			BigDecimal scoreRanking = BigDecimal.ZERO;
//			List<ElementoRanking> elementiRanking = scoreGiocatore.getElementiRanking();
//			List<ContributoRanking> contributiRanking = new ArrayList<ContributoRanking>();
//			for (ElementoRanking elementoRanking: elementiRanking){
//				scoreRanking = scoreRanking.add(elementoRanking.getScore());
//				ContributoRanking contributoRanking = rankingRow.new ContributoRanking();
//				contributoRanking.setIdTorneo(elementoRanking.getTorneo().getIdTorneo());
//				contributoRanking.setPuntiRanking(elementoRanking.getScore().doubleValue());
//				
//				contributiRanking.add(contributoRanking);
//			}
//			//TODO Eventualmente sortare i contributi ranking per idTorneo (che equivale alla data)
//			rankingRow.setContributiRanking(contributiRanking);
//			scoreGiocatore.setScoreRanking(scoreRanking);
//			rankingRow.setPuntiRanking(scoreGiocatore.getScoreRanking().doubleValue());
//			righeRankingOrdinate.add(rankingRow);
//		}
//		Collections.sort(righeRankingOrdinate, new Comparator() {
//
//			public int compare(Object o1, Object o2) {
//				int compare = 0;
//				RankingRow rankingRow1 = (RankingRow) o1;
//				RankingRow rankingRow2 = (RankingRow) o2; 
//				if (rankingRow2 != null){
//					compare = rankingRow2.getPuntiRanking().compareTo(rankingRow1.getPuntiRanking());
//				}
//				return compare;
//			}
//		});
//		int position = 0;
//		for (SheetRow row: righeRankingOrdinate){
//			RankingRow rankingRow = (RankingRow) row;
//			rankingRow.setPosizioneRanking(++position);
//		}
//		return righeRankingOrdinate;
//	}
//	
//	private static AnagraficaGiocatoreRow findGiocatoreById(List<AnagraficaGiocatoreRow> anagraficheGiocatoriRow, Integer id){
//		AnagraficaGiocatoreRow result = null;
//		AnagraficaGiocatoreRow sonda = new AnagraficaGiocatoreRow();
//		sonda.setId(id);
//		Integer index = anagraficheGiocatoriRow.indexOf(sonda);
//		if (index >= 0){
//			result = anagraficheGiocatoriRow.get(index);
//		}
//		return result;
//	}
//	
//	private static Map<Integer, List<ElementoRanking>> mapGiocatoreVSTornei(){
//		
//		Map<Integer, List<ElementoRanking>> torneiPerGiocatore = new HashMap<Integer, List<ElementoRanking>>();
//		String year = "2019";
//		List<TorneiRow> tornei = TorneiUtils.getAllTornei(year);
//		List<ClassificheRow> classifiche = TorneiUtils.getAllClassifiche(year);
//		
//		for (ClassificheRow classificaRow: classifiche){
//			Integer idGiocatore = classificaRow.getIdGiocatore();
//			String idTorneo = classificaRow.getIdTorneo();
//			Integer posizione = classificaRow.getPosizione();
//			Integer torneoIndex = tornei.indexOf(idTorneo);
//			if (torneoIndex >=0){
//				TorneiRow torneo = tornei.get(torneoIndex);
//				List<ElementoRanking> torneiGiocatore = torneiPerGiocatore.get(idGiocatore);
//				if (torneiGiocatore == null){
//					torneiGiocatore = new ArrayList<ElementoRanking>();
//				}
//				ElementoRanking elementoRanking = new ElementoRanking();
//				elementoRanking.setTorneo(torneo);
//				elementoRanking.setPosizione(posizione);
//				torneiGiocatore.add(elementoRanking);
//				torneiPerGiocatore.put(idGiocatore, torneiGiocatore);
//			}
//		}
//		return torneiPerGiocatore;
//	}
//	
//	private static List<ScoreGiocatore> getScoreGiocatori(String year){
//		
//		MyLogger.getLogger().entering("RankingCalculator", "getScoreGiocatori");
//		
//		List<ScoreGiocatore> torneiPerGiocatore = new ArrayList<ScoreGiocatore>();
//		List<TorneiRow> tornei = TorneiUtils.getAllTornei(year);
//		List<ClassificheRow> classifiche = TorneiUtils.getAllClassifiche(year);
//		
//		TorneiRow torneoRowSonda = new TorneiRow();
//		for (ClassificheRow classificaRow: classifiche){
//			Integer idGiocatore = classificaRow.getIdGiocatore();
//			String idTorneo = classificaRow.getIdTorneo();
//			Integer posizione = classificaRow.getPosizione();
//			torneoRowSonda.setIdTorneo(idTorneo);
//			Integer torneoIndex = tornei.indexOf(torneoRowSonda);
//			if (torneoIndex >=0){
//				TorneiRow torneo = tornei.get(torneoIndex);
//				ElementoRanking elementoRanking = new ElementoRanking();
//				elementoRanking.setTorneo(torneo);
//				elementoRanking.setPosizione(posizione);
//				ScoreGiocatore scoreGiocatore = new ScoreGiocatore(idGiocatore);
//				int indexGiocatore = torneiPerGiocatore.indexOf(scoreGiocatore);
//				if (indexGiocatore > 0){
//					scoreGiocatore = torneiPerGiocatore.get(indexGiocatore);
//				}else{
//					torneiPerGiocatore.add(scoreGiocatore);
//				}
//				scoreGiocatore.addElementoRanking(elementoRanking);
//			}
//		}
//		ScoreGiocatore scoreAnonimo = new ScoreGiocatore(0);
//		torneiPerGiocatore.remove(scoreAnonimo);
//		return torneiPerGiocatore;
//	}

}
