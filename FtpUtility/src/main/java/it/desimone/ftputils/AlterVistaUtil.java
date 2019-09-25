package it.desimone.ftputils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AlterVistaUtil {

	private static final String ROOT = "RisiKo";
	private static final String HOST = "ftp.marcodesimone.altervista.org";
	private static final String USERNAME = "marcodesimone";
	private static final String PASSWORD = "V4qsduw7YScF";
	
	public static void main(String[] args) throws IOException {
		FtpClient client = new FtpClient("ftp.marcodesimone.altervista.org", 21, "marcodesimone", "V4qsduw7YScF");
		client.open();
		
		File torneo = new File("C:\\Users\\mds\\Desktop\\RisiKo Pages\\TORNEI\\20190901-GENOVA_BorgoPila_.html");
		client.uploadFiles("RisiKo/TORNEI", Collections.singletonList(torneo));

	}

	public static void uploadInRoot(List<File> files){
		FtpClient client = new FtpClient(HOST, 21, USERNAME, PASSWORD);
		try {
			client.open();
			client.uploadFiles(ROOT, files);
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void uploadInTornei(List<File> files){
		FtpClient client = new FtpClient(HOST, 21, USERNAME, PASSWORD);
		try {
			client.open();
			client.changeDirectory(ROOT);
			client.uploadFiles("TORNEI", files);
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
