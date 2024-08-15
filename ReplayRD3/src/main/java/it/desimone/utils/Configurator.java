package it.desimone.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class Configurator {

	public static final int DEFAULT_ANIMATION_TIME = 1000;
	public static int ANIMATION_TIME = DEFAULT_ANIMATION_TIME;
	public static PlanciaSize selectedResolution;
	public enum PlanciaSize{
		BIG, SMALL;
	}
	
//public static final String ROOT = System.getProperty("rootRD3Analyzer"); //new File("").getAbsolutePath();
	public static final String ROOT = new File("").getAbsolutePath();
	public static final String PATH_CONFIGURATION = ROOT+File.separator+"configuration"; //ROOT+File.separator+"configuration";
	private static final String CONFIG_FILE = "configuration.properties";	
	private volatile static Properties properties = new Properties();

	static{
//		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//		InputStream propertiesStream = classLoader.getResourceAsStream(CONFIG_FILE);
		try {
			FileInputStream propertiesStream = new FileInputStream(new File(PATH_CONFIGURATION+File.separator+CONFIG_FILE));
			properties.load(propertiesStream);
		} catch (IOException e) {
			MyLogger.getLogger().severe("IOException nel caricamento del file di Properties: "+e.getMessage());
		}
	}

	public static String getPathDB(){
		String pathDB = ROOT+File.separator+"data"+File.separator+"rd3data.sqlite";
		return pathDB;
	}

	public static String getPathLog(){
		String pathLog = ROOT+File.separator+"log";
		return pathLog;
	}
	
	public static Boolean isProcessoAutomatico(){
		return Boolean.valueOf((String)properties.get("processoAutomatico"));
	}
	
	public static String getUrlSchedaStagione(){
		return (String) properties.get("urlSchedaStagione");
	}
	
	public static Boolean caricaSoloLeNuove(){
		return Boolean.valueOf((String)properties.get("caricaSoloLeNuove"));
	}
	
	public static String getPathClassificaPrestigious(){
		return (String) properties.get("pathClassifica");
	}
	
	
	public static String getDominio(){
		return (String) properties.get("dominio");
	}
	
	public static Integer getTempoAttesaElaborazioneSchede(){
		return Integer.valueOf((String) properties.get("tempoAttesaElaborazioneSchede"));
	}
	
	public static Integer getTempoAttesaElaborazioneRanking(){
		return Integer.valueOf((String)properties.get("tempoAttesaElaborazioneRanking"));
	}
	
	public static Level getFileHandlerLevel(){
		Level result = Level.OFF;
		String level = (String) properties.get("livelloLogFile");
		if (level.equalsIgnoreCase("SEVERE")) result = Level.SEVERE;
		if (level.equalsIgnoreCase("WARNING")) result = Level.WARNING;
		if (level.equalsIgnoreCase("INFO")) result = Level.INFO;
		if (level.equalsIgnoreCase("FINE")) result = Level.FINE;
		if (level.equalsIgnoreCase("FINER")) result = Level.FINER;
		if (level.equalsIgnoreCase("FINEST")) result = Level.FINEST;
		if (level.equalsIgnoreCase("OFF")) result = Level.OFF;
		return result;
	}
	
	public static Level getConsoleHandlerLevel(){
		Level result = Level.OFF;
		String level = (String) properties.get("livelloLogConsole");
		if (level.equalsIgnoreCase("SEVERE")) result = Level.SEVERE;
		if (level.equalsIgnoreCase("INFO")) result = Level.INFO;
		if (level.equalsIgnoreCase("FINEST")) result = Level.FINEST;
		if (level.equalsIgnoreCase("OFF")) result = Level.OFF;
		return result;
	}
}
