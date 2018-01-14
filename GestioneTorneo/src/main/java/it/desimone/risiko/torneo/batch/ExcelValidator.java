package it.desimone.risiko.torneo.batch;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import it.desimone.risiko.torneo.batch.ExcelValidator.ExcelValidatorMessages.Scheda;
import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.risiko.torneo.dto.SchedaClassifica;
import it.desimone.risiko.torneo.dto.SchedaClassifica.RigaClassifica;
import it.desimone.risiko.torneo.dto.SchedaTorneo;
import it.desimone.risiko.torneo.dto.SchedaTurno;
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
		@Override
		public String toString() {
			return "ExcelValidatorMessages [message=" + message
					+ ", schedaDiRiferimento=" + schedaDiRiferimento + "]";
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
	 * 2) verificare che tutti i giocatori che hanno giocato almeno una partita siano in classifica (però così non puoi squalificare)
	 * 3) verificare che tutti i giocatori in classifica siano nella scheda iscritti
	 * 4) Se sono dichiarati X turni ma se ne trovano Y dove Y > X è un errore
	 * 
	 */
	
	public List<ExcelValidatorMessages> validazioniIncrociate(){
		List<ExcelValidatorMessages> result = new ArrayList<ExcelValidatorMessages>();
		
		excelAccess.openFileExcel();
		
		SchedaTorneo schedaTorneo = excelAccess.leggiSchedaTorneo();
		
		String[] sheetNames = excelAccess.getSheetNames();
		
		if (schedaTorneo != null){
			int numeroTurni = schedaTorneo.getNumeroTurni();
			List<Date> dateTurni = schedaTorneo.getDataTurni();
			if (sheetNames != null){
				int counterNumeroTurni = 0;
				for (String sheetName: sheetNames){
					if (sheetName != null && sheetName.endsWith(ExcelAccess.SCHEDA_TURNO_SUFFIX)){
						counterNumeroTurni++;
						try{
							String numeroTurno = sheetName.substring(0, sheetName.indexOf(ExcelAccess.SCHEDA_TURNO_SUFFIX));
							if (numeroTurno != null){
								numeroTurno = numeroTurno.trim();
								Integer numeroTurnoInt = Integer.valueOf(numeroTurno);
								if (numeroTurnoInt > numeroTurni){
									result.add(new ExcelValidatorMessages(Scheda.TORNEO, "E' presente la scheda per il turno "+numeroTurnoInt+" ma il torneo prevede solo "+numeroTurni+" turni"));
								}else{
									if (dateTurni == null || dateTurni.isEmpty() || dateTurni.size() < numeroTurnoInt || dateTurni.get(numeroTurnoInt-1) == null){
										result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Non è stata indicata o non è correttamente formattata la data in cui è stato disputato il "+numeroTurnoInt+"° turno"));
									}
								}
							}else{
								result.add(new ExcelValidatorMessages(Scheda.TURNO,"Inizio dello sheet "+sheetName+" assente"));
							}

						}catch(Exception e){
							result.add(new ExcelValidatorMessages(Scheda.TURNO,"Inizio dello sheet "+sheetName+" non numerico"));
						}
					}
				}
				if (counterNumeroTurni > numeroTurni){
					result.add(new ExcelValidatorMessages(Scheda.TORNEO, "I turni dichiarati per questo Torneo ("+numeroTurni+") sono inferiori a quelli presenti sul foglio Torneo ("+counterNumeroTurni+")"));
				}
			}
		}
		
		if (excelAccess.checkSheet(ExcelAccess.SCHEDA_CLASSIFICA_RIDOTTA)){
			SchedaClassifica schedaClassifica = excelAccess.leggiSchedaClassifica();
			List<RigaClassifica> giocatoriInClassifica = schedaClassifica.getClassifica();
			if (giocatoriInClassifica != null && !giocatoriInClassifica.isEmpty()){
				List<GiocatoreDTO> iscritti = excelAccess.getListaGiocatori(false);
				GiocatoreDTO giocatore = new GiocatoreDTO();
				for (RigaClassifica rigaClassifica: giocatoriInClassifica){
					if (rigaClassifica != null){
						if (rigaClassifica.getIdGiocatore() != null){
							giocatore.setId(rigaClassifica.getIdGiocatore());
							if (!iscritti.contains(giocatore)){
								result.add(new ExcelValidatorMessages(Scheda.CLASSIFICA_RIDOTTA, "Il giocatore presente in classifica con l'ID "+rigaClassifica.getIdGiocatore()+" non è presente nella Scheda Iscritti"));
							}
						}
					}
				}
			}
		}
		
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
			if (schedaTorneo.getTipoTorneo() == null){
				result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Non è stata indicata una corretta tipologia di Torneo"));
			}
			if (schedaTorneo.getNumeroTurni() == 0){
				result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Non è stato indicato il numero di Turni previsti"));
			}
			List<Date> dateTurni = schedaTorneo.getDataTurni();
			if (dateTurni != null && !dateTurni.isEmpty()){
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Date turnoPrecedente = null;
				for (Date dataTurno: dateTurni){
					if (dataTurno != null){
						if (turnoPrecedente == null){
							turnoPrecedente = dataTurno;
						}else{
							if (dataTurno.before(turnoPrecedente)){
								result.add(new ExcelValidatorMessages(Scheda.TORNEO, "Errata sequenza cronologica per le date dei turni: il turno del "+df.format(dataTurno)+" è precedente a quello del "+df.format(turnoPrecedente)));
								turnoPrecedente = dataTurno;
							}
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
		
		List<SchedaTurno> schedeTurni = excelAccess.leggiSchedeTurno();
		
		boolean almenoUnTurno = schedeTurni != null && !schedeTurni.isEmpty();
		
//		for(int numeroTurno = 1; ; numeroTurno++){
//			Partita[] partite = excelAccess.loadPartite(numeroTurno, true, null);
//			if (partite != null){
//				almenoUnTurno = true;
//				for (Partita partita: partite){
//					if (partita.getPunteggioVincitore() == 0f){
//						result.add(new ExcelValidatorMessages(Scheda.TURNO, "Nella partita del tavolo "+partita.getNumeroTavolo()+" del Turno "+numeroTurno+" i punteggi dei giocatori non sono compilati"));
//					}
//				}
//			}else{
//				break;
//			}
//		}
		if (!almenoUnTurno){
			result.add(new ExcelValidatorMessages(Scheda.TURNO, "Non risulta presente nemmeno un turno di gioco: le schede Turno devono chiamarsi \"N° Turno\" (dove N è un numero)"));
		}else{
			for (SchedaTurno schedaTurno: schedeTurni){
				if (schedaTurno.getNumeroTurno() == null){
					result.add(new ExcelValidatorMessages(Scheda.TURNO, "E' presente un turno di gioco del quale non è stato possibile determinare il numero: le schede Turno devono chiamarsi \"N° Turno\" (dove N è un numero)"));
				}
				Partita[] partiteTurno = schedaTurno.getPartite();
				if (partiteTurno == null || partiteTurno.length == 0){
					result.add(new ExcelValidatorMessages(Scheda.TURNO, "Per la scheda del turno "+schedaTurno.getNumeroTurno()+" non sono presenti partite"));
				}
			}
		}
		
//		String[] sheetNames = excelAccess.getSheetNames();
//		
//		if (sheetNames != null){
//			int counterNumeroTurni = 0;
//			for (String sheetName: sheetNames){
//				if (sheetName != null && sheetName.endsWith(ExcelAccess.SCHEDA_TURNO_SUFFIX)){
//					try{
//						String numeroTurno = sheetName.substring(0, sheetName.indexOf(ExcelAccess.SCHEDA_TURNO_SUFFIX));
//						if (numeroTurno != null){
//							numeroTurno = numeroTurno.trim();
//							Integer.valueOf(numeroTurno);
//							counterNumeroTurni++;
//						}
//					}catch(Exception e){
//						result.add(new ExcelValidatorMessages(Scheda.TURNO,"Inizio dello sheet "+sheetName+" non numerico"));
//					}
//				}
//			}
//			if (counterNumeroTurni == 0){
//				result.add(new ExcelValidatorMessages(Scheda.TURNO, "Non risulta presente nemmeno un turno di gioco: le schede Turno devono chiamarsi \"N° Turno\" (dove N è un numero)"));
//			}
//		}else{
//			result.add(new ExcelValidatorMessages(Scheda.TURNO, "Non risulta presente nemmeno un turno di gioco: le schede Turno devono chiamarsi \"N° Turno\" (dove N è un numero)"));
//		}
		
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
			if (excelAccess.checkSheet(ExcelAccess.SCHEDA_CLASSIFICA_RIDOTTA)){
				SchedaClassifica schedaClassifica = excelAccess.leggiSchedaClassifica();
				List<RigaClassifica> giocatoriInClassifica = schedaClassifica.getClassifica();
				if (giocatoriInClassifica == null || giocatoriInClassifica.isEmpty()){
					result.add(new ExcelValidatorMessages(Scheda.CLASSIFICA_RIDOTTA, "Non ci sono giocatori in classifica"));
				}else{
					int counterRiga = 1;
					for (RigaClassifica rigaClassifica: giocatoriInClassifica){
						if (rigaClassifica != null){
							counterRiga++;
							if (rigaClassifica.getIdGiocatore() == null){
								result.add(new ExcelValidatorMessages(Scheda.CLASSIFICA_RIDOTTA, "Non è indicato l'ID del giocatore nella 5° colonna della "+counterRiga+"° riga"));
							}
							if (rigaClassifica.getPosizioneGiocatore() == null){
								result.add(new ExcelValidatorMessages(Scheda.CLASSIFICA_RIDOTTA, "Non è indicata la posizione del giocatore nella 1° colonna della "+counterRiga+"° riga"));
							}
							if (rigaClassifica.getPunteggioFinaleGiocatore() == null){
								result.add(new ExcelValidatorMessages(Scheda.CLASSIFICA_RIDOTTA, "Non è indicato il punteggio finale del giocatore nella 4° colonna della "+counterRiga+"° riga"));
							}
						}
					}
				}
			}
		}
		excelAccess.closeFileExcel();
		
		return result;
	}
}
