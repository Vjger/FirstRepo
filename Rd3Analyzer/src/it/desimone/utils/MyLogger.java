package it.desimone.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

public class MyLogger implements org.apache.velocity.runtime.log.LogChute{
	
	private static DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
	private static final String LOGGER_CONFIG_FILE = "loggerConfiguration.properties";
	private static java.util.logging.Logger _log = java.util.logging.Logger.getAnonymousLogger();
	private static ConsoleHandler consoleHandler = null;
	private static FileHandler fileHandler = null;

	static{
//		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//		InputStream propertiesStream = classLoader.getResourceAsStream("loggerConfiguration.properties");
		String logConfiguratorFileName = null;
		try {
			logConfiguratorFileName = Configurator.PATH_CONFIGURATION+File.separator+LOGGER_CONFIG_FILE;
			FileInputStream propertiesStream = new FileInputStream(new File(logConfiguratorFileName));
			LogManager logManager = LogManager.getLogManager();
			logManager.readConfiguration(propertiesStream);
		} catch (SecurityException e) {
			MyLogger.getLogger().severe("SecurityException nell'accesso al file "+logConfiguratorFileName+": "+e.getMessage());
			throw new RuntimeException(e);
		} catch (IOException e) {
			MyLogger.getLogger().severe("IOException nell'accesso al file "+logConfiguratorFileName+": "+e.getMessage());
			throw new RuntimeException(e);
		}

		_log.setLevel(Level.FINEST);
		_log.setUseParentHandlers(false);
		consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Configurator.getConsoleHandlerLevel());
		
		String logFileName = null;
		try{
			File dirLog = new File(Configurator.getPathLog());
			if (!dirLog.exists()){dirLog.mkdir();}
			logFileName = dirLog+File.separator+"RD3Analyzer.log";
			fileHandler = new FileHandler(logFileName);
			fileHandler.setFormatter(new SimpleFormatter());	
		}catch(IOException ioe){
			MyLogger.getLogger().severe("IOException nell'accesso al file "+logFileName+": "+ioe.getMessage());
			throw new RuntimeException(ioe);
		}
		fileHandler.setLevel(Configurator.getFileHandlerLevel());

		_log.addHandler(fileHandler);
		_log.addHandler(consoleHandler);
	}
	
	public static void closeFileHandler(){
		fileHandler.close();
	}
	
	public static void setConsoleLogLevel(Level level){
		if (consoleHandler != null) consoleHandler.setLevel(level);
	}
	public static void setFileLogLevel(Level level){
		if (fileHandler != null) fileHandler.setLevel(level);
	}

	public static void setLogLevel(Level level){
		_log.setLevel(level);
		if (consoleHandler != null) consoleHandler.setLevel(level);
		if (fileHandler != null)  fileHandler.setLevel(level);
	}
	

	public static java.util.logging.Logger getLogger(){
		return _log;
	}
	
	public static void main (String[] args){
		System.out.println(File.separator+" "+File.pathSeparator);
	}

	@Override
	public void init(RuntimeServices arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLevelEnabled(int level) {
		boolean result = false;
		switch (level) {
		case LogChute.DEBUG_ID:
			result = _log.isLoggable(Level.FINEST);
			break;
		case LogChute.INFO_ID:
			result = _log.isLoggable(Level.INFO);
			break;
		case LogChute.TRACE_ID:
			result = _log.isLoggable(Level.FINE);
			break;
		case LogChute.WARN_ID:
			result = _log.isLoggable(Level.FINER);
			break;
		case LogChute.ERROR_ID:
			result = _log.isLoggable(Level.SEVERE);
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public void log(int level, String message) {
		switch (level) {
		case LogChute.DEBUG_ID:
			_log.finest(message);
			break;
		case LogChute.INFO_ID:
			_log.info(message);
			break;
		case LogChute.TRACE_ID:
			_log.fine(message);
			break;
		case LogChute.WARN_ID:
			_log.finer(message);
			break;
		case LogChute.ERROR_ID:
			_log.severe(message);
			break;
		default:
			break;
		}	
	}

	@Override
	public void log(int level, String message, Throwable t) {
		log(level,message);	
	}
	
}
