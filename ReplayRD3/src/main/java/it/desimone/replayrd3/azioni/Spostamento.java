package it.desimone.replayrd3.azioni;

import it.desimone.replayrd3.RigaLog;
import it.desimone.replayrd3.Territorio;

public class Spostamento extends AbstractAzione {

	public Spostamento(RigaLog rigaLog) {
		super(rigaLog);
	}

	private Territorio territorioDiProvenienza;
	private Territorio territorioDiArrivo;
	private Short numeroDiArmateSpostate;
	
	@Override
	public TipoAzione getTipoAzione() {
		return TipoAzione.SPOSTAMENTO;
	}

	public Territorio getTerritorioDiProvenienza() {
		return territorioDiProvenienza;
	}

	public void setTerritorioDiProvenienza(Territorio territorioDiProvenienza) {
		this.territorioDiProvenienza = territorioDiProvenienza;
	}

	public Territorio getTerritorioDiArrivo() {
		return territorioDiArrivo;
	}

	public void setTerritorioDiArrivo(Territorio territorioDiArrivo) {
		this.territorioDiArrivo = territorioDiArrivo;
	}

	public Short getNumeroDiArmateSpostate() {
		return numeroDiArmateSpostate;
	}

	public void setNumeroDiArmateSpostate(Short numeroDiArmateSpostate) {
		this.numeroDiArmateSpostate = numeroDiArmateSpostate;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\nIl giocatore "+getGiocatoreCheAgisce()+" ha spostato "+numeroDiArmateSpostate+" armate da "+territorioDiProvenienza+" a "+territorioDiArrivo;
	}
	
}
