package it.desimone.rd3analyzer.service;

import it.desimone.rd3analyzer.Partita;
import it.desimone.rd3analyzer.azioni.Attacco;
import it.desimone.rd3analyzer.azioni.Azione;
import it.desimone.rd3analyzer.azioni.GiocoTris;
import it.desimone.rd3analyzer.azioni.Invasione;
import it.desimone.rd3analyzer.azioni.RicezioneCarta;
import it.desimone.rd3analyzer.azioni.Rinforzo;
import it.desimone.rd3analyzer.azioni.Sdadata;
import it.desimone.rd3analyzer.azioni.Spostamento;
import it.desimone.rd3analyzer.azioni.TempoRimasto;
import it.desimone.rd3analyzer.dao.PartiteRD3AttacchiDAO;
import it.desimone.rd3analyzer.dao.PartiteRD3GiocoTrisDAO;
import it.desimone.rd3analyzer.dao.PartiteRD3InvasioneDAO;
import it.desimone.rd3analyzer.dao.PartiteRD3RicezioneCartaDAO;
import it.desimone.rd3analyzer.dao.PartiteRD3RinforziDAO;
import it.desimone.rd3analyzer.dao.PartiteRD3SdadataDAO;
import it.desimone.rd3analyzer.dao.PartiteRD3SpostamentoDAO;
import it.desimone.rd3analyzer.dao.PartiteRD3TempoRimastoDAO;
import it.desimone.rd3analyzer.database.ConnectionManager;
import it.desimone.rd3analyzer.database.ErroriImportDao;
import it.desimone.rd3analyzer.database.PartiteRD3DAO;
import it.desimone.rd3analyzer.database.PrestigeDao;
import it.desimone.rd3analyzer.dto.PartiteRD3Attacchi;
import it.desimone.rd3analyzer.dto.PartiteRD3GiocoTris;
import it.desimone.rd3analyzer.dto.PartiteRD3Invasione;
import it.desimone.rd3analyzer.dto.PartiteRD3RicezioneCarta;
import it.desimone.rd3analyzer.dto.PartiteRD3Rinforzi;
import it.desimone.rd3analyzer.dto.PartiteRD3Sdadata;
import it.desimone.rd3analyzer.dto.PartiteRD3Spostamento;
import it.desimone.rd3analyzer.dto.PartiteRD3TempoRimasto;
import it.desimone.utils.MyLogger;

import java.sql.Connection;
import java.sql.SQLException;

public class PartiteService {
	
	public static final String TABELLA_PRESTIGIOUS 		= "PartitePrestigious";
	public static final String TABELLA_CHALLENGE 		= "PartiteChallenge";
	public static final String TABELLA_PRESTICHALLENGE 	= "PartitePrestichallenge";
	public static final String TABELLA_DIGITAL 			= "PartiteDigital";
	public static final String TABELLA_ULTRADIGITAL 	= "PartiteUltraDigital";
	public static final String TABELLA_TRESTIGE 		= "PartiteTrestige";
	public static final String TABELLA_PRESTIGE 		= "PartitePrestige";
	
	
	public int inserisciPartita(Partita partita){
		//MyLogger.getLogger().finest("[BEGIN]: "+partita);
		int righeInserite = 0;
		Connection connection = ConnectionManager.getConnection();
		
		try {			
			PartiteRD3DAO partiteRD3DAO = new PartiteRD3DAO(connection);
			
			righeInserite = partiteRD3DAO.inserisciPartita(partita);		
			inserisciRisultatoPartita(connection, partita, false);
			
			if (partita.getAzioniLog() != null && !partita.getAzioniLog().isEmpty()){
				inserisciDettaglioPartita(connection, partita);
			}

			connection.commit();	
		} catch (SQLException e) {
			try {
				connection.rollback();
				MyLogger.getLogger().severe("SQLException per la Partita "+partita+": "+e.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao();
				erroriImportDao.inserisciErrore(partita.getIdPartita(), e);
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		} catch (Exception e) {
			try {
				connection.rollback();
				MyLogger.getLogger().severe("Exception per la Partita "+partita+": "+e.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao();
				erroriImportDao.inserisciErrore(partita.getIdPartita(), e);
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		}
		finally{
			try {
				ConnectionManager.closeConnection();
			} catch (Exception e) {
				MyLogger.getLogger().severe("Exception: "+e.getMessage());
				throw new RuntimeException(e);
			}
		}
		//MyLogger.getLogger().finest("[END]");
		return righeInserite;
	}
	
	
	
	private void inserisciDettaglioPartita(Connection connection, Partita partita){
		if (partita.getAzioniLog() != null && !partita.getAzioniLog().isEmpty()){
			for (Azione azione: partita.getAzioniLog()){
				MyLogger.getLogger().finest(azione.toString());
				
				PartiteRD3AttacchiDAO partiteRD3AttacchiDAO = new PartiteRD3AttacchiDAO(connection);
				PartiteRD3GiocoTrisDAO partiteRD3GiocoTrisDAO = new PartiteRD3GiocoTrisDAO(connection);
				PartiteRD3InvasioneDAO partiteRD3InvasioneDAO = new PartiteRD3InvasioneDAO(connection);
				PartiteRD3RicezioneCartaDAO partiteRD3RicezioneCartaDAO = new PartiteRD3RicezioneCartaDAO(connection);
				PartiteRD3RinforziDAO partiteRD3RinforziDAO = new PartiteRD3RinforziDAO(connection);
				PartiteRD3SpostamentoDAO partiteRD3SpostamentoDAO = new PartiteRD3SpostamentoDAO(connection);
				PartiteRD3TempoRimastoDAO partiteRD3TempoRimastoDAO = new PartiteRD3TempoRimastoDAO(connection);
				PartiteRD3SdadataDAO partiteRD3SdadataDAO = new PartiteRD3SdadataDAO(connection);
				try{
					switch (azione.getTipoAzione()) {
					case ATTACCO:
						PartiteRD3Attacchi partiteRD3Attacchi = new PartiteRD3Attacchi(partita.getIdPartita(), (Attacco)azione);
						partiteRD3AttacchiDAO.inserisciPartita(partiteRD3Attacchi);
						break;
					case GIOCO_TRIS:
						PartiteRD3GiocoTris partiteRD3GiocoTris = new PartiteRD3GiocoTris(partita.getIdPartita(), (GiocoTris)azione);
						partiteRD3GiocoTrisDAO.inserisciPartita(partiteRD3GiocoTris);
						break;
					case INVASIONE:
						PartiteRD3Invasione partiteRD3Invasione = new PartiteRD3Invasione(partita.getIdPartita(), (Invasione)azione);
						partiteRD3InvasioneDAO.inserisciPartita(partiteRD3Invasione);
						break;		
					case RICEZIONE_CARTA:
						PartiteRD3RicezioneCarta partiteRD3RicezioneCarta = new PartiteRD3RicezioneCarta(partita.getIdPartita(), (RicezioneCarta)azione);
						partiteRD3RicezioneCartaDAO.inserisciPartita(partiteRD3RicezioneCarta);
						break;		
					case RINFORZO:
						PartiteRD3Rinforzi partiteRD3Rinforzi = new PartiteRD3Rinforzi(partita.getIdPartita(), (Rinforzo)azione);
						partiteRD3RinforziDAO.inserisciPartita(partiteRD3Rinforzi);
						break;
					case SPOSTAMENTO:
						PartiteRD3Spostamento partiteRD3Spostamento = new PartiteRD3Spostamento(partita.getIdPartita(), (Spostamento)azione);
						partiteRD3SpostamentoDAO.inserisciPartita(partiteRD3Spostamento);
						break;		
					case TEMPO_RIMASTO:
						PartiteRD3TempoRimasto partiteRD3TempoRimasto = new PartiteRD3TempoRimasto(partita.getIdPartita(), (TempoRimasto)azione);
						partiteRD3TempoRimastoDAO.inserisciPartita(partiteRD3TempoRimasto);
						break;			
					case SDADATA:
						PartiteRD3Sdadata partiteRD3Sdadata = new PartiteRD3Sdadata(partita.getIdPartita(), (Sdadata)azione);
						partiteRD3SdadataDAO.inserisciPartita(partiteRD3Sdadata);
						break;
					default:
						break;
					}	
				}catch(Exception e){
					MyLogger.getLogger().severe(azione.toString()+" "+e.getMessage());
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	private void aggiornaDettaglioPartita(Connection connection, Partita partita){
		if (partita.getAzioniLog() != null && !partita.getAzioniLog().isEmpty()){
			for (Azione azione: partita.getAzioniLog()){
				MyLogger.getLogger().finest(azione.toString());
				
				PartiteRD3AttacchiDAO partiteRD3AttacchiDAO = new PartiteRD3AttacchiDAO(connection);
				PartiteRD3GiocoTrisDAO partiteRD3GiocoTrisDAO = new PartiteRD3GiocoTrisDAO(connection);
				PartiteRD3InvasioneDAO partiteRD3InvasioneDAO = new PartiteRD3InvasioneDAO(connection);
				PartiteRD3RicezioneCartaDAO partiteRD3RicezioneCartaDAO = new PartiteRD3RicezioneCartaDAO(connection);
				PartiteRD3RinforziDAO partiteRD3RinforziDAO = new PartiteRD3RinforziDAO(connection);
				PartiteRD3SpostamentoDAO partiteRD3SpostamentoDAO = new PartiteRD3SpostamentoDAO(connection);
				PartiteRD3TempoRimastoDAO partiteRD3TempoRimastoDAO = new PartiteRD3TempoRimastoDAO(connection);
				PartiteRD3SdadataDAO partiteRD3SdadataDAO = new PartiteRD3SdadataDAO(connection);
				try{
					switch (azione.getTipoAzione()) {
					case ATTACCO:
						PartiteRD3Attacchi partiteRD3Attacchi = new PartiteRD3Attacchi(partita.getIdPartita(), (Attacco)azione);
						partiteRD3AttacchiDAO.updatePartita(partiteRD3Attacchi);
						break;
					case GIOCO_TRIS:
						PartiteRD3GiocoTris partiteRD3GiocoTris = new PartiteRD3GiocoTris(partita.getIdPartita(), (GiocoTris)azione);
						partiteRD3GiocoTrisDAO.updatePartita(partiteRD3GiocoTris);
						break;
					case INVASIONE:
						PartiteRD3Invasione partiteRD3Invasione = new PartiteRD3Invasione(partita.getIdPartita(), (Invasione)azione);
						partiteRD3InvasioneDAO.updatePartita(partiteRD3Invasione);
						break;		
					case RICEZIONE_CARTA:
						PartiteRD3RicezioneCarta partiteRD3RicezioneCarta = new PartiteRD3RicezioneCarta(partita.getIdPartita(), (RicezioneCarta)azione);
						int righeAggiornate = partiteRD3RicezioneCartaDAO.updatePartita(partiteRD3RicezioneCarta);
						if (righeAggiornate == 0){
							partiteRD3RicezioneCartaDAO.inserisciPartita(partiteRD3RicezioneCarta);
						}
						break;		
					case RINFORZO:
						PartiteRD3Rinforzi partiteRD3Rinforzi = new PartiteRD3Rinforzi(partita.getIdPartita(), (Rinforzo)azione);
						partiteRD3RinforziDAO.updatePartita(partiteRD3Rinforzi);
						break;
					case SPOSTAMENTO:
						PartiteRD3Spostamento partiteRD3Spostamento = new PartiteRD3Spostamento(partita.getIdPartita(), (Spostamento)azione);
						partiteRD3SpostamentoDAO.updatePartita(partiteRD3Spostamento);
						break;		
					case TEMPO_RIMASTO:
						PartiteRD3TempoRimasto partiteRD3TempoRimasto = new PartiteRD3TempoRimasto(partita.getIdPartita(), (TempoRimasto)azione);
						partiteRD3TempoRimastoDAO.updatePartita(partiteRD3TempoRimasto);
						break;			
					case SDADATA:
						PartiteRD3Sdadata partiteRD3Sdadata = new PartiteRD3Sdadata(partita.getIdPartita(), (Sdadata)azione);
						partiteRD3SdadataDAO.updatePartita(partiteRD3Sdadata);
						break;
					default:
						break;
					}	
				}catch(Exception e){
					MyLogger.getLogger().severe(azione.toString()+" "+e.getMessage());
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public int aggiornaPartita(Partita partita){
		MyLogger.getLogger().finest("[BEGIN]: "+partita);
		int righeAggiornate = 0;
		Connection connection = ConnectionManager.getConnection();
		try {					
			PartiteRD3DAO partiteRD3DAO = new PartiteRD3DAO(connection);

			righeAggiornate = partiteRD3DAO.aggiornaPartita(partita);
			
			righeAggiornate = inserisciRisultatoPartita(connection, partita, true);
			
			if (partita.getAzioniLog() != null && !partita.getAzioniLog().isEmpty()){
				aggiornaDettaglioPartita(connection, partita);
			}

			connection.commit();	
		} catch (SQLException e) {
			try {
				connection.rollback();
				MyLogger.getLogger().severe("SQLException per la Partita "+partita+": "+e.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao();
				erroriImportDao.inserisciErrore(partita.getIdPartita(), e);
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		} catch (Exception e) {
			try {
				connection.rollback();
				MyLogger.getLogger().severe("SQLException per la Partita "+partita+": "+e.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao();
				erroriImportDao.inserisciErrore(partita.getIdPartita(), e);
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		}
		finally{
			try {		
				if (righeAggiornate == 0){
					righeAggiornate = inserisciPartita(partita);
				}else{
					//connection.close();
					ConnectionManager.closeConnection();
				}
			} catch (Exception e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			}
		}
		MyLogger.getLogger().finest("[END]");
		return righeAggiornate;
	}
	
	
	
	private int inserisciRisultatoPartita(Connection connection, Partita partita, boolean update) throws SQLException, Exception{
		//MyLogger.getLogger().finest("[BEGIN]: "+partita+" - "+update);
		int righe = 0;
		PrestigeDao prestigeDao;
		String tipoPartita = partita.getTipoPartita();
		if(tipoPartita.equalsIgnoreCase(Partita.PRESTIGE)){
			prestigeDao = new PrestigeDao(TABELLA_PRESTIGE);
		}else if(tipoPartita.equalsIgnoreCase(Partita.DIGITAL)){
			prestigeDao = new PrestigeDao(TABELLA_DIGITAL);
		}else if(tipoPartita.equalsIgnoreCase(Partita.CHALLENGE)){
			prestigeDao = new PrestigeDao(TABELLA_CHALLENGE);
		}else if(tipoPartita.equalsIgnoreCase(Partita.PRESTICHALLENGE)){
			prestigeDao = new PrestigeDao(TABELLA_PRESTICHALLENGE);
		}else if(tipoPartita.equalsIgnoreCase(Partita.PRESTIGIOUS)){
			prestigeDao = new PrestigeDao(TABELLA_PRESTIGIOUS);
		}else if(tipoPartita.equalsIgnoreCase(Partita.TRESTIGE)){
			prestigeDao = new PrestigeDao(TABELLA_TRESTIGE);
		}else if(tipoPartita.equalsIgnoreCase(Partita.ULTRADIGITAL)){
			prestigeDao = new PrestigeDao(TABELLA_ULTRADIGITAL);
		}else {
			throw new RuntimeException("Tipo partita non riconosciuto: "+tipoPartita);
		}
		if (update){
			righe = prestigeDao.aggiornaPartita(partita, connection);			
		}else{
			righe = prestigeDao.inserisciPartita(partita, connection);
		}
		//MyLogger.getLogger().finest("[END]");
		return righe;
	}
	
	public Long obtainLastMatch(){
		MyLogger.getLogger().finest("[BEGIN]");
		Long lastMatch = null;
		Connection connection = ConnectionManager.getConnection();

		try {			
			PartiteRD3DAO partiteRD3DAO = new PartiteRD3DAO(connection);
			lastMatch = partiteRD3DAO.obtainLastMatch();
		} catch (Exception e) {
			MyLogger.getLogger().severe("Exception: "+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			try {
				ConnectionManager.closeConnection();
			} catch (Exception e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			}
		}
		MyLogger.getLogger().finest("[END]");
		return lastMatch;
	}
	
	public boolean partitaGiaRegistrata(Long idPartita){
		//MyLogger.getLogger().finest("[BEGIN]");
		boolean result = false;
		Connection connection = ConnectionManager.getConnection();
		try {			
			PartiteRD3DAO partiteRD3DAO = new PartiteRD3DAO(connection);
			result = partiteRD3DAO.partitaGiaRegistrata(idPartita);
		} catch (Exception e) {
			MyLogger.getLogger().severe("Exception: "+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			try {
				ConnectionManager.closeConnection();
			} catch (Exception e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			}
		}
		//MyLogger.getLogger().finest("[END]");
		return result;
	}
	
}
