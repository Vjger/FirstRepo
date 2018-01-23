package it.desimone.gsheets.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnagraficaGiocatoreRow extends AbstractSheetRow{

	private Integer id;
	private String nome;
	private String cognome;
	private String ultimoClub;
	private String idUltimoTorneo;
	private String updateTime;
	
	static class ColPosition{
		//zero-based
		public static final Integer ID 					= 0;
		public static final Integer NOME 				= 1;
		public static final Integer COGNOME 			= 2;
		public static final Integer ULTIMO_CLUB 		= 3;
		public static final Integer ID_ULTIMO_TORNEO 	= 4;
		public static final Integer UPDATE_TIME 		= 5;
	}
	
	
	public List<Integer> keyCols() {
		return Collections.singletonList(ColPosition.ID);
	}

	public List<Object> getData() {
		List<Object> data = Arrays.asList(new Object[6]);
		Collections.fill(data, "");
		if (id != null) data.set(ColPosition.ID, id);
		if (nome != null) data.set(ColPosition.NOME, nome);
		if (cognome != null) data.set(ColPosition.COGNOME, cognome);
		if (ultimoClub != null) data.set(ColPosition.ULTIMO_CLUB, ultimoClub);
		if (idUltimoTorneo != null) data.set(ColPosition.ID_ULTIMO_TORNEO, idUltimoTorneo);
		if (updateTime != null) data.set(ColPosition.UPDATE_TIME, updateTime);
		return data;
	}

	public void setData(List<Object> data) {
		if (data == null || data.isEmpty()) return;
		
		id 				= (Integer) data.get(ColPosition.ID);
		nome 			= (String) data.get(ColPosition.NOME);
		cognome 		= (String) data.get(ColPosition.COGNOME);
		ultimoClub 		= (String) data.get(ColPosition.ULTIMO_CLUB);
		idUltimoTorneo 	= (String) data.get(ColPosition.ID_ULTIMO_TORNEO);
		updateTime		= (String) data.get(ColPosition.UPDATE_TIME);
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCognome() {
		return cognome;
	}


	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	public String getUltimoClub() {
		return ultimoClub;
	}


	public void setUltimoClub(String ultimoClub) {
		this.ultimoClub = ultimoClub;
	}


	public String getIdUltimoTorneo() {
		return idUltimoTorneo;
	}


	public void setIdUltimoTorneo(String idUltimoTorneo) {
		this.idUltimoTorneo = idUltimoTorneo;
	}


	public String getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	
	
}
