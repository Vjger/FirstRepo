package it.desimone.gsheetsaccess.ranking;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import com.thoughtworks.xstream.XStream;

import it.desimone.gsheetsaccess.common.ResourceWorking;

public class RankingBuilderNew {

	private static TreeMap<String, RankingThresholdsNew> mappingScorers = new TreeMap<String, RankingThresholdsNew>();
	
	static{
		//Inizializzazione mappa
		XStream xStream = new XStream();
		File thresholdsFile = new File(ResourceWorking.rankingThresholds());
		mappingScorers = (TreeMap) xStream.fromXML(thresholdsFile);
	}
	
	/* Se non si trova quell'anno si prende il più recente successivo ad esso. 
	 * Se non c'è nemmeno così si prende l'ultimo.
	 * */
	private static RankingThresholdsNew scorersFactory(String year){
		RankingThresholdsNew result = mappingScorers.get(year);
		if (result == null){
			Map.Entry<String, RankingThresholdsNew> greaterEntry = mappingScorers.higherEntry(year);
			if (greaterEntry == null){
				greaterEntry = mappingScorers.lastEntry();
			}
			if (greaterEntry != null){
				result = greaterEntry.getValue();
			}
		}
		return result;
	}
	
	public static RankingThresholdsNew getRankingThreshold(String year){
		RankingThresholdsNew result = scorersFactory(year);
		return result;
	}

	
}
