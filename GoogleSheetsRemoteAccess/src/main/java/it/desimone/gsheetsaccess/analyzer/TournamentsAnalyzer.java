package it.desimone.gsheetsaccess.analyzer;

import it.desimone.gsheetsaccess.analyzer.ClubAnalysis.ClubPlayerData;
import it.desimone.gsheetsaccess.dto.TorneoPubblicato;
import it.desimone.gsheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gsheetsaccess.utils.TorneiUtils;

import java.util.List;

public class TournamentsAnalyzer {


	public static void main (String[] args){
		String year = "2019";
		List<TorneoPubblicato> torneiPubblicati = TorneiUtils.caricamentoTornei(year);
		ClubAnalysis clubAnalysis = elaboraPartecipazioniTornei(year, torneiPubblicati);
		
		List<ClubPlayerData> clubPlayerData = clubAnalysis.getPlayerDataByClub("ROMA [Il Gufo]");
		
		for (ClubPlayerData cluPlayerData: clubPlayerData){
			System.out.println(cluPlayerData);
		}
	}
	
	public static ClubAnalysis elaboraPartecipazioniTornei(String year, List<TorneoPubblicato> torneiPubblicati){
		List<AnagraficaGiocatoreRow> anagraficheGiocatoriRow = TorneiUtils.getAllAnagraficheGiocatori(year);
		ClubAnalysis clubAnalysis = new ClubAnalysis();
		for (TorneoPubblicato torneoPubblicato: torneiPubblicati){
			clubAnalysis.populateData(torneoPubblicato, anagraficheGiocatoriRow);
		}
		return clubAnalysis;
	}

}
