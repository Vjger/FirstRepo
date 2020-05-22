package it.desimone.rd3analyzer.main;

import it.desimone.utils.MyLogger;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class LoaderMassivoStoricoByIdList {

	private static final String PATH = "C:\\Utenti\\mds\\Desktop\\lista_01-01-2020_23-04-2020.txt"; 
	private static final Long sleepTime = 1000L;
	
	public static void main(String[] args) {
		
		MyLogger.getLogger().info("[BEGIN]");
		
		LoaderMassivoStoricoByIdList lms = new LoaderMassivoStoricoByIdList();
		lms.start();
	
		MyLogger.getLogger().info("[END]");
	}
	
	public void start() {
			
		try{
		     Path path = FileSystems.getDefault().getPath("C:\\Utenti\\mds\\Desktop\\", "lista_01-01-2020_23-04-2020.txt");
		     

		     Stream<String> lines = Files.lines(path);

			 MyLogger.getLogger().info("Working with "+lines.count()+" records");

		     BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);  
		     
		     String line = null;
		     while ((line = reader.readLine()) != null){
		    	 elaborateLine(line);
		     }

			 //lines.iterate(0, n -> n + 1).forEach(line -> elaborateLine(line));

			
		}catch(Exception e){
			MyLogger.getLogger().severe(e.getMessage());
		}
	
	}
	
	private void elaborateLine(String idPartita){
		try{
			LoaderByIdPartita.saveMatch(idPartita);
			
			Thread.sleep(sleepTime);
		}catch(Exception e){
			MyLogger.getLogger().severe(e.getMessage());
		}
	}
	
}
