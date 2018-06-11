package it.desimone;

import java.util.logging.Level;

import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.common.Configurator.Environment;
import it.desimone.gsheetsaccess.common.GDriveUtils;
import it.desimone.utils.MyLogger;

public class GDriveUtilsTest {

	public static void main(String[] args) {
		MyLogger.setConsoleLogLevel(Level.ALL);
		Configurator.loadConfiguration(Environment.PRODUCTION);
		try {
			GDriveUtils.backup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
