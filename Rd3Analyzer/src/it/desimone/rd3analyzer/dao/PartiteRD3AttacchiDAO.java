package it.desimone.rd3analyzer.dao;

import it.desimone.rd3analyzer.dto.PartiteRD3Attacchi;
import it.desimone.utils.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class PartiteRD3AttacchiDAO extends AbstractDAO {

	public PartiteRD3AttacchiDAO(Connection connection) {
		super(connection);
	}

	private static final String INSERT = 
			"INSERT INTO PartiteRD3Attacchi "
			+ "(idPartita, logId, ColoreGiocatore, Time, TerritorioAttaccante, TerritorioAttaccato, Dado_1_Attacco, Dado_1_Difesa, Dado_2_Attacco, Dado_2_Difesa, Dado_3_Attacco, Dado_3_Difesa) "
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"; 
	
	
	private static final String UPDATE = 
			"UPDATE PartiteRD3Attacchi "
			+ "SET ColoreGiocatore = ?, Time = ?, TerritorioAttaccante = ?, TerritorioAttaccato = ?, Dado_1_Attacco = ?, Dado_1_Difesa = ?, Dado_2_Attacco = ?, Dado_2_Difesa = ?, Dado_3_Attacco = ?, Dado_3_Difesa = ? "
			+ "WHERE idPartita = ? AND logId = ?"; 

	public int inserisciPartita(PartiteRD3Attacchi partita){
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
			preparedStatement.setString(5, partita.getTerritorioAttaccante());
			preparedStatement.setString(6, partita.getTerritorioAttaccato());
			preparedStatement.setInt(7, partita.getDado1Attacco());
			preparedStatement.setInt(8, partita.getDado1Difesa());
			if (partita.getDado2Attacco() != null){
				preparedStatement.setInt(9, partita.getDado2Attacco());
			}else{
				preparedStatement.setNull(9, Types.INTEGER);
			}
			if (partita.getDado2Difesa() != null){
				preparedStatement.setInt(10, partita.getDado2Difesa());
			}else{
				preparedStatement.setNull(10, Types.INTEGER);
			}
			if (partita.getDado3Attacco() != null){
				preparedStatement.setInt(11, partita.getDado3Attacco());
			}else{
				preparedStatement.setNull(11, Types.INTEGER);
			}
			if (partita.getDado3Difesa() != null){
				preparedStatement.setInt(12, partita.getDado3Difesa());
			}else{
				preparedStatement.setNull(12, Types.INTEGER);
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
	
	public int updatePartita(PartiteRD3Attacchi partita){
		//MyLogger.getLogger().finest("[BEGIN]: "+partita);
		boolean psOpened = false;
		int righeInserite = 0;
		PreparedStatement preparedStatement = null;
		try {						
			preparedStatement = connection.prepareStatement(UPDATE);
			psOpened = true;

			preparedStatement.setString(1, partita.getColoreGiocatore());
			preparedStatement.setString(2, partita.getTime());
			preparedStatement.setString(3, partita.getTerritorioAttaccante());
			preparedStatement.setString(4, partita.getTerritorioAttaccato());
			preparedStatement.setInt(5, partita.getDado1Attacco());
			preparedStatement.setInt(6, partita.getDado1Difesa());
			if (partita.getDado2Attacco() != null){
				preparedStatement.setInt(7, partita.getDado2Attacco());
			}else{
				preparedStatement.setNull(7, Types.INTEGER);
			}
			if (partita.getDado2Difesa() != null){
				preparedStatement.setInt(8, partita.getDado2Difesa());
			}else{
				preparedStatement.setNull(8, Types.INTEGER);
			}
			if (partita.getDado3Attacco() != null){
				preparedStatement.setInt(9, partita.getDado3Attacco());
			}else{
				preparedStatement.setNull(9, Types.INTEGER);
			}
			if (partita.getDado3Difesa() != null){
				preparedStatement.setInt(10, partita.getDado3Difesa());
			}else{
				preparedStatement.setNull(10, Types.INTEGER);
			}
			preparedStatement.setLong(11, partita.getIdPartita());
			preparedStatement.setInt(12, partita.getLogId());
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
