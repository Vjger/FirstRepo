package it.desimone.rd3analyzer.database;

import it.desimone.rd3analyzer.RankingPrestigious;
import it.desimone.utils.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TorneoPrestigiousDao {
	
	public static final String VIEW_TORNEO_PRESTIGIOUS 		= "TorneoPrestigiousGlobal";
	
	private static final String ESTRAZIONE_RANKING = 
			"SELECT giocatore, UltimaPartita, ranking, giocate, vinte,"
			+ " PartitePrimaSettimana, PartiteSecondaSettimana, PartiteTerzaSettimana, PartiteQuartaSettimana"
			+ " FROM "+VIEW_TORNEO_PRESTIGIOUS			
			+ " where anno = ?  and mese = ?"
			+ " ORDER BY ranking DESC";


	public List<RankingPrestigious> estraiRankingDelMese(int mese, int anno){
		MyLogger.getLogger().finest("[BEGIN]: "+mese+" - "+anno);
		List<RankingPrestigious> listaRanking = null;
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {			
			preparedStatement = connection.prepareStatement(ESTRAZIONE_RANKING);		
			String meseString = new DecimalFormat("00").format(mese);
			String annoString = new DecimalFormat("0000").format(anno);
			preparedStatement.setString(1,annoString);
			preparedStatement.setString(2,meseString);
			rs = preparedStatement.executeQuery();
			if (rs != null){
				listaRanking = new ArrayList<RankingPrestigious>();
				while(rs.next()){
					RankingPrestigious ranking 	= new RankingPrestigious();
					ranking.setNickName(rs.getString(1));
					ranking.setDataUltimaPartita(rs.getString(2));
					ranking.setRanking(rs.getInt(3));
					ranking.setNumeroPartiteGiocate(rs.getInt(4));
					ranking.setNumeroPartiteVinte(rs.getInt(5));
					ranking.setNumeroPartiteGiocatePrimaSettimana(rs.getInt(6));
					ranking.setNumeroPartiteGiocateSecondaSettimana(rs.getInt(7));
					ranking.setNumeroPartiteGiocateTerzaSettimana(rs.getInt(8));
					ranking.setNumeroPartiteGiocateQuartaSettimana(rs.getInt(9));
					listaRanking.add(ranking);
				}
			}
			MyLogger.getLogger().finest("Esecuzione di "+ESTRAZIONE_RANKING+"\nRighe Estratte: "+(listaRanking==null?0:listaRanking.size()));
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
		return listaRanking;
	}
	
	
}
