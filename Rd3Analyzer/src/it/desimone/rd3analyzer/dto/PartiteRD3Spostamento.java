package it.desimone.rd3analyzer.dto;

import it.desimone.rd3analyzer.azioni.Spostamento;

public class PartiteRD3Spostamento extends PartiteRD3CommonData{

	private String territorioProvenienza;
	private String territorioArrivo;
	private Integer numeroArmateSpostate;	
	
	public PartiteRD3Spostamento(Long idPartita, Spostamento spostamento){
		this.idPartita = idPartita;
		this.logId = spostamento.getLogId();
		this.coloreGiocatore = spostamento.getGiocatoreCheAgisce().getColore();
		this.time = spostamento.getTime();
		this.territorioProvenienza = spostamento.getTerritorioDiProvenienza().getNomeTerritorio();
		this.territorioArrivo = spostamento.getTerritorioDiArrivo().getNomeTerritorio();
		this.numeroArmateSpostate  = (int) spostamento.getNumeroDiArmateSpostate();
	}

	public String getTerritorioProvenienza() {
		return territorioProvenienza;
	}

	public void setTerritorioProvenienza(String territorioProvenienza) {
		this.territorioProvenienza = territorioProvenienza;
	}

	public String getTerritorioArrivo() {
		return territorioArrivo;
	}

	public void setTerritorioArrivo(String territorioArrivo) {
		this.territorioArrivo = territorioArrivo;
	}

	public Integer getNumeroArmateSpostate() {
		return numeroArmateSpostate;
	}

	public void setNumeroArmateSpostate(Integer numeroArmateSpostate) {
		this.numeroArmateSpostate = numeroArmateSpostate;
	}


}
