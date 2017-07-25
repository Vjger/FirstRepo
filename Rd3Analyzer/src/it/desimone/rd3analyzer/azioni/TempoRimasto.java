package it.desimone.rd3analyzer.azioni;

import it.desimone.rd3analyzer.RigaLog;

public class TempoRimasto extends AbstractAzione {

	public TempoRimasto(RigaLog rigaLog) {
		super(rigaLog);
	}

	private Short minutiRimasti;
	private Short secondiRimasti;

	@Override
	public TipoAzione getTipoAzione() {
		return TipoAzione.TEMPO_RIMASTO;
	}

	public Short getMinutiRimasti() {
		return minutiRimasti;
	}

	public void setMinutiRimasti(Short minutiRimasti) {
		this.minutiRimasti = minutiRimasti;
	}

	public Short getSecondiRimasti() {
		return secondiRimasti;
	}

	public void setSecondiRimasti(Short secondiRimasti) {
		this.secondiRimasti = secondiRimasti;
	}
	
	@Override
	public String toString() {
		return "Al giocatore "+getGiocatoreCheAgisce()+" son rimasti "+minutiRimasti+" minuti e "+secondiRimasti+" secondi";
	}
	
}
