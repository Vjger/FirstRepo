package it.desimone.rd3analyzer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RD3Analyzer {
	
	private static final String URL_BASE = "http://rd3.editricegiochi.it/site/schedapartita.php?gameid=";
	private static final String URL_BASE_CHALLENGE = "http://rd3.editricegiochi.it/site/schedagiocatore.php?mtype=challenge&timeframe=tutto&login=";
	private static final String URL_BASE_DIGITAL = "http://rd3.editricegiochi.it/site/schedagiocatore.php?mtype=digital&timeframe=tutto&login=";
	
	public static Partita elaboraSchedaRD3(String url, String giocatore) throws IOException {
		//System.out.println(url);
		Partita partita = new Partita();
		Connection connection = Jsoup.connect(url);
		connection = connection.timeout(0);
		Document doc = connection.get();
		Element body = doc.body();
		
		determinaNumeroPartita(body, partita);
		
		elaboraSchedaPartita(body, partita, giocatore);
		//determinaGiocatori(body, partita);
		//determinaStatistiche(body, partita);
		//System.out.println(partita);
		return partita;
	}

	public static List<String> elaboraPartiteRD3(String url) throws IOException {
		System.out.println(url);
		Connection connection = Jsoup.connect(url);
		connection = connection.timeout(0);
		Document doc = connection.get();
		Element body = doc.body();
				
		return elaboraListaPartite(body);
	}
	
	private static void determinaNumeroPartita(Element body, Partita partita){
		Elements h1s = body.select("h1");
		for (Element element: h1s){
			Elements childrens = element.children();
			for (Element child: childrens){
				//E' il primo <h1> con un figlio ><h1><font color="blue">digital_3513907_fortitudo</font></h1>
				partita.setNomePartita(child.html());
				break;
			}
		}
	}
	
	private static void elaboraSchedaPartita(Element body, Partita partita, String giocatore){
		Element section = body.getElementById("contents");
		Elements tables = section.getElementsByTag("table");
		Element tabellaScore = tables.get(0);
		analizzaTabellaScore(tabellaScore, partita);
		String colore = partita.getColore(giocatore);
		//System.out.println(partita+" - "+giocatore+" ha il colore "+colore);
		Element tabellaLog = tables.get(2);
		Integer[] attacchi = analizzaTabellaLog(tabellaLog, colore);
		System.out.println("Partita "+partita.getNomePartita()+": "+giocatore+" ha effettuato "+attacchi[0]+" attacchi di cui "+attacchi[1]+" veloci");
	}
	
	private static List<String> elaboraListaPartite(Element body){
		Element section = body.getElementById("contents");
		Elements tables = section.getElementsByTag("table");
		Element tabellaPartite = tables.get(4);
		List<String> listaPartite = analizzaTabellaPartite(tabellaPartite);
		return listaPartite;
	}
	
	private static List<String> analizzaTabellaPartite(Element tabellaPartite){
		List<String> listaPartite = new ArrayList<String>();
		Element tbody = tabellaPartite.child(0);

		Elements trs = tbody.children();
		for (int index = 1; index < trs.size(); index++){
			Element tr = trs.get(index);
			Elements tds = tr.children();
			listaPartite.add(tds.get(0).child(0).html());
		}
		return listaPartite;
	}
	
	private static void analizzaTabellaScore(Element tabellaScore, Partita partita){
		Element tbody = tabellaScore.child(0);

		Elements trs = tbody.children();
		Map<String,Integer> risultati = new HashMap<String,Integer>();
		for (Element tr: trs){
			Elements tds = tr.children();
			risultati.put(tds.get(0).child(0).child(0).html(), Integer.valueOf(tds.get(1).html()));
		}
		partita.setRisultati(risultati);
	}
	
	
	private static Integer[] analizzaTabellaLog(Element tabellaLog, String colorePlayer){
		Element tbody = tabellaLog.child(0);
		Integer progressivo;
		String orario;
		String colore;
		String log;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar oldCalendar = null;
		int numeroAttacchi = 0; int numeroAttacchiVeloci = 0;
		Elements trs = tbody.children();
		for (Element tr: trs){
			Elements tds = tr.children();
			progressivo = Integer.valueOf(tds.get(0).html().replaceAll("&nbsp;", ""));
			orario = tds.get(1).html().replaceAll("&nbsp;", "");
			if (tds.get(3).html().contains("Attacca")){
				colore = tds.get(2).child(0).child(0).html().trim();
				log = tds.get(3).child(0).html();
				if (colore.equalsIgnoreCase(colorePlayer)){
					numeroAttacchi++;
					//System.out.println(progressivo +"\t"+ orario+"\t"+colore+"\t"+log);
					try {
						Date ora = dateFormat.parse(orario);
						Calendar calendar = new GregorianCalendar();
						calendar.setTimeInMillis(ora.getTime());
						if (oldCalendar != null){
							if (calendar.compareTo(oldCalendar) == 0){
								numeroAttacchiVeloci++;
								System.out.println(progressivo +"\t"+ orario+"\t"+colore+"\t"+log);
							}
							oldCalendar.add(Calendar.SECOND, 1);
							if (calendar.compareTo(oldCalendar) == 0){
								numeroAttacchiVeloci++;
								System.out.println(progressivo +"\t"+ orario+"\t"+colore+"\t"+log);
							}
						}
						oldCalendar = calendar;
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return new Integer[]{numeroAttacchi, numeroAttacchiVeloci};
	}
	
	public static void main (String[] args) throws IOException{
		String giocatore = "panzanella";
		List<String> partiteGiocatore = elaboraPartiteRD3(URL_BASE_CHALLENGE+giocatore);
		System.out.println("Lette "+partiteGiocatore.size()+" partite");

		for (String match: partiteGiocatore){
			elaboraSchedaRD3(URL_BASE+match, giocatore);
		}
	}
}
