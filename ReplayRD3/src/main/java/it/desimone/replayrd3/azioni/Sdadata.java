package it.desimone.replayrd3.azioni;

import it.desimone.replayrd3.RigaLog;

public class Sdadata extends AbstractAzione {

	public Sdadata(RigaLog rigaLog) {
		super(rigaLog);
	}

	private Short chiusura;
	private Short sdadata;

	@Override
	public TipoAzione getTipoAzione() {
		return TipoAzione.SDADATA;
	}

	public Short getChiusura() {
		return chiusura;
	}

	public void setChiusura(Short chiusura) {
		this.chiusura = chiusura;
	}

	public Short getSdadata() {
		return sdadata;
	}

	public void setSdadata(Short sdadata) {
		this.sdadata = sdadata;
	}

	@Override
	public String toString() {
		return super.toString()+"\nSdadata [chiusura=" + chiusura + ", sdadata=" + sdadata + "]";
	}
	
}
