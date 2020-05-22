package it.desimone.rd3analyzer.dto;

import it.desimone.rd3analyzer.azioni.Attacco;

public class PartiteRD3Attacchi extends PartiteRD3CommonData{

	private String territorioAttaccante;
	private String territorioAttaccato;
	private Integer dado1Attacco;
	private Integer dado1Difesa;
	private Integer dado2Attacco;
	private Integer dado2Difesa;
	private Integer dado3Attacco;
	private Integer dado3Difesa;
	
	
	public PartiteRD3Attacchi(Long idPartita, Attacco attacco){
		this.idPartita = idPartita;
		this.logId = attacco.getLogId();
		this.coloreGiocatore = attacco.getGiocatoreCheAgisce().getColore();
		this.time = attacco.getTime();
		this.territorioAttaccante = attacco.getTerritorioAttaccante().getNomeTerritorio();
		this.territorioAttaccato  = attacco.getTerritorioAttaccato().getNomeTerritorio();
		this.dado1Attacco = (int)attacco.getDadiAttacco()[0];
		this.dado1Difesa = (int)attacco.getDadiDifesa()[0];
		if (attacco.getDadiAttacco().length >= 2){
			this.dado2Attacco = (int)attacco.getDadiAttacco()[1];
		}
		if (attacco.getDadiDifesa().length >=2){
			this.dado2Difesa = (int)attacco.getDadiDifesa()[1];
		}
		if (attacco.getDadiAttacco().length == 3){
			this.dado3Attacco = (int)attacco.getDadiAttacco()[2];
		}
		if (attacco.getDadiDifesa().length ==3){
			this.dado3Difesa = (int)attacco.getDadiDifesa()[2];
		}
	}

	public String getTerritorioAttaccante() {
		return territorioAttaccante;
	}
	public void setTerritorioAttaccante(String territorioAttaccante) {
		this.territorioAttaccante = territorioAttaccante;
	}
	public String getTerritorioAttaccato() {
		return territorioAttaccato;
	}
	public void setTerritorioAttaccato(String territorioAttaccato) {
		this.territorioAttaccato = territorioAttaccato;
	}
	public Integer getDado1Attacco() {
		return dado1Attacco;
	}
	public void setDado1Attacco(Integer dado1Attacco) {
		this.dado1Attacco = dado1Attacco;
	}
	public Integer getDado1Difesa() {
		return dado1Difesa;
	}
	public void setDado1Difesa(Integer dado1Difesa) {
		this.dado1Difesa = dado1Difesa;
	}
	public Integer getDado2Attacco() {
		return dado2Attacco;
	}
	public void setDado2Attacco(Integer dado2Attacco) {
		this.dado2Attacco = dado2Attacco;
	}
	public Integer getDado2Difesa() {
		return dado2Difesa;
	}
	public void setDado2Difesa(Integer dado2Difesa) {
		this.dado2Difesa = dado2Difesa;
	}
	public Integer getDado3Attacco() {
		return dado3Attacco;
	}
	public void setDado3Attacco(Integer dado3Attacco) {
		this.dado3Attacco = dado3Attacco;
	}
	public Integer getDado3Difesa() {
		return dado3Difesa;
	}
	public void setDado3Difesa(Integer dado3Difesa) {
		this.dado3Difesa = dado3Difesa;
	}
	
	
	
}
