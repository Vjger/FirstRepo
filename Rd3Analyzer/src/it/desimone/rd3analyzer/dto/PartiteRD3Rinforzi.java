package it.desimone.rd3analyzer.dto;

import it.desimone.rd3analyzer.azioni.Rinforzo;

public class PartiteRD3Rinforzi extends PartiteRD3CommonData{

	private String territorioRinforzato;
	private Integer numeroRinforzi;
	
	
	public PartiteRD3Rinforzi(Long idPartita, Rinforzo rinforzo){
		this.idPartita = idPartita;
		this.logId = rinforzo.getLogId();
		this.coloreGiocatore = rinforzo.getGiocatoreCheAgisce().getColore();
		this.time = rinforzo.getTime();
		this.territorioRinforzato = rinforzo.getTerritorioRinforzato().getNomeTerritorio();
		this.numeroRinforzi  = (int) rinforzo.getNumeroDiRinforzi();
	}

	public String getTerritorioRinforzato() {
		return territorioRinforzato;
	}

	public void setTerritorioRinforzato(String territorioRinforzato) {
		this.territorioRinforzato = territorioRinforzato;
	}

	public Integer getNumeroRinforzi() {
		return numeroRinforzi;
	}

	public void setNumeroRinforzi(Integer numeroRinforzi) {
		this.numeroRinforzi = numeroRinforzi;
	}

}
