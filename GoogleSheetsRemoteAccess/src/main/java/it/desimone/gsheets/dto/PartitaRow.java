package it.desimone.gsheets.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PartitaRow extends AbstractSheetRow {

	public static final String SHEET_PARTITE_NAME 	= "PARTITE";
	
	private String idTorneo;
	private Integer numeroTurno;
	private Integer numeroTavolo;
	private Integer idGiocatore1;
	private Double punteggioGiocatore1;
	private Integer idGiocatore2;
	private Double punteggioGiocatore2;
	private Integer idGiocatore3;
	private Double punteggioGiocatore3;
	private Integer idGiocatore4;
	private Double punteggioGiocatore4;
	private Integer idGiocatore5;
	private Double punteggioGiocatore5;
	private Integer idGiocatoreVincitore;
	
	static class ColPosition{
		//zero-based
		public static final Integer ID_TORNEO 				= 0;
		public static final Integer NUMERO_TURNO 			= 1;
		public static final Integer NUMERO_TAVOLO 			= 2;
		public static final Integer ID_GIOCATORE1			= 3;
		public static final Integer PUNTEGGIO_GIOCATORE1 	= 4;
		public static final Integer ID_GIOCATORE2			= 5;
		public static final Integer PUNTEGGIO_GIOCATORE2 	= 6;
		public static final Integer ID_GIOCATORE3			= 7;
		public static final Integer PUNTEGGIO_GIOCATORE3 	= 8;
		public static final Integer ID_GIOCATORE4			= 9;
		public static final Integer PUNTEGGIO_GIOCATORE4 	= 10;
		public static final Integer ID_GIOCATORE5			= 11;
		public static final Integer PUNTEGGIO_GIOCATORE5 	= 12;
		public static final Integer ID_GIOCATORE_VINCITORE	= 13;

	}
	
	public List<Object> getData() {
		List<Object> data = Arrays.asList(new Object[14]);
		Collections.fill(data, "");
		if (idTorneo != null) data.set(ColPosition.ID_TORNEO, idTorneo);
		if (numeroTurno != null) data.set(ColPosition.NUMERO_TURNO, numeroTurno);
		if (numeroTavolo != null) data.set(ColPosition.NUMERO_TAVOLO, numeroTavolo);
		if (idGiocatore1 != null) data.set(ColPosition.ID_GIOCATORE1, idGiocatore1);
		if (punteggioGiocatore1 != null) data.set(ColPosition.PUNTEGGIO_GIOCATORE1, punteggioGiocatore1);
		if (idGiocatore2 != null) data.set(ColPosition.ID_GIOCATORE2, idGiocatore2);
		if (punteggioGiocatore2 != null) data.set(ColPosition.PUNTEGGIO_GIOCATORE2, punteggioGiocatore2);
		if (idGiocatore3 != null) data.set(ColPosition.ID_GIOCATORE3, idGiocatore3);
		if (punteggioGiocatore3 != null) data.set(ColPosition.PUNTEGGIO_GIOCATORE3, punteggioGiocatore3);
		if (idGiocatore4 != null) data.set(ColPosition.ID_GIOCATORE4, idGiocatore4);
		if (punteggioGiocatore4 != null) data.set(ColPosition.PUNTEGGIO_GIOCATORE4, punteggioGiocatore4);
		if (idGiocatore5 != null) data.set(ColPosition.ID_GIOCATORE5, idGiocatore5);
		if (punteggioGiocatore5 != null) data.set(ColPosition.PUNTEGGIO_GIOCATORE5, punteggioGiocatore5);
		if (idGiocatoreVincitore != null) data.set(ColPosition.ID_GIOCATORE_VINCITORE, idGiocatoreVincitore);
		return data;
	}

	public void setData(List<Object> data) {
		if (data == null || data.isEmpty()) return;
		
		idTorneo 			= (String) data.get(ColPosition.ID_TORNEO);
		numeroTurno 		= (Integer) data.get(ColPosition.NUMERO_TURNO);
		numeroTavolo 		= (Integer) data.get(ColPosition.NUMERO_TAVOLO);
		idGiocatore1 		= (Integer) data.get(ColPosition.ID_GIOCATORE1);
		punteggioGiocatore1 = (Double) data.get(ColPosition.PUNTEGGIO_GIOCATORE1);
		idGiocatore1 		= (Integer) data.get(ColPosition.ID_GIOCATORE1);
		punteggioGiocatore2 = (Double) data.get(ColPosition.PUNTEGGIO_GIOCATORE2);
		idGiocatore3 		= (Integer) data.get(ColPosition.ID_GIOCATORE3);
		punteggioGiocatore3 = (Double) data.get(ColPosition.PUNTEGGIO_GIOCATORE3);
		idGiocatore4 		= (Integer) data.get(ColPosition.ID_GIOCATORE4);
		punteggioGiocatore4 = (Double) data.get(ColPosition.PUNTEGGIO_GIOCATORE4);
		idGiocatore5 		= (Integer) data.get(ColPosition.ID_GIOCATORE5);
		punteggioGiocatore5 = (Double) data.get(ColPosition.PUNTEGGIO_GIOCATORE5);
		idGiocatoreVincitore= (Integer) data.get(ColPosition.ID_GIOCATORE_VINCITORE);
	}

	public List<Integer> keyCols(){
		List<Integer> keyCols = new ArrayList<Integer>();
		keyCols.add(ColPosition.ID_TORNEO);
		keyCols.add(ColPosition.NUMERO_TURNO);
		keyCols.add(ColPosition.NUMERO_TAVOLO);
		return keyCols;
	}

	public String getIdTorneo() {
		return idTorneo;
	}

	public void setIdTorneo(String idTorneo) {
		this.idTorneo = idTorneo;
	}

	public Integer getNumeroTurno() {
		return numeroTurno;
	}

	public void setNumeroTurno(Integer numeroTurno) {
		this.numeroTurno = numeroTurno;
	}

	public Integer getNumeroTavolo() {
		return numeroTavolo;
	}

	public void setNumeroTavolo(Integer numeroTavolo) {
		this.numeroTavolo = numeroTavolo;
	}

	public Integer getIdGiocatore1() {
		return idGiocatore1;
	}

	public void setIdGiocatore1(Integer idGiocatore1) {
		this.idGiocatore1 = idGiocatore1;
	}

	public Double getPunteggioGiocatore1() {
		return punteggioGiocatore1;
	}

	public void setPunteggioGiocatore1(Double punteggioGiocatore1) {
		this.punteggioGiocatore1 = punteggioGiocatore1;
	}

	public Integer getIdGiocatore2() {
		return idGiocatore2;
	}

	public void setIdGiocatore2(Integer idGiocatore2) {
		this.idGiocatore2 = idGiocatore2;
	}

	public Double getPunteggioGiocatore2() {
		return punteggioGiocatore2;
	}

	public void setPunteggioGiocatore2(Double punteggioGiocatore2) {
		this.punteggioGiocatore2 = punteggioGiocatore2;
	}

	public Integer getIdGiocatore3() {
		return idGiocatore3;
	}

	public void setIdGiocatore3(Integer idGiocatore3) {
		this.idGiocatore3 = idGiocatore3;
	}

	public Double getPunteggioGiocatore3() {
		return punteggioGiocatore3;
	}

	public void setPunteggioGiocatore3(Double punteggioGiocatore3) {
		this.punteggioGiocatore3 = punteggioGiocatore3;
	}

	public Integer getIdGiocatore4() {
		return idGiocatore4;
	}

	public void setIdGiocatore4(Integer idGiocatore4) {
		this.idGiocatore4 = idGiocatore4;
	}

	public Double getPunteggioGiocatore4() {
		return punteggioGiocatore4;
	}

	public void setPunteggioGiocatore4(Double punteggioGiocatore4) {
		this.punteggioGiocatore4 = punteggioGiocatore4;
	}

	public Integer getIdGiocatore5() {
		return idGiocatore5;
	}

	public void setIdGiocatore5(Integer idGiocatore5) {
		this.idGiocatore5 = idGiocatore5;
	}

	public Double getPunteggioGiocatore5() {
		return punteggioGiocatore5;
	}

	public void setPunteggioGiocatore5(Double punteggioGiocatore5) {
		this.punteggioGiocatore5 = punteggioGiocatore5;
	}

	public Integer getIdGiocatoreVincitore() {
		return idGiocatoreVincitore;
	}

	public void setIdGiocatoreVincitore(Integer idGiocatoreVincitore) {
		this.idGiocatoreVincitore = idGiocatoreVincitore;
	}
	
}
