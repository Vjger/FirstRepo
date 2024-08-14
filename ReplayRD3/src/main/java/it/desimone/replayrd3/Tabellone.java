package it.desimone.replayrd3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.desimone.replayrd3.azioni.Attacco;
import it.desimone.replayrd3.azioni.Azione;
import it.desimone.replayrd3.azioni.GiocoTris;
import it.desimone.replayrd3.azioni.Invasione;
import it.desimone.replayrd3.azioni.RicezioneCarta;
import it.desimone.replayrd3.azioni.Rinforzo;
import it.desimone.replayrd3.azioni.Spostamento;

public class Tabellone {
	
	private Integer numeroTurno = 0;
	private Integer logId;
	private String time;
	
	private Map<ColoreGiocatore, TabellinoGiocatore> tabelliniGiocatori;
	private Map<Territorio, TabellinoTerritorio> tabelliniTerritori;
	
	
	public Tabellone(Partita partita){
		tabelliniGiocatori = new HashMap<ColoreGiocatore, TabellinoGiocatore>();
		tabelliniTerritori = new HashMap<Territorio, TabellinoTerritorio>();
		initTabellini(partita);
	}
	
	private void initTabellini(Partita partita){
		for (Territorio territorio: Territorio.values()){
			if (territorio != Territorio.JOLLY1 && territorio != Territorio.JOLLY2){
				tabelliniTerritori.put(territorio, new TabellinoTerritorio());
			}
		}	
		for (Giocatore giocatore: partita.getGiocatori()){
			tabelliniGiocatori.put(giocatore.getColoreGiocatore(), new TabellinoGiocatore(giocatore.getObiettivo()));
		}
	}
	
	public Tabellone(Tabellone tabellone){
		this.numeroTurno = tabellone.getNumeroTurno();
		this.logId = tabellone.getLogId();
		this.time = tabellone.getTime();
		this.tabelliniGiocatori = new HashMap<ColoreGiocatore, TabellinoGiocatore>();
		for (ColoreGiocatore coloreGiocatore: tabellone.getTabelliniGiocatori().keySet()){
			this.tabelliniGiocatori.put(coloreGiocatore,(TabellinoGiocatore) tabellone.getTabelliniGiocatori().get(coloreGiocatore).clone());
		}
		this.tabelliniTerritori = new HashMap<Territorio, TabellinoTerritorio>();
		for (Territorio territorio: tabellone.getTabelliniTerritori().keySet()){
			this.tabelliniTerritori.put(territorio,(TabellinoTerritorio) tabellone.getTabelliniTerritori().get(territorio).clone());
		}
	}
	
	public Map<ColoreGiocatore, TabellinoGiocatore> getTabelliniGiocatori() {
		return tabelliniGiocatori;
	}

	public Map<Territorio, TabellinoTerritorio> getTabelliniTerritori() {
		return tabelliniTerritori;
	}
	
	public Integer getNumeroTurno() {
		return numeroTurno;
	}

	public void setNumeroTurno(Integer numeroTurno) {
		this.numeroTurno = numeroTurno;
	}

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setTabelliniGiocatori(Map<ColoreGiocatore, TabellinoGiocatore> tabelliniGiocatori) {
		this.tabelliniGiocatori = tabelliniGiocatori;
	}

	public TabellinoGiocatore getTabellinoGiocatore(ColoreGiocatore coloreGiocatore){
		return tabelliniGiocatori.get(coloreGiocatore);
	}
	public TabellinoTerritorio getTabellinoTerritorio(Territorio territorio){
		return tabelliniTerritori.get(territorio);
	}
	
	public void calcolaAttacco(Attacco attacco){
		setRecord(attacco);
		
		Byte[] dadiAttacco = attacco.getDadiAttacco();
		Byte[] dadiDifesa = attacco.getDadiDifesa();
		
		Arrays.sort(dadiAttacco, Collections.reverseOrder());
		Arrays.sort(dadiDifesa, Collections.reverseOrder());
		
		TabellinoTerritorio tabellinoTerritoriAttaccante = tabelliniTerritori.get(attacco.getTerritorioAttaccante());
		TabellinoTerritorio tabellinoTerritoriAttaccato = tabelliniTerritori.get(attacco.getTerritorioAttaccato());
		
		TabellinoGiocatore tabellinoGiocatoreAttaccante = tabelliniGiocatori.get(tabellinoTerritoriAttaccante.getGiocatore());
		TabellinoGiocatore tabellinoGiocatoreAttaccato = tabelliniGiocatori.get(tabellinoTerritoriAttaccato.getGiocatore());
		
		
		for (byte index = 0; index < dadiAttacco.length; index++){
			try{
			if (dadiDifesa.length>= index+1 && dadiDifesa[index] != null){
				if (dadiAttacco[index] > dadiDifesa[index]){
					tabellinoTerritoriAttaccato.perdeUnaArmata();	
					tabellinoGiocatoreAttaccato.perdeUnaArmata();
				}else{
					tabellinoTerritoriAttaccante.perdeUnaArmata();	
					tabellinoGiocatoreAttaccante.perdeUnaArmata();
				}
			}
			}catch (ArrayIndexOutOfBoundsException ai){
				System.out.println(attacco.getLogId());
			}
		}
	}
	
	public void calcolaRinforzo(Rinforzo rinforzo){
		setRecord(rinforzo);
		TabellinoTerritorio tabellinoTerritorioRinforzato = tabelliniTerritori.get(rinforzo.getTerritorioRinforzato());
		ColoreGiocatore giocatoreCheRinforza = tabellinoTerritorioRinforzato.getGiocatore();
		TabellinoGiocatore tabellinoGiocatoreCheRinforza = tabelliniGiocatori.get(rinforzo.getGiocatoreCheAgisce());
		if (giocatoreCheRinforza == null) {//significa che � il piazzamento iniziale
			tabellinoTerritorioRinforzato.setGiocatore(rinforzo.getGiocatoreCheAgisce());
			tabellinoGiocatoreCheRinforza.acquisisciUnTerritorio(rinforzo.getTerritorioRinforzato());
		}
		//TabellinoGiocatore tabellinoGiocatoreCheRinforza = tabelliniGiocatori.get(tabellinoTerritorioRinforzato.getGiocatore());
		tabellinoTerritorioRinforzato.aggiungeArmate(rinforzo.getNumeroDiRinforzi());
		tabellinoGiocatoreCheRinforza.aggiungeArmate(rinforzo.getNumeroDiRinforzi());	
	}
	
	
	public void calcolaSpostamento(Spostamento spostamento){
		setRecord(spostamento);
		TabellinoTerritorio tabellinoTerritorioDiProvenienza = tabelliniTerritori.get(spostamento.getTerritorioDiProvenienza());
		TabellinoTerritorio tabellinoTerritorioDiArrivo = tabelliniTerritori.get(spostamento.getTerritorioDiArrivo());
		tabellinoTerritorioDiProvenienza.sottraiArmate(spostamento.getNumeroDiArmateSpostate());
		tabellinoTerritorioDiArrivo.aggiungeArmate(spostamento.getNumeroDiArmateSpostate());
	}
	
	
	public void calcolaInvasione(Invasione invasione){
		setRecord(invasione);
		TabellinoTerritorio tabellinoTerritorioInvasore = tabelliniTerritori.get(invasione.getTerritorioInvasore());
		TabellinoTerritorio tabellinoTerritorioInvaso = tabelliniTerritori.get(invasione.getTerritorioInvaso());
		tabellinoTerritorioInvasore.sottraiArmate(invasione.getNumeroDiArmateInvasori());
		tabellinoTerritorioInvaso.setNumeroArmate(invasione.getNumeroDiArmateInvasori().intValue());

		TabellinoGiocatore tabellinoGiocatoreSconfitto  = tabelliniGiocatori.get(tabellinoTerritorioInvaso.getGiocatore());
		TabellinoGiocatore tabellinoGiocatoreVittorioso = tabelliniGiocatori.get(tabellinoTerritorioInvasore.getGiocatore());
		tabellinoGiocatoreSconfitto.perdiUnTerritorio(invasione.getTerritorioInvaso());
		tabellinoGiocatoreVittorioso.acquisisciUnTerritorio(invasione.getTerritorioInvaso());
		
		tabellinoTerritorioInvaso.setGiocatore(tabellinoTerritorioInvasore.getGiocatore());

	}
	
	public void calcolaRicezioneCarta(RicezioneCarta ricezioneCarta){
		setRecord(ricezioneCarta);
		TabellinoGiocatore tabellinoGiocatore = tabelliniGiocatori.get(ricezioneCarta.getGiocatoreCheAgisce());
		tabellinoGiocatore.aggiungiCarta(ricezioneCarta.getCartaRicevuta());
	}
	
	public void calcolaGiocoTris(GiocoTris giocoTris){
		setRecord(giocoTris);
		TabellinoGiocatore tabellinoGiocatore = tabelliniGiocatori.get(giocoTris.getGiocatoreCheAgisce());
		tabellinoGiocatore.restituisciTris(giocoTris.getCarteTris());
	}
	
	
	public Integer getArmateTerritorio(Territorio territorio){
		TabellinoTerritorio tabellinoTerritorio = tabelliniTerritori.get(territorio);
		return tabellinoTerritorio.getNumeroArmate();
	}
	
	private void setRecord(Azione azione){
		this.logId = azione.getLogId();
		this.time = azione.getTime();
	}
	
	
		
	@Override
	public String toString() {
		return "Tabellone [logId=" + logId + ", time=" + time + ", tabelliniGiocatori=" + tabelliniGiocatori
				+ ", tabelliniTerritori=" + tabelliniTerritori + "]";
	}



	public static class TabellinoTerritorio implements Cloneable{
		private ColoreGiocatore giocatore;
		private int numeroArmate;

		public ColoreGiocatore getGiocatore() {
			return giocatore;
		}
		public void setGiocatore(ColoreGiocatore giocatore) {
			this.giocatore = giocatore;
		}
		public int getNumeroArmate() {
			return numeroArmate;
		}
		public void setNumeroArmate(int numeroArmate) {
			this.numeroArmate = numeroArmate;
		}
		
		public void perdeUnaArmata(){
			numeroArmate--;
		}
		public void aggiungeArmate(Short rinforzi){
			numeroArmate += rinforzi;
		}
		public void sottraiArmate(Short rinforzi){
			numeroArmate -= rinforzi;
		}
		@Override
		public String toString() {
			return "TabellinoTerritorio [giocatore=" + giocatore + ", numeroArmate=" + numeroArmate + "]";
		}
		
		public Object clone(){
			Object result = null;
			try {
				result = super.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
	}
	
	
	public static class TabellinoGiocatore implements Cloneable{
		private int numeroArmate;
		private List<Territorio> territori;
		private List<Territorio> carte;
		private List<Territorio> obiettivo;
		
		public TabellinoGiocatore(List<Territorio> obiettivo){
			territori = new ArrayList<Territorio>();
			carte = new ArrayList<Territorio>();
			this.obiettivo = obiettivo;
		}
		public int getNumeroArmate() {
			return numeroArmate;
		}
		public void setNumeroArmate(int numeroArmate) {
			this.numeroArmate = numeroArmate;
		}
		public List<Territorio> getTerritori() {
			return territori;
		}
		public void setTerritori(List<Territorio> territori) {
			this.territori = territori;
		}
		public List<Territorio> getCarte() {
			return carte;
		}
		public void setCarte(List<Territorio> carte) {
			this.carte = carte;
		}
		public int getNumeroRinforzi() {
			int numeroRinforzi = territori.size()/3;
			if (hasOceania())numeroRinforzi+=2;
			if (hasSudAmerica())numeroRinforzi+=2;
			if (hasAfrica())numeroRinforzi+=3;
			if (hasNordAmerica())numeroRinforzi+=5;
			if (hasEuropa())numeroRinforzi+=5;
			if (hasAsia())numeroRinforzi+=7;
			return numeroRinforzi;
		}

		public int getNumeroPunti() {
			int numeroPunti = 0;
			for (Territorio territorioInPossesso: territori){
				if (obiettivo.contains(territorioInPossesso)){
					numeroPunti += territorioInPossesso.getValore();
				}
			}
			return numeroPunti;
		}

		public void perdeUnaArmata(){
			numeroArmate--;
		}
		public void aggiungeArmate(Short rinforzi){
			numeroArmate += rinforzi;
		}
		public void sottraiArmate(Short rinforzi){
			numeroArmate -= rinforzi;
		}
		
		public void aggiungiCarta(Territorio carta){
			carte.add(carta);
		}
		public void restituisciTris(Territorio[] carteTris){
			for (Territorio carta: carteTris){
				carte.remove(carta);
			}
		}
		public void acquisisciUnTerritorio(Territorio territorioAcquisito){
			territori.add(territorioAcquisito);
		}
		public void perdiUnTerritorio(Territorio territorioPerso){
			territori.remove(territorioPerso);
		}
		
		private boolean hasOceania(){
			boolean result = 
			   territori.contains(Territorio.INDONESIA)
			&& territori.contains(Territorio.NUOVA_GUINEA)
			&& territori.contains(Territorio.AUSTRALIA_OCCIDENTALE)
			&& territori.contains(Territorio.AUSTRALIA_ORIENTALE)
			;
			return result;
		}
		private boolean hasSudAmerica(){
			boolean result = 
			   territori.contains(Territorio.VENEZUELA)
			&& territori.contains(Territorio.BRASILE)
			&& territori.contains(Territorio.PERU)
			&& territori.contains(Territorio.ARGENTINA)
			;
			return result;
		}
		private boolean hasNordAmerica(){
			boolean result = 
			   territori.contains(Territorio.ALASKA)
			&& territori.contains(Territorio.ALBERTA)
			&& territori.contains(Territorio.TERRITORI_DEL_NORD_OVEST)
			&& territori.contains(Territorio.ONTARIO)
			&& territori.contains(Territorio.GROENLANDIA)
			&& territori.contains(Territorio.QUEBEC)
			&& territori.contains(Territorio.STATI_UNITI_OCCIDENTALI)
			&& territori.contains(Territorio.STATI_UNITI_ORIENTALI)
			&& territori.contains(Territorio.AMERICA_CENTRALE)
			;
			return result;
		}
		private boolean hasAfrica(){
			boolean result = 
			   territori.contains(Territorio.AFRICA_DEL_NORD)
			&& territori.contains(Territorio.EGITTO)
			&& territori.contains(Territorio.AFRICA_ORIENTALE)
			&& territori.contains(Territorio.CONGO)
			&& territori.contains(Territorio.AFRICA_DEL_SUD)
			&& territori.contains(Territorio.MADAGASCAR)
			;
			return result;
		}
		
		private boolean hasEuropa(){
			boolean result = 
			   territori.contains(Territorio.ISLANDA)
			&& territori.contains(Territorio.SCANDINAVIA)
			&& territori.contains(Territorio.GRAN_BRETAGNA)
			&& territori.contains(Territorio.EUROPA_SETTENTRIONALE)
			&& territori.contains(Territorio.UCRAINA)
			&& territori.contains(Territorio.EUROPA_OCCIDENTALE)
			&& territori.contains(Territorio.EUROPA_MERIDIONALE)
			;
			return result;
		}
		private boolean hasAsia(){
			boolean result = 
			   territori.contains(Territorio.URALI)
			&& territori.contains(Territorio.SIBERIA)
			&& territori.contains(Territorio.CITA)
			&& territori.contains(Territorio.JACUZIA)
			&& territori.contains(Territorio.KAMCHATKA)
			&& territori.contains(Territorio.GIAPPONE)
			&& territori.contains(Territorio.MONGOLIA)
			&& territori.contains(Territorio.AFGHANISTAN)
			&& territori.contains(Territorio.CINA)
			&& territori.contains(Territorio.MEDIO_ORIENTE)
			&& territori.contains(Territorio.INDIA)
			&& territori.contains(Territorio.SIAM)
			;
			return result;
		}
		@Override
		public String toString() {
			return "TabellinoGiocatore [numeroArmate=" + numeroArmate + ", territori=" + territori + ", carte=" + carte
					+ ", obiettivo=" + obiettivo + "]";
		}
		public Object clone(){
			Object result = null;
			try {
				result = super.clone();
				
				TabellinoGiocatore cloned = (TabellinoGiocatore) result;
				cloned.setCarte((List<Territorio>) ((ArrayList)this.carte).clone());
				cloned.setTerritori((List<Territorio>) ((ArrayList)this.territori).clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
		
	}
	
	public boolean isTabelloneCompleto(){
		boolean completo = false;
		
		int numeroCarriIniziale = 0;
		switch (tabelliniGiocatori.size()) {
		case 3:
			numeroCarriIniziale = 35*3;
			break;
		case 4:
			numeroCarriIniziale = 30*4;
			break;
		case 5:
			numeroCarriIniziale = 25*5;
			break;
		case 6:
			numeroCarriIniziale = 20*6;
			break;
		default:
			break;
		}
		
		int numeroArmateGlobali = 0;
		for (Territorio territorio: tabelliniTerritori.keySet()){
			numeroArmateGlobali += getArmateTerritorio(territorio);
		}
		completo = numeroArmateGlobali == numeroCarriIniziale;
		return completo;
	}

}
