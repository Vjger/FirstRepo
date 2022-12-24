package it.desimone.rd3analyzer.dto;

import it.desimone.rd3analyzer.azioni.RicezioneCarta;

public class PartiteRD3RicezioneCarta extends PartiteRD3CommonData{

	private String cartaRicevuta;
	
	public PartiteRD3RicezioneCarta(Long idPartita, RicezioneCarta ricezioneCarta){
		this.idPartita = idPartita;
		this.logId = ricezioneCarta.getLogId();
		this.coloreGiocatore = ricezioneCarta.getGiocatoreCheAgisce().getColore();
		this.time = ricezioneCarta.getTime();
		this.cartaRicevuta = ricezioneCarta.getCartaRicevuta() != null ? ricezioneCarta.getCartaRicevuta().getNomeTerritorio() : null;
	}

	public String getCartaRicevuta() {
		return cartaRicevuta;
	}

	public void setCartaRicevuta(String cartaRicevuta) {
		this.cartaRicevuta = cartaRicevuta;
	}


}
