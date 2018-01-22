package it.desimone.gsheets.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TorneiRow extends AbstractSheetRow {
	
	static class ColPosition{
		public static final Integer ID_TORNEO 		= 1;
		public static final Integer NOME_TORNEO 	= 2;
		public static final Integer ORGANIZZATORE 	= 3;
		public static final Integer SEDE 			= 4;
		public static final Integer START_DATE 		= 5;
		public static final Integer END_DATE 		= 6;
		public static final Integer TIPO_TORNEO 	= 7;
		public static final Integer NUMERO_TURNI 	= 8;
		public static final Integer NOTE 			= 9;
	}
	
	private String idTorneo;
	private String nomeTorneo;
	private String organizzatore;
	private String sede;
	private Date startDate;
	private Date endDate;
	private String tipoTorneo;
	private Integer numeroTurni;
	private String note;

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
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


	public List<Object> getData() {
		List<Object> data = new ArrayList<Object>();
		data.set(ColPosition.ID_TORNEO -1, idTorneo);
		data.set(ColPosition.NOME_TORNEO -1, nomeTorneo);
		data.set(ColPosition.SEDE -1, sede);
		data.set(ColPosition.TIPO_TORNEO -1, tipoTorneo);
		data.set(ColPosition.START_DATE -1, startDate);
		data.set(ColPosition.END_DATE -1, endDate);
		data.set(ColPosition.ORGANIZZATORE -1, organizzatore);
		data.set(ColPosition.NUMERO_TURNI -1, numeroTurni);
		data.set(ColPosition.NOTE -1, note);
		return data;
	}

	public void setData(List<Object> data) {
		if (data == null || data.isEmpty()) return;
		
		idTorneo 		= (String) data.get(ColPosition.ID_TORNEO -1);
		nomeTorneo 		= (String) data.get(ColPosition.NOME_TORNEO -1);
		sede 			= (String) data.get(ColPosition.SEDE -1);
		tipoTorneo 		= (String) data.get(ColPosition.TIPO_TORNEO -1);
		startDate 		= (Date) data.get(ColPosition.START_DATE -1);
		endDate 		= (Date) data.get(ColPosition.END_DATE -1);
		organizzatore 	= (String) data.get(ColPosition.ORGANIZZATORE -1);
		numeroTurni 	= (Integer) data.get(ColPosition.NUMERO_TURNI -1);
		note 			= (String) data.get(ColPosition.NOTE -1);
	}

	public List<Integer> keyCols(){
		List<Integer> keyCols = new ArrayList<Integer>();
		keyCols.add(ColPosition.ORGANIZZATORE);
		keyCols.add(ColPosition.START_DATE);
		return keyCols;
	}
	
}
