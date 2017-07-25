package it.desimone.rd3analyzer.parser;

import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ClassificaPrestigiousParser {

	private static final String DOMINIO = "http://rd3.editricegiochi.it/";
	private static final String URL_BASE_PRESTIGIOUS = "http://rd3.editricegiochi.it/site/classifica_prestigious_m.php?t=";
	

	private String idTorneo;
	
	public static void main (String[] args){
		ClassificaPrestigiousParser parser = new ClassificaPrestigiousParser("201502");
		parser.elaboraClassifica("http://rd3.editricegiochi.it/site/classifica_prestigious.php?t=20132");
	}
	
	public ClassificaPrestigiousParser(String idTorneo){
		this.idTorneo = idTorneo;
	}
	
	public Set<String> elaboraClassifica(){
		String url = URL_BASE_PRESTIGIOUS+idTorneo;
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
		
		Elements tabelleClassifica = contentsSection.getElementsByTag("table");
		Element tabellaClassifica = tabelleClassifica.get(0);
		Element tbody = tabellaClassifica.getElementsByTag("tbody").get(0);
		Elements righe = tbody.children(); //getElementsByTag("tr");

		Set<String> nicks = new HashSet<String>();
		//Salto lE primE righe che sono quelle delle intestazioni
		for (int index=3; index < righe.size(); index++){
			Element scheda = righe.get(index);
			Elements datiGiocatore = scheda.getElementsByTag("td");
			if (datiGiocatore.size() >= 2){ //serve ad evitare la riga di separazione tra quelli sopra e sotto soglia
				Element urlSchedaGiocatore = datiGiocatore.get(2);		
				String nick = urlSchedaGiocatore.text();
				nicks.add(nick);
			}
		}

		return nicks;
	}
	
}
