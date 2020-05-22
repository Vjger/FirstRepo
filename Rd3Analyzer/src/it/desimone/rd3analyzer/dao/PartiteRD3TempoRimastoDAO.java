package it.desimone.rd3analyzer.dao;

import it.desimone.rd3analyzer.dto.PartiteRD3TempoRimasto;
import it.desimone.utils.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PartiteRD3TempoRimastoDAO extends AbstractDAO {

	public PartiteRD3TempoRimastoDAO(Connection connection) {
		super(connection);
	}

	private static final String INSERT = 
			"INSERT INTO PartiteRD3TempoRimasto "
			+ "(idPartita, logId, ColoreGiocatore, Time, MinutiRimasti, SecondiRimasti) "
			+ "VALUES (?,?,?,?,?,?)"; 
	
	private static final String UPDATE = 
			"UPDATE PartiteRD3TempoRimasto "
			+ "SET ColoreGiocatore = ?, Time = ?, MinutiRimasti = ?, SecondiRimasti = ? "
			+ "WHERE idPartita = ? AND logId = ?";
	
	public int inserisciPartita(PartiteRD3TempoRimasto partita){
		//MyLogger.getLogger().finest("[BEGIN]: "+partita);
		boolean psOpened = false;
		int righeInserite = 0;
		PreparedStatement preparedStatement = null;
		try {						
			preparedStatement = connection.prepareStatement(INSERT);
			psOpened = true;
			preparedStatement.setLong(1, partita.getIdPartita());
			preparedStatement.setInt(2, partita.getLogId());
			preparedStatement.setString(3, partita.getColoreGiocatore());
			preparedStatement.setString(4, partita.getTime());
			preparedStatement.setInt(5, partita.getMinutiRimasti());
			preparedStatement.setInt(6, partita.getSecondiRimasti());
			
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
	
	public int updatePartita(PartiteRD3TempoRimasto partita){
		//MyLogger.getLogger().finest("[BEGIN]: "+partita);
		boolean psOpened = false;
		int righeInserite = 0;
		PreparedStatement preparedStatement = null;
		try {						
			preparedStatement = connection.prepareStatement(UPDATE);
			psOpened = true;

			preparedStatement.setString(1, partita.getColoreGiocatore());
			preparedStatement.setString(2, partita.getTime());
			preparedStatement.setInt(3, partita.getMinutiRimasti());
			preparedStatement.setInt(4, partita.getSecondiRimasti());
			preparedStatement.setLong(5, partita.getIdPartita());
			preparedStatement.setInt(6, partita.getLogId());
			
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
	

	
}
