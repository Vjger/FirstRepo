package it.desimone.rd3analyzer.azioni;

import it.desimone.rd3analyzer.ColoreGiocatore;
import it.desimone.rd3analyzer.RigaLog;

public abstract class AbstractAzione implements Azione {

	private ColoreGiocatore coloreGiocatore;
	private Integer logId;
	private String time;
	
	public AbstractAzione(RigaLog rigaLog){
		String idLog = rigaLog.getIdLog().replaceAll("\\D","");
		setLogId(Integer.valueOf(idLog));
		setTime(rigaLog.getTimeLog());
		setGiocatoreCheAgisce(ColoreGiocatore.getColoreByDescrizione(rigaLog.getColoreLog().replaceAll("\\W","")));
	}
	
	@Override
	public Integer getLogId() {
		return logId;
	}

	@Override
	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	@Override
	public String getTime() {
		return time;
	}

	@Override
	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public void setGiocatoreCheAgisce(ColoreGiocatore coloreGiocatore) {
		this.coloreGiocatore = coloreGiocatore;	
	}
	
	@Override
	public ColoreGiocatore getGiocatoreCheAgisce() {
		return coloreGiocatore;
	}

	@Override
	public abstract TipoAzione getTipoAzione();


}
