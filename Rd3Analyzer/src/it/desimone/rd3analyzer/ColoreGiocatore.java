package it.desimone.rd3analyzer;

public enum ColoreGiocatore {
	
	 GIALLO("Giallo")
	,ROSSO("Rosso")
	,VERDE("Verde")
	,BLU("Blu")
	,VIOLA("Viola")
	,NERO("Nero")
	
	;

	private String colore;
	
	ColoreGiocatore(String colore){
		this.colore = colore;
	}

	public static ColoreGiocatore getColoreByDescrizione(String colore){
	   if (colore != null) {
	      for (ColoreGiocatore c : ColoreGiocatore.values()) {
	        if (colore.equalsIgnoreCase(c.colore)) {
	          return c;
	        }
	      }
	    }
	    return null;
	}
	
	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	
	
}
