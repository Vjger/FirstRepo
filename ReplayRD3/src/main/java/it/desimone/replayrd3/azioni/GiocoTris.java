package it.desimone.replayrd3.azioni;

import it.desimone.replayrd3.RigaLog;
import it.desimone.replayrd3.Territorio;

public class GiocoTris extends AbstractAzione {

	public GiocoTris(RigaLog rigaLog) {
		super(rigaLog);
	}

	private Territorio[] carteTris;
	private Short valoreTris;

	@Override
	public TipoAzione getTipoAzione() {
		return TipoAzione.GIOCO_TRIS;
	}

	public Territorio[] getCarteTris() {
		return carteTris;
	}

	public void setCarteTris(Territorio[] carteTris) {
		this.carteTris = carteTris;
	}

	public Short getValoreTris() {
		return valoreTris;
	}

	public void setValoreTris(Short valoreTris) {
		this.valoreTris = valoreTris;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\nIl giocatore "+getGiocatoreCheAgisce()+" ha giocato un tris di "+valoreTris+" armate con le seguenti carte: "+trisToString();
	}

	private String trisToString(){
		String result = "";
		for (Territorio territorio: carteTris){
			result += territorio.toString()+" ";
		}
		
		return result;
	}
}
