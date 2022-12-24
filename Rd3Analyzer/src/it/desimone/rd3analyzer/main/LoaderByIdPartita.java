package it.desimone.rd3analyzer.main;

import it.desimone.rd3analyzer.Analizzatore;
import it.desimone.rd3analyzer.Partita;
import it.desimone.rd3analyzer.database.ErroriImportDao;
import it.desimone.rd3analyzer.database.PartiteDao;
import it.desimone.rd3analyzer.service.PartiteService;
import it.desimone.utils.MyLogger;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;

public class LoaderByIdPartita {

	public static void main(String[] args) {
		MyLogger.getLogger().setLevel(Level.FINEST);
		// TODO Auto-generated method stub
		saveMatch("4036261");
	}

	public static void saveMatch(String idPartita){
		MyLogger.getLogger().fine("[BEGIN] "+idPartita);
		
		PartiteDao partiteDao = new PartiteDao();
		PartiteService partiteService = new PartiteService();	
		Long idPartitaL = Long.valueOf(idPartita);
		boolean partitaGiaRegistrata = partiteDao.partitaGiaRegistrata(idPartitaL);	

			try{
				Partita partita = Analizzatore.elaboraPartita(idPartita, false);

				if (partita != null){
					if (!partitaGiaRegistrata){
						int righeInserite = partiteService.inserisciPartita(partita);
					}else{
						int righeAggiornate = partiteService.aggiornaPartita(partita);
					}
				}else{
					//MyLogger.getLogger().severe("Partita "+partitaGiocatore+" scartata per assenza della scheda");
					throw new IllegalStateException("Partita "+idPartita+" scartata per assenza della scheda o scheda incompleta");
				}
			} catch (IOException ioe) {
				MyLogger.getLogger().severe("IOException per la Partita "+idPartita+": "+ioe.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao(false);
				erroriImportDao.inserisciErrore(idPartitaL, ioe);
			} catch (ParseException pe) {
				MyLogger.getLogger().severe("ParseException per la Partita "+idPartita+": "+pe.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao(false);
				erroriImportDao.inserisciErrore(idPartitaL, pe);
			} catch (IllegalStateException e) {
				//MyLogger.getLogger().info("IllegalStateException per la Partita "+idPartita+": "+e.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao(false);
				erroriImportDao.inserisciErrore(idPartitaL, e);
			} catch (Exception e) {
				MyLogger.getLogger().severe("Exception per la Partita "+idPartita+": "+e.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao(false);
				erroriImportDao.inserisciErrore(idPartitaL, e);
			}

		MyLogger.getLogger().fine("[END] "+idPartita);
	}
	
	
}
