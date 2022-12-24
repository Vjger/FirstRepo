package it.desimone.rd3analyzer.azioni;

import it.desimone.rd3analyzer.RigaLog;
import it.desimone.rd3analyzer.Territorio;

public class RicezioneCarta extends AbstractAzione {

	public RicezioneCarta(RigaLog rigaLog) {
		super(rigaLog);
	}

	private Territorio cartaRicevuta;

	@Override
	public TipoAzione getTipoAzione() {
		return TipoAzione.RICEZIONE_CARTA;
	}

	public Territorio getCartaRicevuta() {
		return cartaRicevuta;
	}

	public void setCartaRicevuta(Territorio cartaRicevuta) {
		this.cartaRicevuta = cartaRicevuta;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\nIl giocatore "+getGiocatoreCheAgisce()+" ha ricevuto la carta "+cartaRicevuta;
	}
}
