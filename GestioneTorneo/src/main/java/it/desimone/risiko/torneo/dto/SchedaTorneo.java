package it.desimone.risiko.torneo.dto;

import java.util.Date;
import java.util.List;

public class SchedaTorneo {

	private String sedeTorneo;
	private String organizzatore;
	private String nomeTorneo;
	private TipoTorneo tipoTorneo;
	private int numeroTurni;
	
	private List<Date> dataTurni;
	
	public enum TipoTorneo{
		RADUNO_NAZIONALE("Raduno Nazionale"), MASTER("Torneo Master"), OPEN("Torneo Open"), CAMPIONATO("Campionato Periodico");
		TipoTorneo(String tipoTorneo){
			this.tipoTorneo = tipoTorneo;
		}
		String tipoTorneo;
		public String getTipoTorneo() {
			return tipoTorneo;
		}
		public static TipoTorneo parseTipoTorneo(String tipologiaTorneo){
			TipoTorneo result = null;
			if (tipologiaTorneo != null){
				TipoTorneo[] tipiTornei = TipoTorneo.values();
				for (TipoTorneo torneo: tipiTornei){
					if (torneo.getTipoTorneo().equalsIgnoreCase(tipologiaTorneo.trim())){
						result = torneo;
						break;
					}
				}
			}
			return result;
		}
	}

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

	public TipoTorneo getTipoTorneo() {
		return tipoTorneo;
	}

	public void setTipoTorneo(TipoTorneo tipoTorneo) {
		this.tipoTorneo = tipoTorneo;
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
		return "SchedaTorneo [sedeTorneo=" + sedeTorneo + ", organizzatore="
				+ organizzatore + ", nomeTorneo=" + nomeTorneo
				+ ", tipoTorneo=" + tipoTorneo + ", numeroTurni=" + numeroTurni
				+ ", dataTurni=" + dataTurni + "]";
	}
	
	
}