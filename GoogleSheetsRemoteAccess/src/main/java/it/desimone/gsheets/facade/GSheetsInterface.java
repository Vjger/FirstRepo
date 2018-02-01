package it.desimone.gsheets.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.services.sheets.v4.model.ValueRange;

import it.desimone.gsheets.GoogleSheetsAccess;
import it.desimone.gsheets.dto.AnagraficaGiocatoreRidottaRow;
import it.desimone.gsheets.dto.SheetRow;

public class GSheetsInterface {

	private static GoogleSheetsAccess googleSheetAccess;
	
	public static GoogleSheetsAccess getGoogleSheetsInstance(){
		if (googleSheetAccess == null){
			googleSheetAccess = new GoogleSheetsAccess();
		}
		return googleSheetAccess;
	}
	
	private static String toAlphabetic(int i) {
	    if( i<0 ) {
	        return "-"+toAlphabetic(-i-1);
	    }

	    int quot = i/26;
	    int rem = i%26;
	    char letter = (char)((int)'A' + rem);
	    if( quot == 0 ) {
	        return ""+letter;
	    } else {
	        return toAlphabetic(quot-1) + letter;
	    }
	}
	
	private static List<String> byKeyColumnsToRanges(String sheetName, List<Integer> keyColumns){
		List<String> ranges = null;
		if (sheetName != null){
			ranges = new ArrayList<String>();
			if (keyColumns!= null && !keyColumns.isEmpty()){
				for (Integer keyColumn: keyColumns){
					String columnLetter = toAlphabetic(keyColumn);
					ranges.add(sheetName+"!"+columnLetter+":"+columnLetter);
				}
			}else{
				ranges.add(sheetName);	
			}
		}
		return ranges;
	}
	
	
	public static List<SheetRow> findSheetRowsByCols(String spreadSheetId, String sheetName, SheetRow sheetRow, Integer... searchCols) throws IOException{
		List<SheetRow> result = null;
			
		List<String> ranges = byKeyColumnsToRanges(sheetName, Arrays.asList(searchCols));
		
		List<List<Object>> data = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);
		
		List<Object> dataSheetRow = sheetRow.getData();
		int indexRow = 0;
		for (List<Object> row: data){
			indexRow++;
			boolean rowFound = true;
			for (int i=0; i < searchCols.length && rowFound; i++){
				Object elementoRigaRemota = row.get(i);
				Object elementoRigaInCanna = dataSheetRow.get(searchCols[i]);
				rowFound = rowFound && elementoRigaInCanna.toString().equalsIgnoreCase(elementoRigaRemota.toString());
			}
			if (rowFound){
				if (result == null){
					result = new ArrayList<SheetRow>();
				}
				SheetRow sRow;
				try {
					sRow = (SheetRow) sheetRow.clone();
					sRow.setSheetRow(indexRow);
					result.add(sRow);
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	
	
	public static SheetRow findSheetRowByKey(String spreadSheetId, String sheetName, SheetRow sheetRow) throws IOException{
		SheetRow result = null;
		
		List<Integer> keyCols = sheetRow.keyCols();
		
		List<String> ranges = byKeyColumnsToRanges(sheetName, keyCols);
		
		List<List<Object>> data = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);
		
		List<Object> dataSheetRow = sheetRow.getData();
		int indexRow = 0;
		for (List<Object> row: data){
			indexRow++;
			boolean rowFound = true;
			for (int i=0; i < keyCols.size() && rowFound; i++){
				Object elementoRigaRemota = row.get(i);
				Object elementoRigaInCanna = dataSheetRow.get(keyCols.get(i));
				rowFound = rowFound && elementoRigaInCanna.toString().equalsIgnoreCase(elementoRigaRemota.toString());
			}
			if (rowFound){
				sheetRow.setSheetRow(indexRow);
				result = sheetRow;
				break;
			}
		}

		return result;
	}
	
	public static SheetRow findSheetRowByLineNumber(String spreadSheetId, String sheetName, SheetRow sheetRow) throws IOException{
		SheetRow result = null;
	
		List<String> ranges = Collections.singletonList(sheetName+"!"+sheetRow.getSheetRow()+":"+sheetRow.getSheetRow());
		
		List<List<Object>> data = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);
		
		if (data != null && !data.isEmpty()){
			List<Object> sheetRowData = data.get(0);
			sheetRow.setData(sheetRowData);
			result = sheetRow;
		}

		return result;
	}
	
	public static void deleteRow(String spreadSheetId, String sheetName, SheetRow sheetRow) throws IOException{
		getGoogleSheetsInstance().deleteRow(spreadSheetId, sheetName, sheetRow.getSheetRow());
	}
	
	public static void appendRows(String spreadSheetId, String sheetName, List<SheetRow> sheetRows) throws IOException{
	
		if (sheetRows != null && !sheetRows.isEmpty()){
			List<List<Object>> rowData = new ArrayList<List<Object>>();
			for (SheetRow row: sheetRows){
				rowData.add(row.getData());
			}
			getGoogleSheetsInstance().appendDataToSheet(spreadSheetId, sheetName, rowData);
		}
	}
	
	public static Integer findMaxIdAnagrafica(String spreadSheetId) throws IOException{
		List<String> ranges = Collections.singletonList(AnagraficaGiocatoreRidottaRow.SHEET_DATA_ANALYSIS_NAME+"!"+"B2");
		
		List<List<Object>> data = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);

		return (Integer) Integer.valueOf((String)data.get(0).get(0));
	}
	
    public static Integer updateRows(String spreadsheetId, String sheetName, List<SheetRow> rows, boolean userEntered) throws IOException{
    	if (rows == null || rows.isEmpty()) return null;
    	
    	List<ValueRange> data = new ArrayList<ValueRange>();
    	for (SheetRow row: rows){
    		String range = sheetName+"!"+row.getSheetRow()+":"+row.getSheetRow();
    		List<List<Object>> values = new ArrayList<List<Object>>();
    		values.add(row.getData());
    		data.add(new ValueRange().setRange(range).setValues(values));
    	}
    	
    	Integer updatedRows = getGoogleSheetsInstance().updateRows(spreadsheetId, sheetName, data, userEntered);
    	
    	return updatedRows;
    }
	
}
