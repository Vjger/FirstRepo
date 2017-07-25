package it.desimone.batch;

import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.risiko.torneo.dto.StoricoRanking;
import it.desimone.utils.MyLogger;
import it.desimone.utils.RankingCalculator;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RankingAnalyzer {

	private List<GiocatoreDTO> elencoGiocatoriInRanking = new ArrayList<GiocatoreDTO>();
	
	public List<Partita> calcolaRankingGiocatoriTorneo(File file){
		//Ipotesi alla base: la ranking di un giocatore che fa più di una partita nel torneo viene calcolata per la partita N+1 in base
		//al valore della ranking ottenuto subito dopo la partita N
		MyLogger.getLogger().finer("Elaborazione del file "+file.getName());
		List<Partita> partite = FileAnalyzer.estraiPartiteDaFile(file);
		MyLogger.getLogger().finer("Trovate "+partite.size()+" partite");
		for (Partita partita: partite){
			calcolaRankingGiocatoriPartita(partita, file.getName());
		}
		return partite;
	}
	
	private void calcolaRankingGiocatoriPartita(Partita partita, String fileName){
		Set<GiocatoreDTO> giocatori = partita.getGiocatori(); 
		for (GiocatoreDTO giocatore: giocatori){
			//Cerco il giocatore nell'elenco ranking per determinare la sua ranking attuale
			int indexGiocatore = elencoGiocatoriInRanking.indexOf(giocatore);
			BigDecimal rankingIniziale;
			if (indexGiocatore != -1){
				GiocatoreDTO giocatoreGiaInRanking = elencoGiocatoriInRanking.get(indexGiocatore);
				rankingIniziale = giocatoreGiaInRanking.getRanking();
			}else{
				rankingIniziale = RankingCalculator.RANKING_INIZIALE;
			}
			giocatore.setRanking(rankingIniziale);
		}
		for (GiocatoreDTO giocatore: giocatori){
			//Creo l'elenco dei giocatori avversari per il calcolo della nuova Ranking
			List<GiocatoreDTO> giocatoriAvversari = new ArrayList<GiocatoreDTO>(giocatori);
			giocatoriAvversari.remove(giocatore);
			List<GiocatoreDTO> giocatoriSuCuiConfrontareRanking = new ArrayList<GiocatoreDTO>(giocatoriAvversari);
			boolean isVincitore = partita.isVincitore(giocatore);
			if (isVincitore){//devo verificare se è vincitore da solo ed in caso contrario togliere gli altri vincitori dagli avversari 
								//per il calcolo della ranking
				if (partita.numeroVincitori() > 1){
					for (GiocatoreDTO giocatoreAvversario: giocatoriAvversari){
						if (partita.getPunteggio(giocatoreAvversario).equals(partita.getPunteggioVincitore())){
							giocatoriSuCuiConfrontareRanking.remove(giocatoreAvversario);
							MyLogger.getLogger().finer("Tolto "+giocatoreAvversario+" dal calcolo della ranking di "+giocatore+" in quanto hanno finito in testa a pari punti");
						}
					}
				}
			}
			
			MyLogger.getLogger().fine("Inizio calcolo ranking di "+giocatore+" con avversari "+ giocatoriSuCuiConfrontareRanking);
			
			if (giocatore.getCognome().equalsIgnoreCase("giurdanella")){
				int i = 0;
			}
			
			BigDecimal nuovaRanking = RankingCalculator.computeNewRanking(giocatore, giocatoriSuCuiConfrontareRanking, isVincitore);
			
			MyLogger.getLogger().fine(giocatore+" passa da "+giocatore.getRanking()+" a "+nuovaRanking);
			
			//Per adesso ricalcolo lo storicoRanking ad ogni partita; poi si vedrà se farlo a livello di torneo
			StoricoRanking storicoRanking = new StoricoRanking();
			storicoRanking.setTorneo(fileName);
			storicoRanking.setRankingAfineTorneo(nuovaRanking);
			storicoRanking.setIsVincitore(isVincitore);
			storicoRanking.setGiocatoriAvversari(giocatoriAvversari);
			//Cerco il giocatore nell'elenco ranking per determinare la sua ranking attuale
			int indexGiocatore = elencoGiocatoriInRanking.indexOf(giocatore);
			if (indexGiocatore != -1){
				GiocatoreDTO giocatoreGiaInRanking = elencoGiocatoriInRanking.get(indexGiocatore);
				List<StoricoRanking> storiciRanking = giocatoreGiaInRanking.getStorico();
				storiciRanking.add(storicoRanking);
				giocatoreGiaInRanking.setRanking(nuovaRanking);
				giocatoreGiaInRanking.setClub(giocatore.getClub()); //Aggiorno sempre con l'ultimo
			}else{
				//DEvo crearne uno nuovo altrimenti all'interno del loop la nuova ranking appena calcolata entrerebbe nel calcolo degli altri
				GiocatoreDTO giocatoreNuovo = new GiocatoreDTO(giocatore);
				giocatoreNuovo.setRanking(nuovaRanking);
				List<StoricoRanking> storiciRanking = new ArrayList<StoricoRanking>();
				storiciRanking.add(storicoRanking);
				giocatoreNuovo.setStorico(storiciRanking);			
				elencoGiocatoriInRanking.add(giocatoreNuovo);
			}
		}
	}

	public List<GiocatoreDTO> getElencoGiocatoriInRanking() {
		return elencoGiocatoriInRanking;
	}

	public void setElencoGiocatoriInRanking(
			List<GiocatoreDTO> elencoGiocatoriInRanking) {
		this.elencoGiocatoriInRanking = elencoGiocatoriInRanking;
	}
	
	
	
}
