package it.desimone.replayrd3;

public class RigaLog{
	private String idLog;
	private String timeLog;
	private String coloreLog;
	private String azioneLog;
	
	public String getIdLog() {
		return idLog;
	}
	public void setIdLog(String idLog) {
		this.idLog = idLog;
	}
	public String getTimeLog() {
		return timeLog;
	}
	public void setTimeLog(String timeLog) {
		this.timeLog = timeLog;
	}
	public String getColoreLog() {
		return coloreLog;
	}
	public void setColoreLog(String coloreLog) {
		this.coloreLog = coloreLog;
	}
	public String getAzioneLog() {
		return azioneLog;
	}
	public void setAzioneLog(String azioneLog) {
		this.azioneLog = azioneLog;
	}
	
	public String toString(){return idLog+"\t"+timeLog+"\t"+coloreLog+"\t"+azioneLog;}
}
