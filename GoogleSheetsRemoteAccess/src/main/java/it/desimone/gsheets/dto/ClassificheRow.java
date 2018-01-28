package it.desimone.gsheets.dto;

import it.desimone.gsheets.dto.PartitaRow.ColPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClassificheRow extends AbstractSheetRow {
	
	public static final String SHEET_CLASSIFICHE 	= "CLASSIFICHE";
	
	public static class ColPosition{
		//zero-based
		public static final Integer ID_TORNEO 		= 0;
		public static final Integer ID_GIOCATORE 	= 1;
		public static final Integer CLUB_GIOCATORE 	= 2;
		public static final Integer POSIZIONE 		= 3;
		public static final Integer PUNTI 			= 4;
		public static final Integer NUMERO_VITTORIE = 5;
		public static final Integer PARTITE_GIOCATE = 6;
		public static final Integer UPDATE_TIME		= 7;
	}
	
	private String idTorneo;
	private Integer idGiocatore;
	private String clubGiocatore;
	private Integer posizione;
	private Double punti;
	private Integer numeroVittorie;
	private Integer partiteGiocate;
	private String updateTime;

	public String getIdTorneo() {
		return idTorneo;
	}

	public void setIdTorneo(String idTorneo) {
		this.idTorneo = idTorneo;
	}

	public Integer getIdGiocatore() {
		return idGiocatore;
	}

	public void setIdGiocatore(Integer idGiocatore) {
		this.idGiocatore = idGiocatore;
	}

	public String getClubGiocatore() {
		return clubGiocatore;
	}

	public void setClubGiocatore(String clubGiocatore) {
		this.clubGiocatore = clubGiocatore;
	}

	public Integer getPosizione() {
		return posizione;
	}

	public void setPosizione(Integer posizione) {
		this.posizione = posizione;
	}

	public Double getPunti() {
		return punti;
	}

	public void setPunti(Double punti) {
		this.punti = punti;
	}

	public Integer getNumeroVittorie() {
		return numeroVittorie;
	}

	public void setNumeroVittorie(Integer numeroVittorie) {
		this.numeroVittorie = numeroVittorie;
	}

	public Integer getPartiteGiocate() {
		return partiteGiocate;
	}

	public void setPartiteGiocate(Integer partiteGiocate) {
		this.partiteGiocate = partiteGiocate;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public List<Object> getData() {
		List<Object> data = Arrays.asList(new Object[8]);
		Collections.fill(data, "");
		if (idTorneo != null) data.set(ColPosition.ID_TORNEO, idTorneo);
		if (idGiocatore != null) data.set(ColPosition.ID_GIOCATORE, idGiocatore);
		if (clubGiocatore != null) data.set(ColPosition.CLUB_GIOCATORE, clubGiocatore);
		if (posizione != null) data.set(ColPosition.POSIZIONE, posizione);
		if (punti != null) data.set(ColPosition.PUNTI, punti);
		if (numeroVittorie != null) data.set(ColPosition.NUMERO_VITTORIE, numeroVittorie);
		if (partiteGiocate != null) data.set(ColPosition.PARTITE_GIOCATE, partiteGiocate);
		if (updateTime != null) data.set(ColPosition.UPDATE_TIME, updateTime);
		return data;
	}

	public void setData(List<Object> data) {
		if (data == null || data.isEmpty()) return;
		
		idTorneo 		= (String) data.get(ColPosition.ID_TORNEO);
		idGiocatore 	= Integer.valueOf((String)data.get(ColPosition.ID_GIOCATORE));
		clubGiocatore 	= (String) data.get(ColPosition.CLUB_GIOCATORE);
		posizione 		= Integer.valueOf((String)data.get(ColPosition.POSIZIONE));
		punti 			= (Double) data.get(ColPosition.PUNTI);
		numeroVittorie 	= Integer.valueOf((String)data.get(ColPosition.NUMERO_VITTORIE));
		partiteGiocate 	= Integer.valueOf((String)data.get(ColPosition.PARTITE_GIOCATE));
		updateTime		= (String) data.get(ColPosition.UPDATE_TIME);
	}

	public List<Integer> keyCols(){
		List<Integer> keyCols = new ArrayList<Integer>();
		keyCols.add(ColPosition.ID_TORNEO);
		keyCols.add(ColPosition.ID_GIOCATORE);
		return keyCols;
	}

}
