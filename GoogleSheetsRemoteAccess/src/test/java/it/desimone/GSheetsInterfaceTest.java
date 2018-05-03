package it.desimone;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import it.desimone.gheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.googleaccess.GoogleSheetsAccess;
import junit.framework.TestCase;

public class GSheetsInterfaceTest extends TestCase {

	public void testLeggiSheetStringString() throws IOException {
		GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
		String spreadSheetIdTornei = Configurator.getTorneiSheetId();
		String sheetNameAnagrafica = AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME;
		
		Long before = System.currentTimeMillis();
		
		List<List<Object>> sheetRows = googleSheetsAccess.leggiSheet(spreadSheetIdTornei, sheetNameAnagrafica);
		
		Long after = System.currentTimeMillis();
		
		System.out.println("leggiSheet(String spreadsheetId, String sheetName): "+(after-before));
		
//		for (List<Object> row: sheetRows){
//			System.out.println(row);
//		}
	}

	public void testLeggiSheetStringListOfString() throws IOException{
		GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
		String spreadSheetIdTornei = Configurator.getTorneiSheetId();
		String sheetNameAnagrafica = AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME;
		List<String> ranges = Collections.singletonList(sheetNameAnagrafica+"!A2:G");
		
		Long before = System.currentTimeMillis();
		
		List<List<Object>> sheetRows = googleSheetsAccess.leggiSheet(spreadSheetIdTornei, ranges);
		
		Long after = System.currentTimeMillis();
		
		System.out.println("leggiSheet(String spreadsheetId, List<String> ranges): "+(after-before));
		
//		for (List<Object> row: sheetRows){
//			System.out.println(row);
//		}
	}

	public void testFindRow() throws IOException {
		GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
		String spreadSheetIdTornei = Configurator.getTorneiSheetId();
		String sheetNameAnagrafica = AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME;
		List<String> ranges = Collections.singletonList(sheetNameAnagrafica+"!A2:G");
		
		Long before = System.currentTimeMillis();
		
		List<List<Object>> sheetRows = googleSheetsAccess.findRow(spreadSheetIdTornei, ranges);
		
		Long after = System.currentTimeMillis();
		
		System.out.println("findRow(String spreadsheetId, List<String> ranges): "+(after-before));
		
		for (List<Object> row: sheetRows){
			System.out.println(row);
		}
	}

}
