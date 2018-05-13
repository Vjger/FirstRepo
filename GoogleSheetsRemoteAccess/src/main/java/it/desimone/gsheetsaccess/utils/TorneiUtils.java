package it.desimone.gsheetsaccess.utils;

import it.desimone.gheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gheetsaccess.gsheets.dto.PartitaRow;
import it.desimone.gheetsaccess.gsheets.dto.SheetRow;
import it.desimone.gheetsaccess.gsheets.dto.TorneiRow;
import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.gsheets.facade.GSheetsInterface;
import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TorneiUtils {

	public static void mergePlayer(Integer idPlayerFrom, Integer idPlayerTo){
		MyLogger.getLogger().info("Inizio merge dati da giocatore con id ["+idPlayerFrom+"] a giocatore con id ["+idPlayerTo+"]");
		
		try {
			String spreadSheetIdTornei = Configurator.getTorneiSheetId();
			ClassificheRow classificheRow = new ClassificheRow();
			classificheRow.setIdGiocatore(idPlayerFrom);
			List<SheetRow> righeClassificaGiocatore = GSheetsInterface.findClassificaRowsByIdGiocatore(spreadSheetIdTornei, classificheRow);
			
			PartitaRow partitaRow = new PartitaRow();
			partitaRow.setIdGiocatoreVincitore(idPlayerFrom);
			List<SheetRow> righePartiteGiocatore = GSheetsInterface.findPartiteRowsByIdGiocatore(spreadSheetIdTornei, partitaRow);
			
			for (SheetRow rigaClassifica: righeClassificaGiocatore){
				ClassificheRow riga = (ClassificheRow) rigaClassifica;
				riga.setIdGiocatore(idPlayerTo);
			}
			
			for (SheetRow rigaPartite: righePartiteGiocatore){
				PartitaRow riga = (PartitaRow) rigaPartite;
				if (riga.getIdGiocatore1() != null && riga.getIdGiocatore1().equals(idPlayerFrom)){
					riga.setIdGiocatore1(idPlayerTo);
				}
				if (riga.getIdGiocatore2() != null && riga.getIdGiocatore2().equals(idPlayerFrom)){
					riga.setIdGiocatore2(idPlayerTo);
				}
				if (riga.getIdGiocatore3() != null && riga.getIdGiocatore3().equals(idPlayerFrom)){
					riga.setIdGiocatore3(idPlayerTo);
				}
				if (riga.getIdGiocatore4() != null && riga.getIdGiocatore4().equals(idPlayerFrom)){
					riga.setIdGiocatore4(idPlayerTo);
				}
				if (riga.getIdGiocatore5() != null && riga.getIdGiocatore5().equals(idPlayerFrom)){
					riga.setIdGiocatore5(idPlayerTo);
				}
				if (riga.getIdGiocatoreVincitore() != null && riga.getIdGiocatoreVincitore().equals(idPlayerFrom)){
					riga.setIdGiocatoreVincitore(idPlayerTo);
				}
			}
			
			GSheetsInterface.updateRows(spreadSheetIdTornei, ClassificheRow.SHEET_CLASSIFICHE, righeClassificaGiocatore, true);
			GSheetsInterface.updateRows(spreadSheetIdTornei, PartitaRow.SHEET_PARTITE_NAME, righePartiteGiocatore, true);
			
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore accedendo ai dati "+e.getMessage());
		}
	}
	
		
	public static void deleteTorneo(String idTorneo){
		MyLogger.getLogger().info("INIZIO Cancellazione del torneo ["+idTorneo+"]");
		
		try {
			deletePartiteTorneoRows(idTorneo);
			deleteClassificaTorneoRows(idTorneo);
			deleteTorneoRow(idTorneo);
		} catch (IOException e) {
			MyLogger.getLogger().severe("Errore accedendo ai dati del torneo ["+idTorneo+"] - "+e.getMessage());
		}
		
		MyLogger.getLogger().info("FINE Cancellazione del torneo ["+idTorneo+"]");
	}
	
	private static void deletePartiteTorneoRows(String idTorneo) throws IOException{
		MyLogger.getLogger().info("Inizio cancellazione righe partita del torneo ["+idTorneo+"]");
		String spreadSheetIdTornei = Configurator.getTorneiSheetId();
		String sheetNamePartite = PartitaRow.SHEET_PARTITE_NAME;
		
		//Basta un oggetto: tanto l'id del torneo è sempre lo stesso.
		PartitaRow partitaRowDiRicerca = new PartitaRow();
		partitaRowDiRicerca.setIdTorneo(idTorneo);
		List<Integer> partiteRowFound = GSheetsInterface.findNumPartiteRowsByIdTorneo(spreadSheetIdTornei, partitaRowDiRicerca);

		if (partiteRowFound != null && !partiteRowFound.isEmpty()){
			MyLogger.getLogger().info("Cancellazione di "+partiteRowFound.size()+" partite del torneo "+idTorneo);
			GSheetsInterface.deleteRowsByNumRow(spreadSheetIdTornei, sheetNamePartite, partiteRowFound);
		}
	}
	
	private static void deleteClassificaTorneoRows(String idTorneo) throws IOException{
		MyLogger.getLogger().info("Inizio cancellazione righe classifica del torneo ["+idTorneo+"]");
		String spreadSheetIdTornei = Configurator.getTorneiSheetId();
		String sheetNameClassifiche = ClassificheRow.SHEET_CLASSIFICHE;
		
		ClassificheRow classificheRowDiRicerca = new ClassificheRow();
		classificheRowDiRicerca.setIdTorneo(idTorneo);
		List<Integer> classificheRowFound = GSheetsInterface.findClassificaRowsByIdTorneo(spreadSheetIdTornei, classificheRowDiRicerca);

		if (classificheRowFound != null && !classificheRowFound.isEmpty()){
			MyLogger.getLogger().info("Cancellazione di "+classificheRowFound.size()+" giocatori in classifica del torneo "+idTorneo);
			GSheetsInterface.deleteRowsByNumRow(spreadSheetIdTornei, sheetNameClassifiche, classificheRowFound);
		}
	}
	
	private static void deleteTorneoRow(String idTorneo) throws IOException{
		MyLogger.getLogger().info("Inizio cancellazione riga del Torneo ["+idTorneo+"]");
		TorneiRow torneoRow = new TorneiRow();
		torneoRow.setIdTorneo(idTorneo);
		
		String spreadSheetIdTornei = Configurator.getTorneiSheetId();
		String sheetNameTornei = TorneiRow.SHEET_TORNEI_NAME;
		Integer torneoRowFound = GSheetsInterface.findNumTorneoRowByIdTorneo(spreadSheetIdTornei, sheetNameTornei, torneoRow);
		
		if (torneoRowFound != null){
			List<Integer> partitaList = new ArrayList<Integer>();
			partitaList.add(torneoRowFound);
			GSheetsInterface.deleteRowsByNumRow(spreadSheetIdTornei, sheetNameTornei, partitaList);
		}else{
			MyLogger.getLogger().severe("Non trovato il torneo ["+idTorneo+"]");
		}
	}
}
