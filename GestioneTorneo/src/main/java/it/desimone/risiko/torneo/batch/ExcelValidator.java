package it.desimone.risiko.torneo.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.desimone.risiko.torneo.batch.ExcelValidator.ExcelValidatorMessages.Scheda;
import it.desimone.risiko.torneo.dto.SchedaTorneo;
import it.desimone.utils.StringUtils;

public class ExcelValidator {

	public static class ExcelValidatorMessages{
		
		public ExcelValidatorMessages(Scheda schedaDiRiferimento, String message){
			this.schedaDiRiferimento = schedaDiRiferimento;
			this.message = message;
		}
		
		private String message;
		private Scheda schedaDiRiferimento;
		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}

		public Scheda getSchedaDiRiferimento() {
			return schedaDiRiferimento;
		}
		public void setSchedaDiRiferimento(Scheda schedaDiRiferimento) {
			this.schedaDiRiferimento = schedaDiRiferimento;
		}
		public enum Scheda{
			TORNEO, ISCRITTI, TURNO, CLASSIFICA
		}
	}
	
	private ExcelAccess excelAccess;
	
	public ExcelValidator(File excelFile){
		this.excelAccess = new ExcelAccess(excelFile);
	}
	
	
	public List<ExcelValidatorMessages> validaFoglioExcel(){
		List<ExcelValidatorMessages> result = new ArrayList<ExcelValidatorMessages>();
		
		List<ExcelValidatorMessages> validazioniSchedaTorneo = validaSchedaTorneo();
		if (validazioniSchedaTorneo != null){
			result.addAll(validazioniSchedaTorneo);
		}
		List<ExcelValidatorMessages> validazioniSchedaIscritti = validaSchedaIscritti();
		if (validazioniSchedaIscritti != null){
			result.addAll(validazioniSchedaIscritti);
		}
		List<ExcelValidatorMessages> validazioniSchedeTurni = validaSchedeTurni();
		if (validazioniSchedeTurni != null){
			result.addAll(validazioniSchedeTurni);
		}
		
		List<ExcelValidatorMessages> validazioniSchedaClassifica = validaSchedaClassifica();
		if (validazioniSchedaClassifica != null){
			result.addAll(validazioniSchedaClassifica);
		}
		return result;
	}
	
	public List<ExcelValidatorMessages> validaSchedaTorneo(){
		List<ExcelValidatorMessages> result = new ArrayList<ExcelValidatorMessages>();
		
		excelAccess.openFileExcel();
		
		SchedaTorneo schedaTorneo = excelAccess.leggiSchedaTorneo();
		if (schedaTorneo == null){
			result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Sheet Assente"));
		}else{
			if (StringUtils.isNullOrEmpty(schedaTorneo.getOrganizzatore())){
				result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Non è stato indicato l'organizzatore del Torneo"));
			}
			if (StringUtils.isNullOrEmpty(schedaTorneo.getNomeTorneo())){
				result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Non è stato indicato il nome del Torneo"));
			}
			if (schedaTorneo.getNumeroTurni() == 0){
				result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Non è stato indicato il numero di Turni disputati"));
			}else{
				List<Date> dateTurni = schedaTorneo.getDataTurni();
				if (dateTurni == null || dateTurni.isEmpty()){
					result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Non sono stati indicate le date per i "+schedaTorneo.getNumeroTurni()+" turni disputati"));
				}else{
					for (int i = 1; i <=schedaTorneo.getNumeroTurni(); i++){
						Date dataTurno = null;
						try{
							dataTurno = dateTurni.get(i-1);
						}catch(IndexOutOfBoundsException iobe){
							
						}
						if (dataTurno == null){
							result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Non è stata indicata la date in cui è stato disputato il "+i+"° turno"));		
						}
					}
				}
			}
		}
		
		
		excelAccess.closeFileExcel();
		
		return result;
	}
	
	public List<ExcelValidatorMessages> validaSchedaIscritti(){
		List<ExcelValidatorMessages> result = null;
		
		
		
		return result;
	}
	
	public List<ExcelValidatorMessages> validaSchedeTurni(){
		List<ExcelValidatorMessages> result = null;
		
		
		
		return result;
	}
	
	public List<ExcelValidatorMessages> validaSchedaClassifica(){
		List<ExcelValidatorMessages> result = null;
		
		
		
		return result;
	}
}
