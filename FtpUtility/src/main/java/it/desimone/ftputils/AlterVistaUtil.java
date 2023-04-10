package it.desimone.ftputils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AlterVistaUtil {

//	private static final String ROOT = "RisiKo";
//	private static final String HOST = "ftp.marcodesimone.altervista.org";
//	private static final String USERNAME = "marcodesimone";
//	private static final String PASSWORD = "V4qsduw7YScF";
	
//	private static final String ROOT = "/";
//	private static final String HOST = "ftp.rcu.altervista.org";
//	private static final String USERNAME = "rcu";
//	private static final String PASSWORD = "Ar44Q6hjgtdm";
	
	private static final String ROOT = "/forum.egcommunity.it/risikolive";
	//private static final String HOST = "94.237.88.138"; //Server prima del 12/03/2023
	private static final String HOST = "149.62.186.21";
	private static final String USERNAME = "ftp_egcommunity";
	private static final String PASSWORD = "forum_ftp_2013";
	
	public static void main(String[] args) throws IOException {
		System.out.println("START");
		FtpClient client = new FtpClient(HOST, 21, USERNAME, PASSWORD);
		client.openSSH();
		
		File torneo = new File("C:\\Users\\mds\\Desktop\\GoogleSheetsRemoteAccess 1.3\\working\\htmlPages\\index.html");
		try{
			client.uploadFiles(ROOT, Collections.singletonList(torneo));
			Collection<String> files = client.listFiles(ROOT);
			
			for (String file: files){
				System.out.println(file);
			}
		}catch(Throwable t){
			t.printStackTrace();
		}
		System.out.println("END");

	}

	public static void uploadInRoot(List<File> files)  throws IOException{
		FtpClient client = new FtpClient(HOST, 21, USERNAME, PASSWORD);
			//client.open();//Altervista
			client.openSSH();//EG
			client.uploadFiles(ROOT, files);
			client.close();

	}
	public static void uploadInTornei(List<File> files) throws IOException{
		for (File file: files){
			FtpClient client = new FtpClient(HOST, 21, USERNAME, PASSWORD);
			//client.open();//Altervista
			client.openSSH();//EG
			client.changeDirectory(ROOT);
			client.uploadFiles("TORNEI", Collections.singletonList(file));
			client.close();
		}
	}
	public static void uploadInTabelliniPerClub(List<File> files) throws IOException{
		for (File file: files){
			FtpClient client = new FtpClient(HOST, 21, USERNAME, PASSWORD);
			//client.open();//Altervista
			client.openSSH();//EG
			client.changeDirectory(ROOT);
			client.uploadFiles("TABELLINI_CLUB", Collections.singletonList(file));
			client.close();
		}
	}
}
