package it.desimone.gheetsaccess.gsheets.dto;

import java.util.ArrayList;
import java.util.List;

public class TabellinoGiocatore {

	private AnagraficaGiocatoreRidottaRow anagraficaRidottaGiocatoreRowFrom;
	
	private List<PartitaRow> partiteGiocate;
	
	public TabellinoGiocatore(AnagraficaGiocatoreRidottaRow anagraficaRidottaGiocatoreRowFrom, List<SheetRow> partiteGiocate) {
		super();
		this.anagraficaRidottaGiocatoreRowFrom = anagraficaRidottaGiocatoreRowFrom;
		if (partiteGiocate != null && !partiteGiocate.isEmpty()){
			this.partiteGiocate = new ArrayList<PartitaRow>();
			for (SheetRow row: partiteGiocate){
				this.partiteGiocate.add((PartitaRow)row);
			}
		}
	}

	public AnagraficaGiocatoreRidottaRow getAnagraficaRidottaGiocatoreRowFrom() {
		return anagraficaRidottaGiocatoreRowFrom;
	}

	public void setAnagraficaRidottaGiocatoreRowFrom(
			AnagraficaGiocatoreRidottaRow anagraficaRidottaGiocatoreRowFrom) {
		this.anagraficaRidottaGiocatoreRowFrom = anagraficaRidottaGiocatoreRowFrom;
	}

	public List<PartitaRow> getPartiteGiocate() {
		return partiteGiocate;
	}

	public void setPartiteGiocate(List<PartitaRow> partiteGiocate) {
		this.partiteGiocate = partiteGiocate;
	}
	
	
}
