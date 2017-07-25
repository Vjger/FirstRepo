package it.desimone.rd3analyzer.azioni;

import it.desimone.rd3analyzer.RigaLog;
import it.desimone.rd3analyzer.Territorio;

public class Rinforzo extends AbstractAzione {
	
	public Rinforzo(RigaLog rigaLog) {
		super(rigaLog);
	}

	private Territorio territorioRinforzato;
	private Short numeroDiRinforzi;

	@Override
	public TipoAzione getTipoAzione() {
		return TipoAzione.RINFORZO;
	}

	public Territorio getTerritorioRinforzato() {
		return territorioRinforzato;
	}

	public void setTerritorioRinforzato(Territorio territorioRinforzato) {
		this.territorioRinforzato = territorioRinforzato;
	}

	public Short getNumeroDiRinforzi() {
		return numeroDiRinforzi;
	}

	public void setNumeroDiRinforzi(Short numeroDiRinforzi) {
		this.numeroDiRinforzi = numeroDiRinforzi;
	}

	@Override
	public String toString() {
		return "Il giocatore "+getGiocatoreCheAgisce()+" ha rinforzato "+territorioRinforzato+" con "+numeroDiRinforzi+" armate";
	}

	
}
