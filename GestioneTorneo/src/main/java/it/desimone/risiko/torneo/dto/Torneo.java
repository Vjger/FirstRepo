package it.desimone.risiko.torneo.dto;

import java.util.List;

public class Torneo {

	private SchedaTorneo schedaTorneo;
	
	private List<GiocatoreDTO> partecipanti;
	
	private List<SchedaTurno> schedeTurno;
	
	private SchedaClassifica schedaClassifica;

	public SchedaTorneo getSchedaTorneo() {
		return schedaTorneo;
	}

	public void setSchedaTorneo(SchedaTorneo schedaTorneo) {
		this.schedaTorneo = schedaTorneo;
	}

	public List<GiocatoreDTO> getPartecipanti() {
		return partecipanti;
	}

	public void setPartecipanti(List<GiocatoreDTO> partecipanti) {
		this.partecipanti = partecipanti;
	}

	public List<SchedaTurno> getSchedeTurno() {
		return schedeTurno;
	}

	public void setSchedeTurno(List<SchedaTurno> schedeTurno) {
		this.schedeTurno = schedeTurno;
	}

	public SchedaClassifica getSchedaClassifica() {
		return schedaClassifica;
	}

	public void setSchedaClassifica(SchedaClassifica schedaClassifica) {
		this.schedaClassifica = schedaClassifica;
	}
	
}
