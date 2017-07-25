package it.desimone.rd3analyzer.parser;

public enum FiltroPeriodo {
	
	 TUTTO ("tutto")
	,ULTIME_24_H ("24h")
	,ULTIMI_7_GIORNI ("7gg")
	,ULTIMO_MESE ("mese")
	,ANNO_IN_CORSO ("anno");
	 
	private String codiceRicerca;
	private FiltroPeriodo(String codiceRicerca){
		this.codiceRicerca = codiceRicerca;
	}
	public String getCodiceRicerca() {
		return codiceRicerca;
	}
	public void setCodiceRicerca(String codiceRicerca) {
		this.codiceRicerca = codiceRicerca;
	}
}
