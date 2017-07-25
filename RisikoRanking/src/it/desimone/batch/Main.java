package it.desimone.batch;

import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.risiko.torneo.dto.StoricoRanking;
import it.desimone.utils.MyLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;

public class Main {

	private static final String PATH_DI_TEST = "C:\\Users\\Marco\\Desktop\\TestRanking";
	//private static final String PATH_TORNEI = "C:\\Users\\mds\\Desktop\\Master e Raduni\\Tornei csv"; 
	private static final String PATH_TORNEI = "C:\\Users\\mds\\Documents\\Master e Raduni\\Tornei csv - 2013";
	private static final String FILE_OUT = "Ranking.csv"; 
	
	private static DecimalFormat numberFormat;
	private static List<File> tornei;
	static{
		numberFormat = new DecimalFormat();
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ITALY);
		numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);
	}
	private static RankingAnalyzer rankingAnalyzer;
	private static short partiteTotali = 0;
	
	public static void main(String[] args) {
		MyLogger.setLogLevel(Level.INFO);
		
		tornei = FileAnalyzer.readAllFilesCsv(PATH_TORNEI);

		rankingAnalyzer = new RankingAnalyzer();
		for (File torneo: tornei){
			List<Partita> partite = rankingAnalyzer.calcolaRankingGiocatoriTorneo(torneo);
			Set<GiocatoreDTO> giocatoriTorneo = new HashSet<GiocatoreDTO>();
			for (Partita partita: partite){giocatoriTorneo.addAll(partita.getGiocatori());}
			System.out.println(torneo.getName().substring(0,4)+";"+torneo.getName().substring(11,torneo.getName().indexOf("."))+";"+giocatoriTorneo.size());
			partiteTotali += partite.size();
		}
		printRanking();
		//printRankingRidotta();
		MyLogger.getLogger().info("********************************");
		MyLogger.getLogger().info("*Tornei complessivi: "+tornei.size());
		MyLogger.getLogger().info("*Partite complessive: "+partiteTotali);
		MyLogger.getLogger().info("********************************");
	}
	
	private static void printRanking(){
		List<GiocatoreDTO> elencoGiocatoriInRanking = rankingAnalyzer.getElencoGiocatoriInRanking();
		Comparator<GiocatoreDTO> comparator = new Comparator<GiocatoreDTO>(){
			public int compare(GiocatoreDTO o1, GiocatoreDTO o2) {
				return o2.getRanking().compareTo(o1.getRanking());
				//return o1.getCognome().compareTo(o2.getCognome());
			}
			
		};
		Collections.sort(elencoGiocatoriInRanking,comparator);
		short index = 1;
		File rankingFile = new File(PATH_TORNEI+"\\"+FILE_OUT);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(rankingFile);
			writer.write(printIntestazione());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (GiocatoreDTO giocatore: elencoGiocatoriInRanking){
			short numeroVittorie = 0;
			short numeroAvversariIncontrati = 0;
			Set<GiocatoreDTO> avversariDifferenti = new HashSet<GiocatoreDTO>();
			List<StoricoRanking> storico = giocatore.getStorico();
			for (StoricoRanking storicoRanking: storico){
				if (storicoRanking.getIsVincitore()){numeroVittorie++;}
				numeroAvversariIncontrati += storicoRanking.getGiocatoriAvversari().size();
				avversariDifferenti.addAll(storicoRanking.getGiocatoriAvversari());
			}
			String textOut = index +" "+giocatore.toString()+" "+giocatore.getRanking()+" "+storico.size()+" "+storico;
			//System.out.println(textOut);
			
			//String textFile = index +";"+giocatore.getNome()+";"+giocatore.getCognome()+";"+giocatore.getClub()+";"+numberFormat.format(giocatore.getRanking())+";"+giocatore.getStorico().size()+";"+giocatore.getStorico()+"\n";
			//String textFile = index +";"+giocatore.getNome()+";"+giocatore.getCognome()+";"+giocatore.getClub()+";"+numberFormat.format(giocatore.getRanking())+";"+storico.size()+";"+numeroVittorie+";"+printStorico(storico)+"\n";
			String textFile = index +";"+giocatore.getNome()+";"+giocatore.getCognome()+";"+giocatore.getClub()+";"
							+numberFormat.format(giocatore.getRanking())+";"+storico.size()+";"+numeroVittorie+";"
							+numeroAvversariIncontrati+";"+avversariDifferenti.size()+";"+printStorico(storico)+"\n";
			writer.write(textFile);
			
			index++;
		}
		writer.flush();
		writer.close();
	}
	
	private static void printRankingRidotta(){
		List<GiocatoreDTO> elencoGiocatoriInRanking = rankingAnalyzer.getElencoGiocatoriInRanking();
		Comparator<GiocatoreDTO> comparator = new Comparator<GiocatoreDTO>(){
			public int compare(GiocatoreDTO o1, GiocatoreDTO o2) {
				return o2.getRanking().compareTo(o1.getRanking());
				//return o1.getCognome().compareTo(o2.getCognome());
			}
			
		};
		
		int numeroMedioPartite = partiteTotali * 4 / elencoGiocatoriInRanking.size() + 1;
		Iterator<GiocatoreDTO> iterator = elencoGiocatoriInRanking.iterator();
		while (iterator.hasNext()){
			GiocatoreDTO giocatore = iterator.next();
			if(giocatore.getStorico().size() < numeroMedioPartite){
				iterator.remove();
			}			
		}
		
		Collections.sort(elencoGiocatoriInRanking,comparator);
		short index = 1;
		File rankingFile = new File(PATH_TORNEI+"\\"+FILE_OUT);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(rankingFile);
			writer.write(printIntestazione());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (GiocatoreDTO giocatore: elencoGiocatoriInRanking){
			short numeroVittorie = 0;
			short numeroAvversariIncontrati = 0;
			Set<GiocatoreDTO> avversariDifferenti = new HashSet<GiocatoreDTO>();
			List<StoricoRanking> storico = giocatore.getStorico();
			for (StoricoRanking storicoRanking: storico){
				if (storicoRanking.getIsVincitore()){numeroVittorie++;}
				Iterator<GiocatoreDTO> iterator2 = storicoRanking.getGiocatoriAvversari().iterator();
				while (iterator2.hasNext()){
					GiocatoreDTO giocatore2 = iterator2.next();
					if(!elencoGiocatoriInRanking.contains(giocatore2)){
						iterator2.remove();
					}			
				}
				numeroAvversariIncontrati += storicoRanking.getGiocatoriAvversari().size();
				avversariDifferenti.addAll(storicoRanking.getGiocatoriAvversari());
			}
			String textOut = index +" "+giocatore.toString()+" "+giocatore.getRanking()+" "+storico.size()+" "+storico;
			//System.out.println(textOut);
			
			//String textFile = index +";"+giocatore.getNome()+";"+giocatore.getCognome()+";"+giocatore.getClub()+";"+numberFormat.format(giocatore.getRanking())+";"+giocatore.getStorico().size()+";"+giocatore.getStorico()+"\n";
			//String textFile = index +";"+giocatore.getNome()+";"+giocatore.getCognome()+";"+giocatore.getClub()+";"+numberFormat.format(giocatore.getRanking())+";"+storico.size()+";"+numeroVittorie+";"+printStorico(storico)+"\n";
			String textFile = index +";"+giocatore.getNome()+";"+giocatore.getCognome()+";"+giocatore.getClub()+";"
							+numberFormat.format(giocatore.getRanking())+";"+storico.size()+";"+numeroVittorie+";"
							+numeroAvversariIncontrati+";"+avversariDifferenti.size()+";"+printStorico(storico)+"\n";
			writer.write(textFile);
			
			index++;
		}
		writer.flush();
		writer.close();
	}
	
	
	private static String printIntestazione(){
		String result = "Pos.;Nome;Cognome;Club;Indice Confronti;N.ro partite;N.ro Vittorie;Avversari;Avversari diversi;";
		//for (File torneo: tornei){
		for (int index=tornei.size() -1; index >=0; index--){
			File torneo = tornei.get(index);
			result +=torneo.getName()+";";
		}		
		result +="\n";
		return result;
	}
	
	private static String printStorico(List<StoricoRanking> storico){
		String result = "";
		//for (File torneo: tornei){
		for (int index=tornei.size() -1; index >=0; index--){
			File torneo = tornei.get(index);
			for (StoricoRanking storicoRanking: storico){
				if (storicoRanking.getTorneo().equals(torneo.getName())){
					result += numberFormat.format(storicoRanking.getRankingAfineTorneo())+" ";
				}
			}
			result +=";";
		}
		
		return result;
	}

}
