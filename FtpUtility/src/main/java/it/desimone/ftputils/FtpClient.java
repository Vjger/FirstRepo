package it.desimone.ftputils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpClient {

	   private String server;
	    private int port;
	    private String user;
	    private String password;
	    private FTPClient ftp;
	 
	    // constructor
	    
		 
	    public FtpClient(String server, int port, String user, String password) {
			super();
			this.server = server;
			this.port = port;
			this.user = user;
			this.password = password;
		}	    
	 
	    public void open() throws IOException {
	        ftp = new FTPClient();
	 
	        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	 
	        ftp.connect(server, port);
	        int reply = ftp.getReplyCode();
	        if (!FTPReply.isPositiveCompletion(reply)) {
	            ftp.disconnect();
	            throw new IOException("Exception in connecting to FTP Server");
	        }
	 
	        ftp.login(user, password);
	    }

		public void close() throws IOException {
	        ftp.disconnect();
	    }
	
		
		public Collection<String> listFiles(String path) throws IOException {
			Collection<String> result = null;
		    FTPFile[] files = ftp.listFiles(path);
		    if (files != null) {
		    	result = new ArrayList<String>();
		    	for(FTPFile ftpFile: files) {
		    		result.add(ftpFile.getName());
		    	}
		    }
		    return result;
		}
		
		public void changeDirectory(String path) throws IOException {
			ftp.changeWorkingDirectory(path);
		}
		
		public void uploadFiles(String directory, List<File> files) throws IOException {
			changeDirectory(directory);
			if (files != null) {
				for (File file: files) {
					ftp.storeFile(file.getName(), new FileInputStream(file));
				}
			}
		}
}
