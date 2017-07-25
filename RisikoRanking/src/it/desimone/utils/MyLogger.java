package it.desimone.utils;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class MyLogger {
	
	private static java.util.logging.Logger _log = java.util.logging.Logger.getAnonymousLogger();
	private static ConsoleHandler consoleHandler = null;
	private static ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	private static StreamHandler streamHandler = null;

	static{
		_log.setLevel(Level.FINEST);
		_log.setUseParentHandlers(false);
		consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.FINEST);
		streamHandler = new StreamHandler(byteArrayOutputStream, new SimpleFormatter());
		streamHandler.setLevel(Level.INFO);
		_log.addHandler(consoleHandler);
		_log.addHandler(streamHandler);
	}
	
	
	public static void setConsoleLogLevel(Level level){
		if (consoleHandler != null) consoleHandler.setLevel(level);
	}
	
	public static void setStreamLogLevel(Level level){
		if (streamHandler != null)  streamHandler.setLevel(level);
	}
	
	
	public static void setLogLevel(Level level){
		_log.setLevel(level);
		if (consoleHandler != null) consoleHandler.setLevel(level);
		if (streamHandler != null)  streamHandler.setLevel(level);
	}
	
	public static String getLogStream(){
		streamHandler.flush();
		return byteArrayOutputStream.toString();
	}
	
	public static List<String> getListLogStream(){
		streamHandler.flush();
		String logString = byteArrayOutputStream.toString();
		String[] stringRows = logString.split("\n");
		List result = Arrays.asList(stringRows);
		return result;
	}
	
	public static java.util.logging.Logger getLogger(){
		return _log;
	}
	
	public static void main (String[] args){
		DateFormat df =  new SimpleDateFormat("dd_MM_yyyy_HH:mm:ss");
		System.out.println(df.format(Calendar.getInstance().getTime()));
	}
	
}
