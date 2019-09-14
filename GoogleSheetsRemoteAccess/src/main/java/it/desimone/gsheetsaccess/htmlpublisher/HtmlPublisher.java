package it.desimone.gsheetsaccess.htmlpublisher;

import it.desimone.gsheetsaccess.RankingCalculator;
import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.common.Configurator.Environment;
import it.desimone.gsheetsaccess.common.ResourceWorking;
import it.desimone.gsheetsaccess.dto.ScorePlayer;
import it.desimone.gsheetsaccess.dto.TorneoPubblicato;
import it.desimone.gsheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gsheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gsheetsaccess.gsheets.dto.PartitaRow;
import it.desimone.gsheetsaccess.utils.TorneiUtils;
import it.desimone.utils.Capitalize;
import it.desimone.utils.MyLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class HtmlPublisher {
	
	private static final String FOLDER_PATH = "C:\\Users\\mds\\Desktop\\RisiKo Pages";

	public static void main(String[] args) {
		MyLogger.setConsoleLogLevel(Level.INFO);
		Configurator.loadConfiguration(Environment.PRODUCTION);
		
		MyLogger.getLogger().info("Inizio elaborazione");
		String year = "2019";
		
		MyLogger.getLogger().info("Inizio estrazione tornei pubblicati");
		List<TorneoPubblicato> torneiPubblicati = TorneiUtils.caricamentoTornei(year);
		
		listaTorneiPublisher(torneiPubblicati);
		
		MyLogger.getLogger().info("Inizio elaborazione tabellini");
		List<ScorePlayer> tabellini = RankingCalculator.elaboraTabellini(year, torneiPubblicati);
		
		assegnaNominativiAPartita(torneiPubblicati, year);
		
		rankingPublisher(torneiPubblicati, tabellini);
		torneiPublisher(torneiPubblicati);
		
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
	
	public static void rankingPublisher(List<TorneoPubblicato> torneiPubblicati, List<ScorePlayer> tabellini){

		MyLogger.getLogger().info("Inizio scrittura file");
	    Properties p = new Properties();
	    p.setProperty("resource.loader.file.path", ResourceWorking.velocityTemplatePath());
	    Velocity.init( p );

		VelocityContext context = new VelocityContext();

		context.put( "scorePlayers", tabellini );
		context.put( "styleGenerator", StyleGenerator.class);
		context.put( "Capitalize", Capitalize.class);

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

		File ranking = new File(FOLDER_PATH,"ranking.html");
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
	
	public static void torneiPublisher(List<TorneoPubblicato> torneiPubblicati){
		
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
		  template = Velocity.getTemplate("Torneo2.vm");
		}catch( ResourceNotFoundException rnfe ){
			MyLogger.getLogger().severe(rnfe.getMessage());
		}catch( ParseErrorException pee ){
			MyLogger.getLogger().severe(pee.getMessage());
		}catch( MethodInvocationException mie ){
			MyLogger.getLogger().severe(mie.getMessage());
		}catch( Exception e ){
			MyLogger.getLogger().severe(e.getMessage());
		}

		for (TorneoPubblicato torneo: torneiPubblicati){
			MyLogger.getLogger().info("Inizio elaborazione torneo "+torneo.getIdTorneo());

			context.put( "torneo", torneo );
			context.put( "styleGenerator", StyleGenerator.class);
		
			File ranking = new File(FOLDER_PATH+File.separator+"TORNEI", getTorneoPage(torneo.getIdTorneo())+".html");
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
	}

	public static void listaTorneiPublisher(List<TorneoPubblicato> torneiPubblicati){
		
		MyLogger.getLogger().info("Inizio scrittura file");
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
		context.put( "styleGenerator", StyleGenerator.class);
	
		File listaTornei = new File(FOLDER_PATH, "listaTornei.html");
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
	
	public static String getTorneoPage(String idTorneo){
		return idTorneo.replaceAll("\\s+", "");
	}
	
}
