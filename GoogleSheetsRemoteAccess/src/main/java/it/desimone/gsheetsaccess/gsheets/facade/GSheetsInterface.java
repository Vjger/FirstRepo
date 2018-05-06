package it.desimone.gsheetsaccess.gsheets.facade;

import it.desimone.gheetsaccess.gsheets.dto.AbstractSheetRow;
import it.desimone.gheetsaccess.gsheets.dto.AnagraficaGiocatoreRidottaRow;
import it.desimone.gheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gheetsaccess.gsheets.dto.PartitaRow;
import it.desimone.gheetsaccess.gsheets.dto.SheetRow;
import it.desimone.gheetsaccess.gsheets.dto.SheetRowFactory;
import it.desimone.gheetsaccess.gsheets.dto.TorneiRow;
import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.googleaccess.GoogleSheetsAccess;
import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.services.sheets.v4.model.ValueRange;

public class GSheetsInterface {

	private static GoogleSheetsAccess googleSheetAccess;
	
	public static GoogleSheetsAccess getGoogleSheetsInstance(){
		if (googleSheetAccess == null){
			googleSheetAccess = new GoogleSheetsAccess();
		}
		return googleSheetAccess;
	}
	
	public static void main(String[]s){
		for (int i = 1; i <= 30; i++){
			System.out.println(i+": "+toAlphabetic(i));
		}
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
			
		List<Integer> rangeCols = Arrays.asList(searchCols);
		rangeCols.add(sheetRow.getSheetRowNumberColPosition());
		List<String> ranges = byKeyColumnsToRanges(sheetName, rangeCols);
		
		List<List<Object>> data = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);
		
		List<Object> dataSheetRow = sheetRow.getData();
		int indexRow = 0;
		for (List<Object> row: data){
			indexRow++;
			boolean rowFound = true;
			for (int i=0; i < searchCols.length && rowFound; i++){
				Object elementoRigaRemota = row.get(i);
				Object elementoRigaInCanna = dataSheetRow.get(searchCols[i]);
				rowFound = rowFound && elementoRigaInCanna.toString().trim().equalsIgnoreCase(elementoRigaRemota.toString().trim());
			}
			if (rowFound){
				if (result == null){
					result = new ArrayList<SheetRow>();
				}
				SheetRow sRow;
				try {
					sRow = (SheetRow) sheetRow.clone();
					//sRow.setSheetRowNumber(indexRow);
					sRow.setSheetRowNumber((Integer)row.get(row.size() -1));
					result.add(sRow);
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	
	
	public static Integer findNumTorneoRowByIdTorneo(String spreadSheetId, String sheetName, SheetRow sheetRow) throws IOException{
		Integer result = null;
		String query = getQueryTorneo(((TorneiRow) sheetRow).getIdTorneo());
		List<Integer> numRows = findNumRowsByIdTorneo(spreadSheetId, sheetRow, query);
		if (numRows != null){
			result = numRows.get(0);
		}
		return result;
	}
	
	public static List<Integer> findNumPartiteRowsByIdTorneo(String spreadSheetId, String sheetName, SheetRow sheetRow) throws IOException{
		String query = getQueryPartiteTorneo(((PartitaRow) sheetRow).getIdTorneo());
		List<Integer> numRows = findNumRowsByIdTorneo(spreadSheetId, sheetRow, query);
		return numRows;
	}
	
	public static List<Integer> findClassificaRowsByIdTorneo(String spreadSheetId, String sheetName, SheetRow sheetRow) throws IOException{
		String query = getQueryClassificaTorneo(((ClassificheRow) sheetRow).getIdTorneo());
		List<Integer> numRows = findNumRowsByIdTorneo(spreadSheetId, sheetRow, query);
		return numRows;
	}
	
	
	private static List<Integer> findNumRowsByIdTorneo(String spreadSheetId, SheetRow sheetRow, String query) throws IOException{
    	List<ValueRange> data = new ArrayList<ValueRange>();

    	String sheetNameDataAnalysis = AbstractSheetRow.SHEET_DATA_ANALYSIS_NAME;
    	
		List<List<Object>> values = new ArrayList<List<Object>>();

		String rangeRicerca = sheetNameDataAnalysis+"!A1:A1";

		List<Object> rigaFormula = Arrays.asList(new Object[]{query});
		values.add(rigaFormula);
		
		data.add(new ValueRange().setRange(rangeRicerca).setValues(values));
    	Integer updatedRows = getGoogleSheetsInstance().updateRows(spreadSheetId, data, true);
		String columnLetterNumRows = toAlphabetic(sheetRow.getSheetRowNumberColPosition());
		List<String> ranges = Collections.singletonList(AbstractSheetRow.SHEET_DATA_ANALYSIS_NAME+"!"+columnLetterNumRows+":"+columnLetterNumRows);
		
		List<List<Object>> queryResponses = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);
		
		List<Integer> numRows = null;
		if (queryResponses != null && !queryResponses.isEmpty()){
			numRows = new ArrayList<Integer>();
			for (List<Object> queryResponse: queryResponses){
				String valueQuery = (String)queryResponse.get(0);
				try{
					Integer numRow = Integer.valueOf(valueQuery);
					numRows.add(numRow);
				}catch(NumberFormatException ne){
					MyLogger.getLogger().info("Not found: "+valueQuery);
				}
			}
		}

		getGoogleSheetsInstance().clearRows(spreadSheetId, Collections.singletonList(rangeRicerca));
		return numRows;
	}
	
	public static void clearSheet(String spreadSheetId, String sheetName) throws IOException{
		List<ValueRange> data = Collections.singletonList(new ValueRange().setRange(sheetName));
		//getGoogleSheetsInstance().updateRows(spreadSheetId, data, true);
		
		getGoogleSheetsInstance().clearRows(spreadSheetId, Collections.singletonList(sheetName));
	}
	
	public static SheetRow findSheetRowByKey(String spreadSheetId, String sheetName, SheetRow sheetRow) throws IOException{
		SheetRow result = null;
		
		List<Integer> keyCols = sheetRow.keyCols();
		
		List<Integer> rangeCols = new ArrayList<Integer>(keyCols);
		rangeCols.add(sheetRow.getSheetRowNumberColPosition());
		List<String> ranges = byKeyColumnsToRanges(sheetName, rangeCols);
		
		List<List<Object>> data = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);
		
		List<Object> dataSheetRow = sheetRow.getData();
		int indexRow = 0;
		for (List<Object> row: data){
			indexRow++;
			boolean rowFound = true;
			for (int i=0; i < keyCols.size() && rowFound; i++){
				Object elementoRigaRemota = row.get(i);
				Object elementoRigaInCanna = dataSheetRow.get(keyCols.get(i));
				rowFound = rowFound && elementoRigaInCanna.toString().trim().equalsIgnoreCase(elementoRigaRemota.toString().trim());
			}
			if (rowFound){
				//sheetRow.setSheetRowNumber(indexRow);
				sheetRow.setSheetRowNumber(Integer.valueOf((String)row.get(row.size() -1)));
				result = sheetRow;
				break;
			}
		}

		return result;
	}
	
	public static List<AnagraficaGiocatoreRidottaRow> findAnagraficheRidotteByKey(String spreadSheetId, String sheetName, List<AnagraficaGiocatoreRidottaRow> sheetRows) throws IOException{
    	List<ValueRange> data = new ArrayList<ValueRange>();

    	String sheetNameDataAnalysis = AnagraficaGiocatoreRidottaRow.SHEET_DATA_ANALYSIS_NAME;
    	
		List<List<Object>> values = new ArrayList<List<Object>>();
    	int indexStartingRow = 8;
    	int numeroRiga = indexStartingRow;
		String rangeRicerca = sheetNameDataAnalysis+"!A"+indexStartingRow+":D"+(indexStartingRow+sheetRows.size()-1);
    	for (AnagraficaGiocatoreRidottaRow sheetRow: sheetRows){
			List<Object> rigaRicerca = Arrays.asList(new Object[]{sheetRow.getNome(), sheetRow.getCognome(), sheetRow.getEmail(), getQueryAnagraficaRidotta(numeroRiga)});
			values.add(rigaRicerca);
			numeroRiga++;
    	}
		data.add(new ValueRange().setRange(rangeRicerca).setValues(values));
    	Integer updatedRows = getGoogleSheetsInstance().updateRows(spreadSheetId, data, true);
		
    	String range = AnagraficaGiocatoreRidottaRow.SHEET_DATA_ANALYSIS_NAME+"!"+"D"+indexStartingRow+":D"+(indexStartingRow+sheetRows.size()-1);
		List<String> ranges = Collections.singletonList(range);
		
		List<List<Object>> queryResponses = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);
		
		if (queryResponses != null && !queryResponses.isEmpty()){
			int index = 0;
			for (List<Object> queryResponse: queryResponses){
				String valueQuery = (String)queryResponse.get(0);
				try{
					Integer idAnagrafica = Integer.valueOf(valueQuery);
					sheetRows.get(index).setId(idAnagrafica);
				}catch(NumberFormatException ne){
					MyLogger.getLogger().info("Not found: "+valueQuery);
				}
				index++;
			}
		}
		
		getGoogleSheetsInstance().clearRows(spreadSheetId, Collections.singletonList(rangeRicerca));
		
		return sheetRows;
	}
	
	public static List<SheetRow> findAnagraficheByKey(String spreadSheetId, String sheetName, List<SheetRow> sheetRows) throws IOException{
    	List<ValueRange> data = new ArrayList<ValueRange>();

    	String sheetNameDataAnalysis = AbstractSheetRow.SHEET_DATA_ANALYSIS_NAME;
    	
		List<List<Object>> values = new ArrayList<List<Object>>();
    	int indexStartingRow = 1;
    	int numeroRiga = indexStartingRow;
		String rangeRicerca = sheetNameDataAnalysis+"!A"+indexStartingRow+":B"+(indexStartingRow+sheetRows.size()-1);
    	for (SheetRow sheetRow: sheetRows){
			List<Object> rigaRicerca = Arrays.asList(new Object[]{((AnagraficaGiocatoreRow) sheetRow).getId(), getQueryAnagrafica(numeroRiga)});
			values.add(rigaRicerca);
			numeroRiga++;
    	}
		data.add(new ValueRange().setRange(rangeRicerca).setValues(values));
    	Integer updatedRows = getGoogleSheetsInstance().updateRows(spreadSheetId, data, true);
		String columnLetterNumRows = toAlphabetic(sheetRows.get(0).getSheetRowNumberColPosition()+1);
		List<String> ranges = Collections.singletonList(AbstractSheetRow.SHEET_DATA_ANALYSIS_NAME+"!"+columnLetterNumRows+indexStartingRow+":"+columnLetterNumRows+(indexStartingRow+sheetRows.size()-1));
		
		List<List<Object>> queryResponses = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);
		
		if (queryResponses != null && !queryResponses.isEmpty()){
			int index = 0;
			for (List<Object> queryResponse: queryResponses){
				String valueQuery = (String)queryResponse.get(0);
				try{
					Integer numRow = Integer.valueOf(valueQuery);
					sheetRows.get(index).setSheetRowNumber(numRow);
				}catch(NumberFormatException ne){
					MyLogger.getLogger().info("Not found: "+valueQuery);
				}
				index++;
			}
		}

		getGoogleSheetsInstance().clearRows(spreadSheetId, Collections.singletonList(rangeRicerca));
		
		return sheetRows;
	}
	
	private static String getQueryTorneo(String idTorneo){
		StringBuilder buffer = new StringBuilder();
		buffer.append("=FILTER(");
		buffer.append(TorneiRow.SHEET_TORNEI_NAME + "!A2:");
		buffer.append(toAlphabetic(new TorneiRow().getDataSize() -1)+";");
		buffer.append(TorneiRow.SHEET_TORNEI_NAME + "!A2:A = ");
		buffer.append("\""+idTorneo+"\"");
		buffer.append(")");
		return buffer.toString();
	}
	
	private static String getQueryClassificaTorneo(String idTorneo){
		StringBuilder buffer = new StringBuilder();
		buffer.append("=FILTER(");
		buffer.append(ClassificheRow.SHEET_CLASSIFICHE + "!A2:");
		buffer.append(toAlphabetic(new ClassificheRow().getDataSize() -1)+";");
		buffer.append(ClassificheRow.SHEET_CLASSIFICHE + "!A2:A = ");
		buffer.append("\""+idTorneo+"\"");
		buffer.append(")");
		return buffer.toString();
	}
	
	private static String getQueryPartiteTorneo(String idTorneo){
		//String query = "=QUERY("+AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME+"!A2:G;ʺSELECT G WHERE A = 'ʺ&A"+numeroRiga+"&ʺ'ʺ; -1)";
		String query = "=FILTER("+PartitaRow.SHEET_PARTITE_NAME+"!A2:V; "+PartitaRow.SHEET_PARTITE_NAME+"!A2:A = \""+idTorneo+"\")";
		return query;
	}
	
	private static String getQueryAnagrafica(Integer numeroRiga){
		//String query = "=QUERY("+AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME+"!A2:G;ʺSELECT G WHERE A = 'ʺ&A"+numeroRiga+"&ʺ'ʺ; -1)";
		String query = "=FILTER("+AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME+"!A2:G; "+AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME+"!A2:A = A"+numeroRiga+")";
		return query;
	}
	
	private static String getQueryAnagraficaRidotta(Integer numeroRiga){
		//String query = "=QUERY(ANAGRAFICA!A2:E;ʺSELECT A WHERE upper(B) = upper('ʺ&A"+numeroRiga+"&ʺ') AND upper(C) = upper('ʺ&B"+numeroRiga+"&ʺ') AND upper(D) = upper('ʺ&C"+numeroRiga+"&ʺ')ʺ; -1)";
		return "=FILTER(ANAGRAFICA!A2:F; upper(ANAGRAFICA!B2:B) = upper(A"+numeroRiga+"); upper(ANAGRAFICA!C2:C) = upper(B"+numeroRiga+"); upper(ANAGRAFICA!D2:D) = upper(C"+numeroRiga+"))";
	}
	
	public static SheetRow findSheetRowByLineNumber(String spreadSheetId, String sheetName, SheetRow sheetRow) throws IOException{
		SheetRow result = null;
	
		List<String> ranges = Collections.singletonList(sheetName+"!"+sheetRow.getSheetRowNumber()+":"+sheetRow.getSheetRowNumber());
		
		List<List<Object>> data = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);
		
		if (data != null && !data.isEmpty()){
			List<Object> sheetRowData = data.get(0);
			sheetRow.setData(sheetRowData);
			result = sheetRow;
		}

		return result;
	}
	
	public static void deleteRow(String spreadSheetId, String sheetName, SheetRow sheetRow) throws IOException{
		getGoogleSheetsInstance().deleteRow(spreadSheetId, sheetName, sheetRow.getSheetRowNumber());
	}
	
	public static void deleteRows(String spreadSheetId, String sheetName, List<SheetRow> sheetRows) throws IOException{
		List<Integer> numRows = new ArrayList<Integer>();
		for (SheetRow row: sheetRows){
			numRows.add(row.getSheetRowNumber());
		}
		getGoogleSheetsInstance().deleteRows(spreadSheetId, sheetName, numRows);
	}
	
	public static void deleteRowsByNumRow(String spreadSheetId, String sheetName, List<Integer> sheetRows) throws IOException{
		getGoogleSheetsInstance().deleteRows(spreadSheetId, sheetName, sheetRows);
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
	
	public static Integer findIdAnagraficaVerificato(String spreadSheetId) throws IOException{
		Integer result = null;
		List<String> ranges = Collections.singletonList(AnagraficaGiocatoreRidottaRow.SHEET_DATA_ANALYSIS_NAME+"!"+"B5");
		
		List<List<Object>> data = getGoogleSheetsInstance().leggiSheet(spreadSheetId, ranges);

		if (data != null && !data.isEmpty() && data.get(0) != null && !data.get(0).isEmpty()){
			String value = (String)data.get(0).get(0);
			try{
				result = Integer.valueOf((String)data.get(0).get(0));
			}catch(NumberFormatException ne){
				MyLogger.getLogger().info("Not found: "+value);
			}
		}
		return result;
	}
	
    public static Integer updateRows(String spreadsheetId, String sheetName, List<SheetRow> rows, boolean userEntered) throws IOException{
    	if (rows == null || rows.isEmpty()) return null;
    	
    	List<ValueRange> data = new ArrayList<ValueRange>();
    	for (SheetRow row: rows){
    		String range = sheetName+"!"+row.getSheetRowNumber()+":"+row.getSheetRowNumber();
    		List<List<Object>> values = new ArrayList<List<Object>>();
    		values.add(row.getData());
    		data.add(new ValueRange().setRange(range).setValues(values));
    	}
    	
    	Integer updatedRows = getGoogleSheetsInstance().updateRows(spreadsheetId, data, userEntered);
    	
    	return updatedRows;
    }
	
    
	public static <T> List<T> getAllRows(String spreadSheetId, SheetRowFactory.SheetRowType sheetRowType) throws IOException{
		List<T> result = null;
		GoogleSheetsAccess googleSheetsAccess = getGoogleSheetsInstance();

		String sheetName = SheetRowFactory.getSheetName(sheetRowType);
		List<String> ranges = Collections.singletonList(sheetName+"!A2:"+toAlphabetic(SheetRowFactory.create(sheetRowType).getDataSize()));

		List<List<Object>> sheetRows = googleSheetsAccess.leggiSheet(spreadSheetId, ranges);

		if (sheetRows != null && !sheetRows.isEmpty()){
			result = new ArrayList<T>();
			for (List<Object> sheetRow: sheetRows){
				SheetRow row = SheetRowFactory.create(sheetRowType);
				row.setData(sheetRow);
				
				result.add((T) row);
			}
		}
		return result;
	}

}
