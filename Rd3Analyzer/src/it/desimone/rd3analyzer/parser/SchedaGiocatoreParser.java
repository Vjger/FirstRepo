package it.desimone.rd3analyzer.parser;

import it.desimone.rd3analyzer.Partita;
import it.desimone.utils.MyLogger;
import it.desimone.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SchedaGiocatoreParser {

	private static final String URL_BASE = "http://rd3.editricegiochi.it/site/schedagiocatore.php?";
	private List<String> listaNumeriPagina;
	private String login;
	private FiltroModalita modalita;
	private FiltroPeriodo periodo;
	
	public static void main (String[] args){
		List<Partita> partite = new SchedaGiocatoreParser("Voyager").elaboraScheda();
		System.out.println(partite);
	}
	
	public SchedaGiocatoreParser(String nick, FiltroModalita modalita, FiltroPeriodo periodo){
		this.login = nick;
		if (modalita != null){
			this.modalita = modalita;
		}else{
			this.modalita = FiltroModalita.TUTTE;
		}
		if (periodo != null){
			this.periodo = periodo;
		}else{
			this.periodo = FiltroPeriodo.TUTTO;
		}
	}
	
	public SchedaGiocatoreParser(String nick){
		this.login = nick;
		this.modalita = FiltroModalita.TUTTE;
		this.periodo = FiltroPeriodo.TUTTO;
	}
	
	public List<Partita> elaboraScheda(){
		if (login == null){throw new RuntimeException("Non indicato il nick del giocatore interessato");};
		String urlScheda = URL_BASE+"login="+login+"&mtype="+modalita.getCodiceRicerca()+"&timeframe="+periodo.getCodiceRicerca();
		List<Partita> partite = elaboraScheda(urlScheda);
		//MyLogger.getLogger().info("Trovate "+partite.size()+" partite per il giocatore "+login);
		return partite;
	}
	
	private List<Partita> elaboraScheda(String urlScheda){
		List<Partita> partite = elaboraPagina(urlScheda);
		if (listaNumeriPagina != null && listaNumeriPagina.size() > 1){
			for (String numeroPagina: listaNumeriPagina){
				if (!numeroPagina.equals("1")){
					List<Partita> partiteAltrePagine = elaboraPagina(urlScheda+"&page="+numeroPagina);
					partite.addAll(partiteAltrePagine);
				}
			}
		}
		return partite;
	}
	
//	private List<PartitaGiocatore> elaboraPagina(String urlSchedaPagina){
//		Document doc = null;
//		try{
//			Connection connection = Jsoup.connect(urlSchedaPagina);
//			connection = connection.timeout(0);
//			//connection = connection.maxBodySize(6500);
//			doc = connection.get();
//		}catch(IOException ioe){
//			throw new RuntimeException("Errore nell'accesso alla scheda "+urlSchedaPagina+" :"+ioe.getMessage());
//		}
//
//		Element body = doc.body();
//		
//		Element gamesDiv = body.getElementById("gamesDiv");
//		
//		estraiTutteLePagineEventuali(gamesDiv);
//		
//		Elements tabellePartita = gamesDiv.getElementsByTag("table");
//		Element tabellaPartita = tabellePartita.get(0);
//		Element tbody = tabellaPartita.child(0);
//		Elements partite = tbody.children(); //getElementsByTag("tr");
//
//		List<PartitaGiocatore> idPartite = new ArrayList<PartitaGiocatore>();
//		//Salto la prima riga che è quella delle intestazioni
//		for (int index=1; index < partite.size(); index++){
//			Element partita = partite.get(index);
//			Elements datiPartita = partita.getElementsByTag("td");
//			Element urlPartita = datiPartita.get(0);
//			String idPartita = urlPartita.text();
//			String tipoPartita = datiPartita.get(1).text().trim();
//			if (idPartita != null && idPartita.trim().length() > 0){
//				PartitaGiocatore partitaGiocatore = new PartitaGiocatore(idPartita, tipoPartita);
//				idPartite.add(partitaGiocatore);
//			}
//		}
//		return idPartite;
//	}
	
	private List<Partita> elaboraPagina(String urlSchedaPagina){
		Document doc = null;
		try{
			Connection connection = Jsoup.connect(urlSchedaPagina);
			connection = connection.timeout(0);
			//connection = connection.maxBodySize(6500);
			doc = connection.get();
		}catch(IOException ioe){
			throw new RuntimeException("Errore nell'accesso alla scheda "+urlSchedaPagina+" :"+ioe.getMessage());
		}

		Element body = doc.body();
		
		Element gamesDiv = body.getElementById("gamesDiv");
		
		estraiTutteLePagineEventuali(gamesDiv);
		
		Elements tabellePartita = gamesDiv.getElementsByTag("table");
		Element tabellaPartita = tabellePartita.get(0);
		Element tbody = tabellaPartita.child(0);
		Elements righePartite = tbody.children(); //getElementsByTag("tr");

		List<Partita> partite = new ArrayList<Partita>();
		//Salto la prima riga che è quella delle intestazioni
		for (int index=1; index < righePartite.size(); index++){
			Element rigaPartita = righePartite.get(index);
			Elements datiPartita = rigaPartita.getElementsByTag("td");
			
			String idPartita   = estrapolaDatoPartita(datiPartita.get(0));
			if (idPartita != null && idPartita.length() > 0){
				Partita partita = new Partita();
				
				String tipoPartita = estrapolaDatoPartita(datiPartita.get(1));
				String puntiPartita = estrapolaDatoPartita(datiPartita.get(4));
				String durataPartita = estrapolaDatoPartita(datiPartita.get(5));
				String turniPartita = estrapolaDatoPartita(datiPartita.get(6));
				String inizioPartita = estrapolaDatoPartita(datiPartita.get(7));
	
				Element colonnaGiocatori = datiPartita.get(8);
				
				Elements tagsColonnaGiocatori = colonnaGiocatori.children();
				Map<String,Integer> risultati = new HashMap<String, Integer>();
				boolean isVincitore = false;
				for (Element tagColonnaGiocatori: tagsColonnaGiocatori){
					String tagName = tagColonnaGiocatori.tagName();
					if (tagName.equalsIgnoreCase("img")){
						String pathImmagine = tagColonnaGiocatori.attr("src");
						if (pathImmagine != null && pathImmagine.contains("trophy")){
							isVincitore = true;
						}
					}else if (tagName.equalsIgnoreCase("a")){
						String giocatore = estrapolaDatoPartita(tagColonnaGiocatori);
						if (giocatore != null){
							if (isVincitore){
								partita.setVincitore(giocatore);
								isVincitore = false;
							}
							if (giocatore.equalsIgnoreCase(login)){
								risultati.put(giocatore, Integer.valueOf(puntiPartita));
							}else{
								risultati.put(giocatore, null);
							}
						}
					}
				}
				
				partita.setIdPartita(Long.valueOf(idPartita));
				partita.setTipoPartita(tipoPartita);
				Date oraDiInizio = null;
				try {
					oraDiInizio = Utils.DATA_INIZIO.parse(inizioPartita);
				} catch (ParseException e) {
					MyLogger.getLogger().severe("Errore nel parsing della data di inizio della partita "+idPartita+" nella scheda del giocatore "+login);
				}
				partita.setDataDiInizio(oraDiInizio);
				partita.setDurata(Integer.valueOf(durataPartita));
				partita.setTurniPartita(Integer.valueOf(turniPartita));
				partita.setRisultati(risultati);
				partite.add(partita);
			}
		}
		return partite;
	}
	
	private String estrapolaDatoPartita(Element element){
		String result = null;
		if (element != null){
			result = element.text();
			if (result != null) result = result.trim();
		}
		return result;
	}
	
	private void estraiTutteLePagineEventuali(Element gamesDiv){
		if (listaNumeriPagina == null){
			listaNumeriPagina = new ArrayList<String>();
			Elements buttonsNumeriPagina = gamesDiv.getElementsByTag("button");
			if (buttonsNumeriPagina != null && !buttonsNumeriPagina.isEmpty()){
				for (Element buttonNumeroPagina: buttonsNumeriPagina){
					listaNumeriPagina.add(buttonNumeroPagina.html().trim());
				}
			}
		}
	}
	
	public static List<String> getNumeriDiPagina(String urlSchedaPagina) throws IOException{
		Connection connection = Jsoup.connect(urlSchedaPagina);
		connection = connection.timeout(0);
		Document doc = connection.get();
		Element body = doc.body();
		
		Element gamesDiv = body.getElementById("gamesDiv");
		
		List<String> listaPagine = new ArrayList<String>();
		Elements buttonsNumeriPagina = gamesDiv.getElementsByTag("button");
		if (buttonsNumeriPagina != null && !buttonsNumeriPagina.isEmpty()){
			for (Element buttonNumeroPagina: buttonsNumeriPagina){
				listaPagine.add(buttonNumeroPagina.html());
			}
		}
		
		return listaPagine;
	}

}
