package it.desimone;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
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

public class GDriveQuickStart {
	
	private static final String PROJECT_LOCATION = "C:\\GIT Repositories\\FirstRepo\\GoogleSheetsRemoteAccess\\";
    /** Application name. */
    private static final String APPLICATION_NAME =
        "Drive API Java Quickstart";

    /** Directory to store user credentials for this application. */
   
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
    		PROJECT_LOCATION+"\\resources", ".credentials/drive-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        //InputStream in = GDriveQuickStart.class.getResourceAsStream("/client_secret.json");
    	
    	 InputStream in = new FileInputStream(PROJECT_LOCATION+"/resources//client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService(Credential credential) throws IOException {
        //Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws IOException {
    	extractReportByFolders();
    }
    
    private static void testFileAndFolders() throws IOException{
        Drive service = getDriveService(authorize());

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
        	.setQ("\'0B-WU8eY52U1IdDVqNTVkT2RWd2c\' in parents and mimeType = 'application/vnd.google-apps.folder'")
             .setPageSize(10)
             .setFields("nextPageToken, files(id, name)")
             .execute();
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
                System.out.println(file.getParents());
            }
        }
    }
    
    private static void extractReportByFolders() throws IOException{
        Credential credential = authorize();
        Drive service = getDriveService(credential);

//        File fileMetadata = new File();
//        fileMetadata.setName("photo.jpg");
//        java.io.File filePath = new java.io.File("files/photo.jpg");
//        FileContent mediaContent = new FileContent("image/jpeg", filePath);
//        File file = service.files().create(fileMetadata, mediaContent)
//            .setFields("id")
//            .execute();
//        System.out.println("File ID: " + file.getId());

        
        
        // Print the names and IDs for up to 10 files.
        FileList resultFolders = service.files().list()
        	.setQ("\'0B-WU8eY52U1IdDVqNTVkT2RWd2c\' in parents and mimeType = 'application/vnd.google-apps.folder'")
             //.setFields("files(id, name)")
             .execute();
        List<File> folders = resultFolders.getFiles();
        if (folders == null || folders.size() == 0) {
            System.out.println("No folders found.");
        } else {
            System.out.println("Folders:");
            for (File folder : folders) {
                System.out.printf("%s (%s)\n", folder.getName(), folder.getId());
                FileList resultReports = service.files().list()
                    	.setQ("\'"+folder.getId()+"\' in parents and mimeType = 'application/vnd.google-apps.spreadsheet'")
                         .setFields("files(id, name, mimeType, webContentLink)")
                         .execute();
                List<File> reports = resultReports.getFiles();
                if (reports == null || reports.size() == 0) {
                    System.out.println("No files found.");
                } else {
                    System.out.println("Files:");
                    for (File report : reports) {
                        System.out.printf("%s (%s)\n", report.getName(), report.getId());
                        File newMetadata = new File();
                        newMetadata.setMimeType("application/vnd.google-apps.spreadsheet");
                        service.files().update(report.getId(), newMetadata).execute();
                        System.out.println(report.getMimeType());
                        System.out.println(report.getWebContentLink());
                        System.out.println(report.getProperties());
                        //SheetsQuickstart.leggiGiocatori(credential, report.getId());
                    }
                }
            }
        }
    }

}