package it.desimone.gsheetsaccess.htmlpublisher;

import java.util.Objects;

public class MatchByYearAndClubKey {

	private String organizzatore;
	
	public MatchByYearAndClubKey(String organizzatore) {
		super();
		this.organizzatore = organizzatore;
	}
	public String getOrganizzatore() {
		return organizzatore;
	}
	public void setOrganizzatore(String organizzatore) {
		this.organizzatore = organizzatore;
	}
	@Override
	public int hashCode() {
		return Objects.hash(organizzatore);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatchByYearAndClubKey other = (MatchByYearAndClubKey) obj;
		return Objects.equals(organizzatore, other.organizzatore);
	}
	@Override
	public String toString() {
		return "MatchByYearAndClubKey [organizzatore=" + organizzatore + "]";
	}

	
	
}
