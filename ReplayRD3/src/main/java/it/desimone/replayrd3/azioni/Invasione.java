package it.desimone.replayrd3.azioni;

import it.desimone.replayrd3.RigaLog;
import it.desimone.replayrd3.Territorio;

public class Invasione extends AbstractAzione {

	public Invasione(RigaLog rigaLog) {
		super(rigaLog);
	}

	private Territorio territorioInvasore;
	private Territorio territorioInvaso;
	private Short numeroDiArmateInvasori;
	
	@Override
	public TipoAzione getTipoAzione() {
		return TipoAzione.INVASIONE;
	}

	public Territorio getTerritorioInvasore() {
		return territorioInvasore;
	}

	public void setTerritorioInvasore(Territorio territorioInvasore) {
		this.territorioInvasore = territorioInvasore;
	}

	public Territorio getTerritorioInvaso() {
		return territorioInvaso;
	}

	public void setTerritorioInvaso(Territorio territorioInvaso) {
		this.territorioInvaso = territorioInvaso;
	}

	public Short getNumeroDiArmateInvasori() {
		return numeroDiArmateInvasori;
	}

	public void setNumeroDiArmateInvasori(Short numeroDiArmateInvasori) {
		this.numeroDiArmateInvasori = numeroDiArmateInvasori;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\nIl giocatore "+getGiocatoreCheAgisce()+" ha invaso da "+territorioInvasore+" a "+territorioInvaso+" con "+numeroDiArmateInvasori+" armate";
	}

}
