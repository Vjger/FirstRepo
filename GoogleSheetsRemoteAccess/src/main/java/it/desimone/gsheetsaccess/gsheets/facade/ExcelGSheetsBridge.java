package it.desimone.gsheetsaccess.gsheets.facade;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.desimone.gheetsaccess.gsheets.dto.AnagraficaGiocatoreRidottaRow;
import it.desimone.gheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gheetsaccess.gsheets.dto.PartitaRow;
import it.desimone.gheetsaccess.gsheets.dto.SheetRow;
import it.desimone.gheetsaccess.gsheets.dto.TorneiRow;
import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.risiko.torneo.dto.SchedaTorneo;
import it.desimone.risiko.torneo.dto.SchedaTurno;
import it.desimone.risiko.torneo.dto.Torneo;
import it.desimone.risiko.torneo.dto.SchedaClassifica.RigaClassifica;
import it.desimone.utils.MyLogger;

public class ExcelGSheetsBridge {

	private static final DateFormat dfIdTorneo = new SimpleDateFormat("yyyyMMdd");
	private static final DateFormat dfDateTorneo = new SimpleDateFormat("dd/MM/yyyy");
	private static final DateFormat dfUpdateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static TorneiRow getTorneoRowByTorneo(Torneo torneo){
		if (torneo == null || torneo.getSchedaTorneo() == null){
			return null;
		}
		SchedaTorneo schedaTorneo = torneo.getSchedaTorneo();
		TorneiRow torneiRow = new TorneiRow();
		List<Date> dateTurni = schedaTorneo.getDataTurni();
		String idTorneo = obtainIdTorneo(torneo);
		torneiRow.setIdTorneo(idTorneo);
		torneiRow.setNomeTorneo(schedaTorneo.getNomeTorneo());
		torneiRow.setOrganizzatore(schedaTorneo.getOrganizzatore());
		torneiRow.setSede(schedaTorneo.getSedeTorneo());
		torneiRow.setTipoTorneo(schedaTorneo.getTipoTorneo().getTipoTorneo());
		if (dateTurni != null && !dateTurni.isEmpty()){
			Date dataInizioTorneo = dateTurni.get(0);
			Date dataFineTorneo = dateTurni.get(dateTurni.size()-1);
			torneiRow.setStartDate(dfDateTorneo.format(dataInizioTorneo));
			torneiRow.setEndDate(dfDateTorneo.format(dataFineTorneo));
		}
		torneiRow.setNote(schedaTorneo.getNote());
		torneiRow.setNumeroTurni(schedaTorneo.getNumeroTurni());
		if (torneo.getPartecipanti() != null){
			Integer numeroPartecipanti = torneo.getPartecipanti().size();
			if (torneo.getPartecipanti().contains(GiocatoreDTO.FITTIZIO)){
				numeroPartecipanti--;
			}
			torneiRow.setNumeroPartecipanti(numeroPartecipanti);
			
		}
		if (torneo.getSchedeTurno() != null){
			Integer numeroPartite = 0;
			for (SchedaTurno turno: torneo.getSchedeTurno()){
				if (turno.getPartite() != null){
					numeroPartite += turno.getPartite().length;
				}
			}
			torneiRow.setNumeroTavoli(numeroPartite);
		}
		torneiRow.setFilename(torneo.getFilename());
		torneiRow.setUpdateTime(dfUpdateTime.format(new Date()));
		return torneiRow;
	}
	
	public static String obtainIdTorneo(Torneo torneo){
		String result = null;
		String organizzatore = torneo.getSchedaTorneo().getOrganizzatore();
		List<Date> dateTurni = torneo.getSchedaTorneo().getDataTurni();
		if (organizzatore != null && dateTurni != null && !dateTurni.isEmpty()){
			result = dfIdTorneo.format(dateTurni.get(0))+" - "+organizzatore;
		}
		return result;
	}
	
	public static SheetRow[][] getAnagraficheRowByTorneo(Torneo torneo){
		if (torneo == null || torneo.getPartecipanti() == null){
			return null;
		}
		List<GiocatoreDTO> partecipanti = torneo.getPartecipanti();
		SheetRow[][] result = new SheetRow[partecipanti.size()][2];
		
		String idTorneo = obtainIdTorneo(torneo);
		Date now = new Date();
		for (int index = 0; index < partecipanti.size(); index++){
			GiocatoreDTO giocatore = partecipanti.get(index);
			AnagraficaGiocatoreRidottaRow anagraficaGiocatoreRidottaRow = new AnagraficaGiocatoreRidottaRow();
			if (giocatore.getDataDiNascita() != null){
				anagraficaGiocatoreRidottaRow.setNome(giocatore.getNome().trim());
				anagraficaGiocatoreRidottaRow.setCognome(giocatore.getCognome().trim());
				//anagraficaGiocatoreRidottaRow.setEmail(giocatore.getEmail().trim());
				anagraficaGiocatoreRidottaRow.setDataDiNascita(dfDateTorneo.format(giocatore.getDataDiNascita()));
			}else{
				anagraficaGiocatoreRidottaRow.setNome(AnagraficaGiocatoreRidottaRow.NOME_ANONIMO);
				anagraficaGiocatoreRidottaRow.setCognome(AnagraficaGiocatoreRidottaRow.COGNOME_ANONIMO);
				anagraficaGiocatoreRidottaRow.setDataDiNascita(AnagraficaGiocatoreRidottaRow.DATA_NASCITA_ANONIMO);
			}
			anagraficaGiocatoreRidottaRow.setUpdateTime(dfUpdateTime.format(now));
			AnagraficaGiocatoreRow anagraficaGiocatoreRow = new AnagraficaGiocatoreRow();
			if (giocatore.getDataDiNascita() != null){
				anagraficaGiocatoreRow.setNome(giocatore.getNome().trim());
				anagraficaGiocatoreRow.setCognome(giocatore.getCognome().trim());
			}else{
				anagraficaGiocatoreRow.setNome(AnagraficaGiocatoreRidottaRow.NOME_ANONIMO);
				anagraficaGiocatoreRow.setCognome(AnagraficaGiocatoreRidottaRow.COGNOME_ANONIMO);
			}
			anagraficaGiocatoreRow.setUltimoClub(giocatore.getClubProvenienza()==null?null:giocatore.getClubProvenienza().getDenominazione());
			anagraficaGiocatoreRow.setIdUltimoTorneo(idTorneo);
			anagraficaGiocatoreRow.setUpdateTime(dfUpdateTime.format(now));
			
			result[index][0] = anagraficaGiocatoreRidottaRow;
			result[index][1] = anagraficaGiocatoreRow;
		}
		
		return result;
	}
	
	public static List<SheetRow> getPartiteRowByTorneo(Torneo torneo, Map<Integer, Integer> idPlayersMap){
		if (torneo == null || torneo.getSchedeTurno() == null || torneo.getSchedeTurno().isEmpty() || idPlayersMap.isEmpty()){
			return null;
		}

		List<SheetRow> result = new ArrayList<SheetRow>();
		List<SchedaTurno> schedeTurno = torneo.getSchedeTurno();
		String idTorneo = obtainIdTorneo(torneo);
		for (SchedaTurno schedaTurno: schedeTurno){
			Integer numeroTurno = schedaTurno.getNumeroTurno();
			Partita[] partite = schedaTurno.getPartite();
			if (partite != null){
				for (Partita partita: partite){
					PartitaRow partitaRow = new PartitaRow();
					partitaRow.setIdTorneo(idTorneo);
					partitaRow.setNumeroTurno(numeroTurno);
					String dataTurno = getDataTurno(torneo, numeroTurno);
					partitaRow.setDataTurno(dataTurno);
					partitaRow.setNumeroTavolo(partita.getNumeroTavolo());

					Set<GiocatoreDTO> giocatoriPartita = partita.getGiocatori();
					int index = 1;
					for (GiocatoreDTO giocatorePartita : giocatoriPartita){
						int indexGiocatore = getIndexByMap(idPlayersMap, giocatorePartita.getId());
						if (index == 1){
							partitaRow.setIdGiocatore1(indexGiocatore);
							partitaRow.setPunteggioGiocatore1(partita.getPunteggio(giocatorePartita).doubleValue());
						} else if (index == 2){
							partitaRow.setIdGiocatore2(indexGiocatore);
							partitaRow.setPunteggioGiocatore2(partita.getPunteggio(giocatorePartita).doubleValue());
						}else if (index == 3){
							partitaRow.setIdGiocatore3(indexGiocatore);
							partitaRow.setPunteggioGiocatore3(partita.getPunteggio(giocatorePartita).doubleValue());
						}else if (index == 4){
							partitaRow.setIdGiocatore4(indexGiocatore);
							partitaRow.setPunteggioGiocatore4(partita.getPunteggio(giocatorePartita).doubleValue());
						}else if (index == 5){
							partitaRow.setIdGiocatore5(indexGiocatore);
							partitaRow.setPunteggioGiocatore5(partita.getPunteggio(giocatorePartita).doubleValue());
						}
						if (partita.isVincitore(giocatorePartita)){
							partitaRow.setIdGiocatoreVincitore(indexGiocatore);
						}
						index++;
					}
					result.add(partitaRow);
				}
			}
		}
		
		return result;
	}
	
	private static Integer getIndexByMap(Map<Integer, Integer> mappaId, Integer idExcel){
		Integer result = mappaId.get(idExcel);
		return result;
	}
	
	private static String getDataTurno(Torneo torneo, int numeroTurno){
		String result = null;
		List<Date> dateTurni = torneo.getSchedaTorneo().getDataTurni();
		
		if (dateTurni != null){
			if (dateTurni.size() >= numeroTurno){
				Date dataTurno = dateTurni.get(numeroTurno-1);
				result = dfDateTorneo.format(dataTurno);
			}else{
				MyLogger.getLogger().severe("Cercata la data per il turno "+numeroTurno+" ma impostate solo "+dateTurni.size()+" date");
			}
		}
		return result;
	}
	
	
	public static List<SheetRow> getClassificaRowsByTorneo(Torneo torneo, Map<Integer, Integer> idPlayersMap, List<SheetRow> partiteRow){
		if (torneo == null || torneo.getSchedaClassifica() == null || torneo.getSchedaClassifica().getClassifica() == null || torneo.getSchedaClassifica().getClassifica().isEmpty()){
			return null;
		}

		List<SheetRow> result = new ArrayList<SheetRow>();
		List<RigaClassifica> righeClassifica = torneo.getSchedaClassifica().getClassifica();
		String idTorneo = obtainIdTorneo(torneo);
		Date now = new Date();
		for (RigaClassifica rigaClassifica: righeClassifica){
			int indexGiocatore = getIndexByMap(idPlayersMap, rigaClassifica.getIdGiocatore());
			ClassificheRow classificheRow = new ClassificheRow();
			classificheRow.setIdGiocatore(indexGiocatore);
			classificheRow.setIdTorneo(idTorneo);
			classificheRow.setPosizione(rigaClassifica.getPosizioneGiocatore());
			classificheRow.setPunti(rigaClassifica.getPunteggioFinaleGiocatore().doubleValue());
			classificheRow.setUpdateTime(dfUpdateTime.format(now));
			Integer[] counters = countPartiteByPlayer(indexGiocatore, partiteRow);
			classificheRow.setPartiteGiocate(counters[0]);
			classificheRow.setNumeroVittorie(counters[1]);
			
			result.add(classificheRow);
		}
		
		return result;
	}
	
	private static Integer[] countPartiteByPlayer(Integer idGiocatore, List<SheetRow> partiteRow) {
		Integer[] result = new Integer[]{0,0};
		for (SheetRow sheetRow: partiteRow){
			PartitaRow partitaRow = (PartitaRow) sheetRow;
			if (idGiocatore.equals(partitaRow.getIdGiocatoreVincitore())){
				result[1]++;
			}
			if (
				idGiocatore.equals(partitaRow.getIdGiocatore1())
			||  idGiocatore.equals(partitaRow.getIdGiocatore2())
			||  idGiocatore.equals(partitaRow.getIdGiocatore3())
			||  idGiocatore.equals(partitaRow.getIdGiocatore4())
			||  idGiocatore.equals(partitaRow.getIdGiocatore5())
			){
				result[0]++;
			}
		}
		return result;
	}
}
