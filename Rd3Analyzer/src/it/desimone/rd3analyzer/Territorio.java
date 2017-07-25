package it.desimone.rd3analyzer;

import java.util.ArrayList;
import java.util.List;

public enum Territorio {
	
	 CONGO("Congo", "CON", 3)
	,AFRICA_ORIENTALE("Africa Orientale", "AFO", 5)
	,EGITTO("Egitto", "EGI", 4)
	,MADAGASCAR("Madagascar", "MAD", 2)
	,AFRICA_DEL_NORD("Nord Africa", "AFN", 6)
	,AFRICA_DEL_SUD("Africa del Sud", "AFS", 3)
	
	,AFGHANISTAN("Afghanistan", "AFG", 4)
	,CINA("Cina", "CHI", 7)
	,INDIA("India", "IND", 3)
	,CITA("Čita", "CIT", 4)
	,GIAPPONE("Giappone", "GIA", 2)
	,KAMCHATKA("Kamchatka", "KAM", 5)
	,MEDIO_ORIENTE("Medio Oriente", "MEO", 6)
	,MONGOLIA("Mongolia", "MON", 5)
	,SIAM("Siam", "SIA", 3)
	,SIBERIA("Siberia", "SIB", 5)
	,URALI("Urali", "URA", 4)
	,JACUZIA("Jacuzia", "JAK", 3)
	
	,GRAN_BRETAGNA("Gran Bretagna", "GBR", 4)
	,ISLANDA("Islanda", "ISL", 3)
	,EUROPA_SETTENTRIONALE("Europa Settentrionale", "EUS", 5)
	,SCANDINAVIA("Scandinavia", "SCA", 4)
	,EUROPA_MERIDIONALE("Europa Meridionale", "EUN", 6)
	,UCRAINA("Ucraina", "UKR", 6)
	,EUROPA_OCCIDENTALE("Europa Occidentale", "EUO", 4)
	
	,ALASKA("Alaska", "ALA", 3)
	,ALBERTA("Alberta", "ALB", 4)
	,STATI_UNITI_ORIENTALI("Stati Uniti Orientali", "USE", 4)
	,AMERICA_CENTRALE("America Centrale", "AMC", 3)
	,GROENLANDIA("Groenlandia", "GRO", 4)
	,TERRITORI_DEL_NORD_OVEST("Territori del Nord Ovest", "TNO", 4)
	,ONTARIO("Ontario", "ONT", 6)
	,QUEBEC("Quebec", "QUE", 3)
	,STATI_UNITI_OCCIDENTALI("Stati Uniti Occidentali", "USO", 4)
	
	,AUSTRALIA_ORIENTALE("Australia Orientale", "AUE", 2)
	,INDONESIA("Indonesia", "IDS", 3)
	,NUOVA_GUINEA("Nuova Guinea", "NGN", 3)
	,AUSTRALIA_OCCIDENTALE("Australia Occidentale", "AUO", 3)
	
	,ARGENTINA("Argentina", "ARG", 2)
	,BRASILE("Brasile", "BRA", 4)
	,PERU("Perù", "PER", 3)
	,VENEZUELA("Venezuela", "VEN", 3)
	
	,JOLLY1("Jolly1", "JO1", 0)
	,JOLLY2("Jolly2", "JO2", 0)
	;

	private String nomeTerritorio;
	private String siglaCarta;
	private int valore;
	
	Territorio(String nomeTerritorio, String siglaCarta, int valore){
		this.nomeTerritorio = nomeTerritorio;
		this.siglaCarta = siglaCarta;
		this.valore = valore;
	}

	public static Territorio getTerritorioByDescrizione(String nomeTerritorio){
	   if (nomeTerritorio != null) {
	      for (Territorio t : Territorio.values()) {
	        if (nomeTerritorio.equalsIgnoreCase(t.nomeTerritorio)) {
	          return t;
	        }
	      }
	    }
	    return null;
	}
	
	public static Territorio getTerritorioBySigla(String sigla){
	   if (sigla != null) {
	      for (Territorio t : Territorio.values()) {
	        if (sigla.equalsIgnoreCase(t.siglaCarta)) {
	          return t;
	        }
	      }
	    }
	    return null;
	}
	
	public String getNomeTerritorio() {
		return nomeTerritorio;
	}

	public void setNomeTerritorio(String nomeTerritorio) {
		this.nomeTerritorio = nomeTerritorio;
	}

	public int getValore() {
		return valore;
	}

	public void setValore(int valore) {
		this.valore = valore;
	}

	public static List<Territorio> getTerritoriNordAmerica(){
		List<Territorio> territoriNordAmerica = new ArrayList<Territorio>();
		territoriNordAmerica.add(ALASKA);
		territoriNordAmerica.add(ALBERTA);
		territoriNordAmerica.add(TERRITORI_DEL_NORD_OVEST);
		territoriNordAmerica.add(GROENLANDIA);
		territoriNordAmerica.add(ONTARIO);
		territoriNordAmerica.add(QUEBEC);
		territoriNordAmerica.add(STATI_UNITI_OCCIDENTALI);
		territoriNordAmerica.add(STATI_UNITI_ORIENTALI);
		territoriNordAmerica.add(AMERICA_CENTRALE);
		return territoriNordAmerica;
	}
	
	public static List<Territorio> getTerritoriSudAmerica(){
		List<Territorio> territoriSudAmerica = new ArrayList<Territorio>();
		territoriSudAmerica.add(VENEZUELA);
		territoriSudAmerica.add(BRASILE);
		territoriSudAmerica.add(PERU);
		territoriSudAmerica.add(ARGENTINA);
		return territoriSudAmerica;
	}
	
	public static List<Territorio> getTerritoriAfrica(){
		List<Territorio> territoriAfrica = new ArrayList<Territorio>();
		territoriAfrica.add(AFRICA_DEL_NORD);
		territoriAfrica.add(EGITTO);
		territoriAfrica.add(AFRICA_ORIENTALE);
		territoriAfrica.add(CONGO);
		territoriAfrica.add(AFRICA_DEL_SUD);
		territoriAfrica.add(MADAGASCAR);
		return territoriAfrica;
	}
	
	public static List<Territorio> getTerritoriEuropa(){
		List<Territorio> territoriEuropa = new ArrayList<Territorio>();
		territoriEuropa.add(ISLANDA);
		territoriEuropa.add(SCANDINAVIA);
		territoriEuropa.add(GRAN_BRETAGNA);
		territoriEuropa.add(EUROPA_SETTENTRIONALE);
		territoriEuropa.add(UCRAINA);
		territoriEuropa.add(EUROPA_OCCIDENTALE);
		territoriEuropa.add(EUROPA_MERIDIONALE);
		return territoriEuropa;
	}
	
	public static List<Territorio> getTerritoriAsia(){
		List<Territorio> territoriAsia = new ArrayList<Territorio>();
		territoriAsia.add(URALI);
		territoriAsia.add(SIBERIA);
		territoriAsia.add(CITA);
		territoriAsia.add(JACUZIA);
		territoriAsia.add(KAMCHATKA);
		territoriAsia.add(GIAPPONE);
		territoriAsia.add(MONGOLIA);
		territoriAsia.add(AFGHANISTAN);
		territoriAsia.add(CINA);
		territoriAsia.add(MEDIO_ORIENTE);
		territoriAsia.add(INDIA);
		territoriAsia.add(SIAM);
		
		return territoriAsia;
	}
	
	public static List<Territorio> getTerritoriOceania(){
		List<Territorio> territoriOceania = new ArrayList<Territorio>();
		territoriOceania.add(INDONESIA);
		territoriOceania.add(NUOVA_GUINEA);
		territoriOceania.add(AUSTRALIA_OCCIDENTALE);
		territoriOceania.add(AUSTRALIA_ORIENTALE);
		return territoriOceania;
	}
}
