package it.desimone.risiko.torneo.batch;

import it.desimone.risiko.torneo.dto.ClubDTO;
import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.risiko.torneo.dto.RegioneDTO;
import it.desimone.risiko.torneo.utils.RegioniLoader;
import it.desimone.utils.Logger;
import it.desimone.utils.MyException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SorteggioTavoli {

	private static List<Integer> listaTavoli = new ArrayList<Integer>();
	
	private static Partita[] getTavoliVuoti(int numeroGiocatori){
		if (numeroGiocatori < 2){
			throw new IllegalArgumentException("Il numero di giocatori non consente di fare nemmeno un tavolo: "+numeroGiocatori);
		}
		Partita[] result = null;
		switch (numeroGiocatori) {
		case 3:
			result = new Partita[1];
			result[0] = new Partita(3);
			break;
		case 6:
			result = new Partita[2];
			result[0] = new Partita(3);
			result[1] = new Partita(3);
			break;
		case 7:
			result = new Partita[2];
			result[0] = new Partita(4);
			result[1] = new Partita(3);
			break;
		case 11:
			result = new Partita[3];
			result[0] = new Partita(4);
			result[1] = new Partita(4);
			result[2] = new Partita(3);
			break;
		default:
			int numeroTavolida5 = numeroGiocatori%4;
			int numeroTavolida4 = (numeroGiocatori - numeroTavolida5*5)/4;
			result = new Partita[numeroTavolida4+numeroTavolida5];
			for (int i = 0; i < numeroTavolida4; i++){
				result[i] = new Partita(4);
			}
			for (int j = 0; j < numeroTavolida5; j++){
				result[j+numeroTavolida4] = new Partita(5);
			}
			break;
		}
		for (int k=0; k<result.length; k++){
			result[k].setNumeroTavolo(k+1);
		}
		return result;
	}
	
	private static Partita[] getTavoliVuotiCampionato(int numeroGiocatori){
		if (numeroGiocatori < 2){
			throw new IllegalArgumentException("Il numero di giocatori non consente di fare nemmeno un tavolo: "+numeroGiocatori);
		}
		Partita[] result = null;
		switch (numeroGiocatori) {
		case 3:
			result = new Partita[1];
			result[0] = new Partita(3);
			//result[0].addGiocatore(GiocatoreDTO.FITTIZIO, null);
			break;
		default:
			int restoDa4 = numeroGiocatori%4;
			int numeroTavolida5 = 0;
			int numeroTavolida3 = 0;
			if (restoDa4 == 1){
				numeroTavolida5 = 1;
			}else if (restoDa4 == 2){
				numeroTavolida3 = 2;
			}else if (restoDa4 == 3){
				numeroTavolida3 = 1;
			}
			int numeroTavolida4 = (numeroGiocatori - numeroTavolida5*5 - numeroTavolida3*3)/4;
			result = new Partita[numeroTavolida4+numeroTavolida5+numeroTavolida3];
			for (int k = 0; k < numeroTavolida3; k++){
				result[k] = new Partita(3);
				//result[k].addGiocatore(GiocatoreDTO.FITTIZIO, null);
			}
			for (int i = 0; i < numeroTavolida4; i++){
				result[i+numeroTavolida3] = new Partita(4);
			}
			for (int j = 0; j < numeroTavolida5; j++){
				result[j+numeroTavolida4+numeroTavolida3] = new Partita(5);
			}
			break;
		}
		for (int k=0; k<result.length; k++){
			result[k].setNumeroTavolo(k+1);
		}
		return result;
	}
	
	private static Partita[] getTavoliVuotiColoni(int numeroGiocatori){
		if (numeroGiocatori < 2 || numeroGiocatori == 5){
			throw new IllegalArgumentException("Il numero di giocatori non consente di fare nemmeno un tavolo: "+numeroGiocatori);
		}
		Partita[] result = null;
		switch (numeroGiocatori) {
		case 3:
			result = new Partita[1];
			result[0] = new Partita(3);
			break;
		default:
			int numeroTavolida3 = numeroGiocatori%4 == 0 ? 0 : 4 - numeroGiocatori%4;
			int numeroTavolida4 = (numeroGiocatori - numeroTavolida3*3)/4;
			result = new Partita[numeroTavolida4+numeroTavolida3];
			for (int i = 0; i < numeroTavolida4; i++){
				result[i] = new Partita(4);
			}
			for (int j = 0; j < numeroTavolida3; j++){
				result[j+numeroTavolida4] = new Partita(3);
			}
			break;
		}
		for (int k=0; k<result.length; k++){
			result[k].setNumeroTavolo(k+1);
		}
		return result;
	}
	
	/* Qui l'unico criterio di separazione è l'appartenenza al club o gruppo familiare */
	public static Partita[] tavoliPrimoTurnoTorneoColoni(List<GiocatoreDTO> giocatori){
		Partita[] partite = getTavoliVuotiColoni(giocatori.size());
		Logger.debug("***  Elaborazione Tavoli Primo Turno. Numero Tavoli: "+partite.length+"  ***");
		/* Divido i giocatori in insiemi corrispondenti al club di appartenenza */
		List<List<GiocatoreDTO>> giocatoriPerClub = getGiocatoriPerClub(giocatori);
		Collections.shuffle(giocatoriPerClub);
		for (List<GiocatoreDTO> giocatoriStessoClub: giocatoriPerClub){
			listaTavoli.clear();
			while (giocatoriStessoClub.size() >0){
				Integer numeroTavolo = getTavoloCasuale(partite.length);
				boolean tavoloIncompleto = partite[numeroTavolo-1].addGiocatore(giocatoriStessoClub.get(0),null);
				if (tavoloIncompleto){
					giocatoriStessoClub.remove(0);
				}
			}
		}
		partite = redistribuisciPartitePerCriterioClubDiversi(partite,null);
		return partite;
	}
	
	public static Partita[] tavoliOrdinatiColoni(List<GiocatoreDTO> giocatori){
		Partita[] partite = getTavoliVuotiColoni(giocatori.size());
		Logger.debug("***  Elaborazione Tavoli. Numero Tavoli: "+partite.length+"  ***");
		Integer numeroTavolo = 1;
		for (GiocatoreDTO giocatore: giocatori){
			if (!partite[numeroTavolo-1].isComplete()){
				numeroTavolo++;
			}
			partite[numeroTavolo-1].addGiocatore(giocatore,null);
		}
		return partite;
	}
	
	/* Qui l'unico criterio di separazione è l'appartenenza al club o gruppo familiare */
	public static Partita[] tavoliPrimoTurnoTorneoGufo(List<GiocatoreDTO> giocatori){
		Partita[] partite = getTavoliVuoti(giocatori.size());
		Logger.debug("***  Elaborazione Tavoli Primo Turno. Numero Tavoli: "+partite.length+"  ***");
		/* Divido i giocatori in insiemi corrispondenti al club di appartenenza */
		List<List<GiocatoreDTO>> giocatoriPerClub = getGiocatoriPerClub(giocatori);
		//Collections.shuffle(giocatoriPerClub);
		for (List<GiocatoreDTO> giocatoriStessoClub: giocatoriPerClub){
			listaTavoli.clear();
			while (giocatoriStessoClub.size() >0){
				Integer numeroTavolo = getTavoloCasuale(partite.length);
				boolean tavoloIncompleto = partite[numeroTavolo-1].addGiocatore(giocatoriStessoClub.get(0),null);
				if (tavoloIncompleto){
					giocatoriStessoClub.remove(0);
				}
			}
		}
		partite = redistribuisciPartitePerCriterioClubDiversi(partite,null);
		return partite;
	}
	
	public static Partita[] tavoliPrimoTurnoCampionatoGufo(List<GiocatoreDTO> giocatori){
		Partita[] partite = getTavoliVuotiCampionato(giocatori.size());
		Logger.debug("***  Elaborazione Tavoli Primo Turno. Numero Tavoli: "+partite.length+"  ***");
		/* Divido i giocatori in insiemi corrispondenti al club di appartenenza */
		List<List<GiocatoreDTO>> giocatoriPerClub = getGiocatoriPerClub(giocatori);
		//Collections.shuffle(giocatoriPerClub);
		for (List<GiocatoreDTO> giocatoriStessoClub: giocatoriPerClub){
			listaTavoli.clear();
			while (giocatoriStessoClub.size() >0){
				Integer numeroTavolo = getTavoloCasuale(partite.length);
				boolean tavoloIncompleto = partite[numeroTavolo-1].addGiocatore(giocatoriStessoClub.get(0),null);
				if (tavoloIncompleto){
					giocatoriStessoClub.remove(0);
				}
			}
		}
		partite = redistribuisciPartitePerCriterioClubDiversi(partite,null);
		for (Partita partita: partite){
			if (partita.getNumeroGiocatori() == 3){
				partita.setNumeroGiocatori(Partita.TAVOLO_PERFETTO);
				partita.addGiocatore(GiocatoreDTO.FITTIZIO, null);
			}
		}
		return partite;
	}
	
	/* Qui l'unico criterio di separazione è l'appartenenza al club o gruppo familiare */
	public static Partita[] tavoliTurniSuccessiviTorneoGufo(List<GiocatoreDTO> giocatori, Partita[] partitePrecedenti){
		Partita[] partite = getTavoliVuoti(giocatori.size());
		Logger.debug("***  Elaborazione.  Numero Tavoli: "+partite.length+"  ***");
		/* Divido i giocatori in insiemi corrispondenti al club di appartenenza */
		List<List<GiocatoreDTO>> giocatoriPerClub = getGiocatoriPerClub(giocatori);
		//Collections.shuffle(giocatoriPerClub);
		for (List<GiocatoreDTO> giocatoriStessoClub: giocatoriPerClub){
			listaTavoli.clear();
			while (giocatoriStessoClub.size() >0){
				Integer numeroTavolo = getTavoloCasuale(partite.length);
				boolean tavoloIncompleto = partite[numeroTavolo-1].addGiocatore(giocatoriStessoClub.get(0),null);
				if (tavoloIncompleto){
					giocatoriStessoClub.remove(0);
				}
			}
		}
		partite = redistribuisciPartitePerMinimizzarePartecipazioneTavoloda (partite, partitePrecedenti, Partita.TAVOLO_DA_5);
		partite = redistribuisciPartitePerCriterioClubDiversi				(partite, partitePrecedenti);
		partite = redistribuisciPartiteMinimizzareScontriMultipli			(partite, partitePrecedenti);
		return partite;
	}
	
	public static Partita[] tavoliTurniSuccessiviCampionatoGufo(List<GiocatoreDTO> giocatori, Partita[] partitePrecedenti){
		Partita[] partite = getTavoliVuotiCampionato(giocatori.size());
		Logger.debug("***  Elaborazione.  Numero Tavoli: "+partite.length+"  ***");
		/* Divido i giocatori in insiemi corrispondenti al club di appartenenza */
		List<List<GiocatoreDTO>> giocatoriPerClub = getGiocatoriPerClub(giocatori);
		//Collections.shuffle(giocatoriPerClub);
		for (List<GiocatoreDTO> giocatoriStessoClub: giocatoriPerClub){
			listaTavoli.clear();
			while (giocatoriStessoClub.size() >0){
				Integer numeroTavolo = getTavoloCasuale(partite.length);
				boolean tavoloIncompleto = partite[numeroTavolo-1].addGiocatore(giocatoriStessoClub.get(0),null);
				if (tavoloIncompleto){
					giocatoriStessoClub.remove(0);
				}
			}
		}
		partite = redistribuisciPartitePerMinimizzarePartecipazioneTavoloda (partite, partitePrecedenti, Partita.TAVOLO_DA_3);
		partite = redistribuisciPartitePerMinimizzarePartecipazioneTavoloda (partite, partitePrecedenti, Partita.TAVOLO_DA_5);
		partite = redistribuisciPartitePerCriterioClubDiversi				(partite, partitePrecedenti);
		partite = redistribuisciPartiteMinimizzareScontriMultipli			(partite, partitePrecedenti);
		
		for (Partita partita: partite){
			if (partita.getNumeroGiocatori() == 3){
				partita.setNumeroGiocatori(Partita.TAVOLO_PERFETTO);
				partita.addGiocatore(GiocatoreDTO.FITTIZIO, null);
			}
		}
		return partite;
	}
	
	public static Partita[] tavoliPrimoTurno(List<GiocatoreDTO> giocatori){
		if (giocatori.size()%Partita.TAVOLO_PERFETTO != 0){
			throw new IllegalArgumentException("Il numero di giocatori non è un multiplo di "+Partita.TAVOLO_PERFETTO+": "+giocatori.size());
		}
		int numeroTavoli = giocatori.size()/Partita.TAVOLO_PERFETTO;
		Logger.debug("***  Elaborazione Tavoli Primo Turno. Numero Tavoli: "+numeroTavoli+"  ***");
		Partita[] partite = new Partita[numeroTavoli];

		/* Divido i giocatori in insiemi corrispondenti alla regione di appartenenza */
		List<List<GiocatoreDTO>> giocatoriPerRegione = getGiocatoriPerRegione1(giocatori);
		for (List<GiocatoreDTO> giocatoriStessaRegione: giocatoriPerRegione){
			listaTavoli.clear();
			while (giocatoriStessaRegione.size() >0){
				Integer numeroTavolo = getTavoloCasuale(numeroTavoli);
				if (partite[numeroTavolo-1] == null){
					partite[numeroTavolo-1] = new Partita(Partita.TAVOLO_PERFETTO);
					partite[numeroTavolo-1].setNumeroTavolo(numeroTavolo);
				}
				boolean tavoloIncompleto = partite[numeroTavolo-1].addGiocatore(giocatoriStessaRegione.get(0),null);
				if (tavoloIncompleto){
					giocatoriStessaRegione.remove(0);
				}
			}
		}
		partite = redistribuisciPartitePerCriterio(partite,null);
		return partite;
	}
	
	public static Partita[] getPartitePerCriterioDiNonRigiocabilita(List<GiocatoreDTO> giocatoriSecondoTurno, Partita[] partitePrimoTurno){
		if (giocatoriSecondoTurno.size()%Partita.TAVOLO_PERFETTO != 0){
			throw new IllegalArgumentException("Il numero di giocatori non è un multiplo di "+Partita.TAVOLO_PERFETTO+": "+giocatoriSecondoTurno.size());
		}
		int numeroTavoli = giocatoriSecondoTurno.size()/Partita.TAVOLO_PERFETTO;
		Partita[] partite = new Partita[numeroTavoli];

		int numeroTavoloIniziale = 0;
		for (Partita partitaPrimoTurno: partitePrimoTurno){
			numeroTavoloIniziale++;
			int numeroTavoloInLinea = numeroTavoloIniziale;
			/* Significa che i tavoli del secondo Turno sono meno di quelli del primo */
			if (numeroTavoloIniziale > numeroTavoli){
				break;
			}
//			for (GiocatoreDTO giocatore: partitaPrimoTurno.getGiocatori()){
			for (GiocatoreDTO giocatore: partitaPrimoTurno.getGiocatoriOrdinatiPerPunteggio()){
				if (giocatoriSecondoTurno.contains(giocatore)){
					if (partite[numeroTavoloInLinea-1]== null){
						partite[numeroTavoloInLinea-1]= new Partita(Partita.TAVOLO_PERFETTO);
					}
					partite[numeroTavoloInLinea-1].setNumeroTavolo(numeroTavoloInLinea);
					if (partite[numeroTavoloInLinea-1].addGiocatore(giocatore, null)){
						giocatoriSecondoTurno.remove(giocatore);
					}
					numeroTavoloInLinea++;
					if (numeroTavoloInLinea > numeroTavoli){
						numeroTavoloInLinea = 1;
					}
				}
			}
		}
		
		/* Gestione della rimanenza: giocatori che non hanno giocato il primo turno  o che sono rimasti
		 * fuori dalla ripartizione orizzontale per effetto del fatto che i tavoli del secondo turno sono 
		 * meno di quelli del primo */
		while (giocatoriSecondoTurno.size() >0){ 
//			Iterator<GiocatoreDTO> iterator = giocatoriSecondoTurno.iterator();
//			while (iterator.hasNext()){
//				GiocatoreDTO giocatore = iterator.next();
			//int size = giocatoriSecondoTurno.size();
			//for (int i=0; i < size; i++){
				GiocatoreDTO giocatore = giocatoriSecondoTurno.get(0);
				for (Partita partita: partite){
					if (partita == null){
						partita= new Partita();
					}
					if (partita.addGiocatore(giocatore,null)){
						//iterator.remove();
						giocatoriSecondoTurno.remove(0);
						break;
					}
				}
			//}
		}		
		return partite;
	}
	
	public static Partita[] getTavoliSecondoTurno(List<GiocatoreDTO> giocatoriSecondoTurno, Partita[] partitePrimoTurno){
		Partita[] partite = getPartitePerCriterioDiNonRigiocabilita(giocatoriSecondoTurno, partitePrimoTurno);
		Logger.debug("***  Elaborazione Tavoli Secondo Turno. Numero Tavoli: "+partite.length+"  ***");
		//partite = partiteMock();
		partite = redistribuisciPartitePerCriterio(partite,partitePrimoTurno);
		return partite;
	}
	
	
	private static Partita[] redistribuisciPartitePerCriterio(Partita[] partite, Partita[] partitePrecedenti){
		for (int i=0; i < partite.length; i++){
			Set<GiocatoreDTO> giocatoriAnomali = nonRispettaCriterioStessoClub(partite[i]);
			if (giocatoriAnomali.size() > 0 || ilTavolohaGiocatoriCheSiSonoGiaAffrontati(partite[i], partitePrecedenti)){
				GiocatoreDTO giocatore1 = giocatoriAnomali.iterator().next();
				Logger.debug("Nel tavolo "+(i+1)+" del "+(partitePrecedenti==null?"1°":"2°")+" Turno non sono rispettati i criteri da parte di "+giocatore1);
				boolean sostituito = false;
				for (int j=0; j < partite.length && !sostituito; j++){
					if (i != j){
						GiocatoreDTO[] giocatori = partite[j].getGiocatori().toArray(new GiocatoreDTO[0]);
						for (int k=0; k<giocatori.length && !sostituito; k++){
							GiocatoreDTO giocatore2 = giocatori[k];
							if (sonoIntercambiabili(partite[i], giocatore1, partite[j], giocatore2, partitePrecedenti)){
								scambiaGiocatori(partite[i],giocatore1,partite[j],giocatore2);
								sostituito = true;
								/* Caso in cui il tavolo aveva più di una violazione */
								if (nonRispettaCriterioStessoClub(partite[i]).size() >0){
									i--;
								}
							}
						}
					}
				}
			}
		}
		return partite;
	}
	
	private static Partita[] redistribuisciPartiteMinimizzareScontriMultipli(Partita[] partite, Partita[] partitePrecedenti){
		for (int i=0; i < partite.length; i++){
			if (ilTavolohaGiocatoriCheSiSonoGiaAffrontati(partite[i], partitePrecedenti)){
				Set<GiocatoreDTO> giocatoriCheSisonoGiaAffrontati = giocatoriCheSiSonoGiaAffrontati(partite[i], partitePrecedenti);
				GiocatoreDTO giocatore1 =  giocatoriCheSisonoGiaAffrontati.iterator().next();
				Logger.debug("Nel tavolo "+partite[i].getNumeroTavolo()+" si sono già affrontati i giocatori "+giocatoriCheSisonoGiaAffrontati);
				boolean sostituito = false;
				for (int j=0; j < partite.length && !sostituito; j++){
					if (i != j){
						GiocatoreDTO[] giocatori = partite[j].getGiocatori().toArray(new GiocatoreDTO[0]);
						for (int k=0; k<giocatori.length && !sostituito; k++){
							GiocatoreDTO giocatore2 = giocatori[k];
							if (							
									sonoIntercambiabiliInBaseAMinimizzazioneTavoli	(partite[i], giocatore1, partite[j], giocatore2, partitePrecedenti)
								 && sonoIntercambiabiliInBaseAClubDiversi			(partite[i], giocatore1, partite[j], giocatore2,false)
								 && sonoIntercambiabiliInBaseAPartitePrecedenti		(partite[i], giocatore1, partite[j], giocatore2, partitePrecedenti)
								){
								scambiaGiocatori(partite[i],giocatore1,partite[j],giocatore2);
								sostituito = true;
								/* Caso in cui il tavolo aveva più di una violazione */
								if (ilTavolohaGiocatoriCheSiSonoGiaAffrontati(partite[i], partitePrecedenti)){
									i--;
								}
							}
						}
					}
				}
			}
		}
		return partite;
	}
	
	private static Partita[] redistribuisciPartitePerCriterioClubDiversi(Partita[] partite, Partita[] partitePrecedenti){
		for (int i=0; i < partite.length; i++){
			Set<GiocatoreDTO> giocatoriAnomali = nonRispettaCriterioStessoClub(partite[i]);
//			if (giocatoriAnomali.size() > 0){
//			GiocatoreDTO giocatore1 = giocatoriAnomali.iterator().next();
			Iterator<GiocatoreDTO> iterator = giocatoriAnomali.iterator();
			while (iterator.hasNext()){				
				GiocatoreDTO giocatore1 = iterator.next();
				Logger.debug("Nel tavolo "+partite[i].getNumeroTavolo()+" il giocatore "+giocatore1+ " incontrerebbe compagni di Club");
				boolean sostituito = false;
				for (int j=0; j < partite.length && !sostituito; j++){
					if (i != j){
						GiocatoreDTO[] giocatori = partite[j].getGiocatori().toArray(new GiocatoreDTO[0]);
						for (int k=0; k<giocatori.length && !sostituito; k++){
							GiocatoreDTO giocatore2 = giocatori[k];
							if (	
								(partitePrecedenti == null
								 || (	partitePrecedenti != null
									&& 	sonoIntercambiabiliInBaseAMinimizzazioneTavoli(partite[i], giocatore1, partite[j], giocatore2, partitePrecedenti)
									)
								)
								 && sonoIntercambiabiliInBaseAClubDiversi(partite[i], giocatore1, partite[j], giocatore2, true)
								){
									scambiaGiocatori(partite[i],giocatore1,partite[j],giocatore2);								
									sostituito = true;
//									/* Caso in cui il tavolo aveva più di una violazione */
//									if (nonRispettaCriterioStessoClub(partite[i]).size() >0){
//										i--;
//									}
							}
						}
					}
				}
				iterator.remove();
			}
		}
		return partite;
	}
	
	private static Partita[] redistribuisciPartitePerMinimizzarePartecipazioneTavoloda(Partita[] partite, Partita[] partitePrecedenti, int numeroGiocatori){
		for (int i=0; i < partite.length; i++){
			if(partite[i].getNumeroGiocatori() == numeroGiocatori){
				Set<GiocatoreDTO> giocatori1 = new HashSet<GiocatoreDTO>(partite[i].getGiocatori());
				for (GiocatoreDTO giocatore1: giocatori1){
					int numeroPartecipazioniTavoloda = numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,numeroGiocatori);
					if (numeroPartecipazioniTavoloda > 0){
						Logger.debug("Nel tavolo "+partite[i].getNumeroTavolo()+" il giocatore "+giocatore1+ " ha già giocato "+numeroPartecipazioniTavoloda+" volte al tavolo da "+numeroGiocatori);
						boolean sostituito = false;
						for (int j=0; j < partite.length && !sostituito; j++){
							if (i != j && 
									( 
										partite[j].getNumeroGiocatori() == Partita.TAVOLO_PERFETTO
										/* Priorità del tavolo da 5 su quello da 3*/
									|| (numeroGiocatori == Partita.TAVOLO_DA_3 && partite[j].getNumeroGiocatori() == Partita.TAVOLO_DA_5)		
									)
							){
								GiocatoreDTO[] giocatori2 = partite[j].getGiocatori().toArray(new GiocatoreDTO[0]);
								for (int k=0; k<giocatori2.length && !sostituito; k++){
									GiocatoreDTO giocatore2 = giocatori2[k];
									if (numeroPartecipazioniTavoloda > numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti, numeroGiocatori)){
										scambiaGiocatori(partite[i],giocatore1,partite[j],giocatore2);
										sostituito = true;
									}
								}
							}
						}
					}
				}
			}
		}
		return partite;
	}
	
	private static Integer numeroPartecipazioniTavoloda(GiocatoreDTO giocatore, Partita[] partitePrecedenti, int numeroGiocatori){
		Integer result = 0;
		for (Partita partita: partitePrecedenti){
			if (partita.haGiocato(giocatore) && partita.getNumeroGiocatori() == numeroGiocatori){
				result++;
			}
		}
		return result;
	}
	
	
	private static void scambiaGiocatori(Partita partita1, GiocatoreDTO giocatore1, Partita partita2, GiocatoreDTO giocatore2){
		partita1.removeGiocatore(giocatore1);
		partita1.addGiocatore(giocatore2, null);
		partita2.removeGiocatore(giocatore2);
		partita2.addGiocatore(giocatore1, null);
		Logger.debug("Sostituito "+giocatore1+ " del tavolo "+partita1.getNumeroTavolo()+" con "+giocatore2+" del tavolo "+partita2.getNumeroTavolo());
	}
	
	private static Partita[] partiteMock(){
		ExcelAccess excelAccess = new ExcelAccess("F:\\WorkSpaces\\RisikoWorkSpace\\GestioneRaduno\\Properties\\Properties.txt");
		excelAccess.openFileExcel();
		Partita[] partite = new Partita[4];
		partite[0] = new Partita();
		partite[0].addGiocatore(excelAccess.getGiocatore(13),null);
		partite[0].addGiocatore(excelAccess.getGiocatore(2),null);
		partite[0].addGiocatore(excelAccess.getGiocatore(4),null);
		partite[0].addGiocatore(excelAccess.getGiocatore(16),null);

		partite[1] = new Partita();
		partite[1].addGiocatore(excelAccess.getGiocatore(12),null);
		partite[1].addGiocatore(excelAccess.getGiocatore(3),null);
		partite[1].addGiocatore(excelAccess.getGiocatore(1),null);
		partite[1].addGiocatore(excelAccess.getGiocatore(15),null);
		
		partite[2] = new Partita();
		partite[2].addGiocatore(excelAccess.getGiocatore(9),null);
		partite[2].addGiocatore(excelAccess.getGiocatore(11),null);
		partite[2].addGiocatore(excelAccess.getGiocatore(8),null);
		partite[2].addGiocatore(excelAccess.getGiocatore(6),null);
		
		partite[3] = new Partita();
		partite[3].addGiocatore(excelAccess.getGiocatore(5),null);
		partite[3].addGiocatore(excelAccess.getGiocatore(10),null);
		partite[3].addGiocatore(excelAccess.getGiocatore(7),null);
		partite[3].addGiocatore(excelAccess.getGiocatore(14),null);
		return partite;
	}
	
	private static boolean sonoIntercambiabili(Partita partita1, GiocatoreDTO giocatore1, Partita partita2, GiocatoreDTO giocatore2, Partita[] partitePrimoTurno){
		boolean result = false;
				
		Partita partita1Bis = new Partita(partita1);
		Partita partita2Bis = new Partita(partita2);
		partita1Bis.removeGiocatore(giocatore1);
		partita1Bis.addGiocatore(giocatore2, null);

		partita2Bis.removeGiocatore(giocatore2);
		partita2Bis.addGiocatore(giocatore1, null);
		
		boolean primoTavoloMaiAffrontati 	= !ilTavolohaGiocatoriCheSiSonoGiaAffrontati(partita1Bis, partitePrimoTurno);
		boolean secondoTavoloMaiAffrontati 	= !ilTavolohaGiocatoriCheSiSonoGiaAffrontati(partita2Bis, partitePrimoTurno);
		
		result = primoTavoloMaiAffrontati && secondoTavoloMaiAffrontati;
		
		if (result && partitePrimoTurno == null){
			RegioneDTO regione1 = giocatore1.getRegioneProvenienza();
			RegioneDTO regione2 = giocatore2.getRegioneProvenienza();
			result = (
					(regione1 == null && regione2 == null)
				||  (regione1 != null && regione1.equals(regione2))	
			);
		}
		
		if (result && partitePrimoTurno != null){
			result = entrambiVincitoriEntrambiSconfitti(giocatore1, giocatore2, partitePrimoTurno);
		}
		
		if (result){
			int sizeAnomaliePrimaDelloScambio = nonRispettaCriterioStessoClub(partita1).size()+ nonRispettaCriterioStessoClub(partita2).size();
			int sizeAnomalieDopoLoScambio = nonRispettaCriterioStessoClub(partita1Bis).size()+ nonRispettaCriterioStessoClub(partita2Bis).size();
			result = sizeAnomaliePrimaDelloScambio > sizeAnomalieDopoLoScambio;
//			if (result && partitePrimoTurno == null){
//				sizeAnomaliePrimaDelloScambio = nonRispettaCriterioStessaRegione(partita1).size()+ nonRispettaCriterioStessaRegione(partita2).size();
//				sizeAnomalieDopoLoScambio = nonRispettaCriterioStessaRegione(partita1Bis).size()+ nonRispettaCriterioStessaRegione(partita2Bis).size();
//				result = sizeAnomaliePrimaDelloScambio >= sizeAnomalieDopoLoScambio;
//			}
		}
		return result;
	}
	
	private static boolean sonoIntercambiabiliInBaseAPartitePrecedenti(Partita partita1, GiocatoreDTO giocatore1, Partita partita2, GiocatoreDTO giocatore2, Partita[] partitePrecedenti){
		boolean result = false;
				
		Partita partita1Bis = new Partita(partita1);
		Partita partita2Bis = new Partita(partita2);
		partita1Bis.removeGiocatore(giocatore1);
		partita1Bis.addGiocatore(giocatore2, null);

		partita2Bis.removeGiocatore(giocatore2);
		partita2Bis.addGiocatore(giocatore1, null);
		
		int numeroGiocatoriGiaAffrontatiPrima 	= giocatoriCheSiSonoGiaAffrontati(partita1, partitePrecedenti).size()
												+ giocatoriCheSiSonoGiaAffrontati(partita2, partitePrecedenti).size();

		int numeroGiocatoriGiaAffrontatiDopo 	= giocatoriCheSiSonoGiaAffrontati(partita1Bis, partitePrecedenti).size()
												+ giocatoriCheSiSonoGiaAffrontati(partita2Bis, partitePrecedenti).size();

		result = numeroGiocatoriGiaAffrontatiPrima > numeroGiocatoriGiaAffrontatiDopo;
		
//		boolean primoTavoloMaiAffrontati 	= !ilTavolohaGiocatoriCheSiSonoGiaAffrontati(partita1Bis, partitePrecedenti);
//		boolean secondoTavoloMaiAffrontati 	= !ilTavolohaGiocatoriCheSiSonoGiaAffrontati(partita2Bis, partitePrecedenti);
//		
//		result = primoTavoloMaiAffrontati && secondoTavoloMaiAffrontati;
		
		return result;
	}
	
	private static boolean sonoIntercambiabiliInBaseAMinimizzazioneTavoli(Partita partita1, GiocatoreDTO giocatore1, Partita partita2, GiocatoreDTO giocatore2, Partita[] partitePrecedenti){
		boolean result = false;
				
		Partita partita1Bis = new Partita(partita1);
		Partita partita2Bis = new Partita(partita2);
		partita1Bis.removeGiocatore(giocatore1);
		partita1Bis.addGiocatore(giocatore2, null);

		partita2Bis.removeGiocatore(giocatore2);
		partita2Bis.addGiocatore(giocatore1, null);

		
		/* Se i due tavoli sono uguali lo scambio è sempre fattibile*/
		result = partita1Bis.getNumeroGiocatori() == partita2Bis.getNumeroGiocatori();
		
		/* Se non sono uguali verifico che il numero di partecipazioni di un giocatore sia superiore a quello dell'altro 
		 * in base all'ordine gerarchico per cui si devono minimizzare prima le partecipazioni al tavolo da 3 */
		if (!result){
			switch (partita1Bis.getNumeroGiocatori()) {
			case Partita.TAVOLO_DA_3:
				if (partita2Bis.getNumeroGiocatori() == Partita.TAVOLO_DA_5){
					result = numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_3) > numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_3)
					|| (numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_3) == numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_3)
					 && numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_5) <= numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_5));
				}else{ /* Vuol dire che è un tavolo da 4 */
					result = numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_3) >= numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_3);
				}
				break;
			case Partita.TAVOLO_DA_5:
				if (partita2Bis.getNumeroGiocatori() == Partita.TAVOLO_DA_3){
					result = numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_3) <= numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_3)
						  && numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_5) >= numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_5);
				}else{ /* Vuol dire che è un tavolo da 4 */
					result = numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_5) >= numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_5);
				}
				break;
			case Partita.TAVOLO_PERFETTO:
				if (partita2Bis.getNumeroGiocatori() == Partita.TAVOLO_DA_3){
					result = numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_3) <= numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_3);
				}else{ /* Vuol dire che è un tavolo da 5 */
					result = numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_5) <= numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_5);
				}
				break;
			default:
				throw new MyException("Numero giocatori non previsto: "+partita1Bis.getNumeroGiocatori());
			}
//			if (partita1Bis.getNumeroGiocatori() != Partita.TAVOLO_PERFETTO){
//				result = numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_5) >= numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_5);				
//			}else{
//				result = numeroPartecipazioniTavoloda(giocatore1,partitePrecedenti,Partita.TAVOLO_DA_5) <= numeroPartecipazioniTavoloda(giocatore2,partitePrecedenti,Partita.TAVOLO_DA_5);				
//			}
		}
		return result;
	}

	private static boolean sonoIntercambiabiliInBaseAClubDiversi(Partita partita1, GiocatoreDTO giocatore1, Partita partita2, GiocatoreDTO giocatore2, boolean haPriorita){
		boolean result = false;
				
		Partita partita1Bis = new Partita(partita1);
		Partita partita2Bis = new Partita(partita2);
		partita1Bis.removeGiocatore(giocatore1);
		partita1Bis.addGiocatore(giocatore2, null);

		partita2Bis.removeGiocatore(giocatore2);
		partita2Bis.addGiocatore(giocatore1, null);

		int sizeAnomaliePrimaDelloScambio = nonRispettaCriterioStessoClub(partita1).size()+ nonRispettaCriterioStessoClub(partita2).size();
		int sizeAnomalieDopoLoScambio = nonRispettaCriterioStessoClub(partita1Bis).size()+ nonRispettaCriterioStessoClub(partita2Bis).size();
		if (haPriorita){
			result = sizeAnomaliePrimaDelloScambio >  sizeAnomalieDopoLoScambio;
		}else{
			result = sizeAnomaliePrimaDelloScambio >= sizeAnomalieDopoLoScambio;
		}
		return result;
	}

	
	private static Set<GiocatoreDTO> nonRispettaCriterioStessoClub(Partita partita){
		Set<GiocatoreDTO> result = new HashSet<GiocatoreDTO>();
		GiocatoreDTO[] giocatoriInConfronto = partita.getGiocatori().toArray(new GiocatoreDTO[0]);
		for (int i=0; i<giocatoriInConfronto.length-1; i++){
			for (int j=i+1; j<giocatoriInConfronto.length; j++){
				if (giocatoriInConfronto[i].getClubProvenienza() != null && giocatoriInConfronto[j].getClubProvenienza() != null){
					if(giocatoriInConfronto[i].getClubProvenienza().equals(giocatoriInConfronto[j].getClubProvenienza())
					|| checkScalambra(giocatoriInConfronto[i], giocatoriInConfronto[j])){
						result.add(giocatoriInConfronto[i]);
					}
				}
			}
		}
		return result;
	}

	private static Set<GiocatoreDTO> nonRispettaCriterioScalambra(Partita partita){
		Set<GiocatoreDTO> result = new HashSet<GiocatoreDTO>();
		GiocatoreDTO[] giocatoriInConfronto = partita.getGiocatori().toArray(new GiocatoreDTO[0]);
		for (int i=0; i<giocatoriInConfronto.length-1; i++){
			for (int j=i+1; j<giocatoriInConfronto.length; j++){
				if (checkScalambra(giocatoriInConfronto[i], giocatoriInConfronto[j])){
					result.add(giocatoriInConfronto[i]);
				}
			}
		}
		return result;
	}

	
	public static boolean checkScalambra(GiocatoreDTO giocatore1, GiocatoreDTO giocatore2){
		boolean result = false;
		
		if (
			(	giocatore1.getCognome().equalsIgnoreCase("Scalambra")
			||	giocatore1.getCognome().equalsIgnoreCase("Di Casola")
			||	giocatore1.getCognome().equalsIgnoreCase("Francia")
			)
		&& 	(	giocatore2.getCognome().equalsIgnoreCase("Scalambra")
			||	giocatore2.getCognome().equalsIgnoreCase("Di Casola")
			||	giocatore2.getCognome().equalsIgnoreCase("Francia")
			)
		){
			result = true;
		}
			
		
//		if (		
//			(giocatore1.getNick().equalsIgnoreCase("DannyScala")
//			&& 	(	giocatore2.getCognome().equalsIgnoreCase("Scalambra")
//				||	giocatore2.getCognome().equalsIgnoreCase("Di Casola")
//				||	giocatore2.getCognome().equalsIgnoreCase("Francia")
//				)
//			)
//		|| 
//			(giocatore2.getNick().equalsIgnoreCase("DannyScala")
//			&& 	(	giocatore1.getCognome().equalsIgnoreCase("Scalambra")
//				||	giocatore1.getCognome().equalsIgnoreCase("Di Casola")
//				||	giocatore1.getCognome().equalsIgnoreCase("Francia")
//				)
//			)
//		){
//			result = true;	
//		}		
		return result;
	}
	
	private static Set<GiocatoreDTO> nonRispettaCriterioStessaRegione(Partita partita){
		Set<GiocatoreDTO> result = new HashSet<GiocatoreDTO>();
		GiocatoreDTO[] giocatoriInConfronto = partita.getGiocatori().toArray(new GiocatoreDTO[0]);
		for (int i=0; i<giocatoriInConfronto.length-1; i++){
			for (int j=i+1; j<giocatoriInConfronto.length; j++){
				if (giocatoriInConfronto[i].getRegioneProvenienza() != null && giocatoriInConfronto[j].getRegioneProvenienza() != null){
					if(giocatoriInConfronto[i].getRegioneProvenienza().equals(giocatoriInConfronto[j].getRegioneProvenienza())){
						result.add(giocatoriInConfronto[i]);
					}
				}
			}
		}
		return result;
	}
	
	private static boolean ilTavolohaGiocatoriCheSiSonoGiaAffrontati(Partita partita, Partita[] partitePrimoTurno){
		boolean result = false;
		if (partitePrimoTurno != null){
			GiocatoreDTO[] giocatoriInConfronto = partita.getGiocatori().toArray(new GiocatoreDTO[0]);		
			for (int i=0; i<giocatoriInConfronto.length-1 && !result; i++){
				for (int j=i+1; j<giocatoriInConfronto.length && !result; j++){
					for (Partita partitaPrimoTurno: partitePrimoTurno){
						result = partitaPrimoTurno.haGiocato(giocatoriInConfronto[i]) && partitaPrimoTurno.haGiocato(giocatoriInConfronto[j]);
						if (result) break;
					}
				}
			}
		}
		return result;
	}

	
	private static boolean entrambiVincitoriEntrambiSconfitti(GiocatoreDTO giocatore1, GiocatoreDTO giocatore2, Partita[] partitePrimoTurno){
		boolean result = false;
		if (partitePrimoTurno != null){
			boolean vincitore1 = false;
			boolean vincitore2 = false;
			for (Partita partitaPrimoTurno: partitePrimoTurno){
				if (partitaPrimoTurno.haGiocato(giocatore1)){
					vincitore1 = partitaPrimoTurno.isVincitore(giocatore1);
				}
				if (partitaPrimoTurno.haGiocato(giocatore2)){
					vincitore2 = partitaPrimoTurno.isVincitore(giocatore2);
				}
			}
			result = (vincitore1 == vincitore2);
		}
		return result;
	}
	
	private static Set<GiocatoreDTO> giocatoriCheSiSonoGiaAffrontati(Partita partita, Partita[] partitePrecedenti){
		Set result = new HashSet<GiocatoreDTO>();
		if (partitePrecedenti != null){
			GiocatoreDTO[] giocatoriInConfronto = partita.getGiocatori().toArray(new GiocatoreDTO[0]);		
			for (int i=0; i<giocatoriInConfronto.length-1; i++){
				for (int j=i+1; j<giocatoriInConfronto.length; j++){
					for (Partita partitaPrecedente: partitePrecedenti){
						if(partitaPrecedente.haGiocato(giocatoriInConfronto[i]) && partitaPrecedente.haGiocato(giocatoriInConfronto[j])){
							result.add(giocatoriInConfronto[i]);
							result.add(giocatoriInConfronto[j]);
						}
					}
				}
			}
		}
		return result;
	}
	
	public static List<List<GiocatoreDTO>> getGiocatoriPerRegione1(List<GiocatoreDTO> giocatori){
		
		Comparator<RegioneDTO> comparator = new Comparator(){
			public int compare(Object o1, Object o2){
				Integer i2 = ((List)o2).size();
				Integer i1 = ((List)o1).size();
				return i2.compareTo(i1);
			}
		};
		List giocatoriPerRegione = new ArrayList();
		
		List<GiocatoreDTO> listaDiLavoro = new ArrayList<GiocatoreDTO>(giocatori);
		while (listaDiLavoro.size() >0){
			RegioneDTO regione = listaDiLavoro.get(0).getRegioneProvenienza();
			List<GiocatoreDTO> listaPerRegione = new ArrayList<GiocatoreDTO>();
			Iterator<GiocatoreDTO> iterator = listaDiLavoro.iterator();
			while (iterator.hasNext()){
				GiocatoreDTO giocatore = iterator.next();
				RegioneDTO regioneProvenienza = giocatore.getRegioneProvenienza();
				if ((regioneProvenienza == null && regione == null) 
				|| (regioneProvenienza != null && regioneProvenienza.equals(regione))){
					listaPerRegione.add(giocatore);
					iterator.remove();
				}
			}
			//regione.setPopolazione(listaPerRegione.size());
			Collections.shuffle(listaPerRegione);
			giocatoriPerRegione.add(listaPerRegione);
		}
		Collections.sort(giocatoriPerRegione,comparator);
		return giocatoriPerRegione;
	}
	
	public static List<List<GiocatoreDTO>> getGiocatoriPerClub(List<GiocatoreDTO> giocatori){
		
		Comparator<RegioneDTO> comparator = new Comparator(){
			public int compare(Object o1, Object o2){
				Integer i2 = ((List)o2).size();
				Integer i1 = ((List)o1).size();
				return i2.compareTo(i1);
			}
		};
		List giocatoriPerClub = new ArrayList();
		
		List<GiocatoreDTO> listaDiLavoro = new ArrayList<GiocatoreDTO>(giocatori);
		while (listaDiLavoro.size() >0){
			ClubDTO club = listaDiLavoro.get(0).getClubProvenienza();
			List<GiocatoreDTO> listaPerClub = new ArrayList<GiocatoreDTO>();
			Iterator<GiocatoreDTO> iterator = listaDiLavoro.iterator();
			while (iterator.hasNext()){
				GiocatoreDTO giocatore = iterator.next();
				if ((giocatore.getClubProvenienza() == null && club == null)
				||  (giocatore.getClubProvenienza() != null && giocatore.getClubProvenienza().equals(club))){
					listaPerClub.add(giocatore);
					iterator.remove();
				}
			}
			giocatoriPerClub.add(listaPerClub);
		}
		Collections.sort(giocatoriPerClub,comparator);
		return giocatoriPerClub;
	}
	
	private static Integer getTavoloCasuale(Integer numeroTavoli){
		Integer result;
		if (listaTavoli.size()==0){
			for (int i=1; i<=numeroTavoli; i++){
				listaTavoli.add(i);
			}
			Collections.shuffle(listaTavoli);
		}
		result = listaTavoli.get(0);
		listaTavoli.remove(0);
		return result;
	}
	
	public static Map<RegioneDTO,List<GiocatoreDTO>> getGiocatoriPerRegione2(List<GiocatoreDTO> giocatori){
		Map<RegioneDTO, List<GiocatoreDTO>> giocatoriPerRegione = new LinkedHashMap<RegioneDTO, List<GiocatoreDTO>>();
		List<GiocatoreDTO> listaDiLavoro = new ArrayList<GiocatoreDTO>(giocatori);
		while (listaDiLavoro.size() >0){
			int counterMax = 0;
			RegioneDTO regioneMax = null;
			List<GiocatoreDTO> listaMax = null;
			for (RegioneDTO regione: RegioniLoader.getRegioni()){
				int counter = 0;
				List<GiocatoreDTO> listaPerRegione = new ArrayList<GiocatoreDTO>();
				for (GiocatoreDTO giocatore: listaDiLavoro){
					if (giocatore.getRegioneProvenienza().equals(regione)){
						counter++;
						listaPerRegione.add(giocatore);
					}
				}
				if (counter > counterMax){
					counterMax = counter;
					regioneMax = regione;
					listaMax = listaPerRegione;
				}
			}
			if (regioneMax != null){
				regioneMax.setPopolazione(listaMax.size());
				giocatoriPerRegione.put(regioneMax,listaMax);
				listaDiLavoro.removeAll(listaMax);
			}
		}
		
/*		for (RegioneDTO regione: RegioniLoader.getRegioni()){
			List<GiocatoreDTO> listaPerRegione = new ArrayList<GiocatoreDTO>();
			for (GiocatoreDTO giocatore: giocatori){
				if (giocatore.getRegioneProvenienza().equals(regione)){
					listaPerRegione.add(giocatore);
				}
			}
			if (listaPerRegione.size()>0){
				regione.setPopolazione(listaPerRegione.size());
				giocatoriPerRegione.put(regione,listaPerRegione);
			}
		}*/
		
		return giocatoriPerRegione;
	}
	
}
