package it.desimone.risiko.torneo.dto;

import java.math.BigDecimal;
import java.util.List;

public class StoricoRanking {

	private String torneo;
	private Boolean isVincitore;
	private List<GiocatoreDTO> giocatoriAvversari;
	private BigDecimal rankingAfineTorneo;
	public String getTorneo() {
		return torneo;
	}
	public void setTorneo(String torneo) {
		this.torneo = torneo;
	}
	public Boolean getIsVincitore() {
		return isVincitore;
	}
	public void setIsVincitore(Boolean isVincitore) {
		this.isVincitore = isVincitore;
	}
	public List<GiocatoreDTO> getGiocatoriAvversari() {
		return giocatoriAvversari;
	}
	public void setGiocatoriAvversari(List<GiocatoreDTO> giocatoriAvversari) {
		this.giocatoriAvversari = giocatoriAvversari;
	}
	public BigDecimal getRankingAfineTorneo() {
		return rankingAfineTorneo;
	}
	public void setRankingAfineTorneo(BigDecimal rankingAfineTorneo) {
		this.rankingAfineTorneo = rankingAfineTorneo;
	}
	
	public String toString(){
		return rankingAfineTorneo.toString();
	}
}
