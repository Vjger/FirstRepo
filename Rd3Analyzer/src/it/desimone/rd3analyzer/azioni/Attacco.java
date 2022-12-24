package it.desimone.rd3analyzer.azioni;

import it.desimone.rd3analyzer.RigaLog;
import it.desimone.rd3analyzer.Territorio;

public class Attacco extends AbstractAzione {


	public Attacco(RigaLog rigaLog) {
		super(rigaLog);
	}

	private Territorio territorioAttaccante;
	private Territorio territorioAttaccato ;
	
	private Byte[] dadiAttacco;
	private Byte[] dadiDifesa;


	@Override
	public TipoAzione getTipoAzione() {
		return TipoAzione.ATTACCO;
	}

	public Territorio getTerritorioAttaccante() {
		return territorioAttaccante;
	}

	public void setTerritorioAttaccante(Territorio territorioAttaccante) {
		this.territorioAttaccante = territorioAttaccante;
	}

	public Territorio getTerritorioAttaccato() {
		return territorioAttaccato;
	}

	public void setTerritorioAttaccato(Territorio territorioAttaccato) {
		this.territorioAttaccato = territorioAttaccato;
	}

	public Byte[] getDadiAttacco() {
		return dadiAttacco;
	}

	public void setDadiAttacco(Byte[] dadiAttacco) {
		this.dadiAttacco = dadiAttacco;
	}

	public Byte[] getDadiDifesa() {
		return dadiDifesa;
	}

	public void setDadiDifesa(Byte[] dadiDifesa) {
		this.dadiDifesa = dadiDifesa;
	}

	@Override
	public String toString() {
		return super.toString()+"\nIl giocatore "+getGiocatoreCheAgisce()+" ha attaccato da "+territorioAttaccante+" a "+territorioAttaccato+". Risultato dadi: "+dadiToString(dadiAttacco)+" "+dadiToString(dadiDifesa);
	}

	private String dadiToString(Byte[] dadi){
		String result = "(";
		for (Byte dado: dadi){
			result += dado+",";
		}
		result +=")";
		return result;
	}
}
