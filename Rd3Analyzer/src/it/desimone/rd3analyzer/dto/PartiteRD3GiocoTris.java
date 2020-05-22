package it.desimone.rd3analyzer.dto;

import it.desimone.rd3analyzer.azioni.GiocoTris;

public class PartiteRD3GiocoTris extends PartiteRD3CommonData{

	private String territorioTris1;
	private String territorioTris2;
	private String territorioTris3;
	private Integer valoreTris;
	
	public PartiteRD3GiocoTris(Long idPartita, GiocoTris giocoTris){
		this.idPartita = idPartita;
		this.logId = giocoTris.getLogId();
		this.coloreGiocatore = giocoTris.getGiocatoreCheAgisce().getColore();
		this.time = giocoTris.getTime();
		this.territorioTris1 = giocoTris.getCarteTris()[0].getNomeTerritorio();
		this.territorioTris2 = giocoTris.getCarteTris()[1].getNomeTerritorio();
		this.territorioTris3 = giocoTris.getCarteTris()[2].getNomeTerritorio();
		this.valoreTris = (int)giocoTris.getValoreTris();
	}

	public String getTerritorioTris1() {
		return territorioTris1;
	}

	public void setTerritorioTris1(String territorioTris1) {
		this.territorioTris1 = territorioTris1;
	}

	public String getTerritorioTris2() {
		return territorioTris2;
	}

	public void setTerritorioTris2(String territorioTris2) {
		this.territorioTris2 = territorioTris2;
	}

	public String getTerritorioTris3() {
		return territorioTris3;
	}

	public void setTerritorioTris3(String territorioTris3) {
		this.territorioTris3 = territorioTris3;
	}

	public Integer getValoreTris() {
		return valoreTris;
	}

	public void setValoreTris(Integer valoreTris) {
		this.valoreTris = valoreTris;
	}

	
}
