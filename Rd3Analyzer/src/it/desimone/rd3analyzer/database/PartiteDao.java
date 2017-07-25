package it.desimone.rd3analyzer.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.desimone.rd3analyzer.Partita;
import it.desimone.utils.MyLogger;

public class PartiteDao {
	
	public static final String TABELLA_PRESTIGIOUS 		= "PartitePrestigious";
	public static final String TABELLA_CHALLENGE 		= "PartiteChallenge";
	public static final String TABELLA_PRESTICHALLENGE 	= "PartitePrestichallenge";
	public static final String TABELLA_DIGITAL 			= "PartiteDigital";
	public static final String TABELLA_ULTRADIGITAL 	= "PartiteUltraDigital";
	public static final String TABELLA_TRESTIGE 		= "PartiteTrestige";
	public static final String TABELLA_PRESTIGE 		= "PartitePrestige";
	
	private static final DateFormat DATA_SQLITE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private static final String INSERT = 
			"INSERT INTO PartiteRD3 (idPartita, dataInizio, dataFine, tipoPartita, nomePartita, durata, numeroTurni, dataInserimento) VALUES (?,?,?,?,?,?,?, CURRENT_TIMESTAMP)"; 
	
	private static final String UPDATE = 
			"UPDATE PartiteRD3  SET dataInizio = ? , dataFine = ? , tipoPartita = ? , nomePartita = ?, durata = ?, numeroTurni = ?, dataInserimento = CURRENT_TIMESTAMP WHERE idPartita = ?"; 
	
	private static final String LAST_REGISTERED_MATCH = "SELECT idPartita, dataInserimento FROM PartiteRD3 ORDER BY 2 DESC, 1 DESC";
	
	private static final String PARTITA_GIA_REGISTRATA = "SELECT count(*) FROM PartiteRD3 WHERE idPartita = ?";
	
	private static final String ESTRAI_PARTITE_CHALLENGE_DA_A = 
			"SELECT a.idPartita, a.dataInizio, a.dataFine, b.vincitore, "
			+ "b.giocatore1, b.punteggioGiocatore1, b.giocatore2, b.punteggioGiocatore2, "
			+ "b.giocatore3, b.punteggioGiocatore3, b.giocatore4, b.punteggioGiocatore4"
			+ " FROM PartiteRD3 a "
			+ " INNER JOIN PartiteChallenge b on (a.idPartita = b.idPartita) "
			+ "where strftime('%m', dataFine) = ? "
			+ " and  strftime('%d', dataFine) between '01' and '28'"
			+ " ORDER BY a.dataFine ASC";

	public int inserisciPartita(Partita partita){
		//MyLogger.getLogger().finest("[BEGIN]: "+partita);
		boolean psOpened = false;
		int righeInserite = 0;
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		try {						
			preparedStatement = connection.prepareStatement(INSERT);
			psOpened = true;
			preparedStatement.setLong(1, partita.getIdPartita());
			String dataDiInizio = DATA_SQLITE.format(partita.getDataDiInizio());
			preparedStatement.setString(2, dataDiInizio);
			String dataDiFine = null;
			if (partita.getDataDiFine() != null){
				dataDiFine = DATA_SQLITE.format(partita.getDataDiFine());
			}
			//if (dataDiFine != null){
				preparedStatement.setString(3, dataDiFine);
			//}else{
				//preparedStatement.setNull(3, Types.VARCHAR);
			//}
			preparedStatement.setString(4, partita.getTipoPartita());
			//if (partita.getNomePartita() != null){
				preparedStatement.setString(5, partita.getNomePartita());
			//}else{
			//	preparedStatement.setNull(5, Types.VARCHAR);
			//}
			preparedStatement.setInt(6, partita.getDurata());
			if (partita.getTurniPartita() != null){
				preparedStatement.setInt(7, partita.getTurniPartita());
			}else{
				preparedStatement.setNull(7, Types.INTEGER);
			}
			
			righeInserite = preparedStatement.executeUpdate();
			//MyLogger.getLogger().finest("Esecuzione di "+INSERT+"\nRighe Inserite: "+righeInserite);			
			inserisciRisultatoPartita(connection, partita, false);

			connection.commit();	
		} catch (SQLException e) {
			try {
				preparedStatement.close();
				psOpened = false;
				connection.rollback();
				//connection.close();
				MyLogger.getLogger().severe("SQLException per la Partita "+partita+": "+e.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao();
				erroriImportDao.inserisciErrore(partita.getIdPartita(), e);
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		} catch (Exception e) {
			try {
				preparedStatement.close();
				psOpened = false;
				connection.rollback();
				//connection.close();
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
				if (psOpened) preparedStatement.close();
				//connection.close();
				ConnectionManager.closeConnection();
			} catch (SQLException e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			}
		}
		//MyLogger.getLogger().finest("[END]");
		return righeInserite;
	}
	
	public int aggiornaPartita(Partita partita){
		MyLogger.getLogger().finest("[BEGIN]: "+partita);
		boolean psOpened = false;
		int righeAggiornate = 0;
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		try {						
			preparedStatement = connection.prepareStatement(UPDATE);
			psOpened = true;

			String dataDiInizio = DATA_SQLITE.format(partita.getDataDiInizio());
			preparedStatement.setString(1, dataDiInizio);
			String dataDiFine = DATA_SQLITE.format(partita.getDataDiFine());
			preparedStatement.setString(2, dataDiFine);
			preparedStatement.setString(3, partita.getTipoPartita());
			preparedStatement.setString(4, partita.getNomePartita());	
			preparedStatement.setInt(5, partita.getDurata());
			preparedStatement.setInt(6, partita.getTurniPartita());
			preparedStatement.setLong(7, partita.getIdPartita());	
			
			preparedStatement.executeUpdate();
			
			righeAggiornate = inserisciRisultatoPartita(connection, partita, true);
			MyLogger.getLogger().finest("Esecuzione di "+UPDATE+"\nRighe Aggiornate: "+righeAggiornate);
			connection.commit();	
		} catch (SQLException e) {
			try {
				preparedStatement.close();
				psOpened = false;
				connection.rollback();
				//connection.close();
				MyLogger.getLogger().severe("SQLException per la Partita "+partita+": "+e.getMessage());
				ErroriImportDao erroriImportDao = new ErroriImportDao();
				erroriImportDao.inserisciErrore(partita.getIdPartita(), e);
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		} catch (Exception e) {
			try {
				preparedStatement.close();
				psOpened = false;
				connection.rollback();
				//connection.close();
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
				if (psOpened) preparedStatement.close();				
				if (righeAggiornate == 0){
					righeAggiornate = inserisciPartita(partita);
				}else{
					//connection.close();
					ConnectionManager.closeConnection();
				}
			} catch (SQLException e) {
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
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {			
			preparedStatement = connection.prepareStatement(LAST_REGISTERED_MATCH);			
			rs = preparedStatement.executeQuery();
			if (rs != null && rs.next()){		
				lastMatch = rs.getLong(1);
			}
			MyLogger.getLogger().finest("Esecuzione di "+LAST_REGISTERED_MATCH+"\nLast Match: "+lastMatch);
		} catch (SQLException e) {
			MyLogger.getLogger().severe("SQLException: "+e.getMessage());
			throw new RuntimeException(e);
		} catch (Exception e) {
			MyLogger.getLogger().severe("Exception: "+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			try {
				if (rs != null) rs.close();
				if (preparedStatement != null) preparedStatement.close();
				//connection.close();
				ConnectionManager.closeConnection();
			} catch (SQLException e) {
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
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {			
			preparedStatement = connection.prepareStatement(PARTITA_GIA_REGISTRATA);		
			preparedStatement.setLong(1, idPartita);
			rs = preparedStatement.executeQuery();
			if (rs != null && rs.next()){		
				long count = rs.getLong(1);
				result = count == 1;
			}
			//MyLogger.getLogger().finest("Esecuzione di "+PARTITA_GIA_REGISTRATA+"\nresult: "+result);
		} catch (SQLException e) {
			MyLogger.getLogger().severe("SQLException: "+e.getMessage());
			throw new RuntimeException(e);
		} catch (Exception e) {
			MyLogger.getLogger().severe("Exception: "+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			try {
				if (rs != null) rs.close();
				if (preparedStatement != null) preparedStatement.close();
				//connection.close();
				ConnectionManager.closeConnection();
			} catch (SQLException e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			}
		}
		//MyLogger.getLogger().finest("[END]");
		return result;
	}
	
	public List<Partita> estraiPartiteDelMese(int mese, String stanza){
		MyLogger.getLogger().finest("[BEGIN]: "+mese+" "+stanza);
		List<Partita> listaPartite = null;
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {			
			preparedStatement = connection.prepareStatement(ESTRAI_PARTITE_CHALLENGE_DA_A);		
			String meseString = new DecimalFormat("00").format(mese);
			preparedStatement.setString(1,meseString);
			rs = preparedStatement.executeQuery();
			if (rs != null){
				listaPartite = new ArrayList<Partita>();
				while(rs.next()){
					Partita partita 	= new Partita();
					Long idPartita 		= rs.getLong(1);
					Date dataInizio 	= rs.getDate(2);
					Date dataFine 		= rs.getDate(3);
					String vincitore 	= rs.getString(4);
					partita.setIdPartita(idPartita);
					partita.setDataDiInizio(dataInizio);
					partita.setDataDiFine(dataFine);
					partita.setVincitore(vincitore);
					Map<String,Integer> risultati = new HashMap<String,Integer>();
					for (int i=5 ; i <=12; i++){
						risultati.put(rs.getString(i), rs.getInt(i++));
					}
					partita.setRisultati(risultati);
					listaPartite.add(partita);
				}
			}
			MyLogger.getLogger().finest("Esecuzione di "+ESTRAI_PARTITE_CHALLENGE_DA_A);
		} catch (SQLException e) {
			MyLogger.getLogger().severe("SQLException: "+e.getMessage());
			throw new RuntimeException(e);
		} catch (Exception e) {
			MyLogger.getLogger().severe("Exception: "+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			try {
				if (rs != null) rs.close();
				if (preparedStatement != null) preparedStatement.close();
				//connection.close();
				ConnectionManager.closeConnection();
			} catch (SQLException e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			}
		}
		MyLogger.getLogger().finest("[END]");
		return listaPartite;
	}
	
	
}
