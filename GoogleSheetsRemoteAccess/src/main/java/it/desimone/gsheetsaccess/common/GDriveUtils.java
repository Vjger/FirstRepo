package it.desimone.gsheetsaccess.common;

import java.io.IOException;
import java.net.UnknownHostException;

import com.google.api.services.drive.model.File;

import it.desimone.gsheetsaccess.gdrive.file.ReportDriveData;
import it.desimone.gsheetsaccess.googleaccess.GoogleDriveAccess;
import it.desimone.utils.MyException;
import it.desimone.utils.MyLogger;

public class GDriveUtils {

	public static void moveToDone(ReportDriveData reportDriveData){
		String doneFolderId = Configurator.getDoneFolderId();
		moveToFolder(reportDriveData, doneFolderId);
	}
	
	public static void moveToError(ReportDriveData reportDriveData){
		String doneFolderId = Configurator.getErrorFolderId();
		moveToFolder(reportDriveData, doneFolderId);
	}
	
	private static void moveToFolder(ReportDriveData reportDriveData, String folderId){
		String fileId = reportDriveData.getIdGoogleDrive();
		String parentFolderName = reportDriveData.getParentFolderName();
		String fileName = reportDriveData.getFileName();
		
    	if (folderId != null){
    		try{
				GoogleDriveAccess googleDriveAccess = new GoogleDriveAccess();
				File destinationFolder = googleDriveAccess.findOrCreateFolderIfNotExists(folderId, parentFolderName);
				if (destinationFolder != null){
					File doneReport = googleDriveAccess.moveFileToNewFolder(fileId, destinationFolder.getId());
					MyLogger.getLogger().fine("Il report "+fileName+" è passato sotto la cartella folderId con ID "+doneReport.getId());
				}else{
    				MyLogger.getLogger().severe("Non è stato possibile accedere al folder "+parentFolderName+" sotto la cartella "+folderId);
    				throw new MyException("Non è stato possibile accedere al folder "+parentFolderName+" sotto la cartella "+folderId);
    			}
    		}catch(UnknownHostException uhe){
    			MyLogger.getLogger().severe(uhe.getMessage());
        		throw new MyException("Verificare la connessione Internet: "+uhe.getMessage());
    		}catch(IOException ioe){
    			MyLogger.getLogger().severe(ioe.getMessage());
        		throw new MyException(ioe);
    		}
    	}else{
    		MyLogger.getLogger().severe("Non è stato trovato il folder di Google Drive con id "+folderId);
    		throw new MyException("Non è stato trovato il folder di Google Drive con id "+folderId);
    	}
	}
}