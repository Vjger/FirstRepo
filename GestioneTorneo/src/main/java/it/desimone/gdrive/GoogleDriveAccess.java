package it.desimone.gdrive;

import it.desimone.utils.Configurator;
import it.desimone.utils.MyException;
import it.desimone.utils.MyLogger;
import it.desimone.utils.ResourceLoader;

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
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveAccess {
	
	private Credential credential;

    /** Application name. */
    private static final String APPLICATION_NAME = "Drive API Java Quickstart";

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
            t.printStackTrace();
            System.exit(1);
        }
    }

    public GoogleDriveAccess(){
    	try {
			this.credential = authorize();
		} catch (IOException e) {
			MyLogger.getLogger().severe("Credenziali errate per l'accesso a Google: "+e);
			throw new MyException(e, "Credenziali errate per l'accesso a Google: verificare la presenza del file "+ResourceLoader.googleClientSecretPath());
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

    		InputStream in = new FileInputStream(ResourceLoader.googleClientSecretPath());
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

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    private Drive getDriveService() {
        //Credential credential = authorize();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }
    
    public void uploadReportOnAvailablesFolders(java.io.File excelReport){
    	String parentFolderId = Configurator.getRCUFolderId();
    	
    	if (parentFolderId != null){
    		try{
    			List<File> availableFolders = getClubFolders(parentFolderId);
    			if (availableFolders != null && !availableFolders.isEmpty()){
    				List<File> uploadedFiles = uploadClubReport(availableFolders, excelReport);
    				if (uploadedFiles != null){
    					MyLogger.getLogger().fine("Caricato il file "+excelReport.getName()+" in "+uploadedFiles.size()+" folder");
    				}
    			}else{
    				MyLogger.getLogger().severe("Non è stato abilitato alcun folder di Google Drive alle credenziali in uso");
    				throw new MyException("Non è stato abilitato alcun folder di Google Drive alle credenziali in uso");
    			}
    		}catch(IOException ioe){
    			MyLogger.getLogger().severe(ioe.getMessage());
        		throw new MyException(ioe);
    		}
    	}else{
    		MyLogger.getLogger().severe("Non è stato trovato l'ID del folder genitore di Google Drive");
    		throw new MyException("Non è stato trovato l'ID del folder genitore di Google Drive");
    	}
    }

    private List<File> getClubFolders(String parentFolderId) throws IOException{
    	List<File> folders = null;
        Drive service = getDriveService();

        if (service != null){
        	Drive.Files driveFiles = service.files();
        	if (driveFiles != null){
        		Drive.Files.List driveFilesList = service.files().list();
        		if (driveFilesList != null){
        			driveFilesList = driveFilesList.setQ("\'"+parentFolderId+"\' in parents and mimeType = 'application/vnd.google-apps.folder' and sharedWithMe=true");
        			FileList fileList = driveFilesList.execute();
        			if (fileList != null){
        				folders = fileList.getFiles();
        			}
        		}
        	}
        }

        return folders;
    }
    
    public List<File> uploadClubReport(List<File> clubFolders, java.io.File report) throws IOException{
    	List<File> uploadedFiles = null;
        Drive service = getDriveService();
        
        if (clubFolders != null && !clubFolders.isEmpty()){
        	uploadedFiles = new ArrayList<File>();
        	for (File clubFolder: clubFolders){
		        File fileMetadata = new File();
		        fileMetadata.setName(report.getName());
		        fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");   
		        
		        FileContent mediaContent = new FileContent("application/vnd.ms-excel", report);
		        
		        String fileId = fileExistsIntoFolder(clubFolder, report.getName());
		        
	        	Drive.Files driveFiles = service.files();
		        if (fileId != null){
		        	File uploadedFile = driveFiles.update(fileId, fileMetadata, mediaContent).setFields("id, parents").execute();
		        	MyLogger.getLogger().fine("Aggiornato file "+report.getName()+" con id "+uploadedFile.getId()+" nel folder "+clubFolder.getName()+"-"+clubFolder.getId());
		        	uploadedFiles.add(uploadedFile);
		        }else{	        
			        fileMetadata.setParents(Collections.singletonList(clubFolder.getId()));
		        	File uploadedFile = driveFiles.create(fileMetadata, mediaContent).setFields("id, parents").execute();
		        	MyLogger.getLogger().fine("Inserito file "+report.getName()+" con id "+uploadedFile.getId()+" nel folder "+clubFolder.getName()+"-"+clubFolder.getId());
		        	uploadedFiles.add(uploadedFile);
	        	}
        	}
        }
        return uploadedFiles;
    }
    
    private String fileExistsIntoFolder(File folder, String reportName) throws IOException{
    	String fileId = null;
    	Drive service = getDriveService();
    	
        if (service != null){
        	Drive.Files driveFiles = service.files();
        	if (driveFiles != null){
        		Drive.Files.List driveFilesList = service.files().list();
        		if (driveFilesList != null){
        			driveFilesList = driveFilesList.setQ("\'"+folder.getId()+"\' in parents and name=\'"+reportName+"\' and trashed=false");
        			FileList fileList = driveFilesList.execute();
        			if (fileList != null && fileList.getFiles() != null && !fileList.getFiles().isEmpty()){
        				fileId = fileList.getFiles().get(0).getId();
        			}
        		}
        	}
        }
    	return fileId;
    }
}