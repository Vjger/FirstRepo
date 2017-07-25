package it.desimone.rd3analyzer;

public class StoricoClassifica {
	private String testo;
	private String url;
	public StoricoClassifica(String testo, String url){
		this.testo = testo;
		this.url = url;
	}
	public String getTesto() {
		return testo;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
