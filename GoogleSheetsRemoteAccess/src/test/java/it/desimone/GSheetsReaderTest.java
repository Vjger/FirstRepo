package it.desimone;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.desimone.gsheets.GoogleSheetsAccess;
import it.desimone.gsheets.dto.AnagraficaGiocatoreRidottaRow;
import it.desimone.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gsheets.dto.ClassificheRow;
import it.desimone.gsheets.dto.PartitaRow;
import it.desimone.gsheets.dto.SheetRow;
import it.desimone.gsheets.dto.TorneiRow;
import it.desimone.gsheets.facade.GSheetsInterface;
import it.desimone.risiko.torneo.batch.ExcelAccess;
import it.desimone.risiko.torneo.dto.SchedaTorneo.TipoTorneo;
import it.desimone.utils.MyLogger;

public class GSheetsReaderTest {

	public static void main(String[] args) throws IOException {
		MyLogger.setConsoleLogLevel(Level.ALL);
		
	    Logger httpLogger = Logger.getLogger("com.google.api.client.googleapis.json");
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
	    httpLogger.addHandler(consoleHandler);
		
	    testInsertOrUpdateTorneo();
	    //testInsertOrUpdateGiocatore();
	    //testDeleteAndInsertPartita();
	    //testDeleteAndInsertClassifica();
	}
	
	
	private static void testInsertOrUpdateGiocatore() throws IOException{
		AnagraficaGiocatoreRidottaRow anagraficaRidottaRow = new AnagraficaGiocatoreRidottaRow();
		
		anagraficaRidottaRow.setNome("Valerio");
		anagraficaRidottaRow.setCognome("Mascagna");
		anagraficaRidottaRow.setEmail("valerio.mascagna@gmail.com");
		anagraficaRidottaRow.setUpdateTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
		
		String spreadSheetIdAnagraficaRidotta = "1nPDrmKcgXJzRZhsEdHIEk_GV36P7AC29C8c9ay8lLHQ";
		SheetRow anagraficaRowFound = GSheetsInterface.findSheetRowByKey(spreadSheetIdAnagraficaRidotta, AnagraficaGiocatoreRidottaRow.SHEET_ANAGRAFICA_NAME, anagraficaRidottaRow);
		
		if (anagraficaRowFound == null){
			Integer maxId = GSheetsInterface.findMaxIdAnagrafica(spreadSheetIdAnagraficaRidotta);
			anagraficaRidottaRow.setId(maxId+1);
			GSheetsInterface.appendRows(spreadSheetIdAnagraficaRidotta, AnagraficaGiocatoreRidottaRow.SHEET_ANAGRAFICA_NAME, Collections.singletonList((SheetRow)anagraficaRidottaRow));
		}else{
			anagraficaRidottaRow = (AnagraficaGiocatoreRidottaRow) GSheetsInterface.findSheetRowByLineNumber(spreadSheetIdAnagraficaRidotta, AnagraficaGiocatoreRidottaRow.SHEET_ANAGRAFICA_NAME, anagraficaRidottaRow);
		}
		
		AnagraficaGiocatoreRow anagraficaRow = new AnagraficaGiocatoreRow();
		anagraficaRow.setId(anagraficaRidottaRow.getId());
		anagraficaRow.setNome(anagraficaRidottaRow.getNome());
		anagraficaRow.setCognome(anagraficaRidottaRow.getCognome());
		anagraficaRow.setIdUltimoTorneo("20180223 - CASTELFRANCO VENETO [I Masnadieri]");
		anagraficaRow.setUltimoClub("[MODENA] Il Maialino");
		anagraficaRow.setUpdateTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
		
		String spreadSheetIdTornei = "1CsD-U3lpgBNHX0PgnRWwbGlKX6hcTtmrNKlqOdfwXtI";
		String sheetNameGiocatori = TorneiRow.SHEET_GIOCATORI_NAME;
		SheetRow giocatoriRowFound = GSheetsInterface.findSheetRowByKey(spreadSheetIdTornei, sheetNameGiocatori, anagraficaRow);
		
		if (giocatoriRowFound != null){
			anagraficaRow.setSheetRow(giocatoriRowFound.getSheetRow());
			List<SheetRow> rows = new ArrayList<SheetRow>();
			rows.add(anagraficaRow);
			GSheetsInterface.updateRows(spreadSheetIdTornei, sheetNameGiocatori, rows, true);
		}else{
			GSheetsInterface.appendRows(spreadSheetIdTornei, sheetNameGiocatori, Collections.singletonList((SheetRow)anagraficaRow));
		}
		
	}

	
	private static void testDeleteAndInsertClassifica() throws IOException{
		ClassificheRow classificheRow = new ClassificheRow();
		classificheRow.setIdTorneo("20180223 - CASTELFRANCO VENETO [I Masnadieri]");
		classificheRow.setClubGiocatore("ROMA [Il Gufo]");
		classificheRow.setIdGiocatore(1);
		classificheRow.setPosizione(10);
		classificheRow.setNumeroVittorie(2);
		classificheRow.setPartiteGiocate(5);
		classificheRow.setPunti(257D);
		classificheRow.setUpdateTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
		
		String spreadSheetIdTornei = "1CsD-U3lpgBNHX0PgnRWwbGlKX6hcTtmrNKlqOdfwXtI";
		String sheetNameClassifiche = ClassificheRow.SHEET_CLASSIFICHE;
		List<SheetRow> classificheRowFound = GSheetsInterface.findSheetRowsByCols(spreadSheetIdTornei, sheetNameClassifiche, classificheRow, ClassificheRow.ColPosition.ID_TORNEO);

		if (classificheRowFound != null && !classificheRowFound.isEmpty()){
			for (SheetRow row: classificheRowFound){
				GSheetsInterface.deleteRow(spreadSheetIdTornei, sheetNameClassifiche, row);
			}
		}
		
		GSheetsInterface.appendRows(spreadSheetIdTornei, sheetNameClassifiche, Collections.singletonList((SheetRow)classificheRow));
	}
	
	
	private static void testDeleteAndInsertPartita() throws IOException{
		PartitaRow partitaRow = new PartitaRow();
		partitaRow.setDataTurno("26/01/2018");
		partitaRow.setIdTorneo("20180223 - CASTELFRANCO VENETO [I Masnadieri]");
		partitaRow.setNumeroTurno(1);
		partitaRow.setNumeroTavolo(1);
		partitaRow.setIdGiocatore1(1);
		partitaRow.setPunteggioGiocatore1(45D);
		partitaRow.setIdGiocatore2(2);
		partitaRow.setPunteggioGiocatore2(43D);
		partitaRow.setIdGiocatore3(3);
		partitaRow.setPunteggioGiocatore3(42D);
		partitaRow.setIdGiocatore4(4);
		partitaRow.setPunteggioGiocatore4(41D);
		partitaRow.setIdGiocatore5(5);
		partitaRow.setPunteggioGiocatore5(40D);
		partitaRow.setIdGiocatoreVincitore(1);
		
		String spreadSheetIdTornei = "1CsD-U3lpgBNHX0PgnRWwbGlKX6hcTtmrNKlqOdfwXtI";
		String sheetNamePartite = PartitaRow.SHEET_PARTITE_NAME;
		List<SheetRow> partiteRowFound = GSheetsInterface.findSheetRowsByCols(spreadSheetIdTornei, sheetNamePartite, partitaRow, PartitaRow.ColPosition.ID_TORNEO);

		if (partiteRowFound != null && !partiteRowFound.isEmpty()){
			for (SheetRow row: partiteRowFound){
				GSheetsInterface.deleteRow(spreadSheetIdTornei, sheetNamePartite, row);
			}
		}
		
		GSheetsInterface.appendRows(spreadSheetIdTornei, sheetNamePartite, Collections.singletonList((SheetRow)partitaRow));
	}

	
	
	private static void testInsertOrUpdateTorneo() throws IOException{
		TorneiRow torneoRow = new TorneiRow();
		
		torneoRow.setNomeTorneo("Torneo di test");
		torneoRow.setOrganizzatore("CASTELFRANCO VENETO [I Masnadieri]");
		torneoRow.setSede("Comune di Castelfranco Veneto");
		torneoRow.setNumeroTurni(4);
		torneoRow.setNumeroPartecipanti(100);
		torneoRow.setNumeroTavoli(55);
		torneoRow.setStartDate("22/01/2018");
		torneoRow.setEndDate(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		torneoRow.setIdTorneo("20180223 - CASTELFRANCO VENETO [I Masnadieri]");
		torneoRow.setTipoTorneo(TipoTorneo.OPEN.getTipoTorneo());
		torneoRow.setNote("Torneo di San Valentino");
		torneoRow.setUpdateTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
		
		String spreadSheetIdTornei = "1CsD-U3lpgBNHX0PgnRWwbGlKX6hcTtmrNKlqOdfwXtI";
		String sheetNameTornei = TorneiRow.SHEET_TORNEI_NAME;
		SheetRow torneoRowFound = GSheetsInterface.findSheetRowByKey(spreadSheetIdTornei, sheetNameTornei, torneoRow);
		
		if (torneoRowFound != null){
			torneoRow.setSheetRow(torneoRowFound.getSheetRow());
			List<SheetRow> rows = new ArrayList<SheetRow>();
			rows.add(torneoRow);
			GSheetsInterface.updateRows(spreadSheetIdTornei, sheetNameTornei, rows, true);
		}else{
			GSheetsInterface.appendRows(spreadSheetIdTornei, sheetNameTornei, Collections.singletonList((SheetRow)torneoRow));
		}
	}


	private static void testDeleteRow() throws IOException{
		GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
		
		String spreadsheetId = "18UrsEJb4_JkqZmaSQyVGtEVkc047DPvgfCK93uiUiMc";

		//googleSheetsAccess.elencoSheets(spreadsheetId);
		
		googleSheetsAccess.deleteRow(spreadsheetId, 1910805432, 204);

	}
	
	
	private static void testAppend() throws IOException{
		GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
		
		String spreadsheetId = "18UrsEJb4_JkqZmaSQyVGtEVkc047DPvgfCK93uiUiMc";
		
		List<Object> giocatoreDaAggiungere = new ArrayList<Object>();
		giocatoreDaAggiungere.add("201");
		giocatoreDaAggiungere.add("Paolo");
		giocatoreDaAggiungere.add("Bianchi");
		giocatoreDaAggiungere.add("Il Maestro");
		giocatoreDaAggiungere.add("CAPALBIO [RCU]");
		giocatoreDaAggiungere.add("");
		giocatoreDaAggiungere.add("");
		giocatoreDaAggiungere.add("paolo.bianchi@gmail.com");
		
		List<List<Object>> records = new ArrayList<List<Object>>();
		records.add(giocatoreDaAggiungere);
		
		googleSheetsAccess.appendDataToSheet(spreadsheetId, "Iscritti", records);
	}
	
	private static void testLetture() throws IOException{
		GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
		
		String spreadsheetId = "18UrsEJb4_JkqZmaSQyVGtEVkc047DPvgfCK93uiUiMc";
		List<List<Object>> rows = googleSheetsAccess.leggiSheet(spreadsheetId, ExcelAccess.SCHEDA_CLASSIFICA_RIDOTTA+"!A:E");

		for (List row: rows){
			MyLogger.getLogger().finest(row.toString());
		}
		
		List<String> ranges = new ArrayList<String>();
		ranges.add(ExcelAccess.SCHEDA_CLASSIFICA_RIDOTTA+"!A2:A");
		ranges.add(ExcelAccess.SCHEDA_CLASSIFICA_RIDOTTA+"!B2:B");
		
		List<List<Object>> rows2 = googleSheetsAccess.leggiSheet(spreadsheetId, ranges);
		
		for (List row2: rows2){
			MyLogger.getLogger().finest(row2.toString());
		}
		
		
		List<String> filteredRanges = new ArrayList<String>();
		filteredRanges.add(ExcelAccess.SCHEDA_CLASSIFICA_RIDOTTA+"!A2:A");
		filteredRanges.add(ExcelAccess.SCHEDA_CLASSIFICA_RIDOTTA+"!B2:B");
		
		List<List<Object>> rows3 = googleSheetsAccess.findRow(spreadsheetId, filteredRanges);
		
		for (List row3: rows3){
			MyLogger.getLogger().finest(row3.toString());
		}
		
		//googleSheetsAccess.elencoSheets(spreadsheetId);
		
		List<List<Object>> rows4 = googleSheetsAccess.findData(spreadsheetId, 1365207118, null, null, null, null);
		
		if (rows4 != null){
			for (List row4: rows4){
				MyLogger.getLogger().finest(row4.toString());
			}
		}
	}
}