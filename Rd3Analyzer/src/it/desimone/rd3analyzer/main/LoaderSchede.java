package it.desimone.rd3analyzer.main;

import it.desimone.rd3analyzer.Analizzatore;
import it.desimone.rd3analyzer.AnalizzatorePrestigious;
import it.desimone.rd3analyzer.Partita;
import it.desimone.rd3analyzer.database.ErroriImportDao;
import it.desimone.rd3analyzer.database.PartiteDao;
import it.desimone.utils.Configurator;
import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class LoaderSchede {

	private static LoaderSchede instance;
	
	private static final String URL_SCHEDE_STAGIONE = "http://rd3.editricegiochi.it/site/classifica_prestigious.php?t=";
	private Boolean processoAutomatico = Boolean.TRUE;
	private String urlSchedaStagione = null;
	private Boolean soloPartiteNuove = Boolean.TRUE;
	
	private LoaderSchede(){}
	
	public static LoaderSchede getInstance(){
		if (instance == null){
			instance = new LoaderSchede();
		}
		return instance;
	}
	
	public static void main(String[] args){
		try{
			LoaderSchede loaderSchede = getInstance();
			loaderSchede.init(args);
			MyLogger.getLogger().finer("Caricamento "+(loaderSchede.getSoloPartiteNuove()?"delle sole partite nuove ":"di tutte le partite ")+"dalla url "+loaderSchede.getUrlSchedaStagione());
			loaderSchede.caricaSchedePrestigious(loaderSchede.getUrlSchedaStagione(), loaderSchede.getSoloPartiteNuove());
		}catch(Exception e){
			MyLogger.getLogger().severe("L'APPLICAZIONE DI CARICAMENTO DELLE SCHEDE PARTITA E' ANDATA IN ECCEZIONE: "+e.getMessage());
		}
	}
	
	public void init(String[] args){
		//Se args contiene dati, bypassa la configurazione. Se è uno solo, è la url. Se sono due, il primo è la url, l'altro le partite nuove
		if (args == null || args.length == 0){
			processoAutomatico = Configurator.isProcessoAutomatico();
			if (!processoAutomatico){
				urlSchedaStagione = Configurator.getUrlSchedaStagione();
			}else{
				urlSchedaStagione = determinaUrlDaChiamareInBaseAllaData();
			}
			soloPartiteNuove = Configurator.caricaSoloLeNuove();
		}else{
			urlSchedaStagione = args[0];
			if (args.length == 1){
				soloPartiteNuove = Configurator.caricaSoloLeNuove();
			}else{
				soloPartiteNuove = Boolean.valueOf(args[1]);
			}

		}
	}
	
	private static String determinaUrlDaChiamareInBaseAllaData(){
		String url = null;
		
		Calendar adesso = Calendar.getInstance();
		Integer anno = adesso.get(Calendar.YEAR);
		Integer mese = adesso.get(Calendar.MONTH);
		Integer giorno = adesso.get(Calendar.DAY_OF_MONTH);
		Integer ora = adesso.get(Calendar.HOUR_OF_DAY);
		
		String year = Integer.toString(anno);
		String trimestre = null;
		
		switch (mese) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			//Se è l'1 del mese ed è prima delle 2 potrebbe non essere ancora finita qualche partita
			//Inoltre se è gennaio, va portato indietro anche l'anno;
			if (mese == Calendar.JANUARY && giorno == 1 && ora <= 2){
				trimestre = "4";
				year = Integer.toString(anno - 1);
			}else{
				trimestre = "1";
			}
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			if (mese == Calendar.APRIL && giorno == 1 && ora <= 2){
				trimestre = "1";
			}else{
				trimestre = "2";
			}
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			if (mese == Calendar.JULY && giorno == 1 && ora <= 2){
				trimestre = "2";
			}else{
				trimestre = "3";
			}
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			if (mese == Calendar.OCTOBER && giorno == 1 && ora <= 2){
				trimestre = "3";
			}else{
				trimestre = "4";
			}	
			break;			
		default:
			break;
		}
		
		url = URL_SCHEDE_STAGIONE+year+trimestre;
		
		return url;
	}
	
	public void caricaSchede(){
		//determina da quale scheda partire
		PartiteDao partiteDao = new PartiteDao();
		Long idLastMatch = partiteDao.obtainLastMatch();
		
		if (idLastMatch == null || idLastMatch == 0) idLastMatch = 979355L - 1; //Prima partita del 2014 -1
		
		MyLogger.getLogger().info("INIZIO - Ultimo ID["+idLastMatch+"]");
		//apri un loop basato sull'id_partita dal quale esci quando 
		//non viene recuperata nessuna scheda per un numero N di volte
		//l'id dal quale partire è l'ultimo inserito in base dati aumentato di 1
		
		int numeroScartate = 0;
		int numeroElaborate = 0;
		
		while(numeroElaborate <= 1000){
			Partita partita = null;
			numeroElaborate++;
			try {
				partita = Analizzatore.elaboraPartita(Long.toString(++idLastMatch), true);
				if (partita != null){
					PartiteDao prestigeDao = new PartiteDao();
					int righeInserite = prestigeDao.inserisciPartita(partita);
					if (righeInserite == 0){numeroScartate++;}
				}
			} catch (IOException ioe) {
				numeroScartate++;
				MyLogger.getLogger().severe("IOException per la Partita "+idLastMatch+": "+ioe.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao();
				erroriImportDao.inserisciErrore(idLastMatch, ioe);
			} catch (ParseException pe) {
				numeroScartate++;
				MyLogger.getLogger().severe("ParseException per la Partita "+idLastMatch+": "+pe.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao();
				erroriImportDao.inserisciErrore(idLastMatch, pe);
			}
		}
		
		//nel loop elabora le schede e salva in base dati
		//se una delle schede dà qualche tipo di errore si annota in una tabella a parte
		MyLogger.getLogger().info("FINE - Ultimo ID["+idLastMatch+"]: Elaborate "+numeroElaborate+" di cui scartate "+numeroScartate);
	}
	
	public void caricaSchedePrestigious(){
		caricaSchedePrestigious(urlSchedaStagione, soloPartiteNuove);
	}
	
	public void caricaSchedePrestigious(String urlSchedaStagione, boolean soloNuove){
		
		MyLogger.getLogger().info("Estrazione "+(soloNuove?"delle sole partite nuove":"di tutte le partite")+" dalla scheda "+urlSchedaStagione);
		
		int numeroScartate = 0;
		int numeroElaborate = 0;
		Long idLastMatch = null;
		try {

			List<String> idPartitePrestigious = AnalizzatorePrestigious.elaboraScheda(urlSchedaStagione);
			
			if (soloNuove){
				PartiteDao partiteDao = new PartiteDao();
				idLastMatch = partiteDao.obtainLastMatch();	
				if (idLastMatch != null){
					MyLogger.getLogger().info("Ultimo ID partita caricato: "+idLastMatch);
					int indexLastMatch = idPartitePrestigious.indexOf(String.valueOf(idLastMatch));
					idPartitePrestigious = idPartitePrestigious.subList(indexLastMatch+1, idPartitePrestigious.size());
					MyLogger.getLogger().info("Trovate "+idPartitePrestigious.size()+" nuove partite");
				}
			}
			
			for (String idPartita: idPartitePrestigious){
				idLastMatch = Long.valueOf(idPartita);
				Partita partita = null;
				numeroElaborate++;
				partita = Analizzatore.elaboraPartita(idPartita, true);
				if (partita != null){
					PartiteDao prestigeDao = new PartiteDao();
					if (soloNuove){
						int righeInserite = prestigeDao.inserisciPartita(partita);
						if (righeInserite == 0){numeroScartate++;}
					}else{
						int righeInserite = prestigeDao.aggiornaPartita(partita);
						if (righeInserite == 0){numeroScartate++;}
					}
				}
			}
		} catch (IOException ioe) {
			numeroScartate++;
			MyLogger.getLogger().severe("IOException per la Partita "+idLastMatch+": "+ioe.getMessage());
			ErroriImportDao erroriImportDao = new ErroriImportDao();
			erroriImportDao.inserisciErrore(idLastMatch, ioe);
		} catch (ParseException pe) {
			numeroScartate++;
			MyLogger.getLogger().severe("ParseException per la Partita "+idLastMatch+": "+pe.getMessage());
			ErroriImportDao erroriImportDao = new ErroriImportDao();
			erroriImportDao.inserisciErrore(idLastMatch, pe);
		}

		
		//nel loop elabora le schede e salva in base dati
		//se una delle schede dà qualche tipo di errore si annota in una tabella a parte
		MyLogger.getLogger().info("FINE - Ultimo ID["+idLastMatch+"]: Elaborate "+numeroElaborate+" di cui scartate "+numeroScartate);
	}
	public Boolean getProcessoAutomatico() {
		return processoAutomatico;
	}

	public void setProcessoAutomatico(Boolean processoAutomatico) {
		this.processoAutomatico = processoAutomatico;
	}

	public String getUrlSchedaStagione() {
		return urlSchedaStagione;
	}

	public void setUrlSchedaStagione(String urlSchedaStagione) {
		this.urlSchedaStagione = urlSchedaStagione;
	}

	public Boolean getSoloPartiteNuove() {
		return soloPartiteNuove;
	}

	public void setSoloPartiteNuove(Boolean soloPartiteNuove) {
		this.soloPartiteNuove = soloPartiteNuove;
	}
	
	
}
