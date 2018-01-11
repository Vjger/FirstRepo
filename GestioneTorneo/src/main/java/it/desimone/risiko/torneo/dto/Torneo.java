package it.desimone.risiko.torneo.dto;

import java.util.List;

public class Torneo {

	private SchedaTorneo schedaTorneo;
	
	private List<GiocatoreDTO> iscritti;
	
	private List<SchedaTurno> schedaTurno;
	
	private SchedaClassifica schedaClassifica;

	public SchedaTorneo getSchedaTorneo() {
		return schedaTorneo;
	}

	public void setSchedaTorneo(SchedaTorneo schedaTorneo) {
		this.schedaTorneo = schedaTorneo;
	}

	public List<GiocatoreDTO> getIscritti() {
		return iscritti;
	}

	public void setIscritti(List<GiocatoreDTO> iscritti) {
		this.iscritti = iscritti;
	}

	public List<SchedaTurno> getSchedaTurno() {
		return schedaTurno;
	}

	public void setSchedaTurno(List<SchedaTurno> schedaTurno) {
		this.schedaTurno = schedaTurno;
	}

	public SchedaClassifica getSchedaClassifica() {
		return schedaClassifica;
	}

	public void setSchedaClassifica(SchedaClassifica schedaClassifica) {
		this.schedaClassifica = schedaClassifica;
	}
	
}
