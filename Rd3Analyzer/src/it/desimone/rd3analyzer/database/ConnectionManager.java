package it.desimone.rd3analyzer.database;

import it.desimone.utils.Configurator;
import it.desimone.utils.MyLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionManager {
	
	private static Connection connection;
	private static Lock lock = new ReentrantLock();
	
	private ConnectionManager(){}
	public static synchronized Connection getConnection(){
		//lock.lock();
		try {
			if (connection == null || connection.isClosed()){
			    try {
					Class.forName("org.sqlite.JDBC");
//					SQLiteConfig sqLiteConfig = new SQLiteConfig();
//					sqLiteConfig.setOpenMode(SQLiteOpenMode.FULLMUTEX);
					//https://www.dropbox.com/s/tqj2vwa7zwgm7hi/rd3data.sqlite?dl=0
						String pathDB = Configurator.getPathDB();
						connection = DriverManager.getConnection("jdbc:sqlite:"+pathDB /*, sqLiteConfig.toProperties()*/);
						connection.setAutoCommit(false);
						//MyLogger.getLogger().finest("Aperta una nuova connessione al DB");
					} catch (SQLException e) {
						MyLogger.getLogger().severe("SQLException: "+e.getMessage());
						throw new RuntimeException(e);
					} 
				 catch (ClassNotFoundException e) {
					MyLogger.getLogger().severe("ClassNotFoundException: "+e.getMessage());
					throw new RuntimeException(e);
				}
			}
		} catch (SQLException e) {
			MyLogger.getLogger().severe("SQLException: "+e.getMessage());
			throw new RuntimeException(e);
		}
		return connection;
	}
	
	public static synchronized Connection getNewConnection(){
		Connection conn = null;
	    try {
			Class.forName("org.sqlite.JDBC");
		    try {
				String pathDB = Configurator.getPathDB();
				conn = DriverManager.getConnection("jdbc:sqlite:"+pathDB);
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			} 
		} catch (ClassNotFoundException e) {
			MyLogger.getLogger().severe("ClassNotFoundException: "+e.getMessage());
			throw new RuntimeException(e);
		}
		return conn;
	}
	
	public static void closeConnection(){
		try {
			if (connection != null && !connection.isClosed()){
				//MyLogger.getLogger().finest("Chiusa connessione al DB");
				connection.close();
			}
		} catch (SQLException e) {
			MyLogger.getLogger().severe("SQLException: "+e.getMessage());
			throw new RuntimeException(e);
		} finally{
			//lock.unlock();
		}
	}
	
	private static synchronized Connection getConn(){
		return connection;
	}

	
	public static void backupDB(){
		MyLogger.getLogger().finest("[BEGIN]");
		boolean psOpened = false;
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		try {						
			preparedStatement = connection.prepareStatement("backup to backup.sqlite");
			psOpened = true;	
			preparedStatement.executeUpdate();
			
			connection.commit();	
		} catch (SQLException e) {
			try {
				preparedStatement.close();
				psOpened = false;
				connection.rollback();
				//connection.close();
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
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
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
			} catch (SQLException e1) {
				MyLogger.getLogger().severe("SQLException: "+e1.getMessage());
				throw new RuntimeException(e1);
			}
		}
		finally{
			try {
				if (psOpened) preparedStatement.close();				
				ConnectionManager.closeConnection();
			} catch (SQLException e) {
				MyLogger.getLogger().severe("SQLException: "+e.getMessage());
				throw new RuntimeException(e);
			}
		}
		MyLogger.getLogger().finest("[END]");
	}
	
}
