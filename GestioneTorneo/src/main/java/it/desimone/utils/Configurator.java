package it.desimone.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurator {
	
public static final String ROOT = new File("").getAbsolutePath();
public static final String PATH_CONFIGURATION = ROOT+File.separator+"configuration"; //ROOT+File.separator+"configuration";
private static final String CONFIG_FILE = "configuration.properties";	
private volatile static Properties properties = new Properties();

	static{
		FileInputStream propertiesStream = null;
		try {
			propertiesStream = new FileInputStream(new File(PATH_CONFIGURATION+File.separator+CONFIG_FILE));
			properties.load(propertiesStream);
		} catch (IOException e) {
			MyLogger.getLogger().severe("IOException nel caricamento del file di Properties: "+e.getMessage());
		} finally {
			if (propertiesStream != null) {
				try {
					propertiesStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getRCUFolderId(){
		String folderId = ((String)properties.get("rcuFolderId"));
		if (folderId != null) folderId = folderId.trim();
		MyLogger.getLogger().info("ID RCU Folder:<<"+folderId+">>");
		return folderId;
	}
	
	public static Integer getSogliaRadunoConQuarti(){
		String soglia = ((String)properties.get("sogliaRadunoConQuarti"));
		Integer result = 0;
		if (soglia != null) soglia = soglia.trim();
		try{
			result = Integer.valueOf(soglia);
			MyLogger.getLogger().info("Soglia raduno con quarti:<<"+soglia+">>");
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore nel parsing della soglia:<<"+soglia+">>");
		}
		return result;
	}
	
	public static Integer getSogliaMasterSplittato(){
		String soglia = ((String)properties.get("sogliaSuperMaster"));
		Integer result = 0;
		if (soglia != null) soglia = soglia.trim();
		try{
			result = Integer.valueOf(soglia);
			MyLogger.getLogger().info("Soglia master splittato:<<"+soglia+">>");
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore nel parsing della soglia:<<"+soglia+">>");
		}
		return result;
	}
	
	public static Boolean getDefaultVincitoreUnico(){
		String defaultVincitoreUnico = ((String)properties.get("defaultVincitoreUnico"));
		Boolean result = Boolean.TRUE;
		if (defaultVincitoreUnico != null) defaultVincitoreUnico = defaultVincitoreUnico.trim();
		try{
			result = Boolean.valueOf(defaultVincitoreUnico);
			MyLogger.getLogger().info("defaultVincitoreUnico:<<"+defaultVincitoreUnico+">>");
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore nel parsing del defaultVincitoreUnico:<<"+defaultVincitoreUnico+">>");
		}
		return result;
	}
	
	public static Boolean getMemorizzaCredenziali(){
		String memorizzaCredenziali = ((String)properties.get("memorizzaCredenziali"));
		Boolean result = Boolean.TRUE;
		if (memorizzaCredenziali != null) memorizzaCredenziali = memorizzaCredenziali.trim();
		try{
			result = Boolean.valueOf(memorizzaCredenziali);
			MyLogger.getLogger().info("memorizzaCredenziali:<<"+memorizzaCredenziali+">>");
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore nel parsing del memorizzaCredenziali:<<"+memorizzaCredenziali+">>");
		}
		return result;
	}
	
	public static void setMemorizzaCredenziali(Boolean memorizzaCredenziali){
		FileOutputStream out = null; 

		try{
			out = new FileOutputStream(new File(PATH_CONFIGURATION+File.separator+CONFIG_FILE));
			properties.setProperty("memorizzaCredenziali", memorizzaCredenziali.toString());
			properties.store(out, null);
			out.close();
			MyLogger.getLogger().info("memorizzaCredenziali:<<"+memorizzaCredenziali+">>");
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore nel save del memorizzaCredenziali:<<"+memorizzaCredenziali+">>");
		}
	}
}
