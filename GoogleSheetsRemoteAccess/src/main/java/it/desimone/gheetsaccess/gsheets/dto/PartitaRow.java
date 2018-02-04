package it.desimone.gheetsaccess.gsheets.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PartitaRow extends AbstractSheetRow {

	public static final String SHEET_PARTITE_NAME 	= "PARTITE";
	
	private String idTorneo;
	private Integer numeroTurno;
	private String dataTurno;
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
	private String nominativoVincitore;
	private String nominativoGiocatore1;
	private String nominativoGiocatore2;
	private String nominativoGiocatore3;
	private String nominativoGiocatore4;
	private String nominativoGiocatore5;
	
	public static class ColPosition{
		//zero-based
		public static final Integer ID_TORNEO 				= 0;
		public static final Integer NUMERO_TURNO 			= 1;
		public static final Integer DATA_TURNO 				= 2;
		public static final Integer NUMERO_TAVOLO 			= 3;
		public static final Integer ID_GIOCATORE_VINCITORE	= 4;
		public static final Integer NOMINATIVO_VINCITORE	= 5;
		public static final Integer ID_GIOCATORE1			= 6;
		public static final Integer NOMINATIVO_GIOCATORE1	= 7;
		public static final Integer PUNTEGGIO_GIOCATORE1 	= 8;
		public static final Integer ID_GIOCATORE2			= 9;
		public static final Integer NOMINATIVO_GIOCATORE2	= 10;
		public static final Integer PUNTEGGIO_GIOCATORE2 	= 11;
		public static final Integer ID_GIOCATORE3			= 12;
		public static final Integer NOMINATIVO_GIOCATORE3	= 13;
		public static final Integer PUNTEGGIO_GIOCATORE3 	= 14;
		public static final Integer ID_GIOCATORE4			= 15;
		public static final Integer NOMINATIVO_GIOCATORE4	= 16;
		public static final Integer PUNTEGGIO_GIOCATORE4 	= 17;
		public static final Integer ID_GIOCATORE5			= 18;
		public static final Integer NOMINATIVO_GIOCATORE5	= 19;
		public static final Integer PUNTEGGIO_GIOCATORE5 	= 20;
	}
	
	public Integer getDataSize() {
		return 22;
	}
	
	public List<Object> getData() {
		super.getData();
		if (idTorneo != null) data.set(ColPosition.ID_TORNEO, idTorneo.trim());
		if (numeroTurno != null) data.set(ColPosition.NUMERO_TURNO, numeroTurno);
		if (dataTurno != null) data.set(ColPosition.DATA_TURNO, dataTurno.trim());
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
		if (nominativoGiocatore1 != null) data.set(ColPosition.NOMINATIVO_GIOCATORE1, nominativoGiocatore1);
		if (nominativoGiocatore2 != null) data.set(ColPosition.NOMINATIVO_GIOCATORE2, nominativoGiocatore2);
		if (nominativoGiocatore3 != null) data.set(ColPosition.NOMINATIVO_GIOCATORE3, nominativoGiocatore3);
		if (nominativoGiocatore4 != null) data.set(ColPosition.NOMINATIVO_GIOCATORE4, nominativoGiocatore4);
		if (nominativoGiocatore5 != null) data.set(ColPosition.NOMINATIVO_GIOCATORE5, nominativoGiocatore5);
		if (nominativoVincitore != null) data.set(ColPosition.NOMINATIVO_VINCITORE, nominativoVincitore);
		return data;
	}

	public void setData(List<Object> data) {
		if (data == null || data.isEmpty()) return;
		super.setData(data);
		idTorneo 			= (String) data.get(ColPosition.ID_TORNEO);
		numeroTurno 		= Integer.valueOf((String)data.get(ColPosition.NUMERO_TURNO));
		dataTurno 			= (String) data.get(ColPosition.DATA_TURNO);
		numeroTavolo 		= Integer.valueOf((String)data.get(ColPosition.NUMERO_TAVOLO));
		idGiocatore1 		= Integer.valueOf((String)data.get(ColPosition.ID_GIOCATORE1));
		punteggioGiocatore1 = (Double) data.get(ColPosition.PUNTEGGIO_GIOCATORE1);
		idGiocatore2 		= Integer.valueOf((String)data.get(ColPosition.ID_GIOCATORE2));
		punteggioGiocatore2 = (Double) data.get(ColPosition.PUNTEGGIO_GIOCATORE2);
		idGiocatore3 		= Integer.valueOf((String)data.get(ColPosition.ID_GIOCATORE3));
		punteggioGiocatore3 = (Double) data.get(ColPosition.PUNTEGGIO_GIOCATORE3);
		idGiocatore4 		= Integer.valueOf((String)data.get(ColPosition.ID_GIOCATORE4));
		punteggioGiocatore4 = (Double) data.get(ColPosition.PUNTEGGIO_GIOCATORE4);
		idGiocatore5 		= Integer.valueOf((String)data.get(ColPosition.ID_GIOCATORE5));
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
		this.nominativoGiocatore1 = getGiocatoreCellById(idGiocatore1);
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
		this.nominativoGiocatore2 = getGiocatoreCellById(idGiocatore2);
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
		this.nominativoGiocatore3 = getGiocatoreCellById(idGiocatore3);
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
		this.nominativoGiocatore4 = getGiocatoreCellById(idGiocatore4);
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
		this.nominativoGiocatore5 = getGiocatoreCellById(idGiocatore5);
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
		this.nominativoVincitore = getGiocatoreCellById(idGiocatoreVincitore);
	}

	public String getDataTurno() {
		return dataTurno;
	}

	public void setDataTurno(String dataTurno) {
		this.dataTurno = dataTurno;
	}
	
	private static String getGiocatoreCellById(Integer id){
		StringBuilder buffer = new StringBuilder();
		buffer.append("=CONCATENATE(");
		buffer.append("CERCA.VERT(");
		buffer.append(id);
		buffer.append(";");
		buffer.append(AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME);
		buffer.append("!A:E; "+(AnagraficaGiocatoreRow.ColPosition.NOME+1)+"; FALSE);");
		buffer.append("\" \";");
		buffer.append("CERCA.VERT(");
		buffer.append(id);
		buffer.append(";");
		buffer.append(AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME);
		buffer.append("!A:E; "+(AnagraficaGiocatoreRow.ColPosition.COGNOME+1)+"; FALSE);");
		buffer.append("\" - \";");
		buffer.append("CERCA.VERT(");
		buffer.append(id);
		buffer.append(";");
		buffer.append(AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME);
		buffer.append("!A:E; "+(AnagraficaGiocatoreRow.ColPosition.ULTIMO_CLUB+1)+"; FALSE);");
		buffer.append(")");
		return buffer.toString();
	}

}
