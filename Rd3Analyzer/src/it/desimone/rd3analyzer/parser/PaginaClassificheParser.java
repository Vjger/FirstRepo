package it.desimone.rd3analyzer.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PaginaClassificheParser {


	public static void main (String[] args){
		PaginaClassificheParser paginaClassificheParser = new PaginaClassificheParser();
		System.out.println(paginaClassificheParser.elaboraPagina("http://rd3.editricegiochi.it/site/classifiche_uff.php"));
	}
	
	public static Map<FiltroModalita, List<String>> elaboraPagina(String urlPagina){
		
		Map<FiltroModalita, List<String>> result = new HashMap<FiltroModalita, List<String>>();
		
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
		
		Element gamesDiv = body.getElementById("contents");
		
		Elements tabelleClassifica = gamesDiv.getElementsByTag("table");
		Element tabellaClassifica = tabelleClassifica.get(0);
		Element tbody = tabellaClassifica.child(0);
		Elements righeClassifiche = tbody.children(); //E' una tabella di sole due righe: ci interessa la seconda


		Element rigaClassifiche = righeClassifiche.get(1);
		Elements colonneClassifiche = rigaClassifiche.getElementsByTag("td");
		
		//Prestichallenge
		Element colonnaPrestiChallenge = colonneClassifiche.get(0);
		Elements linksPrestiChallenge = colonnaPrestiChallenge.getElementsByTag("a");
		List<String> urlPrestiChallenge = new ArrayList<String>();
		for (Element linkPrestiChallenge: linksPrestiChallenge){
			String url = linkPrestiChallenge.attr("href");
			urlPrestiChallenge.add(url);
		}
		result.put(FiltroModalita.PRESTICHALLENGE, urlPrestiChallenge);
		
		//Challenge
		Element colonnaChallenge = colonneClassifiche.get(1);
		Elements linksChallenge = colonnaChallenge.getElementsByTag("a");
		List<String> urlChallenge = new ArrayList<String>();
		for (Element linkChallenge: linksChallenge){
			String url = linkChallenge.attr("href");
			urlChallenge.add(url);
		}
		result.put(FiltroModalita.CHALLENGE, urlChallenge);
		
		//Trestige
		Element colonnaTrestige = colonneClassifiche.get(3);
		Elements linksTrestige = colonnaTrestige.getElementsByTag("a");
		List<String> urlTrestige = new ArrayList<String>();
		for (Element linkTrestige: linksTrestige){
			String url = linkTrestige.attr("href");
			urlTrestige.add(url);
		}
		result.put(FiltroModalita.TRESTIGE, urlTrestige);
		
		//Trestige
		Element colonnaPrestigious = colonneClassifiche.get(5);
		Elements linksPrestigious = colonnaPrestigious.getElementsByTag("a");
		List<String> urlPrestigious = new ArrayList<String>();
		for (Element linkPrestigious: linksPrestigious){
			String url = linkPrestigious.attr("href");
			urlPrestigious.add(url);
		}
		result.put(FiltroModalita.PRESTIGIOUS, urlPrestigious);
			

		return result;
	}
	

}
