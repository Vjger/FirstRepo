package it.desimone.gdrive.file;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;

import it.desimone.ResourceWorking;
import it.desimone.gsheets.GoogleDriveAccess;
import it.desimone.utils.MyException;
import it.desimone.utils.MyLogger;

public class GDriveDownloader {

	public static void main(String[] args) {

	}
	
	private static List<ReportDriveData> downloadReport(){
		List<ReportDriveData> result = new ArrayList<ReportDriveData>();
		
		GoogleDriveAccess googleDriveAccess = new GoogleDriveAccess();

    	String parentFolderId = ResourceWorking.RCU_FOLDER_ID;
    	
    	if (parentFolderId != null){
    		try{
    			List<File> availableFolders = googleDriveAccess.getClubFolders(parentFolderId);
    			if (availableFolders != null && !availableFolders.isEmpty()){
    				for (File folder: availableFolders){
    					MyLogger.getLogger().info("Folder "+folder.getName());
    					PermissionList permissionList = googleDriveAccess.getFolderPermissionList(folder.getId()); 
    					List<Permission> permissions = permissionList.getPermissions();
    					List<String> emailAddresses = null;
    					if (permissions != null){
    						emailAddresses = new ArrayList<String>();
	    					for (Permission permission: permissions){
	    						MyLogger.getLogger().info("Folder "+folder.getName()+" permission: "+permission.getRole()+" - "+permission.getEmailAddress());
	    						emailAddresses.add(permission.getEmailAddress());
	    					}
    					}
    					FileList fileList = googleDriveAccess.filesIntoFolder(folder);
    					List<File> files = fileList.getFiles();
    					if (files != null){
    						MyLogger.getLogger().info("Trovati "+files.size()+" nel folder "+folder.getName());
    						for (File file: files){
    							MyLogger.getLogger().info("Download del file "+file.getName()+" con id "+file.getId());
    							googleDriveAccess.downloadFile(file, folder.getName());
    							ReportDriveData reportDriveData = new ReportDriveData();
    							reportDriveData.setParentFolderId(folder.getId());
    							reportDriveData.setIdGoogleDrive(file.getId());
    							reportDriveData.setIdGoogleDrive(file.getName());
    							reportDriveData.setEmailContacts(emailAddresses);
    							result.add(reportDriveData);
    						}
    					}
    				}
    			}else{
    				MyLogger.getLogger().severe("Non è stato abilitato alcun folder di Google Drive alle credenziali in uso");
    				throw new MyException("Non è stato abilitato alcun folder di Google Drive alle credenziali in uso");
    			}
    		}catch(UnknownHostException uhe){
    			MyLogger.getLogger().severe(uhe.getMessage());
        		throw new MyException("Verificare la connessione Internet: "+uhe.getMessage());
    		}catch(IOException ioe){
    			MyLogger.getLogger().severe(ioe.getMessage());
        		throw new MyException(ioe);
    		}
    	}else{
    		MyLogger.getLogger().severe("Non è stato trovato l'ID del folder genitore di Google Drive");
    		throw new MyException("Non è stato trovato l'ID del folder genitore di Google Drive");
    	}
    	
    	return result;
	}

}