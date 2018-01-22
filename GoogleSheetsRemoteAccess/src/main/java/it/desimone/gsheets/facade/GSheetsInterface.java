package it.desimone.gsheets.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.desimone.gsheets.GoogleSheetsAccess;
import it.desimone.gsheets.dto.SheetRow;

public class GSheetsInterface {

	
	public static void main(String s[]){
		System.out.println(toAlphabetic(0));
		System.out.println(toAlphabetic(1));
		System.out.println(toAlphabetic(27));
	}
	
	public static String toAlphabetic(int i) {
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
			if (keyColumns!= null && keyColumns.isEmpty()){
				for (Integer keyColumn: keyColumns){
					ranges.add(sheetName+"!"+toAlphabetic(keyColumn-1));
				}
			}else{
				ranges.add(sheetName);	
			}
		}
		return ranges;
	}
	
	public static SheetRow findSheetRow(String spreadSheetId, String sheetName, SheetRow sheetRow){
		SheetRow result = null;
		GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
		
		List<Integer> keyCols = sheetRow.keyCols();
		
		List<String> ranges = byKeyColumnsToRanges(sheetName, keyCols);
		
		try {
			List<List<Object>> data = googleSheetsAccess.leggiSheet(spreadSheetId, ranges);
			
			List<Object> dataSheetRow = sheetRow.getData();
			int indexRow = 0;
			for (List<Object> row: data){
				indexRow++;
				boolean rowFound = true;
				for (int i=0; i < keyCols.size(); i++){
					rowFound = rowFound && row.get(i).equals(dataSheetRow.get(keyCols.get(i)));
				}
				if (rowFound){
					sheetRow.setSheetRow(indexRow);
					result = sheetRow;
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
}
