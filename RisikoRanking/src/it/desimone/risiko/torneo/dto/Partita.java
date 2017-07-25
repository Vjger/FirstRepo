package it.desimone.risiko.torneo.dto;

import it.desimone.utils.MapUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Partita {

	private int numeroGiocatori;
	
	private int numeroTavolo;
	
	private Map<GiocatoreDTO,Float> tavolo = new LinkedHashMap<GiocatoreDTO,Float>();
	
	private GiocatoreDTO vincitore;
	
	public Partita(){}
	
	public Partita(int numeroGiocatori){
		this.numeroGiocatori = numeroGiocatori;
	}
	
	public boolean addGiocatore(GiocatoreDTO giocatore, Float punteggio){
		boolean incompleto = isComplete();		
		if (incompleto){
			tavolo.put(giocatore, punteggio==null?0f:punteggio);
			//tavolo.put(giocatore, punteggio==null?getPunteggioCasualePerTest():punteggio);
		}
		return incompleto;
	}

	public Float setPunteggio(GiocatoreDTO giocatore, Float punteggio){
		Float result = null;
		if (tavolo.containsKey(giocatore)){
			result = tavolo.put(giocatore, punteggio==null?0f:punteggio);
		}
		return result;
	}
	
	
	public Float getPunteggio(GiocatoreDTO giocatore){
		return tavolo.get(giocatore);
	}
	
	public Float getPunteggioVincitore(){
		Float punteggioPrimo = getPunteggio(getGiocatoriOrdinatiPerPunteggio().iterator().next());
		return punteggioPrimo;
	}
	
	public boolean isVincitore(GiocatoreDTO giocatore){
		boolean result = false;
		if (giocatore != null){
			if (vincitore != null){
				result = vincitore.equals(giocatore);
			}else{
				Float punteggioGiocatore = getPunteggio(giocatore);
				if (punteggioGiocatore != null){
					Float punteggioPrimo = getPunteggioVincitore();
					result = punteggioGiocatore.equals(punteggioPrimo);
				}
			}
		}
		return result;
	}

	public int numeroVincitori(){
		int result = 1;
		Iterator<GiocatoreDTO> iterator = getGiocatoriOrdinatiPerPunteggio().iterator();
		Float punteggioPrimo = getPunteggio(iterator.next());
		while (iterator.hasNext()){
			if (punteggioPrimo.equals(getPunteggio(iterator.next()))){
				result++;
			}
		}
		return result;
	}
	
	public boolean isComplete(){
		if (numeroGiocatori != 0){
			return tavolo.size() < numeroGiocatori;
		}else{
			return true; //tavolo.size() < TAVOLO_PERFETTO;
		}
	}

	public Set<GiocatoreDTO> getGiocatori(){
		return tavolo.keySet();
	}
	
	public Set<GiocatoreDTO> getGiocatoriOrdinatiPerPunteggio(){
		return MapUtils.sortByValue(tavolo,true).keySet();
	}
	
	public int getPosizione(GiocatoreDTO giocatore){
		int result = 0;
		int index = 0;
		for (GiocatoreDTO player: getGiocatoriOrdinatiPerPunteggio()){
			index++;
			if (player.equals(giocatore)){
				result = index;
				break;
			}
		}
		return result;
	}
	
	public boolean haGiocato(GiocatoreDTO giocatore){
		boolean result = tavolo.containsKey(giocatore);
		return result;
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("\nTavolo n°"+numeroTavolo+": "+numeroGiocatori+" giocatori\n");
		for (GiocatoreDTO giocatore: tavolo.keySet()){
			result.append(giocatore+" = "+tavolo.get(giocatore)+"\t");
		}
		return result.toString();
	}
	

	public int getNumeroGiocatori() {
		return numeroGiocatori;
	}

	public void setNumeroGiocatori(int numeroGiocatori) {
		this.numeroGiocatori = numeroGiocatori;
	}

	public GiocatoreDTO getVincitore() {
		return vincitore;
	}

	public void setVincitore(GiocatoreDTO vincitore) {
		this.vincitore = vincitore;
	}
}
