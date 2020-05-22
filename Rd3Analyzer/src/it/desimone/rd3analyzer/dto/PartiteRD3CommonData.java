package it.desimone.rd3analyzer.dto;

public abstract class PartiteRD3CommonData {
	
	protected Long idPartita;
	protected Integer logId;
	protected String coloreGiocatore;
	protected String time;

	public Long getIdPartita() {
		return idPartita;
	}
	public void setIdPartita(Long idPartita) {
		this.idPartita = idPartita;
	}
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public String getColoreGiocatore() {
		return coloreGiocatore;
	}
	public void setColoreGiocatore(String coloreGiocatore) {
		this.coloreGiocatore = coloreGiocatore;
	}
	public String getTime() {
		return time;
	}
	
}
