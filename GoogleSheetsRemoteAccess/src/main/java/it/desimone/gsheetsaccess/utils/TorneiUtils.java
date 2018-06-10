package it.desimone.gsheetsaccess.utils;

import it.desimone.gheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gheetsaccess.gsheets.dto.PartitaRow;
import it.desimone.gheetsaccess.gsheets.dto.SheetRow;
import it.desimone.gheetsaccess.gsheets.dto.TorneiRow;
import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.gsheets.facade.GSheetsInterface;
import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TorneiUtils {

	public static void mergePlayer(Integer idPlayerFrom, Integer idPlayerTo){
		MyLogger.getLogger().info("Inizio merge dati da giocatore con id ["+idPlayerFrom+"] a giocatore con id ["+idPlayerTo+"]");
		
		try {
			String spreadSheetIdTornei = Configurator.getTorneiSheetId();
			ClassificheRow classificheRow = new ClassificheRow();
			classificheRow.setIdGiocatore(idPlayerFrom);
			List<SheetRow> righeClassificaGiocatore = GSheetsInterface.findClassificaRowsByIdGiocatore(spreadSheetIdTornei, classificheRow);
			
			MyLogger.getLogger().info("Estratte "+(righeClassificaGiocatore==null?0:righeClassificaGiocatore.size())+" righe Classifica per il giocatore con ID ["+idPlayerFrom+"]");
			if (righeClassificaGiocatore != null) MyLogger.getLogger().info(righeClassificaGiocatore.toString());
			
			PartitaRow partitaRow = new PartitaRow();
			partitaRow.setIdGiocatoreVincitore(idPlayerFrom);
			List<SheetRow> righePartiteGiocatore = GSheetsInterface.findPartiteRowsByIdGiocatore(spreadSheetIdTornei, partitaRow);
			
			MyLogger.getLogger().info("Estratte "+(righePartiteGiocatore==null?0:righePartiteGiocatore.size())+" righe Partita per il giocatore con ID ["+idPlayerFrom+"]");
			if (righePartiteGiocatore != null) MyLogger.getLogger().info(righePartiteGiocatore.toString());
			
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
			MyLogger.getLogger().info("Sostituito nelle righe classifica il giocatore con ID ["+idPlayerFrom+"] con quello con ID ["+idPlayerTo+"]");
			GSheetsInterface.updateRows(spreadSheetIdTornei, PartitaRow.SHEET_PARTITE_NAME, righePartiteGiocatore, true);
			MyLogger.getLogger().info("Sostituito nelle righe partita il giocatore con ID ["+idPlayerFrom+"] con quello con ID ["+idPlayerTo+"]");
			
			//INizio cancellazione giocatore
			SheetRow anagraficaGiocatoreRowFrom = new AnagraficaGiocatoreRow();
			((AnagraficaGiocatoreRow)anagraficaGiocatoreRowFrom).setId(idPlayerFrom);
			List<SheetRow> anagraficheDaCancellareRowFound = GSheetsInterface.findAnagraficheByKey(spreadSheetIdTornei, Collections.singletonList(anagraficaGiocatoreRowFrom));
			
			if (anagraficheDaCancellareRowFound != null && !anagraficheDaCancellareRowFound.isEmpty()){
				List<Integer> rowNumberGiocatoreFrom = new ArrayList<Integer>();
				rowNumberGiocatoreFrom.add(anagraficheDaCancellareRowFound.get(0).getSheetRowNumber());
				GSheetsInterface.deleteRowsByNumRow(spreadSheetIdTornei, AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME, rowNumberGiocatoreFrom);
			}
			
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
