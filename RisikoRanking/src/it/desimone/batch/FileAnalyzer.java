package it.desimone.batch;

import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class FileAnalyzer {
	
	private static DecimalFormat numberFormat;
	static{
		numberFormat = new DecimalFormat();
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ITALY);
		numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	}
	
	public static List<File> readAllFilesCsv(String pathFolder){
		List<File> result = new ArrayList<File>();
		File[] folders = new File(pathFolder).listFiles();
		for (File folder: folders){
			if (folder.isDirectory()){
				File[] listOfFiles = folder.listFiles(getFileNameFilter());
				result.addAll(Arrays.asList(listOfFiles));
			}
		}
		Collections.sort(result, fileComparator());
		return result;
	}
	
//	public static List<File> readAllFilesCsv(String pathFolder){
//		File[] listOfFiles = new File(pathFolder).listFiles(getFileNameFilter());
//		Arrays.sort(listOfFiles, fileComparator());
//		return Arrays.asList(listOfFiles);
//	}
	private static FilenameFilter getFileNameFilter(){
		return new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return name.endsWith("csv");
			}		
		};
	}
	private static Comparator<File> fileComparator(){
		return new Comparator<File>(){
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}		
		};
	}
	
	public static List<Partita> estraiPartiteDaFile(File file){
		
		List<Partita> result = new LinkedList<Partita>();
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		try{
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			Partita partita = new Partita();
			while (line != null){
				Object[] giocatorePunteggio = readGiocatoreFromLine(line);
				if (giocatorePunteggio != null){
					GiocatoreDTO giocatore = (GiocatoreDTO) giocatorePunteggio[0];
					Float punteggio = (Float) giocatorePunteggio[1];
					if (partita == null){
						partita = new Partita();
					}
					partita.addGiocatore(giocatore, punteggio);
				}else{
					result.add(partita);
					partita = null;
				}
				line = bufferedReader.readLine();
			}			
			if (partita != null){
				result.add(partita);
			}
		}catch(FileNotFoundException fne){
			fne.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}finally{
			try {
				fileReader.close();
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	private static Object[] readGiocatoreFromLine(String line){
		Object[] result = null;
		if (line != null && line.length() > 0){
			String[] elementiRiga = line.split(";");
			if (elementiRiga != null){
				if (elementiRiga.length != 4 && elementiRiga.length > 0){
					throw new IllegalStateException("Riga con formattazione imprevista: "+line);
				}else{
					//Altrimenti è terminata la partita
					if (elementiRiga.length > 0 && elementiRiga[0] != null && elementiRiga[0].length() > 0){
						GiocatoreDTO giocatore = new GiocatoreDTO();
						giocatore.setNome(elementiRiga[0].trim());
						giocatore.setCognome(elementiRiga[1].trim());
						giocatore.setClub(elementiRiga[2]);
						//Float punteggio = Float.valueOf(elementiRiga[3]);
						Float punteggio = null;
						try {
							punteggio = numberFormat.parse(elementiRiga[3]).floatValue();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						result = new Object[2];
						result[0] = giocatore;
						result[1] = punteggio;
					}
				}
			}
		}	
		return result;
	}
	
}
