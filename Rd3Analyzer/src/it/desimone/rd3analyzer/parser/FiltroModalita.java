package it.desimone.rd3analyzer.parser;

public enum FiltroModalita {
	
	 TUTTE ("tutte")
	,CHALLENGE ("challenge")
	,DIGITAL ("digital")
	,ULTRADIGITAL ("ultradigital")
	,PRESTICHALLENGE ("prestichallenge")
	,PRESTIGIOUS ("prestigious")
	,TRESTIGE ("trestige")
	,PRESTIGE ("prestige");
	
	private String codiceRicerca;
	private FiltroModalita(String codiceRicerca){
		this.codiceRicerca = codiceRicerca;
	}
	public String getCodiceRicerca() {
		return codiceRicerca;
	}
	public void setCodiceRicerca(String codiceRicerca) {
		this.codiceRicerca = codiceRicerca;
	}
	
	public static FiltroModalita getFiltroByDescrizione(String descrizione){
		FiltroModalita result = null;
		if (descrizione.equalsIgnoreCase("challenge")){
			result = CHALLENGE;
		}else if (descrizione.equalsIgnoreCase("digital")){
			result = DIGITAL;
		}else if (descrizione.equalsIgnoreCase("prestichallenge")){
			result = PRESTICHALLENGE;
		}else if (descrizione.equalsIgnoreCase("prestigious")){
			result = PRESTIGIOUS;
		}else if (descrizione.equalsIgnoreCase("trestige")){
			result = TRESTIGE;
		}else if (descrizione.equalsIgnoreCase("prestige")){
			result = PRESTIGE;
		}else if (descrizione.equalsIgnoreCase("ultradigital")){
			result = ULTRADIGITAL;
		}else{
			throw new IllegalArgumentException("Valore per la descrizione della partita non previsto: "+descrizione);
		}
		return result;
	}
}
