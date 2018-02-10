package it.desimone.gsheetsaccess.gdrive.file;

import java.io.File;
import java.util.List;

import it.desimone.gsheetsaccess.common.ExcelValidationException;
import it.desimone.gsheetsaccess.common.ResourceWorking;
import it.desimone.risiko.torneo.batch.ExcelAccess;
import it.desimone.risiko.torneo.batch.ExcelValidator;
import it.desimone.risiko.torneo.batch.ExcelValidator.ExcelValidatorMessages;
import it.desimone.risiko.torneo.dto.Torneo;

public class ReportAnalyzer {

	public static Torneo analyzeExcelReport(ReportDriveData reportDriveData) throws ExcelValidationException {
		Torneo torneo = null;
		File excelFile = new File(ResourceWorking.workingAreaPath()+java.io.File.separator+reportDriveData.getParentFolderName(),reportDriveData.getFileName());
		ExcelValidator excelValidator = new ExcelValidator(excelFile);
		List<ExcelValidatorMessages> messaggiDiValidazione = excelValidator.validaFoglioExcel();
		if (messaggiDiValidazione != null && !messaggiDiValidazione.isEmpty()){
			throw new ExcelValidationException(messaggiDiValidazione);
		}else{
			ExcelAccess excelAccess = new ExcelAccess(excelFile);
			excelAccess.openFileExcel();
			torneo = excelAccess.elaboraTorneo();
			excelAccess.closeFileExcel();
			torneo.setFilename(reportDriveData.getFileName());
		}
		
		return torneo;
	}
	
}
