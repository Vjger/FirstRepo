package it.desimone;

import it.desimone.utils.MyLogger;

import java.io.File;

public class ResourceWorking {
	
	public static final String ROOT;
	
	public static final String RCU_FOLDER_ID = "1OremAjhDfjtIXhgWsq7lCAcnSBZKWGsF";
	
	static{
		ROOT = new File("").getAbsolutePath();
		MyLogger.getLogger().finer("ROOT: "+ROOT);	
	}
	
	
	private static final String googleClientSecret = "client_secret_manager.json";
	
	public static String workingDownloadPath(){
		return ROOT+File.separator+"working"+File.separator+"download";
	}
	
	public static String googleClientSecretPath(){
		return ROOT+File.separator+"resources"+File.separator+"google"+File.separator+googleClientSecret;
	}
	
	public static File googleCredentials(){
		return new java.io.File(ROOT+File.separator+"resources"+File.separator+"google", ".credentials/RisiKo Data Manager");
	}
	
	
}
