package it.desimone.GestioneTorneo;

import it.desimone.risiko.torneo.batch.ExcelValidator;
import it.desimone.risiko.torneo.batch.ExcelValidator.ExcelValidatorMessages;
import it.desimone.utils.MyLogger;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public class ExcelValidatorTest {

	public static void main(String[] args) {
		
		MyLogger.setConsoleLogLevel(Level.ALL);
		
		//File testFile = new File("C:\\Users\\mds\\Desktop\\TestPublish.xls");
		
		File testFile = new File("C:\\GIT Repositories\\FirstRepo\\GoogleSheetsRemoteAccess\\working\\download\\torneovalerio.xls");
		
		ExcelValidator excelValidator = new ExcelValidator(testFile);
		
		List<ExcelValidatorMessages> messages = excelValidator.validaFoglioExcel();
		
		for(ExcelValidatorMessages message: messages){
			System.out.println(message);
		}
	}

}
