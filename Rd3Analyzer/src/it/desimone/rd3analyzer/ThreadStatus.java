package it.desimone.rd3analyzer;

import it.desimone.rd3analyzer.database.ThreadStatusDao;

public class ThreadStatus {

	private String status;
	private String dataAggiornamento;
	
	public ThreadStatus(String status, String dataAggiornamento){
		this.status = status;
		this.dataAggiornamento = dataAggiornamento;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(String dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	
	public boolean isActive(){
		return status.equals(ThreadStatusDao.ACTIVE_STATUS);
	}
	public boolean isSuspended(){
		return status.equals(ThreadStatusDao.SUSPENDED_STATUS);
	}
	public boolean isStopped(){
		return status.equals(ThreadStatusDao.STOPPED_STATUS);
	}
	
	public String getStatoThread(){
		String result = null;
		if (isActive()){
			result = "ATTIVO";
		}else if (isSuspended()){
			result = "SOSPESO";
		}else if (isStopped()){
			result = "TERMINATO";
		}
		return result;
	}
}
