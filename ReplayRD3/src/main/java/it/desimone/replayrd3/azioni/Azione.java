package it.desimone.replayrd3.azioni;

import java.io.Serializable;

import it.desimone.replayrd3.ColoreGiocatore;

public interface Azione extends Serializable{

	public Integer getLogId();
	public void setLogId(Integer logId);
	public String getTime();
	public void setTime(String time);
	
	public void setGiocatoreCheAgisce(ColoreGiocatore coloreGiocatore);
	public ColoreGiocatore getGiocatoreCheAgisce();
	public TipoAzione getTipoAzione();
}
