package it.desimone.rd3analyzer.database;

import it.desimone.rd3analyzer.Torneo;
import it.desimone.rd3analyzer.parser.FiltroModalita;
import it.desimone.utils.MyLogger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TorneiDao {
	
	private static final DateFormat DATA_SQLITE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private static final String TORNEO_BY_URL = "SELECT tipoTorneo, dataUltimaElaborazione, daElaborare FROM TORNEI WHERE url = ? ";
	
	private static final String INSERT_TORNEO = "INSERT INTO TORNEI (url, tipoTorneo, dataUltimaElaborazione, daElaborare) VALUES (?,?,?,?)";
	
	private static final String UPDATE_TORNEO = "UPDATE TORNEI SET dataUltimaElaborazione = ?, daElaborare = ? WHERE url = ?";
	
	public Torneo getTorneoByUrl(String url){
		MyLogger.getLogger().finest("[BEGIN]");
		Torneo torneo = null;
		
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {			
			preparedStatement = connection.prepareStatement(TORNEO_BY_URL);	
			preparedStatement.setString(1, url);
			rs = preparedStatement.executeQuery();
			if (rs != null && rs.next()){		
				torneo = new Torneo();
				torneo.setUrlTorneo(url);
				String tipoTorneo = rs.getString(1);
				FiltroModalita filtroModalita = FiltroModalita.getFiltroByDescrizione(tipoTorneo);
				torneo.setModalitaTorneo(filtroModalita);
				//Date dataUltimaElaborazione = rs.getDate(2);
				//torneo.setDataUltimaElaborazione(dataUltimaElaborazione);
				String dataUltimaElaborazioneStr = rs.getString(2);
				torneo.setDataUltimaElaborazione(DATA_SQLITE.parse(dataUltimaElaborazioneStr));
				Boolean daElaborare = rs.getBoolean(3);
				torneo.setDaElaborare(daElaborare);
			}
			MyLogger.getLogger().finest("Esecuzione di "+TORNEO_BY_URL+": "+url);
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
		return torneo;
	}

	public int inserisciTorneo(Torneo torneo){
		//MyLogger.getLogger().finest("[BEGIN]: "+partita);
		boolean psOpened = false;
		int righeInserite = 0;
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		try {						
			preparedStatement = connection.prepareStatement(INSERT_TORNEO);
			psOpened = true;
			preparedStatement.setString(1, torneo.getUrlTorneo());
			preparedStatement.setString(2, torneo.getModalitaTorneo().getCodiceRicerca());
			String dataElaborazione = DATA_SQLITE.format(torneo.getDataUltimaElaborazione());
			preparedStatement.setString(3, dataElaborazione);
			preparedStatement.setBoolean(4, torneo.getDaElaborare());
			
			righeInserite = preparedStatement.executeUpdate();
			//MyLogger.getLogger().finest("Esecuzione di "+INSERT+"\nRighe Inserite: "+righeInserite);			
			connection.commit();	
		} catch (SQLException e) {
			try {
				preparedStatement.close();
				psOpened = false;
				connection.rollback();
				//connection.close();
				MyLogger.getLogger().severe("SQLException per il Torneo "+torneo.getUrlTorneo()+": "+e.getMessage());
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
				MyLogger.getLogger().severe("Exception per il Torneo "+torneo.getUrlTorneo()+": "+e.getMessage());
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
	
	public int aggiornaTorneo(Torneo torneo){
		//MyLogger.getLogger().finest("[BEGIN]: "+partita);
		boolean psOpened = false;
		int righeAggiornate = 0;
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		try {						
			preparedStatement = connection.prepareStatement(UPDATE_TORNEO);
			psOpened = true;
			String dataElaborazione = DATA_SQLITE.format(torneo.getDataUltimaElaborazione());
			preparedStatement.setString(1, dataElaborazione);
			preparedStatement.setBoolean(2, torneo.getDaElaborare());
			preparedStatement.setString(3, torneo.getUrlTorneo());
			
			righeAggiornate = preparedStatement.executeUpdate();
			//MyLogger.getLogger().finest("Esecuzione di "+INSERT+"\nRighe Inserite: "+righeInserite);			
			connection.commit();	
		} catch (SQLException e) {
			try {
				preparedStatement.close();
				psOpened = false;
				connection.rollback();
				//connection.close();
				MyLogger.getLogger().severe("SQLException per il Torneo "+torneo.getUrlTorneo()+": "+e.getMessage());
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
				MyLogger.getLogger().severe("Exception per il Torneo "+torneo.getUrlTorneo()+": "+e.getMessage());
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
		return righeAggiornate;
	}
	
}
