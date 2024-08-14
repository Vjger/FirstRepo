package it.desimone.replayrd3;

import java.io.Serializable;
import java.util.List;

public class Giocatore implements Serializable{

	private String nickname;
	private ColoreGiocatore coloreGiocatore;
	
	private List<Territorio> obiettivo;
	private int numeroObiettivo = 0;
	
	public Giocatore(String nickname, short ordine){
		this.nickname = nickname;
		switch (ordine) {
		case 1:
			coloreGiocatore = ColoreGiocatore.GIALLO;
			break;
		case 2:
			coloreGiocatore = ColoreGiocatore.ROSSO;
			break;
		case 3:
			coloreGiocatore = ColoreGiocatore.VERDE;
			break;
		case 4:
			coloreGiocatore = ColoreGiocatore.BLU;
			break;
		case 5:
			coloreGiocatore = ColoreGiocatore.VIOLA;
			break;
		case 6:
			coloreGiocatore = ColoreGiocatore.NERO;
			break;
		default:
			throw new IllegalStateException("Numero di giocatori non previsto: "+ordine);
		}
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public ColoreGiocatore getColoreGiocatore() {
		return coloreGiocatore;
	}
	public void setColoreGiocatore(ColoreGiocatore coloreGiocatore) {
		this.coloreGiocatore = coloreGiocatore;
	}

	public List<Territorio> getObiettivo() {
		return obiettivo;
	}

	public void setObiettivo(List<Territorio> obiettivo) {
		this.obiettivo = obiettivo;
	}
	
	public int getNumeroObiettivo() {
		return numeroObiettivo;
	}

	public void setNumeroObiettivo(int numeroObiettivo) {
		this.numeroObiettivo = numeroObiettivo;
	}

	public boolean equals(Object o){
		Giocatore giocatore = (Giocatore) o;
		return this.nickname.equals(giocatore.getNickname());
	}
	
}
