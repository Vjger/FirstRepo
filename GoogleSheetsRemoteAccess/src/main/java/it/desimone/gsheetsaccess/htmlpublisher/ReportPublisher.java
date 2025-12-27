package it.desimone.gsheetsaccess.htmlpublisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Stream;

import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.dto.TorneoPubblicato;
import it.desimone.gsheetsaccess.gsheets.dto.PartitaRow;
import it.desimone.gsheetsaccess.utils.TorneiUtils;
import it.desimone.utils.MyLogger;

public class ReportPublisher {

	private static Map<MatchByYearAndClubKey, List<MatchByYearAndClubValue>> reportTournaments = new HashMap<MatchByYearAndClubKey, List<MatchByYearAndClubValue>>();
	
	public static void main(String[] args) {
		MyLogger.setConsoleLogLevel(Level.INFO);
		Configurator.loadConfiguration(Configurator.Environment.PRODUCTION);
		List<TorneoPubblicato> torneiPubblicati = TorneiUtils.caricamentoTornei("2025");

		Stream<TorneoPubblicato> streamTorneiPubblicati = torneiPubblicati.stream();
		
		streamTorneiPubblicati.forEach(torneoPubblicato -> convertTournamentsIntoMap(torneoPubblicato));
		
		System.out.println(reportTournaments);
	}

	private static void convertTournamentsIntoMap(TorneoPubblicato torneoPubblicato) {
		List<MatchByYearAndClubValue> values;
		MatchByYearAndClubKey matchByYearAndClubKey = new MatchByYearAndClubKey(torneoPubblicato.getTorneoRow().getOrganizzatore());
		if (reportTournaments.containsKey(matchByYearAndClubKey)) {
			values = reportTournaments.get(matchByYearAndClubKey);
		}else {
			values = new ArrayList<MatchByYearAndClubValue>();
		}
		torneoPubblicato.getPartite().stream().forEach(partita -> manageMatch(partita, torneoPubblicato.getTorneoRow().getTipoTorneo(), values));
		reportTournaments.put(matchByYearAndClubKey, values);
	}

	private static void manageMatch(PartitaRow partita, String tipoTorneo, List<MatchByYearAndClubValue> values) {
		String dataPartita = partita.getDataTurno();
		MatchByYearAndClubValue matchByYearAndClubValue = new MatchByYearAndClubValue(dataPartita);
		if (values.contains(matchByYearAndClubValue)) {
			matchByYearAndClubValue = values.get(values.indexOf(matchByYearAndClubValue));
		}else {
			values.add(matchByYearAndClubValue);
		}
		matchByYearAndClubValue.setTipoTorneo(tipoTorneo);
		matchByYearAndClubValue.addNumeroTavoli(1);
	}
}
