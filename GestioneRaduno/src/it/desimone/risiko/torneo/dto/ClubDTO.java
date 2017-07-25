package it.desimone.risiko.torneo.dto;

public class ClubDTO {

	private String codProvincia;
	private String denominazione;
	
	public ClubDTO(){}
	
	public ClubDTO(String denominazione){
		this.denominazione = denominazione;
	}
	
	public ClubDTO(String codProvincia, String denominazione){
		this.codProvincia = codProvincia;
		this.denominazione = denominazione;
	}
	
	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodProvincia() {
		return codProvincia;
	}

	public void setCodProvincia(String codProvincia) {
		this.codProvincia = codProvincia;
	}
	
	public String toString(){
		return denominazione;
	}
	
	public boolean equals(Object o){
		boolean result = false;
		if (o != null){
			ClubDTO club = (ClubDTO) o;
			result = club.getDenominazione().equals(this.getDenominazione());
		}
		return result;
	}
}
