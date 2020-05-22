package it.desimone.rd3analyzer.database;

import it.desimone.rd3analyzer.Partita;
import it.desimone.rd3analyzer.dao.AbstractDAO;
import it.desimone.utils.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PartiteRD3DAO extends AbstractDAO{
	
	public PartiteRD3DAO(Connection connection) {
		super(connection);
	}

	private static final DateFormat DATA_SQLITE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private static final String INSERT = 
			"INSERT INTO PartiteRD3 (idPartita, dataInizio, dataFine, tipoPartita, nomePartita, durata, numeroTurni, dataInserimento) VALUES (?,?,?,?,?,?,?, CURRENT_TIMESTAMP)"; 
	
	private static final String UPDATE = 
			"UPDATE PartiteRD3  SET dataInizio = ? , dataFine = ? , tipoPartita = ? , nomePartita = ?, durata = ?, numeroTurni = ?, dataInserimento = CURRENT_TIMESTAMP WHERE idPartita = ?"; 
	
	private static final String LAST_REGISTERED_MATCH = "SELECT idPartita, dataInserimento FROM PartiteRD3 ORDER BY 2 DESC, 1 DESC";
	
	private static final String PARTITA_GIA_REGISTRATA = "SELECT count(*) FROM PartiteRD3 WHERE idPartita = ?";
		
	public int inserisciPartita(Partita partita){
		//MyLogger.getLogger().finest("[BEGIN]: "+partita);
		boolean psOpened = false;
		int righeInserite = 0;
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

		} catch (SQLException e) {
			try {
				preparedStatement.close();
				psOpened = false;
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		} catch (Exception e) {
			try {
				preparedStatement.close();
				psOpened = false;
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		}
		finally{
			try {
				if (psOpened) preparedStatement.close();
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
			if (partita.getTurniPartita() != null){
				preparedStatement.setInt(6, partita.getTurniPartita());
			}else{
				preparedStatement.setNull(6, Types.INTEGER);
			}
			preparedStatement.setLong(7, partita.getIdPartita());	
			
			preparedStatement.executeUpdate();
			
			MyLogger.getLogger().finest("Esecuzione di "+UPDATE+"\nRighe Aggiornate: "+righeAggiornate);
		} catch (SQLException e) {
			try {
				preparedStatement.close();
				psOpened = false;
				MyLogger.getLogger().severe("SQLException per la Partita "+partita+": "+e.getMessage());
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		} catch (Exception e) {
			try {
				preparedStatement.close();
				psOpened = false;
				MyLogger.getLogger().severe("SQLException per la Partita "+partita+": "+e.getMessage());
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		}
		finally{
			try {
				if (psOpened) preparedStatement.close();				
			} catch (SQLException e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			}
		}
		MyLogger.getLogger().finest("[END]");
		return righeAggiornate;
	}
	
	
	public Long obtainLastMatch(){
		MyLogger.getLogger().finest("[BEGIN]");
		Long lastMatch = null;
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
			} catch (SQLException e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			}
		}
		//MyLogger.getLogger().finest("[END]");
		return result;
	}
	
	
}
