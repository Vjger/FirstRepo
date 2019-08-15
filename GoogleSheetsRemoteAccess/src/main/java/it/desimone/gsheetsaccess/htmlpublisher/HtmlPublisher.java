package it.desimone.gsheetsaccess.htmlpublisher;

import it.desimone.gsheetsaccess.RankingCalculator;
import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.common.ResourceWorking;
import it.desimone.gsheetsaccess.common.Configurator.Environment;
import it.desimone.gsheetsaccess.dto.ScorePlayer;
import it.desimone.gsheetsaccess.dto.TorneoPubblicato;
import it.desimone.gsheetsaccess.utils.TorneiUtils;
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

	public static void main(String[] args) {
		MyLogger.setConsoleLogLevel(Level.INFO);
		Configurator.loadConfiguration(Environment.PRODUCTION);
		
		MyLogger.getLogger().info("Inizio elaborazione");
		
		rankingPublisher("2019");
		
		MyLogger.getLogger().info("Fine elaborazione");
	}
	
	private static void rankingPublisher(String year){
		MyLogger.getLogger().info("Inizio estrazione tornei pubblicati");
		List<TorneoPubblicato> torneiPubblicati = TorneiUtils.caricamentoTornei(year);
		MyLogger.getLogger().info("Inizio elaborazione tabellini");
		List<ScorePlayer> tabellini = RankingCalculator.elaboraTabellini(year, torneiPubblicati);
		
		MyLogger.getLogger().info("Inizio scrittura file");
	    Properties p = new Properties();
	    p.setProperty("resource.loader.file.path", ResourceWorking.velocityTemplatePath());
	    Velocity.init( p );

		VelocityContext context = new VelocityContext();

		context.put( "scorePlayers", tabellini );
		context.put( "styleGenerator", StyleGenerator.class);

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

		File ranking = new File("C:\\Users\\mds\\Desktop\\Ranking\\ranking.html");
		FileWriter writer;
		try {
			writer = new FileWriter(ranking);
			template.merge( context, writer );
		} catch (IOException e) {
			MyLogger.getLogger().severe(e.getMessage());
		}
	}

}
