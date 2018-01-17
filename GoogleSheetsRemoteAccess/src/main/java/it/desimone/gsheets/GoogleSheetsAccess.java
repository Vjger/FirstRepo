package it.desimone.gsheets;

import it.desimone.ResourceWorking;
import it.desimone.utils.MyException;
import it.desimone.utils.MyLogger;
import it.desimone.utils.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheetsAccess {

	private Credential credential;

    /** Application name. */
    private static final String APPLICATION_NAME = "RisiKo! Data Manager";

    /** Directory to store user credentials for this application. */
   
    private static final java.io.File DATA_STORE_DIR = ResourceLoader.googleCredentials();

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

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

    		// Build flow and trigger user authorization request.
    		GoogleAuthorizationCodeFlow flow =
    				new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
    				.setDataStoreFactory(DATA_STORE_FACTORY)
    				.setAccessType("offline")
    				.build();
    		credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    		
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
        
        MyLogger.getLogger().info(spreadSheetsGet.getFields());
        
        spreadSheetsGet = spreadSheetsGet.setIncludeGridData(false);
        
        MyLogger.getLogger().info(spreadSheetsGet.getFields());
        
	    Spreadsheet response1= spreadSheetsGet.execute();
	
	    return response1.getSheets();
    }
    
    
    public List<List<Object>> leggiSheet(String spreadsheetId, String sheetName) throws IOException{
        
        Sheets service = getSheetsService();

        Sheets.Spreadsheets.Values.Get spreadSheetsValuesGet = service.spreadsheets().values().get(spreadsheetId, sheetName);
        MyLogger.getLogger().info(spreadSheetsValuesGet.getFields());
        
        ValueRange response = spreadSheetsValuesGet.execute();
        return response.getValues();
    }
    
    public List<List<Object>> leggiSheet(String spreadsheetId, List<String> ranges) throws IOException{
        
        Sheets service = getSheetsService();

        Sheets.Spreadsheets.Values.BatchGet spreadSheetsValuesBatchGet = service.spreadsheets().values().batchGet(spreadsheetId).setRanges(ranges);
        MyLogger.getLogger().info(spreadSheetsValuesBatchGet.getFields());
        
        BatchGetValuesResponse response = spreadSheetsValuesBatchGet.execute();
        List<ValueRange> valueRanges = response.getValueRanges();
        
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
}
