package it.desimone.rd3analyzer;

import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AnalizzatorePrestigious {


	public static void main (String[] args) throws IOException, ParseException{
		String url = "http://rd3.editricegiochi.it/site/classifica_prestigious.php?t=20143";
		elaboraScheda(url);

	}
	
	public static List<String> elaboraScheda(String urlSchedaStagione) throws IOException, ParseException{
		Connection connection = Jsoup.connect(urlSchedaStagione);
		connection = connection.timeout(0);
		//connection = connection.maxBodySize(6500);
		Document doc = connection.get();
		Element body = doc.body();
		
		Element gamesDiv = body.getElementById("gamesDiv");
		
		Elements tabellePartita = gamesDiv.getElementsByTag("table");
		Element tabellaPartita = tabellePartita.get(0);
		Element tbody = tabellaPartita.child(0);
		Elements partite = tbody.children(); //getElementsByTag("tr");

		List<String> idPartite = new ArrayList<String>();
		//Salto la prima riga che è quella delle intestazioni
		for (int index=1; index < partite.size(); index++){
			Element partita = partite.get(index);
			Elements datiPartita = partita.getElementsByTag("td");
			Element urlPartita = datiPartita.get(0);
			String idPartita = urlPartita.text();
			idPartite.add(idPartita);
		}

		MyLogger.getLogger().info("Trovate "+(partite.size() - 1)+" partite");
		return idPartite;
	}
	
	

	


}
