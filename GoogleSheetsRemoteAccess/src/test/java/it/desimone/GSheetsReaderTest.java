package it.desimone;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.desimone.gsheets.GoogleSheetsAccess;
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

	}
	
	private static void testInsertOrUpdateTorneo() throws IOException{
		TorneiRow torneoRow = new TorneiRow();
		
		torneoRow.setNomeTorneo("Torneo di test");
		torneoRow.setOrganizzatore("CASTELFRANCO VENETO [I Masnadieri]");
		torneoRow.setSede("Comune di Castelfranco Veneto");
		torneoRow.setNumeroTurni(3);
		torneoRow.setStartDate("22/01/2018");
		torneoRow.setEndDate(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		torneoRow.setIdTorneo("20180223 - CASTELFRANCO VENETO [I Masnadieri]");
		torneoRow.setTipoTorneo(TipoTorneo.OPEN.getTipoTorneo());
		torneoRow.setNote("Torneo di San Valentino");
		torneoRow.setUpdateTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
		
		GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
		
		String spreadSheetIdTornei = "1CsD-U3lpgBNHX0PgnRWwbGlKX6hcTtmrNKlqOdfwXtI";
		String sheetNameTornei = "TORNEI";
		SheetRow torneoRowFound = GSheetsInterface.findSheetRow(spreadSheetIdTornei, sheetNameTornei, torneoRow, googleSheetsAccess);
		
		if (torneoRowFound != null){
			torneoRow.setSheetRow(torneoRowFound.getSheetRow());
			List<SheetRow> rows = new ArrayList<SheetRow>();
			rows.add(torneoRow);
			googleSheetsAccess.updateRows(spreadSheetIdTornei, sheetNameTornei, rows, true);
		}else{
			googleSheetsAccess.appendDataToSheet(spreadSheetIdTornei, sheetNameTornei, Collections.singletonList(torneoRow.getData()));
		}
	}

	
	private static void testDefaultTempDir(){
	    
		Properties props = System.getProperties();
		
		Enumeration enu = props.propertyNames();
		while (enu.hasMoreElements()) {
			String key = (String)enu.nextElement();
			System.out.println(key+" - "+props.getProperty(key));
		}
		try{

    		//create a temp file
    		File temp = File.createTempFile("temp-file-name", ".tmp");

    		System.out.println("Temp file : " + temp.getAbsolutePath());

		//Get tempropary file path
    		String absolutePath = temp.getAbsolutePath();
    		String tempFilePath = absolutePath.
    		    substring(0,absolutePath.lastIndexOf(File.separator));

    		System.out.println("Temp file path : " + tempFilePath);

    	}catch(IOException e){

    		e.printStackTrace();

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
