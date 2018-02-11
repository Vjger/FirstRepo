package it.desimone.gsheetsaccess.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import it.desimone.utils.MyLogger;

public class Configurator {
	
public static final String ROOT = new File("").getAbsolutePath();
public static final String PATH_CONFIGURATION = ROOT+File.separator+"configuration"; //ROOT+File.separator+"configuration";
private static final String CONFIG_FILE = "configuration.properties";	
private volatile static Properties properties = new Properties();

	static{
		try {
			FileInputStream propertiesStream = new FileInputStream(new File(PATH_CONFIGURATION+File.separator+CONFIG_FILE));
			properties.load(propertiesStream);
		} catch (IOException e) {
			MyLogger.getLogger().severe("IOException nel caricamento del file di Properties: "+e.getMessage());
		}
	}
	
	public static String getRCUFolderId(){
		String folderId = ((String)properties.get("rcuFolderId"));
		if (folderId != null) folderId = folderId.trim();
		MyLogger.getLogger().info("ID RCU Folder:<<"+folderId+">>");
		return folderId;
	}
	
	public static String getAnagraficaRidottaSheetId(){
		String folderId = ((String)properties.get("spreadSheetIdAnagraficaRidotta"));
		if (folderId != null) folderId = folderId.trim();
		MyLogger.getLogger().info("ID Anagrafica Ridotta:<<"+folderId+">>");
		return folderId;
	}

	public static String getTorneiSheetId(){
		String folderId = ((String)properties.get("spreadSheetIdTornei"));
		if (folderId != null) folderId = folderId.trim();
		MyLogger.getLogger().info("ID Tornei:<<"+folderId+">>");
		return folderId;
	}
	
	public static String getDoneFolderId(){
		String folderId = ((String)properties.get("DONEFolderId"));
		if (folderId != null) folderId = folderId.trim();
		MyLogger.getLogger().info("DONE Folder Id:<<"+folderId+">>");
		return folderId;
	}
	
	public static String getErrorFolderId(){
		String folderId = ((String)properties.get("ERRORFolderId"));
		if (folderId != null) folderId = folderId.trim();
		MyLogger.getLogger().info("ERROR Folder Id:<<"+folderId+">>");
		return folderId;
	}
	
	public static String getReportElaborazioniSheetId(){
		String sheetId = ((String)properties.get("spreadSheetIdReportElaborazioni"));
		if (sheetId != null) sheetId = sheetId.trim();
		MyLogger.getLogger().info("ID Report Elaborazioni:<<"+sheetId+">>");
		return sheetId;
	}
}
