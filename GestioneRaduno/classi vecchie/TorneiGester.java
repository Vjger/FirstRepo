package it.desimone.risiko.torneo.batch;

import it.desimone.risiko.torneo.panels.PannelloPrincipale;
import it.desimone.utils.ExceptionUtils;
import it.desimone.utils.TextException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class TorneiGester {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		
		try{
			new PannelloPrincipale();
		}catch (Throwable e) {
			writeException(e);
			JOptionPane.showMessageDialog(null, new TextException(e),"Orrore!",JOptionPane.ERROR_MESSAGE);
		}

	}
	
	public static void writeException (Throwable e){
		File log = new File(".\\TorneiGester.log");
		String logString = ExceptionUtils.parseException(e);
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(log));
			bufferedWriter.write(logString);
			bufferedWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
