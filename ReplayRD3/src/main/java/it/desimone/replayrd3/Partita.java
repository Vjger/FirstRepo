package it.desimone.replayrd3;

import it.desimone.replayrd3.azioni.Azione;
import it.desimone.utils.MyLogger;
import it.desimone.utils.Utils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Partita implements Serializable{
	
	private static DecimalFormat format = new DecimalFormat("0.00");
	
	public static final String PRESTIGE 	= "prestige";
	public static final String DIGITAL  	= "digital";
	public static final String CHALLENGE 	= "challenge";
	public static final String PRESTICHALLENGE 	= "prestichallenge";
	public static final String PRESTIGIOUS 	= "prestigious";
	public static final String TRESTIGE 	= "trestige";
	public static final String ULTRADIGITAL = "ultradigital";
	
	private Long idPartita;
	private String nomePartita;
	private String vincitore;
	private Integer punteggioVincitore;
	private List<Giocatore> giocatori;
	private Map<String,Integer> risultati;
	private Date dataDiInizio;
	private Date dataDiFine;
	private Integer durata;
	private Integer turniPartita;
	
	private String tipoPartita;
	
	private List<Azione> azioniLog;
	
	public void setTipoPartita(String tipoPartita) {
		this.tipoPartita = tipoPartita;
	}

	public String getTipoPartita(){
		if (tipoPartita != null) return tipoPartita;
		String result = null;
		if (nomePartita != null){
			String[] strings = nomePartita.split("_");
			if (strings != null && strings.length >= 1){
				result = strings[0];
			}
		}
		return result;
	}
	
	
	public void faiIlMergeDeiRisultati(Map<String,Integer> risultatiDaUnire){
		Set<String> giocatori = risultatiDaUnire.keySet();
		for (String giocatore: giocatori){
			if (risultati.containsKey(giocatore)){
				Integer risultato = risultatiDaUnire.get(giocatore);
				if (risultato != null){
					risultati.put(giocatore, risultato);
				}
			}else{
				MyLogger.getLogger().severe("Si tenta di unire nella stessa partita "+idPartita+" risultati con giocatori diversi: "+risultati+" - "+risultatiDaUnire);
			}
		}
	}
	
	public boolean partitaIncompleta(){	
		boolean result = false;
		
		if (idPartita == null || getTipoPartita() == null || risultati == null || risultati.isEmpty()){
			result = true;
		}else {
			Collection<Integer> risultatiGiocatori = risultati.values();
			for (Integer risultato: risultatiGiocatori){
				if (risultato == null){
					result = true;
					break;
				}
			}
		}		
		return result;
	}
	
	public Long getIdPartita() {
		return idPartita;
	}

	public void setIdPartita(Long idPartita) {
		this.idPartita = idPartita;
	}

	public String getNomePartita() {
		return nomePartita;
	}
	public void setNomePartita(String nomePartita) {
		this.nomePartita = nomePartita;
	}
	public List<Giocatore> getGiocatori() {
		return giocatori;
	}
	public void setGiocatori(List<Giocatore> giocatori) {
		this.giocatori = giocatori;
	}
	public Map<String, Integer> getRisultati() {
		return risultati;
	}
	public void setRisultati(Map<String, Integer> risultati) {
		this.risultati = risultati;
	}

	public Integer getDurata() {
		return durata;
	}

	public void setDurata(Integer durata) {
		this.durata = durata;
	}

	public Integer getTurniPartita() {
		return turniPartita;
	}

	public void setTurniPartita(Integer turniPartita) {
		this.turniPartita = turniPartita;
	}

	public String getColore(String player){
		Set<String> giocatori = risultati.keySet();
		String colore = null;
		byte index = 1;
		for (String giocatore: giocatori){
			if (giocatore.equalsIgnoreCase(player)) break;
			index++;
		}
		switch (index) {
		case 1:
			colore = "GIALLO";
			break;
		case 2:
			colore = "ROSSO";
			break;
		case 3:
			colore = "VERDE";
			break;
		case 4:
			colore = "BLU";
			break;
		}
		return colore;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idPartita == null) ? 0 : idPartita.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Partita other = (Partita) obj;
		if (idPartita == null) {
			if (other.idPartita != null)
				return false;
		} else if (!idPartita.equals(other.idPartita))
			return false;
		return true;
	}

	public String getVincitore() {
		return vincitore;
	}
	public void setVincitore(String vincitore) {
		this.vincitore = vincitore;
	}
	
	public Integer getPunteggioVincitore() {
		if (vincitore != null){
			punteggioVincitore = risultati.get(vincitore);
		}
		return punteggioVincitore;
	}

	public void setPunteggioVincitore(Integer punteggioVincitore) {
		this.punteggioVincitore = punteggioVincitore;
	}

	public boolean isVincitore(String giocatore){
		return giocatore.equals(vincitore);
	}
	public Date getDataDiInizio() {
		return dataDiInizio;
	}
	public void setDataDiInizio(Date dataDiInizio) {
		this.dataDiInizio = dataDiInizio;
	}
	public Date getDataDiFine() {
		return dataDiFine;
	}
	public void setDataDiFine(Date dataDiFine) {
		this.dataDiFine = dataDiFine;
	}
	
	public List<Azione> getAzioniLog() {
		return azioniLog;
	}

	public void setAzioniLog(List<Azione> azioniLog) {
		this.azioniLog = azioniLog;
	}

	public String toString(){
		String descrizione = idPartita.toString();
		descrizione += " "+Utils.DATA_INIZIO.format(dataDiInizio)+" Vinta da "+vincitore;
		descrizione += "\n" + risultati; 
		descrizione += "\n" + azioniLog; 
//		Set<String> giocatori = risultati.keySet();
//		for (String giocatore: giocatori){
//			descrizione += "\t"+giocatore;
//			descrizione += "\t"+risultati.get(giocatore);
//		}
		return descrizione;
	}
	
	
}
