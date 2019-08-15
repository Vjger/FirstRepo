package it.desimone.gsheetsaccess.common;

import it.desimone.utils.MyLogger;

import java.io.File;

public class ResourceWorking {
	
	public static final String ROOT;
	
	static{
		ROOT = new File("").getAbsolutePath();
		MyLogger.getLogger().finer("ROOT: "+ROOT);	
	}
	
	
	private static final String googleClientSecret = "client_secret_manager.json";
	
	public static String workingAreaPath(){
		return ROOT+File.separator+"working"+File.separator+"tmp";
	}
	
	public static String doneAreaPath(){
		return ROOT+File.separator+"working"+File.separator+"done";
	}
	
	public static String errorAreaPath(){
		return ROOT+File.separator+"working"+File.separator+"error";
	}
	
	public static String tabellinoLoaderInputAreaPath(){
		return ROOT+File.separator+"working"+File.separator+"input"+File.separator+"tabellinoLoader";
	}
	public static String tabellinoLoaderOutputAreaPath(){
		return ROOT+File.separator+"working"+File.separator+"output"+File.separator+"tabellinoLoader";
	}
	
	public static String googleClientSecretPath(){
		return ROOT+File.separator+"resources"+File.separator+"google"+File.separator+googleClientSecret;
	}
	
	public static String velocityTemplatePath(){
		return ROOT+File.separator+"resources"+File.separator+"velocity";
	}
	
	public static File googleCredentials(){
		return new java.io.File(ROOT+File.separator+"resources"+File.separator+"google", ".credentials/RisiKo Data Manager");
	}
	
	
}
