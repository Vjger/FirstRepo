package it.desimone.rd3analyzer;

public class RankingPrestigious {
	
	private String nickName;
	private String dataUltimaPartita;
	private Integer ranking;
	private Integer numeroPartiteGiocate;
	private Integer numeroPartiteVinte;
	private Integer numeroPartiteGiocatePrimaSettimana;
	private Integer numeroPartiteGiocateSecondaSettimana;
	private Integer numeroPartiteGiocateTerzaSettimana;
	private Integer numeroPartiteGiocateQuartaSettimana;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getDataUltimaPartita() {
		return dataUltimaPartita;
	}
	
	public void setDataUltimaPartita(String dataUltimaPartita) {
		this.dataUltimaPartita = dataUltimaPartita;
	}
	
	public Integer getRanking() {
		return ranking;
	}
	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
	public Integer getNumeroPartiteGiocate() {
		return numeroPartiteGiocate;
	}
	public void setNumeroPartiteGiocate(Integer numeroPartiteGiocate) {
		this.numeroPartiteGiocate = numeroPartiteGiocate;
	}
	public Integer getNumeroPartiteVinte() {
		return numeroPartiteVinte;
	}
	public void setNumeroPartiteVinte(Integer numeroPartiteVinte) {
		this.numeroPartiteVinte = numeroPartiteVinte;
	}
	public Integer getNumeroPartiteGiocatePrimaSettimana() {
		return numeroPartiteGiocatePrimaSettimana;
	}
	public void setNumeroPartiteGiocatePrimaSettimana(
			Integer numeroPartiteGiocatePrimaSettimana) {
		this.numeroPartiteGiocatePrimaSettimana = numeroPartiteGiocatePrimaSettimana;
	}
	public Integer getNumeroPartiteGiocateSecondaSettimana() {
		return numeroPartiteGiocateSecondaSettimana;
	}
	public void setNumeroPartiteGiocateSecondaSettimana(
			Integer numeroPartiteGiocateSecondaSettimana) {
		this.numeroPartiteGiocateSecondaSettimana = numeroPartiteGiocateSecondaSettimana;
	}
	public Integer getNumeroPartiteGiocateTerzaSettimana() {
		return numeroPartiteGiocateTerzaSettimana;
	}
	public void setNumeroPartiteGiocateTerzaSettimana(
			Integer numeroPartiteGiocateTerzaSettimana) {
		this.numeroPartiteGiocateTerzaSettimana = numeroPartiteGiocateTerzaSettimana;
	}
	public Integer getNumeroPartiteGiocateQuartaSettimana() {
		return numeroPartiteGiocateQuartaSettimana;
	}
	public void setNumeroPartiteGiocateQuartaSettimana(
			Integer numeroPartiteGiocateQuartaSettimana) {
		this.numeroPartiteGiocateQuartaSettimana = numeroPartiteGiocateQuartaSettimana;
	}
	
	public String toString(){
		String result = null;
		StringBuilder buffer = new StringBuilder();
		buffer.append(nickName);
		buffer.append("|");
		buffer.append(dataUltimaPartita);
		buffer.append("|");
		buffer.append(ranking);
		buffer.append("|");
		buffer.append(numeroPartiteGiocate);
		buffer.append("|");
		buffer.append(numeroPartiteVinte);
		buffer.append("|");
		buffer.append(numeroPartiteGiocatePrimaSettimana);
		buffer.append("|");
		buffer.append(numeroPartiteGiocateSecondaSettimana);
		buffer.append("|");
		buffer.append(numeroPartiteGiocateTerzaSettimana);
		buffer.append("|");
		buffer.append(numeroPartiteGiocateQuartaSettimana);
		
		result = buffer.toString();
		return result;
	}
	
}
