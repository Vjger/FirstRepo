package it.desimone.gsheetsaccess.gsheets;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.PermissionList;

import it.desimone.gsheetsaccess.common.ResourceWorking;
import it.desimone.utils.MyException;
import it.desimone.utils.MyLogger;
import it.desimone.utils.ResourceLoader;

public class GoogleDriveAccess {
	
	private Credential credential;

    /** Application name. */
    private static final String APPLICATION_NAME = "RisiKo! Data Manager";

    /** Directory to store user credentials for this application. */
   
    private static final java.io.File DATA_STORE_DIR = ResourceWorking.googleCredentials(); 

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

    public GoogleDriveAccess(){
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

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    private Drive getDriveService() {
        //Credential credential = authorize();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }
    
    
    public PermissionList getFolderPermissionList(String folderId) throws IOException{
    	PermissionList permissionList = null;
        Drive service = getDriveService();

        if (service != null){
        	Drive.Permissions drivePermissions = service.permissions();
        	if (drivePermissions != null){
        		Drive.Permissions.List drivePermissionsList = drivePermissions.list(folderId).setFields("permissions(emailAddress,displayName,domain)");
        		if (drivePermissionsList != null){
        			permissionList = drivePermissionsList.execute();
        		}
        	}
        }

        return permissionList;
    }
    
    public List<File> getSubFolders(String parentFolderId) throws IOException{
    	List<File> folders = null;
        Drive service = getDriveService();

        if (service != null){
        	Drive.Files driveFiles = service.files();
        	if (driveFiles != null){
        		Drive.Files.List driveFilesList = service.files().list();
        		if (driveFilesList != null){
        			driveFilesList = driveFilesList.setQ("\'"+parentFolderId+"\' in parents and mimeType = 'application/vnd.google-apps.folder' and trashed=false");
        			FileList fileList = driveFilesList.execute();
        			if (fileList != null){
        				folders = fileList.getFiles();
        			}
        		}
        	}
        }

        return folders;
    }
    
    public FileList filesIntoFolder(File folder) throws IOException{
    	MyLogger.getLogger().fine("Ricerca dei file nel folder "+folder.getName());
    	FileList fileList = null;
    	Drive service = getDriveService();
    	
        if (service != null){
        	Drive.Files driveFiles = service.files();
        	if (driveFiles != null){
        		Drive.Files.List driveFilesList = service.files().list();
        		if (driveFilesList != null){
        			driveFilesList = driveFilesList.setQ("\'"+folder.getId()+"\' in parents and trashed=false");
        			fileList = driveFilesList.execute();
        		}
        	}
        }
    	return fileList;
    }
    
    public void downloadFile(File file, String folder) throws IOException{
        Drive service = getDriveService();

        if (service != null){
        	Drive.Files driveFiles = service.files();
        	if (driveFiles != null){
        		Drive.Files.Export driveFilesExport = service.files().export(file.getId(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        		if (driveFilesExport != null){
        			java.io.File localFolder = new java.io.File(ResourceWorking.workingAreaPath()+java.io.File.separator+folder);
        			if (!localFolder.exists()){
        				localFolder.mkdir();
        			}
        			java.io.File fileToDownload = new java.io.File(localFolder, file.getName());
        			FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
        			driveFilesExport.executeMediaAndDownloadTo(fileOutputStream);
        		}
        	}
        }
    }

    public File moveFileToNewFolder(String fileId, String newFolderId) throws IOException{
    	File file = null;
        Drive service = getDriveService();

        if (service != null){
        	Drive.Files driveFiles = service.files();
        	if (driveFiles != null){
        		file = driveFiles.get(fileId).setFields("parents").execute();
        		StringBuilder previousParents = new StringBuilder();
	        	for (String parent : file.getParents()) {
	        	  previousParents.append(parent);
	        	  previousParents.append(',');
	        	}
	        	// Move the file to the new folder
	        	file = driveFiles.update(fileId, null)
	        		.setAddParents(newFolderId)
	        	    .setRemoveParents(previousParents.toString())
	        	    .setFields("id, parents")
	        	    .execute();
        	}
        }
        return file;
    }
    
    public File findOrCreateFolderIfNotExists(String parentFolderId, String folderName) throws IOException{
    	File folderFound = null;
    	
    	List<File> subFolders = getSubFolders(parentFolderId);
    	if (subFolders != null && !subFolders.isEmpty()){
	    	for (File file: subFolders){
	    		if(file.getName().equalsIgnoreCase(folderName)){
	    			folderFound = file;
	    			break;
	    		}
	    	}
    	}
    	
    	if (folderFound == null){
    		folderFound = createFolder(parentFolderId, folderName);
    	}
  
        return folderFound;
    }
    
    public File createFolder(String parentFolderId, String folderName) throws IOException{
    	File createdFolder = null;
        Drive service = getDriveService();

        if (service != null){
        	Drive.Files driveFiles = service.files();
        	if (driveFiles != null){
        		File fileMetadata = new File();
        		fileMetadata.setName(folderName);
        		fileMetadata.setMimeType("application/vnd.google-apps.folder");
        		fileMetadata.setParents(Collections.singletonList(parentFolderId));

        		createdFolder = driveFiles.create(fileMetadata).setFields("id").execute();
        	}
        }
        return createdFolder;
    }
}