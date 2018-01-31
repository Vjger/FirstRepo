package it.desimone.gdrive.file;

import java.util.List;

public class ReportDriveData {

	private String parentFolderId;
	private String idGoogleDrive;
	private String fileName;
	private List<String> emailContacts;
	public String getParentFolderId() {
		return parentFolderId;
	}
	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}
	public String getIdGoogleDrive() {
		return idGoogleDrive;
	}
	public void setIdGoogleDrive(String idGoogleDrive) {
		this.idGoogleDrive = idGoogleDrive;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<String> getEmailContacts() {
		return emailContacts;
	}
	public void setEmailContacts(List<String> emailContacts) {
		this.emailContacts = emailContacts;
	}
	
}
