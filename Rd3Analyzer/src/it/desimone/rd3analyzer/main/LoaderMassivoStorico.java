package it.desimone.rd3analyzer.main;

import it.desimone.rd3analyzer.Analizzatore;
import it.desimone.rd3analyzer.Partita;
import it.desimone.rd3analyzer.Torneo;
import it.desimone.rd3analyzer.database.ErroriImportDao;
import it.desimone.rd3analyzer.database.PartiteDao;
import it.desimone.rd3analyzer.database.TorneiDao;
import it.desimone.rd3analyzer.parser.ClassificaChallengeParser;
import it.desimone.rd3analyzer.parser.ClassificaPrestichallengeParser;
import it.desimone.rd3analyzer.parser.ClassificaPrestigiousParser;
import it.desimone.rd3analyzer.parser.ClassificaTrestigeParser;
import it.desimone.rd3analyzer.parser.FiltroModalita;
import it.desimone.rd3analyzer.parser.FiltroPeriodo;
import it.desimone.rd3analyzer.parser.PaginaClassificheParser;
import it.desimone.rd3analyzer.parser.SchedaGiocatoreParser;
import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoaderMassivoStorico {

	private static final String DOMINIO = "http://rd3.editricegiochi.it/site/"; 
	private static final String PAGINACLASSIFICHE = "http://rd3.editricegiochi.it/site/classifiche_uff.php"; 
	
	private static Date dataDiRiferimentoPerEstrazioneElaborazione;
	
	public static void main2(String[] args) {
		
		
		MyLogger.getLogger().finest("[BEGIN]");
		
		Map<FiltroModalita, List<String>> classificheDaElaborare = PaginaClassificheParser.elaboraPagina(PAGINACLASSIFICHE);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 20);
		dataDiRiferimentoPerEstrazioneElaborazione = calendar.getTime();
		
		aggiornaOInserisciClassificheElaborate(classificheDaElaborare);
	
		MyLogger.getLogger().finest("[END]");
	}
	
	
	public static void main(String[] args) {
		
		MyLogger.getLogger().entering(LoaderMassivoStorico.class.getName(), "main");
				
		try{
			Map<FiltroModalita, List<String>> classificheDaElaborare = estrazioneClassificheDaElaborare(PAGINACLASSIFICHE);
			
			if (classificheDaElaborare != null && !classificheDaElaborare.isEmpty()){
				
				MyLogger.getLogger().info("Elaborazione delle seguenti classifiche: \n"+classificheDaElaborare);
				
				Set<String> nicks = estrazioneNick(classificheDaElaborare);
						
				for (String nick: nicks){
					MyLogger.getLogger().info(nick);
				}
				MyLogger.getLogger().info("Nick diversi che hanno giocato almeno una partita: "+nicks.size());
	
				FiltroPeriodo filtroPeriodo = determinaPeriodoTemporaleEstrazionePartite(classificheDaElaborare);
				FiltroModalita filtroModalita = determinaModalitaEstrazionePartite(classificheDaElaborare);
				
				MyLogger.getLogger().info("Estrazione partite con modalità "+filtroModalita+" e periodicità "+filtroPeriodo);
				
				List<Partita> partiteGiocatore = estrazioneIdPartita(nicks, filtroModalita, filtroPeriodo);
				
				MyLogger.getLogger().info("Partite diverse: "+partiteGiocatore.size());
			
				leggiSchedaEInserisciSuDB(partiteGiocatore);
				
				//In update va aggiornata la data di elaborazione e stabilito il booleano in base al fatto che sia o no la prima della lista per quella modalita
				aggiornaOInserisciClassificheElaborate(classificheDaElaborare);
	
			}
		}catch(Throwable t){
			MyLogger.getLogger().severe("Eccezione del programma: "+t.getMessage());
		}
		
		MyLogger.getLogger().exiting(LoaderMassivoStorico.class.getName(), "main");
		
	}
	
	
	private static Map<FiltroModalita, List<String>> estrazioneClassificheDaElaborare(String urlPaginaClassifiche){
		
		MyLogger.getLogger().finest("[BEGIN]");
		
		Map<FiltroModalita, List<String>> classificheDaElaborare = PaginaClassificheParser.elaboraPagina(urlPaginaClassifiche);
		
		TorneiDao torneiDao = new TorneiDao();
		Collection<List<String>> urlsPerTipo = classificheDaElaborare.values();
		for (List<String> urlDelTipo: urlsPerTipo){
			Iterator<String> iterator = urlDelTipo.iterator();
			while (iterator.hasNext()){
				String url = iterator.next();	
				Torneo torneo = torneiDao.getTorneoByUrl(url);
				if (torneo != null && !torneo.getDaElaborare()){
					iterator.remove();
				}
			}
		}
		
		MyLogger.getLogger().finest("[END]");
		return classificheDaElaborare;
	}
	

	
	private static Set<String> estrazioneNick(Map<FiltroModalita, List<String>> classificheDaElaborare) {

		MyLogger.getLogger().finest("[BEGIN]");
		
		if (classificheDaElaborare == null || classificheDaElaborare.isEmpty()) return null;
		
		Set<String> nicks = new HashSet<String>();
		
		
		Set<FiltroModalita> modalita = classificheDaElaborare.keySet();
		
		for (FiltroModalita mod: modalita){
			List<String> urlClassifiche =  classificheDaElaborare.get(mod);
			switch (mod) {
			case TRESTIGE:
				for (String urlClassifica: urlClassifiche){
					String url = DOMINIO+urlClassifica;
					Set<String> nicksTrestige = new ClassificaTrestigeParser(null).elaboraClassifica(url);
					nicks.addAll(nicksTrestige);
					MyLogger.getLogger().info("Trovati "+nicksTrestige.size()+" per la classifica "+url);
				}
				break;
			case PRESTIGIOUS:
				for (String urlClassifica: urlClassifiche){
					String url = DOMINIO+urlClassifica;
					Set<String> nicksPrestigious = new ClassificaPrestigiousParser(null).elaboraClassifica(url);
					nicks.addAll(nicksPrestigious);
					MyLogger.getLogger().info("Trovati "+nicksPrestigious.size()+" per la classifica "+url);
				}
				break;
			case CHALLENGE:
				for (String urlClassifica: urlClassifiche){
					String url = DOMINIO+urlClassifica;
					Set<String> nicksChallenge = new ClassificaChallengeParser().elaboraClassifica(url);
					nicks.addAll(nicksChallenge);
					MyLogger.getLogger().info("Trovati "+nicksChallenge.size()+" per la classifica "+url);
				}
				break;
			case PRESTICHALLENGE:
				for (String urlClassifica: urlClassifiche){
					String url = DOMINIO+urlClassifica;
					Set<String> nicksChallenge = new ClassificaPrestichallengeParser().elaboraClassifica(url);
					nicks.addAll(nicksChallenge);
					MyLogger.getLogger().info("Trovati "+nicksChallenge.size()+" per la classifica "+url);
				}
				break;				
			default:
				throw new IllegalArgumentException("FiltroModalità non gestito: "+mod);
			}
		}
		
		MyLogger.getLogger().finest("[END]");
		
		return nicks;
	}
	
	
	private static FiltroPeriodo determinaPeriodoTemporaleEstrazionePartite(Map<FiltroModalita, List<String>> classificheDaElaborare){
		
		MyLogger.getLogger().finest("[BEGIN]");
		
		dataDiRiferimentoPerEstrazioneElaborazione = Calendar.getInstance().getTime();
		
		FiltroPeriodo result = FiltroPeriodo.TUTTO;
		
		Date dataMinimaUltimaElaborazione = null;
		TorneiDao torneiDao = new TorneiDao();
		Collection<List<String>> urlsPerTipo = classificheDaElaborare.values();
		for (List<String> urlDelTipo: urlsPerTipo){
			Iterator<String> iterator = urlDelTipo.iterator();
			while (iterator.hasNext()){
				String url = iterator.next();	
				Torneo torneo = torneiDao.getTorneoByUrl(url);
				if (torneo != null && torneo.getDataUltimaElaborazione() != null){
					if (dataMinimaUltimaElaborazione == null || torneo.getDataUltimaElaborazione().before(dataMinimaUltimaElaborazione)){
						dataMinimaUltimaElaborazione =  torneo.getDataUltimaElaborazione();
					}
				}
			}
		}
		
		if (dataMinimaUltimaElaborazione != null){
			long millisecondiInUnGiorno = 24*60*60*1000;
			long differenzaTraAdessoEUltimaElaborazione = dataDiRiferimentoPerEstrazioneElaborazione.getTime() - dataMinimaUltimaElaborazione.getTime();
			
			long numeroDiGiorniPassati = differenzaTraAdessoEUltimaElaborazione / millisecondiInUnGiorno;
			
			if (numeroDiGiorniPassati == 0){
				result = FiltroPeriodo.ULTIME_24_H;
			}else if (numeroDiGiorniPassati < 7){
				result = FiltroPeriodo.ULTIMI_7_GIORNI;
			}else if (numeroDiGiorniPassati < 30){
				result = FiltroPeriodo.ULTIMO_MESE;
			}
		}
		
		MyLogger.getLogger().finest("[END]");
		return result;
	}
	
	private static FiltroModalita determinaModalitaEstrazionePartite(Map<FiltroModalita, List<String>> classificheDaElaborare){
		FiltroModalita filtroModalita = FiltroModalita.TUTTE;
		if (classificheDaElaborare.size() == 1){
			filtroModalita = classificheDaElaborare.keySet().iterator().next();
		}
		return filtroModalita;
	}
	
	private static List<Partita> estrazioneIdPartita(Set<String> nicks, FiltroModalita modalita, FiltroPeriodo periodo) {

		MyLogger.getLogger().finest("[BEGIN]");

		List<Partita> partite = new ArrayList<Partita>();

		int counterNick = 0;
		for (String nick: nicks){
			//List<Partita> partiteGiocatore = new SchedaGiocatoreParser(nick).elaboraScheda();
			List<Partita> partiteGiocatore = new SchedaGiocatoreParser(nick, modalita, periodo).elaboraScheda();
			MyLogger.getLogger().info(++counterNick+" "+nick+": "+partiteGiocatore.size()+" partite registrate");
			for (Partita partita: partiteGiocatore){
				String tipoPartita = partita.getTipoPartita();
				FiltroModalita modalitaPartita = FiltroModalita.getFiltroByDescrizione(tipoPartita);
				if (modalitaPartita != FiltroModalita.DIGITAL && modalitaPartita != FiltroModalita.PRESTIGE){
					//Ci si deve chiedere se la partita è stata già registrata. In caso positivo si deve fare il merge dei risultati altrimenti si inserisce
					if (!partite.contains(partita)){
						partite.add(partita);
					}else{
						int indexPartita = partite.indexOf(partita);
						Partita partitaRegistrata = partite.get(indexPartita);
						//MyLogger.getLogger().info("Partita già registrata: "+partitaRegistrata);
						partitaRegistrata.faiIlMergeDeiRisultati(partita.getRisultati());
						//MyLogger.getLogger().info("Partita dopo il Merge: "+partitaRegistrata);
					}
				}
			}
		}

		MyLogger.getLogger().finest("[END]");

		return partite;
	}

	
	private static void leggiSchedaEInserisciSuDB(List<Partita> partiteGiocatore){
		
		MyLogger.getLogger().finest("[BEGIN]");
		
		int numeroScartate = 0;
		int numeroElaborate = 0;
		int numeroInserite = 0;
		int numeroGiaComplete = 0;
		
		PartiteDao partiteDao = new PartiteDao();
		for (Partita partitaGiocatore: partiteGiocatore){
			numeroElaborate++;
			
			if (numeroElaborate % 300 == 0){MyLogger.getLogger().fine("Elaborazione n.ro "+numeroElaborate);}
			
			Long idPartitaL = Long.valueOf(partitaGiocatore.getIdPartita());
			boolean partitaGiaRegistrata = partiteDao.partitaGiaRegistrata(idPartitaL);	
			if (!partitaGiaRegistrata){
				Long idPartita = partitaGiocatore.getIdPartita();
				try{
					Partita partita = null;
					if (partitaGiocatore.partitaIncompleta()){
						//MyLogger.getLogger().fine("La Partita "+idPartita+" è incompleta: la cerco sulla scheda");
						Partita partitaDaScheda = Analizzatore.elaboraPartita(idPartita.toString(), true);
						if (partitaDaScheda != null){
							partitaGiocatore.setRisultati(partitaDaScheda.getRisultati());
							partitaGiocatore.setDataDiFine(partitaDaScheda.getDataDiFine());
							partitaGiocatore.setNomePartita(partitaDaScheda.getNomePartita());
							partita = partitaGiocatore;
						}
					}else{
						numeroGiaComplete++;
						partita = partitaGiocatore;
					}
					if (partita != null){
						int righeInserite = partiteDao.inserisciPartita(partita);
						if (righeInserite == 0){
							numeroScartate++;
						}else{
							numeroInserite++;
						}
					}else{
						//MyLogger.getLogger().severe("Partita "+partitaGiocatore+" scartata per assenza della scheda");
						throw new IllegalStateException("Partita "+partitaGiocatore+" scartata per assenza della scheda");
					}
				} catch (IOException ioe) {
					numeroScartate++;
					MyLogger.getLogger().severe("IOException per la Partita "+idPartita+": "+ioe.getMessage());
					ErroriImportDao erroriImportDao = new ErroriImportDao(false);
					erroriImportDao.inserisciErrore(idPartitaL, ioe);
				} catch (ParseException pe) {
					numeroScartate++;
					MyLogger.getLogger().severe("ParseException per la Partita "+idPartita+": "+pe.getMessage());
					ErroriImportDao erroriImportDao = new ErroriImportDao(false);
					erroriImportDao.inserisciErrore(idPartitaL, pe);
				} catch (IllegalStateException e) {
					numeroScartate++;
					//MyLogger.getLogger().info("IllegalStateException per la Partita "+idPartita+": "+e.getMessage());
					ErroriImportDao erroriImportDao = new ErroriImportDao(false);
					erroriImportDao.inserisciErrore(idPartitaL, e);
				} catch (Exception e) {
					numeroScartate++;
					MyLogger.getLogger().severe("Exception per la Partita "+idPartita+": "+e.getMessage());
					ErroriImportDao erroriImportDao = new ErroriImportDao(false);
					erroriImportDao.inserisciErrore(idPartitaL, e);
				}
			}
		}
		
		MyLogger.getLogger().info("**************************************************");
		MyLogger.getLogger().info("** Partite elaborate: "+numeroElaborate);
		MyLogger.getLogger().info("** Partite inserite: "+numeroInserite);
		MyLogger.getLogger().info("** Partite scartate: "+numeroScartate);
		MyLogger.getLogger().info("** Partite già complete: "+numeroGiaComplete);
		MyLogger.getLogger().info("**************************************************");
		MyLogger.getLogger().finest("[END]");
	}
	
	private static void aggiornaOInserisciClassificheElaborate(Map<FiltroModalita, List<String>> classificheDaElaborare) {
		MyLogger.getLogger().finest("[BEGIN]");
		
		TorneiDao torneiDao = new TorneiDao();
		
		Set<FiltroModalita> modalita = classificheDaElaborare.keySet();
		
		for (FiltroModalita filtroModalita: modalita){
			List<String> urlsPerTipo = classificheDaElaborare.get(filtroModalita);
			short indexTorneo = 0;
			for (String urlPerTipo: urlsPerTipo){
				indexTorneo++;
				Torneo torneo = torneiDao.getTorneoByUrl(urlPerTipo);
				if (torneo != null){
					if (indexTorneo == 1){
						torneo.setDaElaborare(Boolean.TRUE);
					}else{ //Significa che non è il primo della lista e quindi si è concluso: lo elaboro stavolta e poi mai più
						torneo.setDaElaborare(Boolean.FALSE);
					}
					torneo.setDataUltimaElaborazione(dataDiRiferimentoPerEstrazioneElaborazione);
					int torneiAggiornati = torneiDao.aggiornaTorneo(torneo);
					if (torneiAggiornati != 1){
						MyLogger.getLogger().severe("Il Torneo "+urlPerTipo+" non è stato aggiornato");
					}else{
						MyLogger.getLogger().info("Elaborato e aggiornato il Torneo "+urlPerTipo);
					}
				}else{
					torneo = new Torneo();
					torneo.setUrlTorneo(urlPerTipo);
					torneo.setModalitaTorneo(filtroModalita);
					torneo.setDaElaborare(Boolean.TRUE);
					torneo.setDataUltimaElaborazione(dataDiRiferimentoPerEstrazioneElaborazione);
					int torneiInseriti = torneiDao.inserisciTorneo(torneo);
					if (torneiInseriti != 1){
						MyLogger.getLogger().severe("Il Torneo "+urlPerTipo+" non è stato inserito");
					}else{
						MyLogger.getLogger().info("Elaborato e inserito il Torneo "+urlPerTipo);
					}
				}
			}
		}
		MyLogger.getLogger().finest("[END]");
	}

	
}
