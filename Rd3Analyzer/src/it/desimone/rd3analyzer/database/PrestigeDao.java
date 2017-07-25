package it.desimone.rd3analyzer.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import it.desimone.rd3analyzer.Partita;
import it.desimone.utils.MyLogger;

public class PrestigeDao {
	
	private String nomeTabella;

	public PrestigeDao(String nomeTabella){
		this.nomeTabella = nomeTabella;
	}
	
	private String getQueryInsert(Partita partita){
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO "+nomeTabella +" (idPartita, vincitore, punteggioVincitore");
		int numeroGiocatori = partita.getRisultati().size();
		for (int indexGiocatori = 1; indexGiocatori <= numeroGiocatori; indexGiocatori++){
			query.append(", giocatore"+indexGiocatori+", punteggioGiocatore"+indexGiocatori);
		}
		query.append(") VALUES (?,?,?");
		for (int indexGiocatori = 1; indexGiocatori <= numeroGiocatori; indexGiocatori++){
			query.append(",?,?");
		}
		query.append(")");
		return query.toString();
	}
	
	private String getQueryUpdate(Partita partita){
		StringBuilder query = new StringBuilder();
		query.append("UPDATE "+nomeTabella +" SET vincitore = ? , punteggioVincitore = ?");
		int numeroGiocatori = partita.getRisultati().size();
		for (int indexGiocatori = 1; indexGiocatori <= numeroGiocatori; indexGiocatori++){
			query.append(", giocatore"+indexGiocatori+" = ?, punteggioGiocatore"+indexGiocatori+" = ? ");
		}
		query.append(" WHERE idPartita = ?");

		return query.toString();
	}
	
	public int inserisciPartita(Partita partita, Connection connection) throws SQLException, Exception{
		//MyLogger.getLogger().finest("[BEGIN]: "+partita);
		int righeInserite = 0;
		String query = getQueryInsert(partita);
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, partita.getIdPartita());
			preparedStatement.setString(2, partita.getVincitore());
			preparedStatement.setInt(3, partita.getPunteggioVincitore());
			Map<String,Integer> risultati = partita.getRisultati();
			Set<String> giocatori = risultati.keySet();
			int index = 4;
			for (String giocatore: giocatori){
				preparedStatement.setString(index++, giocatore);
				preparedStatement.setInt(index++, risultati.get(giocatore));
			}	
			righeInserite = preparedStatement.executeUpdate();		
			//MyLogger.getLogger().finest("Esecuzione di "+query+"\nRighe Inserite: "+righeInserite);
		}finally{
			if (preparedStatement != null) preparedStatement.close();
		}
		//MyLogger.getLogger().finest("[END]");
		return righeInserite;
	}

	public int aggiornaPartita(Partita partita, Connection connection) throws SQLException, Exception{
		//MyLogger.getLogger().finest("[BEGIN]: "+partita);
		int righeAggiornate = 0;
		String query = getQueryUpdate(partita);
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, partita.getVincitore());
			preparedStatement.setInt(2, partita.getPunteggioVincitore());
			Map<String,Integer> risultati = partita.getRisultati();
			Set<String> giocatori = risultati.keySet();
			int index = 3;
			for (String giocatore: giocatori){
				preparedStatement.setString(index++, giocatore);
				preparedStatement.setInt(index++, risultati.get(giocatore));
			}
			preparedStatement.setLong(index, partita.getIdPartita());
			righeAggiornate = preparedStatement.executeUpdate();	
			//MyLogger.getLogger().finest("Esecuzione di "+query+"\nRighe Aggiornate: "+righeAggiornate);
		}finally{
			if (preparedStatement != null) preparedStatement.close();
		}
		//MyLogger.getLogger().finest("[END]");
		return righeAggiornate;
	}
}
