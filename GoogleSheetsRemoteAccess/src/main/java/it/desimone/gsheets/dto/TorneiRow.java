package it.desimone.gsheets.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TorneiRow extends AbstractSheetRow {
	
	public static final String SHEET_TORNEI_NAME 	= "TORNEI";
	public static final String SHEET_GIOCATORI_NAME = "GIOCATORI";
	
	static class ColPosition{
		//zero-based
		public static final Integer ID_TORNEO 		= 0;
		public static final Integer NOME_TORNEO 	= 1;
		public static final Integer ORGANIZZATORE 	= 2;
		public static final Integer SEDE 			= 3;
		public static final Integer START_DATE 		= 4;
		public static final Integer END_DATE 		= 5;
		public static final Integer TIPO_TORNEO 	= 6;
		public static final Integer NUMERO_TURNI 	= 7;
		public static final Integer NOTE 			= 8;
		public static final Integer UPDATE_TIME		= 9;
	}
	
	private String idTorneo;
	private String nomeTorneo;
	private String organizzatore;
	private String sede;
	private String startDate;
	private String endDate;
	private String tipoTorneo;
	private Integer numeroTurni;
	private String note;
	private String updateTime;

	public String getIdTorneo() {
		return idTorneo;
	}

	public void setIdTorneo(String idTorneo) {
		this.idTorneo = idTorneo;
	}

	public String getNomeTorneo() {
		return nomeTorneo;
	}

	public void setNomeTorneo(String nomeTorneo) {
		this.nomeTorneo = nomeTorneo;
	}

	public String getOrganizzatore() {
		return organizzatore;
	}

	public void setOrganizzatore(String organizzatore) {
		this.organizzatore = organizzatore;
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTipoTorneo() {
		return tipoTorneo;
	}

	public void setTipoTorneo(String tipoTorneo) {
		this.tipoTorneo = tipoTorneo;
	}

	public Integer getNumeroTurni() {
		return numeroTurni;
	}

	public void setNumeroTurni(Integer numeroTurni) {
		this.numeroTurni = numeroTurni;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public List<Object> getData() {
		List<Object> data = Arrays.asList(new Object[10]);
		Collections.fill(data, "");
		if (idTorneo != null) data.set(ColPosition.ID_TORNEO, idTorneo);
		if (nomeTorneo != null) data.set(ColPosition.NOME_TORNEO, nomeTorneo);
		if (sede != null) data.set(ColPosition.SEDE, sede);
		if (tipoTorneo != null) data.set(ColPosition.TIPO_TORNEO, tipoTorneo);
		if (startDate != null) data.set(ColPosition.START_DATE, startDate);
		if (endDate != null) data.set(ColPosition.END_DATE, endDate);
		if (organizzatore != null) data.set(ColPosition.ORGANIZZATORE, organizzatore);
		if (numeroTurni != null) data.set(ColPosition.NUMERO_TURNI, numeroTurni);
		if (note != null) data.set(ColPosition.NOTE, note);
		if (updateTime != null) data.set(ColPosition.UPDATE_TIME, updateTime);
		return data;
	}

	public void setData(List<Object> data) {
		if (data == null || data.isEmpty()) return;
		
		idTorneo 		= (String) data.get(ColPosition.ID_TORNEO);
		nomeTorneo 		= (String) data.get(ColPosition.NOME_TORNEO);
		sede 			= (String) data.get(ColPosition.SEDE);
		tipoTorneo 		= (String) data.get(ColPosition.TIPO_TORNEO);
		startDate 		= (String) data.get(ColPosition.START_DATE);
		endDate 		= (String) data.get(ColPosition.END_DATE);
		organizzatore 	= (String) data.get(ColPosition.ORGANIZZATORE);
		numeroTurni 	= (Integer) data.get(ColPosition.NUMERO_TURNI);
		note 			= (String) data.get(ColPosition.NOTE);
		updateTime		= (String) data.get(ColPosition.UPDATE_TIME);
	}

	public List<Integer> keyCols(){
		List<Integer> keyCols = new ArrayList<Integer>();
		keyCols.add(ColPosition.ORGANIZZATORE);
		keyCols.add(ColPosition.START_DATE);
		return keyCols;
	}

}
