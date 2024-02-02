package it.desimone.gsheetsaccess.ranking;

import java.util.Date;
import java.util.List;

public class BlackListPlayer {

	private int idAnagrafica;
	private List<Integer> forbiddenYears;
	private Date startExclusion;
	private Date endExclusion;
	public int getIdAnagrafica() {
		return idAnagrafica;
	}
	public void setIdAnagrafica(int idAnagrafica) {
		this.idAnagrafica = idAnagrafica;
	}
	public List<Integer> getForbiddenYears() {
		return forbiddenYears;
	}
	public void setForbiddenYears(List<Integer> forbiddenYears) {
		this.forbiddenYears = forbiddenYears;
	}
	public Date getStartExclusion() {
		return startExclusion;
	}
	public void setStartExclusion(Date startExclusion) {
		this.startExclusion = startExclusion;
	}
	public Date getEndExclusion() {
		return endExclusion;
	}
	public void setEndExclusion(Date endExclusion) {
		this.endExclusion = endExclusion;
	}

	public boolean isForbiddenYear(int year){
		return forbiddenYears != null && forbiddenYears.contains(year);
	}
	
	public boolean isExcludedPeriod(Date start, Date end){
		return startExclusion != null && endExclusion != null && (start.after(endExclusion) || end.before(startExclusion));
	}
}
