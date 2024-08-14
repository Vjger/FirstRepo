package it.desimone.replayrd3.azioni;

import it.desimone.replayrd3.RigaLog;
import it.desimone.replayrd3.Territorio;

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
