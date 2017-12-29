package it.desimone.risiko.torneo.dto;

import java.util.Date;
import java.util.List;

public class SchedaTorneo {

	private String sedeTorneo;
	private String organizzatore;
	private String nomeTorneo;
	private boolean torneoConcluso;
	private int numeroTurni;
	
	private List<Date> dataTurni;

	public String getSedeTorneo() {
		return sedeTorneo;
	}

	public void setSedeTorneo(String sedeTorneo) {
		this.sedeTorneo = sedeTorneo;
	}

	public String getOrganizzatore() {
		return organizzatore;
	}

	public void setOrganizzatore(String organizzatore) {
		this.organizzatore = organizzatore;
	}

	public String getNomeTorneo() {
		return nomeTorneo;
	}

	public void setNomeTorneo(String nomeTorneo) {
		this.nomeTorneo = nomeTorneo;
	}

	public boolean isTorneoConcluso() {
		return torneoConcluso;
	}

	public void setTorneoConcluso(boolean torneoConcluso) {
		this.torneoConcluso = torneoConcluso;
	}

	public int getNumeroTurni() {
		return numeroTurni;
	}

	public void setNumeroTurni(int numeroTurni) {
		this.numeroTurni = numeroTurni;
	}

	public List<Date> getDataTurni() {
		return dataTurni;
	}

	public void setDataTurni(List<Date> dataTurni) {
		this.dataTurni = dataTurni;
	}

	@Override
	public String toString() {
		return "SchedaTorneo [sedeTorneo=" + sedeTorneo + ", organizzatore=" + organizzatore + ", nomeTorneo="
				+ nomeTorneo + ", torneoConcluso=" + torneoConcluso + ", numeroTurni=" + numeroTurni + ", dataTurni="
				+ dataTurni + "]";
	}
	
	
}
