package it.desimone.gsheetsaccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.desimone.gheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gheetsaccess.gsheets.dto.RankingRow;
import it.desimone.gheetsaccess.gsheets.dto.SheetRow;
import it.desimone.gheetsaccess.gsheets.dto.SheetRowFactory.SheetRowType;
import it.desimone.gheetsaccess.gsheets.dto.TorneiRow;
import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.gsheets.facade.GSheetsInterface;
import it.desimone.utils.MyLogger;

public class RankingCalculator {
	
	public static void main(String[] args) {
		try{
			calculate();
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore di accesso a google drive "+e.getMessage());
		}
	}
	
	public static void calculate() throws IOException{
		Map<Integer, List<ElementoRanking>> torneiGiocatore = mapGiocatoreVSTornei();
		assignScores(torneiGiocatore);
		List<SheetRow> righeRankingOrdinate = calcolaRankingEOrdina(torneiGiocatore);
		String spreadSheetIdTornei = Configurator.getTorneiSheetId();
		
		//TODO fare prima il clean della pagina
		GSheetsInterface.appendRows(spreadSheetIdTornei, RankingRow.SHEET_NAME, righeRankingOrdinate);
	}
	
	private static void assignScores(Map<Integer, List<ElementoRanking>> torneiGiocatore){
		Set<Integer> giocatori = torneiGiocatore.keySet();
		for (Integer giocatore: giocatori){
			List<ElementoRanking> elementiRanking = torneiGiocatore.get(giocatore);
			
			for (ElementoRanking elementoRanking: elementiRanking){
				assignScore(elementoRanking);
			}
		}
	}
	
	private static void assignScore(ElementoRanking elementoRanking){
		Double assignedScore = 0D;
		//TODO inserire l'algoritmo: in teoria si può invece di implementare questo metodo implementae l'algoritmo direttamente nel getScore di ElementoRanking visto che è autoconsistente
		elementoRanking.setScore(assignedScore);
	}
	
	private static List<SheetRow> calcolaRankingEOrdina(Map<Integer, List<ElementoRanking>> torneiGiocatore){
		//TODO implementare
		return null;
	}
	
	private static Map<Integer, List<ElementoRanking>> mapGiocatoreVSTornei() throws IOException{
		
		Map<Integer, List<ElementoRanking>> torneiPerGiocatore = new HashMap<Integer, List<ElementoRanking>>();
		
		List<TorneiRow> tornei = getAllTornei();
		List<ClassificheRow> classifiche = getAllClassifiche();
		
		for (ClassificheRow classificaRow: classifiche){
			Integer idGiocatore = classificaRow.getIdGiocatore();
			String idTorneo = classificaRow.getIdTorneo();
			Integer posizione = classificaRow.getPosizione();
			Integer torneoIndex = tornei.indexOf(idTorneo);
			if (torneoIndex >=0){
				TorneiRow torneo = tornei.get(torneoIndex);
				List<ElementoRanking> torneiGiocatore = torneiPerGiocatore.get(idGiocatore);
				if (torneiGiocatore == null){
					torneiGiocatore = new ArrayList<ElementoRanking>();
				}
				ElementoRanking elementoRanking = new ElementoRanking();
				elementoRanking.setTorneo(torneo);
				elementoRanking.setPosizione(posizione);
				torneiGiocatore.add(elementoRanking);
				torneiPerGiocatore.put(idGiocatore, torneiGiocatore);
			}
		}
		return torneiPerGiocatore;
	}
	
	private static List<ClassificheRow> getAllClassifiche() throws IOException{
		String spreadSheetIdTornei = Configurator.getTorneiSheetId();
		List<ClassificheRow> result = GSheetsInterface.getAllRows(spreadSheetIdTornei, SheetRowType.Classifica);	
		return result;
	}
	
	private static List<TorneiRow> getAllTornei() throws IOException{
		String spreadSheetIdTornei = Configurator.getTorneiSheetId();
		List<TorneiRow> result = GSheetsInterface.getAllRows(spreadSheetIdTornei, SheetRowType.Torneo);	
		return result;
	}
}
