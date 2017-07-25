package it.desimone.rd3analyzer;

import java.util.Date;

import it.desimone.rd3analyzer.parser.FiltroModalita;

public class Torneo {

	private String urlTorneo;
	private FiltroModalita modalitaTorneo;
	private Date dataUltimaElaborazione;
	private Boolean daElaborare;
	public String getUrlTorneo() {
		return urlTorneo;
	}
	public void setUrlTorneo(String urlTorneo) {
		this.urlTorneo = urlTorneo;
	}
	public FiltroModalita getModalitaTorneo() {
		return modalitaTorneo;
	}
	public void setModalitaTorneo(FiltroModalita modalitaTorneo) {
		this.modalitaTorneo = modalitaTorneo;
	}
	public Date getDataUltimaElaborazione() {
		return dataUltimaElaborazione;
	}
	public void setDataUltimaElaborazione(Date dataUltimaElaborazione) {
		this.dataUltimaElaborazione = dataUltimaElaborazione;
	}
	public Boolean getDaElaborare() {
		return daElaborare;
	}
	public void setDaElaborare(Boolean daElaborare) {
		this.daElaborare = daElaborare;
	}
}
