package it.desimone.risiko.torneo.scoreplayer;

import java.math.BigDecimal;
import java.math.RoundingMode;

import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.utils.MyException;

public class ScorePlayerTorneoStoneAge implements ScorePlayer{

	private int numeroVittorie = 0;
	private Float massimoPunteggio = 0f;
	private GiocatoreDTO giocatore;
	
	private Partita[] partite;
	
	public ScorePlayerTorneoStoneAge(GiocatoreDTO giocatore, Partita[] partite){
		this.giocatore = giocatore;
		this.partite = new Partita[partite.length];
		
		for (int i=0; i < partite.length; i++){
			if (partite[i] != null){
				this.partite[i] = partite[i];
				trascodificaPunteggio(partite[i], giocatore);
			}
		}
	}
	
	public BigDecimal getPunteggioB(boolean conScarto){
		BigDecimal totale = BigDecimal.ZERO;
		for (Partita partita: partite){
			BigDecimal punteggio = partita==null?BigDecimal.ZERO:partita.getPunteggioTrascodificatoB(giocatore);
			totale = totale.add(punteggio);
		}
		return totale;
	}
	
	public Float getPunteggio(boolean conScarto){
		Float totale = 0f;
		for (Partita partita: partite){
			Float punteggio = partita==null?0f:partita.getPunteggioTrascodificato(giocatore);
			totale += punteggio;
		}
		return totale;
	}
		
	private void trascodificaPunteggio(Partita partita, GiocatoreDTO giocatore){		
		int posizione = getPosizione(partita,giocatore);
		if (posizione == 1){
			numeroVittorie++;
		}
		int bonus = 0;
		switch (posizione) {
		case 1:
			bonus = 25;
			break;
		case 2:
			bonus = 15;
			break;
		case 3:
			bonus = 10;
			break;
		case 4:
			bonus = 5;
			break;
		default:
			throw new MyException("Posizione del giocatore "+giocatore+" nella partita "+partita+" imprevista: "+posizione);
		}
		Float percentuale = calcolaPercentualePuntiVittoria(partita, giocatore);
		Float punteggioTrascodificato = percentuale + bonus;
		partita.setPunteggioTrascodificato(giocatore, punteggioTrascodificato);
		BigDecimal punteggioB = new BigDecimal(percentuale).add(new BigDecimal(bonus));
		partita.setPunteggioTrascodificatoB(giocatore, punteggioB);
	}
	
	private int getPosizione(Partita partita, GiocatoreDTO giocatore){
		Object[] datiAggiuntiviG = partita.getDatiAggiuntiviTavolo().get(giocatore);
		Float puntiFattiG =  partita.getPunteggio(giocatore);
		int puntiScommessiG = ((Integer) datiAggiuntiviG[0]);
		Float punteggioG = puntiFattiG - puntiScommessiG;
		Integer numeroGraniG = ((Integer) datiAggiuntiviG[1]);
		Integer numeroUtensiliG = ((Integer) datiAggiuntiviG[2]);
		Integer numeroOminiG = ((Integer) datiAggiuntiviG[3]);
		
		int posizione = 1;
		for (GiocatoreDTO player: partita.getGiocatori()){
			if (!player.equals(giocatore)){
				Object[] datiAggiuntiviP = partita.getDatiAggiuntiviTavolo().get(player);
				Float puntiFattiP =  partita.getPunteggio(player);
				int puntiScommessiP = ((Integer) datiAggiuntiviP[0]);
				Float punteggioP = puntiFattiP - puntiScommessiP;
				Integer numeroGraniP = ((Integer) datiAggiuntiviP[1]);
				Integer numeroUtensiliP = ((Integer) datiAggiuntiviP[2]);
				Integer numeroOminiP = ((Integer) datiAggiuntiviP[3]);
				
				if (punteggioP > punteggioG){
					posizione++;
				}else if (punteggioP.equals(punteggioG)){
					if (numeroGraniP > numeroGraniG){
						posizione++;
					}else if (numeroGraniP == numeroGraniG){
						if (numeroUtensiliP > numeroUtensiliG){
							posizione++;
						}else if (numeroUtensiliP == numeroUtensiliG){
							if (numeroOminiP > numeroOminiG){
								posizione++;
							}else if (numeroOminiP == numeroOminiG){
								if (puntiFattiP > puntiFattiG){
									posizione++;
								}
							}
						}
					}
				}			
			}
		}
		return posizione;
	}
	
	private Float calcolaPercentualePuntiVittoria(Partita partita, GiocatoreDTO giocatore){
		Float percentualePuntiVittoria = 0f;
		int punteggioTotale = 0;
		for (GiocatoreDTO player: partita.getGiocatori()){
			Object[] datiAggiuntivi = partita.getDatiAggiuntiviTavolo().get(player);
			int numeroPuntiScommessi = ((Integer) datiAggiuntivi[0]);
			punteggioTotale += partita.getPunteggio(player) - numeroPuntiScommessi;
		}
		Object[] datiAggiuntivi = partita.getDatiAggiuntiviTavolo().get(giocatore);
		int puntiScommessi = ((Integer) datiAggiuntivi[0]);
		BigDecimal percentuale = new BigDecimal(partita.getPunteggio(giocatore) - puntiScommessi).divide(new BigDecimal(punteggioTotale),4,RoundingMode.UP);
		percentuale = percentuale.multiply(new BigDecimal(100));
		percentualePuntiVittoria = percentuale.floatValue();
		return percentualePuntiVittoria;
	}
	
	public GiocatoreDTO getGiocatore() {
		return giocatore;
	}
	public void setGiocatore(GiocatoreDTO giocatore) {
		this.giocatore = giocatore;
	}
	public Partita[] getPartite() {
		return partite;
	}
	
	public int getNumeroVittorie() {
		return numeroVittorie;
	}

	public Float getPunteggioMassimo() {
		return massimoPunteggio;
	}


}
