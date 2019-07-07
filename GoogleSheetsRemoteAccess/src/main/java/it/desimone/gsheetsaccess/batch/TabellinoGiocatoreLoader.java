package it.desimone.gsheetsaccess.batch;

import it.desimone.gsheetsaccess.common.ResourceWorking;
import it.desimone.gsheetsaccess.gsheets.dto.AnagraficaGiocatoreRidottaRow;
import it.desimone.gsheetsaccess.gsheets.dto.TabellinoGiocatore;
import it.desimone.gsheetsaccess.gsheets.dto.TorneiRow;
import it.desimone.gsheetsaccess.utils.TorneiUtils;
import it.desimone.utils.MyLogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class TabellinoGiocatoreLoader {
	
	private static final String playerData = "\nIl giocatore [%s] %s %s nato il %s ha giocato i seguenti tornei: ";
	private static final String playerDataSenzaData = "\nIl giocatore [%s] %s %s ha giocato i seguenti tornei: ";
	private static final String tournamentData = "%20s organizzato da %34s tra il %s e il %s";

	private static Path  inputPath = Paths.get(ResourceWorking.tabellinoLoaderInputAreaPath(), "tabellinoLoaderInput.txt");
	private static Path outputPath = Paths.get(ResourceWorking.tabellinoLoaderOutputAreaPath(), "tabellinoLoaderOutput.txt");
	
	public static void main(String[] args) {
		MyLogger.setConsoleLogLevel(Level.INFO);
		MyLogger.getLogger().info("START");
		List<String> ids = getInputId();
		if (ids != null){
			
			FileOutputStream fos = null;
			BufferedWriter bw    = null;
			try{
				File outFile = new File(ResourceWorking.tabellinoLoaderOutputAreaPath(), "tabellinoLoaderOutput.txt");
				fos = new FileOutputStream(outFile);
				bw = new BufferedWriter(new OutputStreamWriter(fos));
				
				MyLogger.getLogger().info("Elaborazione di "+ids.size()+" id player");
				List<TabellinoGiocatore> tabellini = new ArrayList<TabellinoGiocatore>();
				for (String id: ids){
					TabellinoGiocatore tabellino = TorneiUtils.getTabellinoPlayer(Integer.valueOf(id.trim()), "2019");
					tabellini.add(tabellino);
				}
	
				for (TabellinoGiocatore tabellino: tabellini){
					AnagraficaGiocatoreRidottaRow anagraficaRidottaGiocatoreRowFrom = tabellino.getAnagraficaRidottaGiocatoreRowFrom();
					Set<TorneiRow> torneiGiocati = tabellino.getTorneiGiocati();
					String player = String.format(playerDataSenzaData, anagraficaRidottaGiocatoreRowFrom.getId(),anagraficaRidottaGiocatoreRowFrom.getNome(), anagraficaRidottaGiocatoreRowFrom.getCognome(), anagraficaRidottaGiocatoreRowFrom.getDataDiNascita()); 
					//MyLogger.getLogger().info(player);
					try {
						//Files.write(outputPath, player.getBytes(), StandardOpenOption.APPEND);
						bw.write(player);
						bw.newLine();
						for (TorneiRow torneoRow: torneiGiocati){
							String torneo = String.format(tournamentData, torneoRow.getTipoTorneo(), torneoRow.getOrganizzatore(), torneoRow.getStartDate(), torneoRow.getEndDate());
							//Files.write(outputPath, torneo.getBytes(), StandardOpenOption.APPEND);
							//MyLogger.getLogger().info(torneo);
							bw.write(torneo);
							bw.newLine();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}catch(IOException ioe){
				MyLogger.getLogger().severe(ioe.getMessage());
			}finally{
				try {
					//fos.close();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		MyLogger.getLogger().info("END");
	}

	private static List<String> getInputId(){
		List<String> ids = null;	
		try {
			ids = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;
	}
}
