package it.desimone.rd3analyzer.dto;

import it.desimone.rd3analyzer.azioni.TempoRimasto;

public class PartiteRD3TempoRimasto extends PartiteRD3CommonData{

	private Integer minutiRimasti;
	private Integer secondiRimasti;
	
	public PartiteRD3TempoRimasto(Long idPartita, TempoRimasto tempoRimasto){
		this.idPartita = idPartita;
		this.logId = tempoRimasto.getLogId();
		this.coloreGiocatore = tempoRimasto.getGiocatoreCheAgisce().getColore();
		this.time = tempoRimasto.getTime();
		this.minutiRimasti = (int) tempoRimasto.getMinutiRimasti();
		this.secondiRimasti = (int) tempoRimasto.getSecondiRimasti();
	}

	public Integer getMinutiRimasti() {
		return minutiRimasti;
	}

	public void setMinutiRimasti(Integer minutiRimasti) {
		this.minutiRimasti = minutiRimasti;
	}

	public Integer getSecondiRimasti() {
		return secondiRimasti;
	}

	public void setSecondiRimasti(Integer secondiRimasti) {
		this.secondiRimasti = secondiRimasti;
	}


}
