package it.desimone;

import java.util.logging.Level;

import it.desimone.gsheetsaccess.utils.TorneiUtils;
import it.desimone.utils.MyLogger;

public class TorneiUtilsTest {

	public static void main(String[] args) {
		MyLogger.setConsoleLogLevel(Level.INFO);
		//TorneiUtils.deleteTorneo("20171120 - ROMA [Il Gufo]");

		TorneiUtils.mergePlayer(12, 71, "2018");
	}

}
