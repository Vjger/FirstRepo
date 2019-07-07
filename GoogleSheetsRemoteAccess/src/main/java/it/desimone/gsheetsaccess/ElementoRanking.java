package it.desimone.gsheetsaccess;

import it.desimone.gsheetsaccess.gsheets.dto.TorneiRow;

public class ElementoRanking {
	private TorneiRow torneo;
	private Integer posizione;
	private Double score;
	
	public TorneiRow getTorneo() {
		return torneo;
	}
	public void setTorneo(TorneiRow torneo) {
		this.torneo = torneo;
	}
	public Integer getPosizione() {
		return posizione;
	}
	public void setPosizione(Integer posizione) {
		this.posizione = posizione;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((torneo == null) ? 0 : torneo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementoRanking other = (ElementoRanking) obj;
		if (torneo == null) {
			if (other.torneo != null)
				return false;
		} else if (!torneo.equals(other.torneo))
			return false;
		return true;
	}
}
