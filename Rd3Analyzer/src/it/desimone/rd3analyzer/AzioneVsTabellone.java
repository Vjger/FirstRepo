package it.desimone.rd3analyzer;

import it.desimone.rd3analyzer.azioni.Azione;

public class AzioneVsTabellone {
	
	private Azione azione;
	private Tabellone tabellone;
	
	public AzioneVsTabellone(Azione azione, Tabellone tabellone) {
		super();
		this.azione = azione;
		this.tabellone = tabellone;
	}

	public Azione getAzione() {
		return azione;
	}

	public void setAzione(Azione azione) {
		this.azione = azione;
	}

	public Tabellone getTabellone() {
		return tabellone;
	}

	public void setTabellone(Tabellone tabellone) {
		this.tabellone = tabellone;
	}	

	
}
