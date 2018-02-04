package it.desimone.gheetsaccess.gsheets.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnagraficaGiocatoreRidottaRow extends AbstractSheetRow{
	
	public static final String SHEET_ANAGRAFICA_NAME 	= "ANAGRAFICA";
	public static final String SHEET_DATA_ANALYSIS_NAME = "DATA_ANALYSIS";

	private Integer id;
	private String nome;
	private String cognome;
	private String email;
	private String updateTime;
	
	static class ColPosition{
		//zero-based
		public static final Integer ID 					= 0;
		public static final Integer NOME 				= 1;
		public static final Integer COGNOME 			= 2;
		public static final Integer E_MAIL		 		= 3;
		public static final Integer UPDATE_TIME 		= 4;
	}
	
	public Integer getDataSize() {
		return 6;
	}
	
	public List<Integer> keyCols() {
		List<Integer> keyCols = new ArrayList<Integer>();
		keyCols.add(ColPosition.NOME);
		keyCols.add(ColPosition.COGNOME);
		keyCols.add(ColPosition.E_MAIL);
		return keyCols;
	}

	public List<Object> getData() {
		super.getData();
		if (id != null) data.set(ColPosition.ID, id);
		if (nome != null) data.set(ColPosition.NOME, nome.trim());
		if (cognome != null) data.set(ColPosition.COGNOME, cognome.trim());
		if (email != null) data.set(ColPosition.E_MAIL, email.trim());
		if (updateTime != null) data.set(ColPosition.UPDATE_TIME, updateTime);
		return data;
	}

	public void setData(List<Object> data) {
		if (data == null || data.isEmpty()) return;
		super.setData(data);
		id 				= Integer.valueOf((String)data.get(ColPosition.ID));
		nome 			= (String) data.get(ColPosition.NOME);
		cognome 		= (String) data.get(ColPosition.COGNOME);
		email	 		= (String) data.get(ColPosition.E_MAIL);
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	
	
}
