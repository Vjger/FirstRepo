package it.desimone.rd3analyzer.azioni;

import it.desimone.rd3analyzer.RigaLog;
import it.desimone.rd3analyzer.Territorio;

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
		return "Il giocatore "+getGiocatoreCheAgisce()+" ha spostato "+numeroDiArmateSpostate+" armate da "+territorioDiProvenienza+" a "+territorioDiArrivo;
	}
	
}
