package it.desimone.rd3analyzer.parser;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ClassificaPrestichallengeParser {

	private static final String DOMINIO = "http://rd3.editricegiochi.it/";
	private static final String URL_BASE_PRESTICHALLENGE = "http://rd3.editricegiochi.it/site/classifica_pchallenge.php?id=";
	
	private List<String> listaUrlPagine;
	private String urlBase;
	private String idTorneo;
	
	public static void main (String[] args){
		ClassificaPrestichallengeParser parser = new ClassificaPrestichallengeParser(FiltroModalita.PRESTICHALLENGE, "97");
		parser.elaboraClassifica();
	}
	
	public ClassificaPrestichallengeParser(){
	}
	
	public ClassificaPrestichallengeParser(FiltroModalita modalita, String idTorneo){
		this.idTorneo = idTorneo;
		urlBase = URL_BASE_PRESTICHALLENGE;
	}
	
	public Set<String> elaboraClassifica(){
		String url = urlBase+idTorneo;
		Set<String> nicks = elaboraClassifica(url);
		return nicks;
	}
	
	public Set<String> elaboraClassifica(String urlClassifica){
		Set<String> nicks = elaboraPagina(urlClassifica);
		return nicks;
	}

	private Set<String> elaboraPagina(String urlPagina){
		Document doc = null;
		try{
			Connection connection = Jsoup.connect(urlPagina);
			connection = connection.timeout(0);
			//connection = connection.maxBodySize(6500);
			doc = connection.get();
		}catch(IOException ioe){
			throw new RuntimeException("Errore nell'accesso alla pagina "+urlPagina+" :"+ioe.getMessage());
		}
		Element body = doc.body();
		
		Element contentsSection = body.getElementById("contents");
		
		Element tabellaClassifica = contentsSection.getElementById("ranktable");
		
		Element tbody = tabellaClassifica.getElementsByTag("tbody").get(0);
		Elements righe = tbody.children(); //getElementsByTag("tr");

		Set<String> nicks = new HashSet<String>();

		for (int index=0; index < righe.size(); index++){
			Element scheda = righe.get(index);
			Elements datiGiocatore = scheda.getElementsByTag("td");
			Element urlSchedaGiocatore = datiGiocatore.get(2);		
			String nick = urlSchedaGiocatore.text();
			//MyLogger.getLogger().info(nick);
			nicks.add(nick);
		}

		return nicks;
	}
	
}
