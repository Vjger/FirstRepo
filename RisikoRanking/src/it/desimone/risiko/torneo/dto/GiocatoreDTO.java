package it.desimone.risiko.torneo.dto;

import java.math.BigDecimal;
import java.util.List;

public class GiocatoreDTO{
	
	private String nome;
	private String cognome;
	private String club;
	
	private BigDecimal ranking;
	
	private List<StoricoRanking> storico;
	
	public GiocatoreDTO(){}
	
	public GiocatoreDTO(GiocatoreDTO giocatore){
		this.nome = giocatore.getNome();
		this.cognome = giocatore.getCognome();
		this.club = giocatore.getClub();
		this.ranking = giocatore.getRanking();
		this.storico = giocatore.getStorico();
	}
	
	public String toString(){
		StringBuilder buffer = new StringBuilder();
		buffer.append(nome);
		buffer.append(" "+cognome);
		buffer.append(" - "+club+" - ");
		buffer.append(" - "+ranking+" - ");
		return buffer.toString();
	}
	
	public boolean equals(Object o){
		boolean result = false;
		GiocatoreDTO giocatore = (GiocatoreDTO) o;
		result = giocatore.getNominativo().equalsIgnoreCase(this.getNominativo());
		//result = giocatore.getNominativo().equalsIgnoreCase(this.getNominativo()) && (this.club == null || giocatore.getClub() == null || (giocatore.getClub().equalsIgnoreCase(this.club)));
		return result;
	}
	
	public int hashCode(){
		return getNominativo().hashCode();		
	}

	private String getNominativo(){
		return nome+cognome;
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

	public String getClub() {
		return club;
	}
	public void setClub(String club) {
		this.club = club;
	}

	public BigDecimal getRanking() {
		return ranking;
	}

	public void setRanking(BigDecimal ranking) {
		this.ranking = ranking;
	}

	public List<StoricoRanking> getStorico() {
		return storico;
	}

	public void setStorico(List<StoricoRanking> storico) {
		this.storico = storico;
	}
}

