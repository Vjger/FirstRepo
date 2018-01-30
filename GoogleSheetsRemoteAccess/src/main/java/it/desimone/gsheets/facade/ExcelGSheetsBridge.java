package it.desimone.gsheets.facade;

import it.desimone.gsheets.dto.TorneiRow;
import it.desimone.risiko.torneo.dto.SchedaTorneo;
import it.desimone.risiko.torneo.dto.SchedaTurno;
import it.desimone.risiko.torneo.dto.Torneo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
		String idTorneo = obtainIdTorneo(schedaTorneo.getOrganizzatore(), schedaTorneo.getDataTurni());
		torneiRow.setIdTorneo(idTorneo);
		torneiRow.setNomeTorneo(schedaTorneo.getNomeTorneo());
		torneiRow.setOrganizzatore(schedaTorneo.getOrganizzatore());
		torneiRow.setSede(schedaTorneo.getSedeTorneo());
		torneiRow.setTipoTorneo(schedaTorneo.getTipoTorneo().getTipoTorneo());
		if (schedaTorneo.getDataTurni() != null && !schedaTorneo.getDataTurni().isEmpty()){
			Date dataInizioTorneo = schedaTorneo.getDataTurni().get(0);
			Date dataFineTorneo = schedaTorneo.getDataTurni().get(schedaTorneo.getDataTurni().size()-1);
			torneiRow.setStartDate(dfDateTorneo.format(dataInizioTorneo));
			torneiRow.setEndDate(dfDateTorneo.format(dataFineTorneo));
		}
		torneiRow.setNote(schedaTorneo.getNote());
		torneiRow.setNumeroTurni(schedaTorneo.getNumeroTurni());
		if (torneo.getPartecipanti() != null){
			torneiRow.setNumeroPartecipanti(torneo.getPartecipanti().size());
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
		torneiRow.setUpdateTime(dfUpdateTime.format(new Date()));
		return torneiRow;
	}
	
	private static String obtainIdTorneo(String organizzatore, List<Date> dateTurni){
		String result = null;
		if (organizzatore != null && dateTurni != null && !dateTurni.isEmpty()){
			result = dfIdTorneo.format(dateTurni.get(0))+" - "+organizzatore;
		}
		return result;
	}
}
