package it.desimone.rd3analyzer.database;

import it.desimone.rd3analyzer.ThreadStatus;
import it.desimone.utils.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ThreadStatusDao {
	
	public static final String ACTIVE_STATUS = "A";
	public static final String SUSPENDED_STATUS = "S";
	public static final String STOPPED_STATUS = "T";
	
	private static final String UPDATE = "UPDATE ThreadStatus SET status = ?, dataAggiornamento = CURRENT_TIMESTAMP WHERE ThreadName = ?";
	private static final String SELECT = "SELECT status FROM ThreadStatus WHERE ThreadName = ?";
	private static final String SELECT_ALL = "SELECT ThreadName, status, strftime('%d/%m/%Y %H:%M:%S', dataAggiornamento) FROM ThreadStatus";

	public static void aggiornaStatus(String threadName, String status){
		MyLogger.getLogger().finest("[BEGIN]: "+threadName+" - "+status);
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(UPDATE);
			preparedStatement.setString(1, status);
			preparedStatement.setString(2, threadName);
			int updatedRows = preparedStatement.executeUpdate();	
			MyLogger.getLogger().finest("Esecuzione di "+UPDATE+"\nRighe Aggiornate: "+updatedRows);
			connection.commit();
		}catch(SQLException se){
			MyLogger.getLogger().severe("SQLException: "+se.getMessage());
			try {
				connection.rollback();
				//connection.close();
			} catch (SQLException e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			}
			throw new RuntimeException(se);
		}finally{
			if (preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					MyLogger.getLogger().severe("SQLException: "+e.getMessage());
					throw new RuntimeException(e);
				}
			}
//			try {
//				connection.close();
//			} catch (SQLException e) {
//				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
//				throw new RuntimeException(e);
//			}
			ConnectionManager.closeConnection();
		}
		MyLogger.getLogger().finest("[END]");
	}
	
	public static void flagAsActive(String threadName){
		aggiornaStatus(threadName, ACTIVE_STATUS);
	}
	
	public static void flagAsSuspended(String threadName){
		aggiornaStatus(threadName, SUSPENDED_STATUS);
	}
	
	public static void flagAsStopped(String threadName){
		aggiornaStatus(threadName, STOPPED_STATUS);
	}
	
	public static boolean isRunning(String threadName){
		MyLogger.getLogger().finest("[BEGIN]: "+threadName);
		boolean result = false;
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(SELECT);
			preparedStatement.setString(1, threadName);
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			String status = rs.getString(1);
			rs.close();
			MyLogger.getLogger().finest("Esecuzione di "+SELECT+"\nStatus: "+status);
			result = status.equals(ACTIVE_STATUS);
		}catch(SQLException se){
			MyLogger.getLogger().severe("SQLException: "+se.getMessage());
			throw new RuntimeException(se);
		}finally{
			if (preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					MyLogger.getLogger().severe("SQLException: "+e.getMessage());
					throw new RuntimeException(e);
				}
			}
//			try {
//				connection.close();
//			} catch (SQLException e) {
//				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
//				throw new RuntimeException(e);
//			}
			ConnectionManager.closeConnection();
		}
		MyLogger.getLogger().finest("[END]");
		return result;
	}
	
	public static Map<String, ThreadStatus> leggiStatusThreads(){
		MyLogger.getLogger().finest("[BEGIN]");
		Map<String, ThreadStatus> statusThreads = new HashMap<String, ThreadStatus>();
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(SELECT_ALL);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()){
				String threadName = rs.getString(1);
				String status = rs.getString(2);
				String dataAggiornamento = rs.getString(3);
				ThreadStatus threadStatus = new ThreadStatus(status, dataAggiornamento);
				statusThreads.put(threadName,threadStatus);
			}
			rs.close();
			MyLogger.getLogger().finest("Esecuzione di "+SELECT_ALL);
		}catch(SQLException se){
			MyLogger.getLogger().severe("SQLException: "+se.getMessage());
			throw new RuntimeException(se);
		}finally{
			if (preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					MyLogger.getLogger().severe("SQLException: "+e.getMessage());
					throw new RuntimeException(e);
				}
			}
//			try {
//				connection.close();
//			} catch (SQLException e) {
//				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
//				throw new RuntimeException(e);
//			}
			ConnectionManager.closeConnection();
		}
		MyLogger.getLogger().finest("[END]");
		return statusThreads;
	}
}
