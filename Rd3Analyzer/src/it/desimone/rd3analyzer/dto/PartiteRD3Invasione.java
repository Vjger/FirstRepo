package it.desimone.rd3analyzer.dto;

import it.desimone.rd3analyzer.azioni.Invasione;

public class PartiteRD3Invasione extends PartiteRD3CommonData{

	private String territorioInvasore;
	private String territorioInvaso;
	private Integer numeroArmateInvasori;
	
	
	public PartiteRD3Invasione(Long idPartita, Invasione invasione){
		this.idPartita = idPartita;
		this.logId = invasione.getLogId();
		this.coloreGiocatore = invasione.getGiocatoreCheAgisce().getColore();
		this.time = invasione.getTime();
		this.territorioInvasore = invasione.getTerritorioInvasore().getNomeTerritorio();
		this.territorioInvaso = invasione.getTerritorioInvaso().getNomeTerritorio();
		this.numeroArmateInvasori  = (int) invasione.getNumeroDiArmateInvasori();
	}


	public String getTerritorioInvasore() {
		return territorioInvasore;
	}


	public void setTerritorioInvasore(String territorioInvasore) {
		this.territorioInvasore = territorioInvasore;
	}


	public String getTerritorioInvaso() {
		return territorioInvaso;
	}


	public void setTerritorioInvaso(String territorioInvaso) {
		this.territorioInvaso = territorioInvaso;
	}


	public Integer getNumeroArmateInvasori() {
		return numeroArmateInvasori;
	}


	public void setNumeroArmateInvasori(Integer numeroArmateInvasori) {
		this.numeroArmateInvasori = numeroArmateInvasori;
	}


}
