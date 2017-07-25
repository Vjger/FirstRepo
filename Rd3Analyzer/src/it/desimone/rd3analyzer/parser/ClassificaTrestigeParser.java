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

public class ClassificaTrestigeParser {

	private Integer numeroPagine = 1;

	private static final String DOMINIO = "http://rd3.editricegiochi.it/";
	private static final String URL_BASE_TRESTIGE = "http://rd3.editricegiochi.it/site/classifica_pr3stige_m.php?t=";
	
	private List<String> listaUrlPagine;
	private String idTorneo;
	
	public static void main (String[] args){
		ClassificaTrestigeParser parser = new ClassificaTrestigeParser("201501");
		parser.elaboraClassifica("http://rd3.editricegiochi.it/site/classifica_pr3stige_2014.php");
	}
	
	public ClassificaTrestigeParser(String idTorneo){
		this.idTorneo = idTorneo;
	}
	
	public Set<String> elaboraClassifica(){
		String url = URL_BASE_TRESTIGE+idTorneo;
		Set<String> nicks = elaboraClassifica(url);
		return nicks;
	}
	
	public Set<String> elaboraClassifica(String urlClassifica){
		Set<String> nicks = elaboraPagina(urlClassifica);
		if (listaUrlPagine != null && listaUrlPagine.size() > 1){
			for (String urlPagina: listaUrlPagine){
				Set<String> nicksAltrePagine = elaboraPagina(DOMINIO+urlPagina);
				nicks.addAll(nicksAltrePagine);
			}
		}
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
		Element tabellaEsterna = tabelleClassifica.get(0);
		Elements tabelleClassifiche = tabellaEsterna.getElementsByTag("table");
		Element tabellaClassifica = tabelleClassifiche.get(1);
		Element tbody = tabellaClassifica.getElementsByTag("tbody").get(0);
		Elements righe = tbody.children(); //getElementsByTag("tr");

		if (numeroPagine == 1){
			Element paginazione = righe.get(1);
			estraiTutteLePagineEventuali(paginazione);
		}
		Set<String> nicks = new HashSet<String>();
		//Salto la prima riga che è quella delle intestazioni
		for (int index=3; index < righe.size() - 1; index++){
			Element scheda = righe.get(index);
			Elements datiGiocatore = scheda.getElementsByTag("td");
			Element urlSchedaGiocatore = datiGiocatore.get(2);		
			String nick = urlSchedaGiocatore.text();
			//MyLogger.getLogger().info(nick);
			nicks.add(nick);
		}

		return nicks;
	}
	
	private void estraiTutteLePagineEventuali(Element paginazione){
		Elements colonneUrlPagine = paginazione.getElementsByTag("td");
		if (colonneUrlPagine != null && !colonneUrlPagine.isEmpty()){
			Element colonnaUrlPagine = paginazione.getElementsByTag("td").get(0);
			Elements urls = colonnaUrlPagine.getElementsByTag("a");
			if (urls != null && !urls.isEmpty()){
				listaUrlPagine = new ArrayList<String>();
				for (Element urlPag: urls){
					listaUrlPagine.add(urlPag.attr("href"));
				}
				numeroPagine = urls.size()+1;
			}
		}
	}
}
