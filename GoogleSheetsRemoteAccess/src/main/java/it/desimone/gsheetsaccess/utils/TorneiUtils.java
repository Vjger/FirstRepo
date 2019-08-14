package it.desimone.gsheetsaccess.utils;

import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.dto.TorneoPubblicato;
import it.desimone.gsheetsaccess.gsheets.dto.AnagraficaGiocatoreRidottaRow;
import it.desimone.gsheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.gsheetsaccess.gsheets.dto.ClassificheRow;
import it.desimone.gsheetsaccess.gsheets.dto.PartitaRow;
import it.desimone.gsheetsaccess.gsheets.dto.SheetRow;
import it.desimone.gsheetsaccess.gsheets.dto.SheetRowFactory.SheetRowType;
import it.desimone.gsheetsaccess.gsheets.dto.TabellinoGiocatore;
import it.desimone.gsheetsaccess.gsheets.dto.TorneiRow;
import it.desimone.gsheetsaccess.gsheets.facade.GSheetsInterface;
import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TorneiUtils {

	
	public static List<TorneoPubblicato> caricamentoTornei(String year){
		MyLogger.getLogger().info("START");
		
		List<TorneiRow> torneiRow = getAllTornei(year);
		MyLogger.getLogger().info("Caricati "+torneiRow.size()+" tornei");
		
		List<PartitaRow> partiteRow = getAllPartite(year);
		MyLogger.getLogger().info("Caricate "+partiteRow.size()+" partite");
		
		List<ClassificheRow> classificheRow = getAllClassifiche(year);
		MyLogger.getLogger().info("Caricate "+classificheRow.size()+" righe classifica");
		
		List<TorneoPubblicato> result = null;
		if (torneiRow != null){
			result = new ArrayList<TorneoPubblicato>();
			for (TorneiRow torneoRow: torneiRow){
				TorneoPubblicato torneoPubblicato = new TorneoPubblicato(torneoRow);
				result.add(torneoPubblicato);
			}
			
			MyLogger.getLogger().info("Inizio associazione partite a tornei");
			if (partiteRow != null){
				for (PartitaRow partitaRow: partiteRow){
					TorneiRow torneoRicerca = new TorneiRow();
					torneoRicerca.setIdTorneo(partitaRow.getIdTorneo());
					int index = result.indexOf(new TorneoPubblicato(torneoRicerca));
					if (index >= 0){
						TorneoPubblicato torneoPubblicato = result.get(index);
						torneoPubblicato.add(partitaRow);
					}
				}
			}	
			MyLogger.getLogger().info("Inizio associazione classifiche a tornei");
			if (classificheRow != null){
				for (ClassificheRow classificaRow: classificheRow){
					TorneiRow torneoRicerca = new TorneiRow();
					torneoRicerca.setIdTorneo(classificaRow.getIdTorneo());
					int index = result.indexOf(new TorneoPubblicato(torneoRicerca));
					if (index >= 0){
						TorneoPubblicato torneoPubblicato = result.get(index);
						torneoPubblicato.add(classificaRow);
					}
				}
			}
		}

		MyLogger.getLogger().info("END");
		
		return result;
	}
	
	public static List<TorneiRow> getAllTornei(String year){
		String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
		List<TorneiRow> result = null;
		try{
			result = GSheetsInterface.getAllRows(spreadSheetIdTornei, SheetRowType.Torneo);
		}catch(IOException ioe){
			MyLogger.getLogger().severe("Eccezione: "+ioe.getMessage());
		}
		return result;
	}
	
	public static List<PartitaRow> getAllPartite(String year){
		String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
		List<PartitaRow> result = null;
		try{
			result = GSheetsInterface.getAllRows(spreadSheetIdTornei, SheetRowType.Partita);
		}catch(IOException ioe){
			MyLogger.getLogger().severe("Eccezione: "+ioe.getMessage());
		}
		return result;
	}
	
	public static List<ClassificheRow> getAllClassifiche(String year){
		String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
		List<ClassificheRow> result = null;
		try{
			result = GSheetsInterface.getAllRows(spreadSheetIdTornei, SheetRowType.Classifica);
		}catch(IOException ioe){
			MyLogger.getLogger().severe("Eccezione: "+ioe.getMessage());
		}	
		return result;
	}
	
	public static List<AnagraficaGiocatoreRow> getAllAnagraficheGiocatori(String year){
		String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
		List<AnagraficaGiocatoreRow> result = null;
		try{
			result = GSheetsInterface.getAllRows(spreadSheetIdTornei, SheetRowType.AnagraficaGiocatore);
		}catch(IOException ioe){
			MyLogger.getLogger().severe("Eccezione: "+ioe.getMessage());
		}	
		return result;
	}
	
	public static List<AnagraficaGiocatoreRidottaRow> getAllAnagraficheGiocatoriRidotte(String year){
		String spreadSheetIdAnagrafiche = Configurator.getAnagraficaRidottaSheetId(year);
		List<AnagraficaGiocatoreRidottaRow> result = null;
		try{
			result = GSheetsInterface.getAllRows(spreadSheetIdAnagrafiche, SheetRowType.AnagraficaGiocatoreRidotta);
		}catch(IOException ioe){
			MyLogger.getLogger().severe("Eccezione: "+ioe.getMessage());
		}	
		return result;
	}
	
	public static boolean haPartecipato(PartitaRow partitaRow, Integer idPlayer){
		boolean result = 
				( partitaRow.getIdGiocatore1()!= null && partitaRow.getIdGiocatore1().equals(idPlayer) )
			||  ( partitaRow.getIdGiocatore2()!= null && partitaRow.getIdGiocatore2().equals(idPlayer) )
			||  ( partitaRow.getIdGiocatore3()!= null && partitaRow.getIdGiocatore3().equals(idPlayer) )
			||  ( partitaRow.getIdGiocatore4()!= null && partitaRow.getIdGiocatore4().equals(idPlayer) )
			||  ( partitaRow.getIdGiocatore5()!= null && partitaRow.getIdGiocatore5().equals(idPlayer) );
		return result;
	}
	
	public static boolean isVincitore(PartitaRow partitaRow, Integer idPlayer){
		boolean result = 
				partitaRow.getIdGiocatoreVincitore()!= null && partitaRow.getIdGiocatoreVincitore().equals(idPlayer);
		return result;
	}
	
	
	public static TabellinoGiocatore[] getTabelliniPlayer(Integer idPlayerFrom, Integer idPlayerTo, String year){
		MyLogger.getLogger().info("Inizio estrazione tabellini giocatore con id ["+idPlayerFrom+"] e id ["+idPlayerTo+"] per l'anno "+year);
		TabellinoGiocatore tabellinoGiocatoreFrom = getTabellinoPlayer(idPlayerFrom, year);
		TabellinoGiocatore tabellinoGiocatoreTo = getTabellinoPlayer(idPlayerTo, year);
		
		return new TabellinoGiocatore[]{tabellinoGiocatoreFrom, tabellinoGiocatoreTo};
	}
	
	public static TabellinoGiocatore getTabellinoPlayer(Integer idPlayer, String year){
		MyLogger.getLogger().info("Inizio estrazione tabellino giocatore con id ["+idPlayer+"] per l'anno "+year);
		TabellinoGiocatore tabellinoGiocatore = null;
		
		try {
			String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
			String spreadSheetAnagraficaRidotta = Configurator.getAnagraficaRidottaSheetId(year);

			PartitaRow partitaRow = new PartitaRow();
			partitaRow.setIdGiocatoreVincitore(idPlayer);
			List<SheetRow> righePartiteGiocatore = GSheetsInterface.findPartiteRowsByIdGiocatore(spreadSheetIdTornei, partitaRow);
			MyLogger.getLogger().info("Estratte "+(righePartiteGiocatore==null?0:righePartiteGiocatore.size())+" righe Partita per il giocatore con ID ["+idPlayer+"]");

			Set<TorneiRow> torneiGiocati = new HashSet<TorneiRow>();
			if (righePartiteGiocatore != null && !righePartiteGiocatore.isEmpty()){
				String sheetNameTornei = TorneiRow.SHEET_TORNEI_NAME;
				for (SheetRow matchRow: righePartiteGiocatore){
					String idTorneo = ((PartitaRow) matchRow).getIdTorneo();
					
					TorneiRow torneoRow = new TorneiRow();
					torneoRow.setIdTorneo(idTorneo);

					if (!torneiGiocati.contains(torneoRow)){
						torneoRow = (TorneiRow) GSheetsInterface.findTorneoRowByIdTorneo(spreadSheetIdTornei, sheetNameTornei, torneoRow);
						if (torneoRow != null){
							torneiGiocati.add(torneoRow);
						}
					}
				}
			}
			
			AnagraficaGiocatoreRidottaRow anagraficaGiocatoreRow = new AnagraficaGiocatoreRidottaRow();
			anagraficaGiocatoreRow.setId(idPlayer);
			List<AnagraficaGiocatoreRidottaRow> anagraficheRidotteRowFound = GSheetsInterface.findAnagraficheRidotteById2(spreadSheetAnagraficaRidotta, Collections.singletonList(anagraficaGiocatoreRow));
			
			tabellinoGiocatore = new TabellinoGiocatore((AnagraficaGiocatoreRidottaRow) ((anagraficheRidotteRowFound != null && !anagraficheRidotteRowFound.isEmpty())?anagraficheRidotteRowFound.get(0):null), righePartiteGiocatore, torneiGiocati);
			
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore accedendo ai dati "+e.getMessage());
		}
		
		return tabellinoGiocatore;
	}
	
	public static void mergePlayer(Integer idPlayerFrom, Integer idPlayerTo, String year){
		MyLogger.getLogger().info("Inizio merge dati da giocatore con id ["+idPlayerFrom+"] a giocatore con id ["+idPlayerTo+"]");
		
		try {
			String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
			String spreadSheetAnagraficaRidotta = Configurator.getAnagraficaRidottaSheetId(year);
			ClassificheRow classificheRow = new ClassificheRow();
			classificheRow.setIdGiocatore(idPlayerFrom);
			List<SheetRow> righeClassificaGiocatore = GSheetsInterface.findClassificaRowsByIdGiocatore(spreadSheetIdTornei, classificheRow);
			
			MyLogger.getLogger().info("Estratte "+(righeClassificaGiocatore==null?0:righeClassificaGiocatore.size())+" righe Classifica per il giocatore con ID ["+idPlayerFrom+"]");
			if (righeClassificaGiocatore != null) MyLogger.getLogger().info(righeClassificaGiocatore.toString());
			
			PartitaRow partitaRow = new PartitaRow();
			partitaRow.setIdGiocatoreVincitore(idPlayerFrom);
			List<SheetRow> righePartiteGiocatore = GSheetsInterface.findPartiteRowsByIdGiocatore(spreadSheetIdTornei, partitaRow);
			
			MyLogger.getLogger().info("Estratte "+(righePartiteGiocatore==null?0:righePartiteGiocatore.size())+" righe Partita per il giocatore con ID ["+idPlayerFrom+"]");
			if (righePartiteGiocatore != null) MyLogger.getLogger().info(righePartiteGiocatore.toString());
			
			for (SheetRow rigaClassifica: righeClassificaGiocatore){
				ClassificheRow riga = (ClassificheRow) rigaClassifica;
				riga.setIdGiocatore(idPlayerTo);
			}
			
			for (SheetRow rigaPartite: righePartiteGiocatore){
				PartitaRow riga = (PartitaRow) rigaPartite;
				if (riga.getIdGiocatore1() != null && riga.getIdGiocatore1().equals(idPlayerFrom)){
					riga.setIdGiocatore1(idPlayerTo);
				}
				if (riga.getIdGiocatore2() != null && riga.getIdGiocatore2().equals(idPlayerFrom)){
					riga.setIdGiocatore2(idPlayerTo);
				}
				if (riga.getIdGiocatore3() != null && riga.getIdGiocatore3().equals(idPlayerFrom)){
					riga.setIdGiocatore3(idPlayerTo);
				}
				if (riga.getIdGiocatore4() != null && riga.getIdGiocatore4().equals(idPlayerFrom)){
					riga.setIdGiocatore4(idPlayerTo);
				}
				if (riga.getIdGiocatore5() != null && riga.getIdGiocatore5().equals(idPlayerFrom)){
					riga.setIdGiocatore5(idPlayerTo);
				}
				if (riga.getIdGiocatoreVincitore() != null && riga.getIdGiocatoreVincitore().equals(idPlayerFrom)){
					riga.setIdGiocatoreVincitore(idPlayerTo);
				}
			}
			
			GSheetsInterface.updateRows(spreadSheetIdTornei, ClassificheRow.SHEET_CLASSIFICHE, righeClassificaGiocatore, true);
			MyLogger.getLogger().info("Sostituito nelle righe classifica il giocatore con ID ["+idPlayerFrom+"] con quello con ID ["+idPlayerTo+"]");
			GSheetsInterface.updateRows(spreadSheetIdTornei, PartitaRow.SHEET_PARTITE_NAME, righePartiteGiocatore, true);
			MyLogger.getLogger().info("Sostituito nelle righe partita il giocatore con ID ["+idPlayerFrom+"] con quello con ID ["+idPlayerTo+"]");
			
			//INizio cancellazione giocatore
			SheetRow anagraficaGiocatoreRowFrom = new AnagraficaGiocatoreRow();
			((AnagraficaGiocatoreRow)anagraficaGiocatoreRowFrom).setId(idPlayerFrom);
			List<SheetRow> anagraficheDaCancellareRowFound = GSheetsInterface.findAnagraficheByKey(spreadSheetIdTornei, Collections.singletonList(anagraficaGiocatoreRowFrom));
			
			if (anagraficheDaCancellareRowFound != null && !anagraficheDaCancellareRowFound.isEmpty()){
				List<Integer> rowNumberGiocatoreFrom = new ArrayList<Integer>();
				rowNumberGiocatoreFrom.add(anagraficheDaCancellareRowFound.get(0).getSheetRowNumber());
				GSheetsInterface.deleteRowsByNumRow(spreadSheetIdTornei, AnagraficaGiocatoreRow.SHEET_GIOCATORI_NAME, rowNumberGiocatoreFrom);
				MyLogger.getLogger().info("Cancellato il giocatore con ID ["+idPlayerFrom+"] dal foglio Tornei");
			}
			
			SheetRow anagraficaRidottaGiocatoreRowFrom = new AnagraficaGiocatoreRidottaRow();
			((AnagraficaGiocatoreRidottaRow)anagraficaRidottaGiocatoreRowFrom).setId(idPlayerFrom);
			List<SheetRow> anagraficheRidotteDaCancellareRowFound = GSheetsInterface.findAnagraficheByKey(spreadSheetAnagraficaRidotta, Collections.singletonList(anagraficaGiocatoreRowFrom));
			
			if (anagraficheRidotteDaCancellareRowFound != null && !anagraficheRidotteDaCancellareRowFound.isEmpty()){
				List<Integer> rowNumberGiocatoreFrom = new ArrayList<Integer>();
				rowNumberGiocatoreFrom.add(anagraficheRidotteDaCancellareRowFound.get(0).getSheetRowNumber());
				GSheetsInterface.deleteRowsByNumRow(spreadSheetAnagraficaRidotta, AnagraficaGiocatoreRidottaRow.SHEET_ANAGRAFICA_NAME, rowNumberGiocatoreFrom);
				MyLogger.getLogger().info("Cancellato il giocatore con ID ["+idPlayerFrom+"] dal foglio Anagrafica Ridotta");
			}
			
		}catch(Exception e){
			MyLogger.getLogger().severe("Errore accedendo ai dati "+e.getMessage());
		}
	}
	
		
	public static void deleteTorneo(String idTorneo, String year){
		MyLogger.getLogger().info("INIZIO Cancellazione del torneo ["+idTorneo+"]");
		
		try {
			deletePartiteTorneoRows(idTorneo, year);
			deleteClassificaTorneoRows(idTorneo, year);
			deleteTorneoRow(idTorneo, year);
		} catch (IOException e) {
			MyLogger.getLogger().severe("Errore accedendo ai dati del torneo ["+idTorneo+"] - "+e.getMessage());
		}
		
		MyLogger.getLogger().info("FINE Cancellazione del torneo ["+idTorneo+"]");
	}
	
	private static void deletePartiteTorneoRows(String idTorneo, String year) throws IOException{
		MyLogger.getLogger().info("Inizio cancellazione righe partita del torneo ["+idTorneo+"]");
		String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
		String sheetNamePartite = PartitaRow.SHEET_PARTITE_NAME;
		
		//Basta un oggetto: tanto l'id del torneo è sempre lo stesso.
		PartitaRow partitaRowDiRicerca = new PartitaRow();
		partitaRowDiRicerca.setIdTorneo(idTorneo);
		List<Integer> partiteRowFound = GSheetsInterface.findNumPartiteRowsByIdTorneo(spreadSheetIdTornei, partitaRowDiRicerca);

		if (partiteRowFound != null && !partiteRowFound.isEmpty()){
			MyLogger.getLogger().info("Cancellazione di "+partiteRowFound.size()+" partite del torneo "+idTorneo);
			GSheetsInterface.deleteRowsByNumRow(spreadSheetIdTornei, sheetNamePartite, partiteRowFound);
		}
	}
	
	private static void deleteClassificaTorneoRows(String idTorneo, String year) throws IOException{
		MyLogger.getLogger().info("Inizio cancellazione righe classifica del torneo ["+idTorneo+"]");
		String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
		String sheetNameClassifiche = ClassificheRow.SHEET_CLASSIFICHE;
		
		ClassificheRow classificheRowDiRicerca = new ClassificheRow();
		classificheRowDiRicerca.setIdTorneo(idTorneo);
		List<Integer> classificheRowFound = GSheetsInterface.findClassificaRowsByIdTorneo(spreadSheetIdTornei, classificheRowDiRicerca);

		if (classificheRowFound != null && !classificheRowFound.isEmpty()){
			MyLogger.getLogger().info("Cancellazione di "+classificheRowFound.size()+" giocatori in classifica del torneo "+idTorneo);
			GSheetsInterface.deleteRowsByNumRow(spreadSheetIdTornei, sheetNameClassifiche, classificheRowFound);
		}
	}
	
	private static void deleteTorneoRow(String idTorneo, String year) throws IOException{
		MyLogger.getLogger().info("Inizio cancellazione riga del Torneo ["+idTorneo+"]");
		TorneiRow torneoRow = new TorneiRow();
		torneoRow.setIdTorneo(idTorneo);
		
		String spreadSheetIdTornei = Configurator.getTorneiSheetId(year);
		String sheetNameTornei = TorneiRow.SHEET_TORNEI_NAME;
		Integer torneoRowFound = GSheetsInterface.findNumTorneoRowByIdTorneo(spreadSheetIdTornei, sheetNameTornei, torneoRow);
		
		if (torneoRowFound != null){
			List<Integer> partitaList = new ArrayList<Integer>();
			partitaList.add(torneoRowFound);
			GSheetsInterface.deleteRowsByNumRow(spreadSheetIdTornei, sheetNameTornei, partitaList);
		}else{
			MyLogger.getLogger().severe("Non trovato il torneo ["+idTorneo+"]");
		}
	}
			
}
