package it.desimone.gsheetsaccess.htmlpublisher;

import it.desimone.ftputils.AlterVistaUtil;
import it.desimone.gsheetsaccess.RankingCalculator;
import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.common.Configurator.Environment;
import it.desimone.gsheetsaccess.common.ResourceWorking;
import it.desimone.gsheetsaccess.dto.ScorePlayer;
import it.desimone.gsheetsaccess.dto.TorneoPubblicato;
import it.desimone.gsheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gsheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gsheetsaccess.gsheets.dto.PartitaRow;
import it.desimone.gsheetsaccess.gsheets.facade.ExcelGSheetsBridge;
import it.desimone.gsheetsaccess.utils.TorneiUtils;
import it.desimone.utils.Capitalize;
import it.desimone.utils.MyLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class HtmlPublisher {
	
	public static final String FOLDER_PATH = "C:\\Users\\mds\\Desktop\\RisiKo Pages";
	public static final DateFormat lastUpdateTimeFormat = new SimpleDateFormat("dd/MM/yyyyHHmmss");
	
	private static Date maxDate = null;

	public static void main(String[] args) {
		MyLogger.setConsoleLogLevel(Level.INFO);
		Configurator.loadConfiguration(Environment.PRODUCTION);
		publish();
	}
	
	public static void publish() {
		MyLogger.getLogger().info("Inizio elaborazione");
		String year = "2019";
		
		MyLogger.getLogger().info("Inizio estrazione tornei pubblicati");
		List<TorneoPubblicato> torneiPubblicati = TorneiUtils.caricamentoTornei(year);
	
		Collections.sort(torneiPubblicati, new Comparator<TorneoPubblicato>() {

			@Override
			public int compare(TorneoPubblicato o1, TorneoPubblicato o2) {
				int result = 0;
				try{
					Date endDate1 = ExcelGSheetsBridge.dfDateTorneo.parse(o1.getTorneoRow().getEndDate());
					Date endDate2 = ExcelGSheetsBridge.dfDateTorneo.parse(o2.getTorneoRow().getEndDate());
					result = endDate2.compareTo(endDate1);
				}catch(ParseException pe){
					MyLogger.getLogger().severe(pe.getMessage());
				}
				//return o2.getIdTorneo().compareTo(o1.getIdTorneo());
				return result;
			}
		});
		File listaTornei = new File(FOLDER_PATH, "listaTornei.html");
		listaTorneiPublisher(torneiPubblicati, listaTornei);
		
		MyLogger.getLogger().info("Inizio elaborazione tabellini");
		List<ScorePlayer> tabellini = RankingCalculator.elaboraTabellini(year, torneiPubblicati, null);
		
		assegnaNominativiAPartita(torneiPubblicati, year);
		
		File ranking = new File(FOLDER_PATH,"ranking.html");
		rankingPublisher(tabellini, ranking);
		File folderTornei = new File(FOLDER_PATH+File.separator+"TORNEI");
		List<File> torneiHtml = torneiPublisher(year, torneiPubblicati, folderTornei);
		
		try{
			uploadFiles(ranking, listaTornei, torneiHtml);
			if (maxDate != null){
				ResourceWorking.setLastTournamentDate(year, lastUpdateTimeFormat.format(maxDate));
			}
		}catch(IOException ioe){
			MyLogger.getLogger().severe("Errore nel ftp dei file: "+ioe.getMessage());
		}
		MyLogger.getLogger().info("Fine elaborazione");
	}
	
	private static void assegnaNominativiAPartita(List<TorneoPubblicato> torneiPubblicati, String year){
		List<AnagraficaGiocatoreRow> anagraficheGiocatoriRow = TorneiUtils.getAllAnagraficheGiocatori(year);
		
		for (TorneoPubblicato torneo: torneiPubblicati){
			List<PartitaRow> partite = torneo.getPartite();
			for (PartitaRow partita: partite){
				AnagraficaGiocatoreRow vincitore = TorneiUtils.findAnagraficaById(anagraficheGiocatoriRow, partita.getIdGiocatoreVincitore());
				AnagraficaGiocatoreRow giocatore1 = TorneiUtils.findAnagraficaById(anagraficheGiocatoriRow, partita.getIdGiocatore1());
				AnagraficaGiocatoreRow giocatore2 = TorneiUtils.findAnagraficaById(anagraficheGiocatoriRow, partita.getIdGiocatore2());
				AnagraficaGiocatoreRow giocatore3 = TorneiUtils.findAnagraficaById(anagraficheGiocatoriRow, partita.getIdGiocatore3());
				AnagraficaGiocatoreRow giocatore4 = TorneiUtils.findAnagraficaById(anagraficheGiocatoriRow, partita.getIdGiocatore4());
				AnagraficaGiocatoreRow giocatore5 = TorneiUtils.findAnagraficaById(anagraficheGiocatoriRow, partita.getIdGiocatore5());
				if (vincitore != null){
					partita.setNominativoVincitore(Capitalize.capitalize(vincitore.getNome())+" "+Capitalize.capitalize(vincitore.getCognome()));
				}else{
					partita.setNominativoVincitore("");
				}
				if (giocatore1 != null){
					partita.setNominativoGiocatore1(Capitalize.capitalize(giocatore1.getNome())+" "+Capitalize.capitalize(giocatore1.getCognome()));
				}else{
					partita.setNominativoGiocatore1("");
				}
				if (giocatore2 != null){
					partita.setNominativoGiocatore2(Capitalize.capitalize(giocatore2.getNome())+" "+Capitalize.capitalize(giocatore2.getCognome()));
				}else{
					partita.setNominativoGiocatore2("");
				}
				if (giocatore3 != null){
					partita.setNominativoGiocatore3(Capitalize.capitalize(giocatore3.getNome())+" "+Capitalize.capitalize(giocatore3.getCognome()));
				}else{
					partita.setNominativoGiocatore3("");
				}
				if (giocatore4 != null){
					partita.setNominativoGiocatore4(Capitalize.capitalize(giocatore4.getNome())+" "+Capitalize.capitalize(giocatore4.getCognome()));
				}else{
					partita.setNominativoGiocatore4("");
				}
				if (giocatore5 != null){
					partita.setNominativoGiocatore5(Capitalize.capitalize(giocatore5.getNome())+" "+Capitalize.capitalize(giocatore5.getCognome()));
				}else{
					partita.setNominativoGiocatore5("");
				}
			}
			
			List<ClassificheRow> classifica = torneo.getClassifica();
			if (classifica != null){
				for (ClassificheRow rigaClassifica: classifica){
					AnagraficaGiocatoreRow giocatore = TorneiUtils.findAnagraficaById(anagraficheGiocatoriRow, rigaClassifica.getIdGiocatore());
					rigaClassifica.setNominativoGiocatore(Capitalize.capitalize(giocatore.getNome())+" "+Capitalize.capitalize(giocatore.getCognome()));
				}
			}
		}		
	}
	
	public static void rankingPublisher(List<ScorePlayer> tabellini, File ranking){

		MyLogger.getLogger().info("Inizio scrittura file");
	    Properties p = new Properties();
	    p.setProperty("resource.loader.file.path", ResourceWorking.velocityTemplatePath());
	    Velocity.init( p );

		VelocityContext context = new VelocityContext();

		context.put( "scorePlayers", tabellini );
		context.put( "styleGenerator", StyleGenerator.class);
		context.put( "Capitalize", Capitalize.class);
		context.put( "htmlPublisher", HtmlPublisher.class);

		Template template = null;

		try{
		  template = Velocity.getTemplate("Ranking.vm");
		}catch( ResourceNotFoundException rnfe ){
			MyLogger.getLogger().severe(rnfe.getMessage());
		}catch( ParseErrorException pee ){
			MyLogger.getLogger().severe(pee.getMessage());
		}catch( MethodInvocationException mie ){
			MyLogger.getLogger().severe(mie.getMessage());
		}catch( Exception e ){
			MyLogger.getLogger().severe(e.getMessage());
		}


		FileWriter writer = null;
		try {
			writer = new FileWriter(ranking);
			template.merge( context, writer );
		} catch (IOException e) {
			MyLogger.getLogger().severe(e.getMessage());
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static List<File> torneiPublisher(String year, List<TorneoPubblicato> torneiPubblicati, File folderTornei){
		
//        BasicConfigurator.configure();
//        Logger log = Logger.getLogger( "HtmlPublisher" );
//        log.info("Log4jLoggerExample: ready to start velocity");
		
		MyLogger.getLogger().info("Inizio scrittura file");
	    Properties p = new Properties();
	    p.setProperty("resource.loader.file.path", ResourceWorking.velocityTemplatePath());
        p.setProperty("runtime.log.logsystem.log4j.logger","HtmlPublisher");
	    
	    Velocity.init( p );

		VelocityContext context = new VelocityContext();
		Template template = null;

		try{
		  template = Velocity.getTemplate("Torneo.vm");
		}catch( ResourceNotFoundException rnfe ){
			MyLogger.getLogger().severe(rnfe.getMessage());
		}catch( ParseErrorException pee ){
			MyLogger.getLogger().severe(pee.getMessage());
		}catch( MethodInvocationException mie ){
			MyLogger.getLogger().severe(mie.getMessage());
		}catch( Exception e ){
			MyLogger.getLogger().severe(e.getMessage());
		}

		String lastDateString = ResourceWorking.getLastTournamentDate(year);
		Date lastDate = null;
		if (lastDateString != null && !lastDateString.trim().isEmpty()){
			try {
				lastDate = lastUpdateTimeFormat.parse(lastDateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<File> result = new ArrayList<File>();
		for (TorneoPubblicato torneo: torneiPubblicati){
			MyLogger.getLogger().info("Inizio elaborazione torneo "+torneo.getIdTorneo());
			try {
				Date updateTime = ExcelGSheetsBridge.dfUpdateTime.parse(torneo.getTorneoRow().getUpdateTime());
				if (lastDate == null || updateTime.after(lastDate)){
					context.put( "torneo", torneo );
					context.put( "styleGenerator", StyleGenerator.class);
				
					File torneoHtml = new File(folderTornei, getTorneoPage(torneo.getIdTorneo())+".html");
					FileWriter writer = null;
					try {
						writer = new FileWriter(torneoHtml);
						template.merge( context, writer );
						result.add(torneoHtml);
						if (maxDate == null || maxDate.before(updateTime)){
							maxDate = updateTime;
						}
					} catch (IOException e) {
						MyLogger.getLogger().severe(e.getMessage());
					}finally{
						try {
							writer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void listaTorneiPublisher(List<TorneoPubblicato> torneiPubblicati, File listaTornei){
		
		MyLogger.getLogger().info("Inizio scrittura file. Primo Torneo: "+torneiPubblicati.get(0).getIdTorneo());
		MyLogger.getLogger().info("Inizio scrittura file. Ultimo Torneo: "+torneiPubblicati.get(torneiPubblicati.size()-1).getIdTorneo());
		
		Set<String> clubs = new TreeSet<String>();
		for (TorneoPubblicato torneo: torneiPubblicati){
			clubs.add(torneo.getTorneoRow().getOrganizzatore());
		}
		
	    Properties p = new Properties();
	    p.setProperty("resource.loader.file.path", ResourceWorking.velocityTemplatePath());
	    p.setProperty("runtime.log.logsystem.log4j.logger","HtmlPublisher");
	    
	    Velocity.init( p );

		VelocityContext context = new VelocityContext();
		Template template = null;

		try{
		  template = Velocity.getTemplate("ListaTornei.vm");
		}catch( ResourceNotFoundException rnfe ){
			MyLogger.getLogger().severe(rnfe.getMessage());
		}catch( ParseErrorException pee ){
			MyLogger.getLogger().severe(pee.getMessage());
		}catch( MethodInvocationException mie ){
			MyLogger.getLogger().severe(mie.getMessage());
		}catch( Exception e ){
			MyLogger.getLogger().severe(e.getMessage());
		}

		context.put( "tornei", torneiPubblicati );
		context.put( "clubs", clubs );
		context.put( "styleGenerator", StyleGenerator.class);
		context.put( "htmlPublisher", HtmlPublisher.class);
	
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(listaTornei);
			template.merge( context, writer );
		} catch (IOException e) {
			MyLogger.getLogger().severe(e.getMessage());
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void uploadFiles(File ranking, File listaTornei, List<File> torneiHtml) throws IOException{
		AlterVistaUtil.uploadInTornei(torneiHtml);
		AlterVistaUtil.uploadInRoot(Collections.singletonList(listaTornei));
		AlterVistaUtil.uploadInRoot(Collections.singletonList(ranking));
	}
	
	public static String getTorneoPage(String idTorneo){
		return idTorneo.replaceAll("\\s+", "").replaceAll("\\[", "_").replaceAll("\\]", "_").replaceAll("!", "");
	}
	
}
