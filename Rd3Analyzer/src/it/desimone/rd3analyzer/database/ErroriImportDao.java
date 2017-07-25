package it.desimone.rd3analyzer.database;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import it.desimone.utils.MyLogger;

public class ErroriImportDao {
	
	private boolean nuovaConnessione;
	
	private static final DateFormat DATA_SQLITE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private static final String INSERT = 
			"INSERT INTO ERRORI_IMPORT (idPartita, message, stacktrace) VALUES (?,?,?)"; 
	
	public ErroriImportDao(){
		this(true);
	}
	
	public ErroriImportDao(boolean nuovaConnessione){
		this.nuovaConnessione = nuovaConnessione;
	}

	public void inserisciErrore(Long idPartita, Exception e){
		MyLogger.getLogger().finest("[BEGIN]: "+idPartita+" - "+e);
		Connection connection;
		if (nuovaConnessione){
			connection = ConnectionManager.getNewConnection();
		}else{
			connection = ConnectionManager.getConnection();
		}
		PreparedStatement preparedStatement = null;
		try {						
			preparedStatement = connection.prepareStatement(INSERT);
			preparedStatement.setLong(1, idPartita);
			preparedStatement.setString(2, e.getMessage());
			
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			preparedStatement.setString(3, writer.toString());
			
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException se) {
			MyLogger.getLogger().severe("Partita: <<"+idPartita+">> - SQLException: "+se.getMessage());
			throw new RuntimeException(se);
		}finally{
			try {
				preparedStatement.close();
				//connection.close();
				ConnectionManager.closeConnection();
			} catch (SQLException see) {
				MyLogger.getLogger().severe("Partita: <<"+idPartita+">> - SQLException: "+see.getMessage());
				throw new RuntimeException(see);
			}
		}
		MyLogger.getLogger().finest("[END]");
	}
}
