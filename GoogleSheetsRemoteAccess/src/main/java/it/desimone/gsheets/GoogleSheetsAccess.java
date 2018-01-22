package it.desimone.gsheets;

import it.desimone.ResourceWorking;
import it.desimone.gsheets.dto.SheetRow;
import it.desimone.utils.MyException;
import it.desimone.utils.MyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesByDataFilterRequest;
import com.google.api.services.sheets.v4.model.BatchGetValuesByDataFilterResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.DataFilter;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.MatchedValueRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheetsAccess {

	private static final String USER_ENTERED = "USER_ENTERED";
	private static final String RAW = "RAW";
	
	private Credential credential;

    /** Application name. */
    private static final String APPLICATION_NAME = "RisiKo! Data Manager";

    /** Directory to store user credentials for this application. */
   
    private static final java.io.File DATA_STORE_DIR = new File(System.getProperty("java.io.tmpdir")); //ResourceLoader.googleCredentials();

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static DataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
        //Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);
    	Arrays.asList(DriveScopes.DRIVE); 

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
    		MyLogger.getLogger().severe("Problema con l'accesso a Google: "+t);
        }
    }

    public GoogleSheetsAccess(){
    	try {
			this.credential = authorize();
		} catch (IOException e) {
			MyLogger.getLogger().severe("Credenziali errate per l'accesso a Google: "+e);
			throw new MyException(e, "Credenziali errate per l'accesso a Google");
		}
    }
    
    
    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    private Credential authorize() throws IOException {
    	// Load client secrets.
    	//InputStream in = GDriveQuickStart.class.getResourceAsStream("/client_secret.json");

    	Credential credential = null;

    		InputStream in = new FileInputStream(ResourceWorking.googleClientSecretPath());
    		
    		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    		MyLogger.getLogger().info(clientSecrets.getDetails().toPrettyString());
    		
    		// Build flow and trigger user authorization request.
    		GoogleAuthorizationCodeFlow flow =
    				new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
    				.setDataStoreFactory(DATA_STORE_FACTORY)
    				.setAccessType("offline")
    				.build();
    		credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    		
    		MyLogger.getLogger().info(credential.getAccessToken()+" "+credential.getRefreshToken()+" "+credential.getExpirationTimeMilliseconds()+" "+credential.getExpiresInSeconds());
    		
    		MyLogger.getLogger().info("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());

        return credential;
    }

    public Sheets getSheetsService() throws IOException {
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    
    public List<Sheet> elencoSheets(String spreadsheetId) throws IOException{
        
        Sheets service = getSheetsService();
        
        Sheets.Spreadsheets.Get spreadSheetsGet = service.spreadsheets().get(spreadsheetId);
        
        spreadSheetsGet = spreadSheetsGet.setIncludeGridData(false);
        
	    Spreadsheet response1= spreadSheetsGet.execute();
	    
	    List<Sheet> sheets = response1.getSheets();
	
	    for (Sheet sheet: sheets){
	    	MyLogger.getLogger().info(sheet.getProperties().toPrettyString());
	    }
	    
	    return sheets;
    }
    
    
    public List<List<Object>> leggiSheet(String spreadsheetId, String sheetName) throws IOException{
        
        Sheets service = getSheetsService();

        Sheets.Spreadsheets.Values.Get spreadSheetsValuesGet = service.spreadsheets().values().get(spreadsheetId, sheetName);
        
        ValueRange response = spreadSheetsValuesGet.execute();
        return response.getValues();
    }
    
    public List<List<Object>> leggiSheet(String spreadsheetId, List<String> ranges) throws IOException{
        
        Sheets service = getSheetsService();

        Sheets.Spreadsheets.Values.BatchGet spreadSheetsValuesBatchGet = service.spreadsheets().values().batchGet(spreadsheetId).setRanges(ranges);
        
        BatchGetValuesResponse response = spreadSheetsValuesBatchGet.execute();
        List<ValueRange> valueRanges = response.getValueRanges();
        
    	MyLogger.getLogger().fine(valueRanges.toString());
        
        List<List<Object>> result = null;
        if (valueRanges != null && !valueRanges.isEmpty()){
	        result = new ArrayList<List<Object>>();
	        
	        for (int i = 0; i < valueRanges.get(0).getValues().size(); i++){
	        	
	        	List<Object> row = new ArrayList<Object>();
		        for (ValueRange range: valueRanges){	        	
		        	row.addAll(range.getValues().get(i));
		        }
		        result.add(row);
	        }
        }
        return result;
    }
    
    public List<List<Object>> findRow(String spreadsheetId, List<String> ranges) throws IOException{
        
        Sheets service = getSheetsService();

        Sheets.Spreadsheets.Values spreadSheetValues = service.spreadsheets().values();
        
        BatchGetValuesByDataFilterRequest batchGetValuesByDataFilterRequest = new BatchGetValuesByDataFilterRequest();

        List<DataFilter> dataFilters = new ArrayList<DataFilter>();
        for (String range: ranges){
            DataFilter dataFilter = new DataFilter();
            dataFilter.setA1Range(range);
        	dataFilters.add(dataFilter);
        }
        batchGetValuesByDataFilterRequest.setDataFilters(dataFilters);
        Sheets.Spreadsheets.Values.BatchGetByDataFilter batchGetByDataFilter = spreadSheetValues.batchGetByDataFilter(spreadsheetId, batchGetValuesByDataFilterRequest);
               
        BatchGetValuesByDataFilterResponse response = batchGetByDataFilter.execute();
        List<MatchedValueRange> matchedValueRanges = response.getValueRanges();
        
        List<List<Object>> result = null;
        if (matchedValueRanges != null && !matchedValueRanges.isEmpty()){
	        result = new ArrayList<List<Object>>();
        	for (MatchedValueRange matchedValueRange: matchedValueRanges){

			        	List<Object> row = new ArrayList<Object>();
       	
				        	row.addAll(matchedValueRange.getValueRange().getValues());

				        result.add(row);
			        }
        	}
        return result;
    }
    
    public List<List<Object>> findData(String spreadsheetId, Integer sheetId, Integer firstColumn, Integer lastColumn, Integer firstRow, Integer lastRow) throws IOException{
        
        Sheets service = getSheetsService();

        Sheets.Spreadsheets.Values spreadSheetValues = service.spreadsheets().values();
        
        BatchGetValuesByDataFilterRequest batchGetValuesByDataFilterRequest = new BatchGetValuesByDataFilterRequest();
        DataFilter dataFilter = new DataFilter();
        GridRange gridRange = new GridRange();
        gridRange.setSheetId(sheetId);
        if (firstColumn != null)
        	gridRange.setStartColumnIndex(firstColumn-1);
        if (lastColumn != null)
        	gridRange.setEndColumnIndex(lastColumn);
        if (firstRow != null)
        	gridRange.setStartRowIndex(firstRow-1);
        if (lastRow != null)
        	gridRange.setEndRowIndex(lastRow);
        dataFilter.setGridRange(gridRange);
        List<DataFilter> dataFilters = new ArrayList<DataFilter>();
        dataFilters.add(dataFilter);
        batchGetValuesByDataFilterRequest.setDataFilters(dataFilters);
        Sheets.Spreadsheets.Values.BatchGetByDataFilter batchGetByDataFilter = spreadSheetValues.batchGetByDataFilter(spreadsheetId, batchGetValuesByDataFilterRequest);
               
        BatchGetValuesByDataFilterResponse response = batchGetByDataFilter.execute();
        List<MatchedValueRange> matchedValueRanges = response.getValueRanges();
        
        List<List<Object>> result = null;
        if (matchedValueRanges != null && !matchedValueRanges.isEmpty()){
	        result = new ArrayList<List<Object>>();
        	for (MatchedValueRange matchedValueRange: matchedValueRanges){

			        	List<Object> row = new ArrayList<Object>();
       	
				        	row.addAll(matchedValueRange.getValueRange().getValues());

				        result.add(row);
			        }
        	}
        return result;
    }
    
    public void appendDataToSheet(String spreadsheetId, String sheetName, List<List<Object>> data) throws IOException{
        
        Sheets service = getSheetsService();

        ValueRange valueRange = new ValueRange();
        valueRange.setRange(sheetName);
        valueRange.setValues(data);
        
        Sheets.Spreadsheets.Values.Append spreadSheetsValuesAppend = service.spreadsheets().values().append(spreadsheetId, sheetName, valueRange);
        
        spreadSheetsValuesAppend = spreadSheetsValuesAppend.setValueInputOption(USER_ENTERED);

        AppendValuesResponse response = spreadSheetsValuesAppend.execute();
        
        MyLogger.getLogger().fine(response.getUpdates().toPrettyString());

    }
    
    
    public void deleteRow(String spreadsheetId, Integer sheetId, Integer numRow) throws IOException{
        
        Sheets service = getSheetsService();

        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
        DeleteDimensionRequest deleteDimensionRequest = new DeleteDimensionRequest();
        DimensionRange dimensionRange = new DimensionRange();
        dimensionRange.setDimension("ROWS");
        dimensionRange.setSheetId(sheetId);
        dimensionRange.setStartIndex(numRow -1);
        dimensionRange.setEndIndex(numRow);
        deleteDimensionRequest.setRange(dimensionRange);
        Request deleteRequest = new Request();
        deleteRequest.setDeleteDimension(deleteDimensionRequest);
        batchUpdateSpreadsheetRequest.setRequests(Collections.singletonList(deleteRequest));
        
        Sheets.Spreadsheets.BatchUpdate batchUpdate = service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest);
        
        BatchUpdateSpreadsheetResponse response = batchUpdate.execute();
        
        MyLogger.getLogger().fine(response.getReplies().toString());

    }
    
    public Integer updateRows(String spreadsheetId, String sheetName, List<SheetRow> rows, boolean userEntered) throws IOException{
    	if (rows == null || rows.isEmpty()) return null;
    	
    	List<ValueRange> data = new ArrayList<ValueRange>();
    	for (SheetRow row: rows){
    		String range = sheetName+"!"+row.getSheetRow();
    		List<List<Object>> values = new ArrayList<List<Object>>();
    		values.add(row.getData());
    		data.add(new ValueRange().setRange(range).setValues(values));
    	}
    	
        Sheets service = getSheetsService();
        String valueInputOption = userEntered?USER_ENTERED:RAW;
    	BatchUpdateValuesRequest body = new BatchUpdateValuesRequest().setValueInputOption(valueInputOption).setData(data);
    	BatchUpdateValuesResponse result = service.spreadsheets().values().batchUpdate(spreadsheetId, body).execute();

    	MyLogger.getLogger().fine(result.toPrettyString());
    	
    	return result.getTotalUpdatedRows();
    }
}
