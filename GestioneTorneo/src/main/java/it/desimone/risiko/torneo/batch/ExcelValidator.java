package it.desimone.risiko.torneo.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import it.desimone.risiko.torneo.batch.ExcelValidator.ExcelValidatorMessages.Scheda;
import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.risiko.torneo.dto.SchedaClassifica;
import it.desimone.risiko.torneo.dto.SchedaTorneo;
import it.desimone.risiko.torneo.dto.SchedaClassifica.RigaClassifica;
import it.desimone.utils.MyLogger;
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
			TORNEO, ISCRITTI, TURNO, CLASSIFICA_RIDOTTA
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
			
			List<ExcelValidatorMessages> validazioniIncrociate = validazioniIncrociate();
			if (validazioniIncrociate != null){
				result.addAll(validazioniIncrociate);
			}

		return result;
	}
	
	/*
	 * Controlli incrociati da fare:
	 * 
	 * 1) se esiste il turno x deve essere compilata la data nella scheda Torneo
	 * 2) verificare che tutti i giocatori che hanno giocato almeno una partita siano in classifica
	 * 3) verificare che tutti i giocatori in classifica siano nella scheda iscritti
	 * 4) Se sono dichiarati X turni ma se ne trovano Y dove Y > X è un errore
	 * 
	 */
	
	public List<ExcelValidatorMessages> validazioniIncrociate(){
		List<ExcelValidatorMessages> result = new ArrayList<ExcelValidatorMessages>();
		
		excelAccess.openFileExcel();
		
		SchedaTorneo schedaTorneo = excelAccess.leggiSchedaTorneo();
		
		String[] sheetNames = excelAccess.getSheetNames();
		
		excelAccess.closeFileExcel();
		
		return result;
	}
	
	public List<ExcelValidatorMessages> validaSchedaTorneo(){
		List<ExcelValidatorMessages> result = new ArrayList<ExcelValidatorMessages>();
		
		excelAccess.openFileExcel();
		
		SchedaTorneo schedaTorneo = excelAccess.leggiSchedaTorneo();
		if (!excelAccess.checkSheet(ExcelAccess.SCHEDA_TORNEO)){
			result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Sheet "+ExcelAccess.SCHEDA_TORNEO+" assente"));
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
							result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Non è stata indicata la date in cui è stato disputato il "+i+"Â° turno"));		
						}
					}
				}
			}
		}
		
		
		excelAccess.closeFileExcel();
		
		return result;
	}
	
	public List<ExcelValidatorMessages> validaSchedaIscritti(){
		List<ExcelValidatorMessages> result = new ArrayList<ExcelValidatorMessages>();
		
		excelAccess.openFileExcel();
		
		if (!excelAccess.checkSheet(ExcelAccess.SCHEDA_ISCRITTI)){
			result.add(new ExcelValidatorMessages(Scheda.ISCRITTI, "Sheet "+ExcelAccess.SCHEDA_ISCRITTI+" assente"));
		}else{
			List<GiocatoreDTO> iscritti = excelAccess.getListaGiocatori(false);
			if (iscritti == null || iscritti.isEmpty()){
				result.add(new ExcelValidatorMessages(Scheda.ISCRITTI, "Lista Iscritti vuota"));
			}else{
				Set<GiocatoreDTO> partecipantiEffettivi = excelAccess.getPartecipantiEffettivi();
				if (partecipantiEffettivi != null && !partecipantiEffettivi.isEmpty()){
					iscritti.retainAll(partecipantiEffettivi);
				}
				for (GiocatoreDTO giocatore: iscritti){
					if (giocatore.getId() == null){
						result.add(new ExcelValidatorMessages(Scheda.ISCRITTI, "L'id del giocatore "+giocatore+" non è indicato"));
					}
					if (StringUtils.isNullOrEmpty(giocatore.getNome())){
						result.add(new ExcelValidatorMessages(Scheda.ISCRITTI, "Il nome del giocatore "+giocatore+" non è indicato"));
					}
					if (StringUtils.isNullOrEmpty(giocatore.getCognome())){
						result.add(new ExcelValidatorMessages(Scheda.ISCRITTI, "Il cognome del giocatore "+giocatore+" non è indicato"));
					}
					if (StringUtils.isNullOrEmpty(giocatore.getEmail())){
						result.add(new ExcelValidatorMessages(Scheda.ISCRITTI, "L'indirizzo email del giocatore "+giocatore+" non è indicato"));
					}
				}
			}
		}
		
		excelAccess.closeFileExcel();
		
		return result;
	}
	
	//Qui valido solo le partite: se qualcosa non va ci sarà un'eccezione
	public List<ExcelValidatorMessages> validaSchedeTurni(){
		List<ExcelValidatorMessages> result = new ArrayList<ExcelValidatorMessages>();
		
		excelAccess.openFileExcel();
		
		for(int numeroTurno = 1; ; numeroTurno++){
			Partita[] partite = excelAccess.loadPartite(numeroTurno, true, null);
			if (partite != null){
			}else{
				break;
			}
		}
		
		excelAccess.closeFileExcel();
		
		return result;
	}
	
	
	
	public List<ExcelValidatorMessages> validaSchedaClassifica(){
		List<ExcelValidatorMessages> result = new ArrayList<ExcelValidatorMessages>();
		
		excelAccess.openFileExcel();
		
		SchedaTorneo schedaTorneo = excelAccess.leggiSchedaTorneo();
		
		boolean giocatoUltimoTurno = schedaTorneo != null 
				&& schedaTorneo.getNumeroTurni() > 0 
				&& excelAccess.checkSheet(excelAccess.getNomeTurno(schedaTorneo.getNumeroTurni()));
		
		if (giocatoUltimoTurno && !excelAccess.checkSheet(ExcelAccess.SCHEDA_CLASSIFICA_RIDOTTA)){
			result.add(new ExcelValidatorMessages(Scheda.CLASSIFICA_RIDOTTA, "Sheet "+ExcelAccess.SCHEDA_CLASSIFICA_RIDOTTA+" assente: obbligatoria perchè giocato l'ultimo turno"));
		}else{
			SchedaClassifica schedaClassifica = excelAccess.leggiSchedaClassifica();
			List<RigaClassifica> giocatoriInClassifica = schedaClassifica.getClassifica();
			if (giocatoriInClassifica == null || giocatoriInClassifica.isEmpty()){
				result.add(new ExcelValidatorMessages(Scheda.CLASSIFICA_RIDOTTA, "Non ci sono giocatori in classifica"));
			}
		}
		excelAccess.closeFileExcel();
		
		return result;
	}
}
