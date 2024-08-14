package it.desimone.replayrd3;

import it.desimone.replayrd3.azioni.Attacco;
import it.desimone.replayrd3.azioni.Azione;
import it.desimone.replayrd3.azioni.GiocoTris;
import it.desimone.replayrd3.azioni.Invasione;
import it.desimone.replayrd3.azioni.RicezioneCarta;
import it.desimone.replayrd3.azioni.Rinforzo;
import it.desimone.replayrd3.azioni.Sdadata;
import it.desimone.replayrd3.azioni.Spostamento;
import it.desimone.replayrd3.azioni.TempoRimasto;
import it.desimone.utils.MyLogger;
import it.desimone.utils.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Analizzatore {


	public static final String SITE_PARTITA = "https://rd3.editricegiochi.it/site/schedapartita.php?gameid=";

	
	private static final String NO_LOG_DATA = "no log data";
	private static final String PARTITA_NON_TERMINATA = "partita non terminata";
	private static final String FINE_PARTITA = "fine partita";
	
	public static void main (String[] args) throws IOException, ParseException{
		//Partita partita = elaboraPartita("1717233", false);
//		Partita partita = elaboraPartita("1717131", false);
//		List<Azione> azioni = partita.getAzioniLog();
//		for (Azione azione: azioni){
//			System.out.println(azione);
//		}

		byte[] citaByte = new byte[]{63, 105, 116, 97};
		String cita = new String(citaByte);	
		
		StringBuilder buffer = new StringBuilder();
		buffer.append("(\\?)");
		buffer.append("ita");

		String toReplace = buffer.toString();
		String testo = "blablabla"+cita+"opsopsops";
		
		System.out.println(testo);
		System.out.println(testo.replaceAll(toReplace, "CITA"));
	}
	
	public static Partita elaboraPartita(String idPartita, boolean soloRisultati) throws IOException, ParseException{

		MyLogger.getLogger().finer("INIZIO elaborazione della partita "+idPartita);
		
		Connection connection = Jsoup.connect(SITE_PARTITA+idPartita);
		connection = connection.timeout(0);
		if (soloRisultati){
			connection = connection.maxBodySize(7500);
		}else{
			connection = connection.maxBodySize(0);
		}
		Document doc = connection.get();
		//Document doc = Jsoup.parse(new URL(SITE_PARTITA+idPartita).openStream(), "utf-8", SITE_PARTITA+idPartita);
		Element body = doc.body();
		
		Element sectionPrincipale = body.getElementById("contents");
		String testoProprio = sectionPrincipale.ownText();
		if (testoProprio != null 
			&& (
				testoProprio.trim().toLowerCase().contains(NO_LOG_DATA) 
			 || testoProprio.trim().toLowerCase().contains(PARTITA_NON_TERMINATA)
			 || !sectionPrincipale.text().toLowerCase().contains(FINE_PARTITA)
			   )
			){
			MyLogger.getLogger().finest("Partita "+idPartita+" scartata: "+testoProprio);
			return null;
		}
		
		Partita partita = new Partita();
		partita.setIdPartita(Long.valueOf(idPartita));
		Elements h1s = body.select("h1");
		for (Element element: h1s){
			Elements childrens = element.children();
			for (Element child: childrens){
				//E' il primo <h1> con un figlio 
				// <h1><div style="font: 22px Exo, Verdana, Arial, Helvetica, sans-serif; font-variant: small-caps;">prestigious_1183824_winter66</div></h1>
				String numeroPartita = child.html();
				partita.setNomePartita(numeroPartita);
				break;
			}
		}
		
		Element divVincitore = body.select("div[style*=background:#1A481F]").get(0);
		String vincitore = divVincitore.child(0).html();
		partita.setVincitore(vincitore);
		
		Elements tabellePartita = sectionPrincipale.getElementsByTag("table");
		
		Element tabellaPartita = tabellePartita.get(0);
		
		//Element tabellaPartita = body.select("table[width=300]").get(0);
		
		popolaPartitaConRisultati(partita, tabellaPartita);
		popolaPartitaConDataEOra(partita, body);
		if (!soloRisultati){
			Element tabellaObiettivi = tabellePartita.get(1);
			popolaPartitaConObiettivi(partita, tabellaObiettivi);
			Element tabellaLog = tabellePartita.get(2);
			popolaPartitaConLog(partita, tabellaLog);
		}	

		MyLogger.getLogger().finest(partita.toString());
		MyLogger.getLogger().finer("FINE elaborazione della partita "+idPartita);
		
		return partita;
	}
	
	
	private static void popolaPartitaConRisultati(Partita partita, Element tabellaPartita){
		Elements players = tabellaPartita.getElementsByTag("tr");

		Map<String,Integer> risultati = new LinkedHashMap<String,Integer>();
		List<Giocatore> giocatori = new ArrayList<Giocatore>();
		short turno = 0;
		for (Element player: players){
			Elements datiPlayer = player.getElementsByTag("td");
			Element nickElement = null;
			Element risultatoElement = null;
			if (datiPlayer.size() > 2){
				nickElement = datiPlayer.get(1).getElementsByTag("font").get(0);
				risultatoElement = datiPlayer.get(2);
			}else{
				nickElement = datiPlayer.get(0).getElementsByTag("font").get(0);
				risultatoElement = datiPlayer.get(1);
			}

			String nickname = nickElement.html();
			String risultato = risultatoElement.html();
			risultati.put(nickname, Integer.valueOf(risultato));
			giocatori.add(new Giocatore(nickname, ++turno));
		}
		partita.setRisultati(risultati);
		partita.setGiocatori(giocatori);
		
	}
	
	private static void popolaPartitaConDataEOra(Partita partita, Element body) throws IOException, ParseException{
		Elements orari = body.getElementsContainingOwnText("iniziata");
		Element orario = orari.get(0);
		String testoOrario = orario.ownText(); //orario.html();
		String oraDiInizioStr = testoOrario.substring(20,40);
		String oraDiFineStr = testoOrario.substring(54,62);
		Date oraDiInizio = Utils.DATA_INIZIO.parse(oraDiInizioStr);
		Calendar dataDiInizio = new GregorianCalendar();
		dataDiInizio.setTime(oraDiInizio);
		
		Date oraDiFine = Utils.DATA_FINE.parse(oraDiFineStr);
		Calendar dataDiFine = new GregorianCalendar();
		dataDiFine.setTime(oraDiFine);
		dataDiFine.set(Calendar.DAY_OF_MONTH, dataDiInizio.get(Calendar.DAY_OF_MONTH));
		dataDiFine.set(Calendar.MONTH, dataDiInizio.get(Calendar.MONTH));
		dataDiFine.set(Calendar.YEAR, dataDiInizio.get(Calendar.YEAR));
		if (dataDiFine.get(Calendar.HOUR_OF_DAY) < dataDiInizio.get(Calendar.HOUR_OF_DAY)){
			dataDiFine.add(Calendar.DAY_OF_MONTH, 1);
		}
		oraDiFine = dataDiFine.getTime();
		
		partita.setDataDiInizio(oraDiInizio);
		partita.setDataDiFine(oraDiFine);
		
		Long durata = getDateDiff(oraDiInizio, oraDiFine, TimeUnit.MINUTES);
		partita.setDurata(durata.intValue());
	}
	
	private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	private static void popolaPartitaConObiettivi(Partita partita, Element tabellaObiettivi){
		Elements stati = tabellaObiettivi.getElementsByTag("tr");

		List<Giocatore> giocatori = partita.getGiocatori();
		for (Giocatore giocatore: giocatori){
			giocatore.setObiettivo(new ArrayList<Territorio>());
		}

		for (Element stato: stati){
			Elements statiObiettivo = stato.getElementsByTag("td");
			if (statiObiettivo.size() > 1){
				Element statoObiettivo = statiObiettivo.get(0);
				String nomeStato = statoObiettivo.text().trim();
				for (short indiceGiocatore = 1; indiceGiocatore <= giocatori.size(); indiceGiocatore++){
					Element statoGiocatore = statiObiettivo.get(indiceGiocatore);
					if (statoGiocatore.hasAttr("bgcolor")){
						Giocatore giocatore = giocatori.get(indiceGiocatore-1);
						giocatore.getObiettivo().add(Territorio.getTerritorioByDescrizione(nomeStato));
					}
				}
			}
		}
		for (Giocatore giocatore: giocatori){
			List<Territorio> obiettivo = giocatore.getObiettivo();
			if (obiettivo != null && !obiettivo.isEmpty()){
				int numeroObiettivo = ObiettiviUtils.determinaNumeroObiettivo(obiettivo);
				giocatore.setNumeroObiettivo(numeroObiettivo);
			}
		}

	}
	
	private static void popolaPartitaConLog(Partita partita, Element tabellaLog) throws UnsupportedEncodingException{
		Elements righeLog = tabellaLog.getElementsByTag("tr");
		
		//La prima riga rappresenta il piazzamento iniziale fatto dal programma
		
		Element piazzamentoLog = righeLog.get(0);
		RigaLog rigaLog1 = elaboraPrimaRiga(piazzamentoLog);
		List<Rinforzo> piazzamentoIniziale = elaboraPiazzamentoIniziale(rigaLog1);
		
		List<Azione> azioniPartita = new ArrayList<Azione>();
		azioniPartita.addAll(piazzamentoIniziale);
		for (int indexLog = 1; indexLog < righeLog.size(); indexLog++){

			//MyLogger.getLogger().finest("[Riga] - "+indexLog+" "+righeLog.get(indexLog).toString());
			RigaLog rigaLog = elaboraRiga(righeLog.get(indexLog));

			try{
				List<Azione> azioni = getAzioniByLog(rigaLog);
				if (azioni != null){
					azioniPartita.addAll(azioni);
				}
			}catch(Exception e){
				throw new IllegalStateException("La riga con ID "+rigaLog.getIdLog()+" ha generato un problema: "+e.getMessage());
			}

		}
		partita.setAzioniLog(azioniPartita);
	}
	
	
	private static RigaLog elaboraPrimaRiga(Element rigaElement){
		byte[] citaByte = new byte[]{63, 105, 116, 97};
		String cita = new String(citaByte);	
		Elements elementsLog = rigaElement.getElementsByTag("td");
		if (elementsLog.size() != 4){
			throw new RuntimeException("La riga del log non ha 4 colonne: <<"+elementsLog.size()+">> "+elementsLog);
		}
		RigaLog rigaLog = new RigaLog();
		rigaLog.setIdLog(elementsLog.get(0).text().trim());
		rigaLog.setTimeLog(elementsLog.get(1).text().trim());
		rigaLog.setColoreLog(elementsLog.get(2).text().trim());
		StringBuilder buffer = new StringBuilder();
		buffer.append("(\\)");
		buffer.append(cita);
		rigaLog.setAzioneLog(elementsLog.get(3).html()/*.replaceAll("&ugrave;","ù").replaceAll(buffer.toString(), "Čita")*/);
		return rigaLog;
	}	
	
	private static List<Rinforzo> elaboraPiazzamentoIniziale(RigaLog rigaLog) throws UnsupportedEncodingException{
		List<Rinforzo> rinforziIniziali = new ArrayList<Rinforzo>();

		String[] piazzamentiIniziali = rigaLog.getAzioneLog().split("<br />");
		
		for (String piazzamentoIniziale: piazzamentiIniziali){
			int indexArmate = piazzamentoIniziale.indexOf("armate");
			int indexBoldInizio = piazzamentoIniziale.indexOf("<b>");
			int indexBoldFine = piazzamentoIniziale.indexOf("</b>");
			
			String nomeTerritorio = piazzamentoIniziale.substring(0, indexArmate).trim();
			String colore = piazzamentoIniziale.substring(indexBoldInizio+3, indexBoldFine).trim();

			Territorio territorio = Territorio.getTerritorioByDescrizione(nomeTerritorio);
			if (territorio == null){
				MyLogger.getLogger().severe(new Boolean(rigaLog.getAzioneLog().contains("?ita")).toString());
				MyLogger.getLogger().severe("ERRORE nel parsing del territorio ["+nomeTerritorio+" - "+Arrays.toString(nomeTerritorio.getBytes())+"]; Riga di log completa: "+rigaLog);
			}
			
			Rinforzo rinforzo = new Rinforzo(rigaLog);
			rinforzo.setGiocatoreCheAgisce(ColoreGiocatore.getColoreByDescrizione(colore));
			rinforzo.setTerritorioRinforzato(territorio);
			rinforzo.setNumeroDiRinforzi((short) 1);
			
			rinforziIniziali.add(rinforzo);
		}
		
		return rinforziIniziali;
	}	
	
	private static RigaLog elaboraRiga(Element rigaElement){
		Elements elementsLog = rigaElement.getElementsByTag("td");
		if (elementsLog.size() != 4){
			throw new RuntimeException("La riga del log non ha 4 colonne: <<"+elementsLog.size()+">> "+elementsLog);
		}
		RigaLog rigaLog = new RigaLog();
		rigaLog.setIdLog(elementsLog.get(0).text().trim());
		rigaLog.setTimeLog(elementsLog.get(1).text().trim());
		rigaLog.setColoreLog(elementsLog.get(2).text().trim());
		rigaLog.setAzioneLog(elementsLog.get(3).text().trim()/*.replace("&ugrave;","ù").replace("?ita", "Čita")*/);
		return rigaLog;
	}	
	
	private static Azione getAzioneByLog(RigaLog rigaLog){
		Azione azione = null;
		String azioneLog = rigaLog.getAzioneLog();
		
		if (azioneLog.startsWith("Rinforza")){	
			try{
			azione = generaRinforzo(rigaLog);
			}catch(Exception e){
				String idLog = rigaLog.getIdLog().trim();
				System.out.println(e);
			}
		}else if (azioneLog.startsWith("Attacca")){
			azione = generaAttacco(rigaLog);
		}else if (azioneLog.startsWith("Invade")){
			azione = generaInvasione(rigaLog);
		}else if (azioneLog.startsWith("Spostamento")){
			azione = generaSpostamento(rigaLog);
		}else if (azioneLog.startsWith("Riceve la carta")){
			azione = generaRicezioneCarta(rigaLog);
		}else if (azioneLog.startsWith("Gioca in tris")){
			azione = generaGiocoTris(rigaLog);
		}else if (azioneLog.startsWith("Tempo")){
			azione = generaTempoRimasto(rigaLog);
		}
		return azione;
	}
	
	private static List<Azione> getAzioniByLog(RigaLog rigaLog){
		List<Azione> azioni = new ArrayList<Azione>();
		String azioneLog = rigaLog.getAzioneLog();
		
		if (azioneLog.startsWith("Rinforza")){	
			List<String> righeRinforzo = getRigheAzioni(azioneLog,"Rinforza"); //necessario perch� quando piazza il server scrive i rinforzi nella stessa riga
			for (String rigaRinforzo: righeRinforzo){
				Rinforzo rinforzo = generaRinforzo(rigaLog, rigaRinforzo);
				azioni.add(rinforzo);
			}
		}else if (azioneLog.startsWith("Attacca")){
			azioni.add(generaAttacco(rigaLog));
		}else if (azioneLog.startsWith("Invade")){
			azioni.add(generaInvasione(rigaLog));
		}else if (azioneLog.startsWith("Spostamento")){
			azioni.add(generaSpostamento(rigaLog));
		}else if (azioneLog.startsWith("Riceve la carta")){
			azioni.add(generaRicezioneCarta(rigaLog));
		}else if (azioneLog.startsWith("Gioca in tris")){
			azioni.add(generaGiocoTris(rigaLog));
		}else if (azioneLog.startsWith("Tempo")){
			azioni.add(generaTempoRimasto(rigaLog));
		}else if (azioneLog.startsWith("Sdadata")){
			azioni.add(generaSdadata(rigaLog));
		}
		return azioni;
	}
	
	private static List<String> getRigheAzioni(String azioneLog, String pattern){
		List<String> result = new ArrayList<String>();
		
		String testoDaEsaminare = azioneLog;
		int indexOfPattern = testoDaEsaminare.indexOf(pattern);
		while (indexOfPattern != -1){
			int indexOfPattern2 = testoDaEsaminare.substring(1).indexOf(pattern);
			if (indexOfPattern2 == -1){
				result.add(testoDaEsaminare);
				indexOfPattern = -1;
			}else{
				result.add(testoDaEsaminare.substring(indexOfPattern, indexOfPattern2));
				testoDaEsaminare = testoDaEsaminare.substring(indexOfPattern2+1);
				indexOfPattern = 0;
			}
		}
		
		return result;
	}
	
	private static Rinforzo generaRinforzo(RigaLog rigaLog){
		
		String azioneLog = rigaLog.getAzioneLog();
		int indexCon = azioneLog.indexOf("con ");
		
		String nomeTerritorio = azioneLog.substring("Rinforza ".length(), indexCon).trim();
		String numeroArmate = azioneLog.substring(indexCon+3).trim();
		
		Territorio territorio = Territorio.getTerritorioByDescrizione(nomeTerritorio);
		Short numeroDiRinforzi = Short.valueOf(numeroArmate);
		
		Rinforzo rinforzo = new Rinforzo(rigaLog);
		rinforzo.setTerritorioRinforzato(territorio);
		rinforzo.setNumeroDiRinforzi(numeroDiRinforzi);
		
		return rinforzo;
	}
	
	private static Rinforzo generaRinforzo(RigaLog rigaLog, String azioneLog){
		
		int indexCon = azioneLog.indexOf("con ");
		
		String nomeTerritorio = azioneLog.substring("Rinforza ".length(), indexCon).trim();
		String numeroArmate = azioneLog.substring(indexCon+3).trim();
		
		Territorio territorio = Territorio.getTerritorioByDescrizione(nomeTerritorio);
		Short numeroDiRinforzi = Short.valueOf(numeroArmate);
		
		Rinforzo rinforzo = new Rinforzo(rigaLog);
		rinforzo.setTerritorioRinforzato(territorio);
		rinforzo.setNumeroDiRinforzi(numeroDiRinforzi);
		
		return rinforzo;
	}
	
	private static Attacco generaAttacco(RigaLog rigaLog){
		String azioneLog = rigaLog.getAzioneLog();
		int indexDa = azioneLog.indexOf(" da ");
		int indexA = azioneLog.indexOf(" a ");
		int indexPar = azioneLog.indexOf("(");
		int indexAtt = azioneLog.indexOf(" Att (");
		int indexDef = azioneLog.indexOf(") Def (");
		
		String nomeTerritorioAttaccante = azioneLog.substring(indexDa+4,indexA).trim();
		String nomeTerritorioAttaccato  = azioneLog.substring(indexA+3,indexPar).trim();
		String dadiAttaccoString = azioneLog.substring(indexAtt+6,indexDef).trim();
		String dadiDifesaString = azioneLog.substring(indexDef+7,azioneLog.lastIndexOf(")")).trim();
		
		Territorio territorioAttaccante = Territorio.getTerritorioByDescrizione(nomeTerritorioAttaccante);
		Territorio territorioAttaccato = Territorio.getTerritorioByDescrizione(nomeTerritorioAttaccato);
		
		String[] dadiAttaccoArray = dadiAttaccoString.split(",");
		String[] dadiDifesaArray = dadiDifesaString.split(",");

		Byte[] dadiAttacco = new Byte[dadiAttaccoArray.length];
		Byte[] dadiDifesa = new Byte[dadiDifesaArray.length];
		
		for (int indexAttacco = 0; indexAttacco < dadiAttaccoArray.length; indexAttacco++){
			String dadoAttacco = dadiAttaccoArray[indexAttacco].trim();
			dadiAttacco[indexAttacco] = Byte.valueOf(dadoAttacco);
		}
		
		for (int indexDifesa = 0; indexDifesa < dadiDifesaArray.length; indexDifesa++){
			String dadoDifesa = dadiDifesaArray[indexDifesa].trim();
			dadiDifesa[indexDifesa] = Byte.valueOf(dadoDifesa);
		}
		
		Attacco attacco = new Attacco(rigaLog);
		attacco.setTerritorioAttaccante(territorioAttaccante);
		attacco.setTerritorioAttaccato(territorioAttaccato);
		attacco.setDadiAttacco(dadiAttacco);
		attacco.setDadiDifesa(dadiDifesa);
		
		return attacco;
	}
	
	private static Invasione generaInvasione(RigaLog rigaLog){
		String azioneLog = rigaLog.getAzioneLog();
		int indexInvade = azioneLog.indexOf("Invade: ");
		int indexCon = azioneLog.indexOf(" con ");
		int indexCarri = azioneLog.indexOf(" carri");
		int indexDa = azioneLog.indexOf(" da ");

		String nomeTerritorioInvaso = azioneLog.substring(indexInvade+8,indexCon).trim();
		String nomeTerritorioInvasore  = azioneLog.substring(indexDa+4).trim();
		String numeroArmateInvasori = azioneLog.substring(indexCon+5,indexCarri).trim();
		
		Territorio territorioInvaso = Territorio.getTerritorioByDescrizione(nomeTerritorioInvaso);
		Territorio territorioInvasore = Territorio.getTerritorioByDescrizione(nomeTerritorioInvasore);
		Short numeroDiArmateInvasori = Short.valueOf(numeroArmateInvasori);

		Invasione invasione = new Invasione(rigaLog);
		invasione.setTerritorioInvaso(territorioInvaso);
		invasione.setTerritorioInvasore(territorioInvasore);
		invasione.setNumeroDiArmateInvasori(numeroDiArmateInvasori);
		
		return invasione;
	}
	
	private static Spostamento generaSpostamento(RigaLog rigaLog){
		String azioneLog = rigaLog.getAzioneLog();
		int indexSpostamento = azioneLog.indexOf("Spostamento: ");
		int indexCarri = azioneLog.indexOf(" carri");
		int indexDa = azioneLog.indexOf(" da ");
		int indexA = azioneLog.indexOf(" a ");

		String armateSpostate = azioneLog.substring(indexSpostamento+13,indexCarri).trim();
		String nomeTerritorioDiProvenienza = azioneLog.substring(indexDa+4,indexA).trim();
		String nomeTerritorioDiArrivo  = azioneLog.substring(indexA+3).trim();
		
		Territorio territorioDiProvenienza = Territorio.getTerritorioByDescrizione(nomeTerritorioDiProvenienza);
		Territorio territorioDiArrivo = Territorio.getTerritorioByDescrizione(nomeTerritorioDiArrivo);
		Short numeroDiArmateSpostate = Short.valueOf(armateSpostate);

		Spostamento spostamento = new Spostamento(rigaLog);
		spostamento.setTerritorioDiProvenienza(territorioDiProvenienza);
		spostamento.setTerritorioDiArrivo(territorioDiArrivo);
		spostamento.setNumeroDiArmateSpostate(numeroDiArmateSpostate);
		
		return spostamento;
	}
	
	private static RicezioneCarta generaRicezioneCarta(RigaLog rigaLog){
		String azioneLog = rigaLog.getAzioneLog();
		int indexRiceve = azioneLog.indexOf("Riceve la carta ");

		String siglaCarta = azioneLog.substring(indexRiceve+16,indexRiceve+19).trim();
		
		Territorio cartaRicevuta = Territorio.getTerritorioBySigla(siglaCarta);

		RicezioneCarta ricezioneCarta = new RicezioneCarta(rigaLog);
		ricezioneCarta.setCartaRicevuta(cartaRicevuta);
		
		return ricezioneCarta;
	}
	
	private static GiocoTris generaGiocoTris(RigaLog rigaLog){
		String azioneLog = rigaLog.getAzioneLog();
		int indexGioco = azioneLog.indexOf("Gioco in tris da ");
		int indexArmate = azioneLog.indexOf(" armate");
		int indexCarte = azioneLog.indexOf("carte:");
		int indexTonda = azioneLog.indexOf(")");

		String valoreTrisStr = azioneLog.substring(indexGioco+17,indexArmate).trim();
		String[] carte = azioneLog.substring(indexCarte+7,indexTonda).trim().split(" ");
		
		Territorio[] carteTris = new Territorio[3];
		byte index = 0;
		for (String carta: carte){
			carteTris[index++] = Territorio.getTerritorioBySigla(carta);
		}
		Short valoreTris = Short.valueOf(valoreTrisStr);

		GiocoTris giocoTris = new GiocoTris(rigaLog);
		giocoTris.setCarteTris(carteTris);
		giocoTris.setValoreTris(valoreTris);
		
		return giocoTris;
	}
	
	private static TempoRimasto generaTempoRimasto(RigaLog rigaLog){
		String azioneLog = rigaLog.getAzioneLog();
		int indexRimasto = azioneLog.indexOf("Tempo rimasto");
		int indexEffettivo = azioneLog.indexOf("Tempo effettivo");
		String tempoRimastoLog = azioneLog.substring(indexRimasto, indexEffettivo);
		
		int indexMinuti = tempoRimastoLog.indexOf("minut");
		int indexSecondi = tempoRimastoLog.indexOf("second");
		
		String minuti = "0";
		if (indexMinuti >=0){
			minuti = tempoRimastoLog.substring(15,indexMinuti).trim();
		}
		String secondi = "0";
		if (indexSecondi >=0){
			if (indexMinuti >=0){
				secondi = tempoRimastoLog.substring(indexMinuti+9,indexSecondi).trim();
			}else{
				secondi = tempoRimastoLog.substring(15,indexSecondi).trim();
			}
		}
		Short minutiRimasti = Short.valueOf(minuti);
		Short secondiRimasti = Short.valueOf(secondi);
		
		TempoRimasto tempoRimasto = new TempoRimasto(rigaLog);
		tempoRimasto.setMinutiRimasti(minutiRimasti);
		tempoRimasto.setSecondiRimasti(secondiRimasti);
		
		return tempoRimasto;
	}
	
	private static Sdadata generaSdadata(RigaLog rigaLog){
		String azioneLog = rigaLog.getAzioneLog();
		int indexChiusura = azioneLog.indexOf("chiusura al ");
		int indexSdadata = azioneLog.indexOf("dadi: ");
		
		String chiusuraLog = azioneLog.substring(indexChiusura+"chiusura al ".length(), indexChiusura+"chiusura al ".length()+1).trim();
		String sdadataLog = null;
		if (indexSdadata >= 0){ //Potrebbe non esserci stata sdadata per numero di stati conquistati.
			sdadataLog = azioneLog.substring(indexSdadata+"dadi: ".length(), indexSdadata+"dadi: ".length()+2).trim();
		}

		Short dadiChiusura = Short.valueOf(chiusuraLog);
		Short dadiSdadata = null;
		
		if (sdadataLog != null){
			dadiSdadata = Short.valueOf(sdadataLog);
		}
		
		Sdadata sdadata = new Sdadata(rigaLog);
		sdadata.setChiusura(dadiChiusura);
		sdadata.setSdadata(dadiSdadata);
		
		return sdadata;
	}
}

