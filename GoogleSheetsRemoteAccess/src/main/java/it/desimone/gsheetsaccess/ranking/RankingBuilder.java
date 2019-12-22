package it.desimone.gsheetsaccess.ranking;

import it.desimone.gsheetsaccess.common.ResourceWorking;
import it.desimone.gsheetsaccess.ranking.RankingThresholds.Thresholds;
import it.desimone.risiko.torneo.dto.SchedaTorneo.TipoTorneo;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import com.thoughtworks.xstream.XStream;

public class RankingBuilder {

	private static TreeMap<String, RankingThresholds> mappingScorers = new TreeMap<String, RankingThresholds>();
	
	static{
		//Inizializzazione mappa
		XStream xStream = new XStream();
		File thresholdsFile = new File(ResourceWorking.rankingThresholds());
		mappingScorers = (TreeMap) xStream.fromXML(thresholdsFile);
	}
	
	/* Se non si trova quell'anno si prende il più recente successivo ad esso. 
	 * Se non c'è nemmeno così si prende l'ultimo.
	 * */
	private static RankingThresholds scorersFactory(String year){
		RankingThresholds result = mappingScorers.get(year);
		if (result == null){
			Map.Entry<String, RankingThresholds> greaterEntry = mappingScorers.higherEntry(year);
			if (greaterEntry == null){
				greaterEntry = mappingScorers.lastEntry();
			}
			if (greaterEntry != null){
				result = greaterEntry.getValue();
			}
		}
		return result;
	}
	
	public static RankingThresholds getRankingThreshold(String year){
		RankingThresholds result = scorersFactory(year);
		return result;
	}

	public static void main (String[] args){
		RankingThresholds rankingThresholds2019 = new RankingThresholds();
		rankingThresholds2019.addThresholds(TipoTorneo.CAMPIONATO, new Thresholds(1,1, new BigDecimal(100)));
		rankingThresholds2019.addThresholds(TipoTorneo.INTERCLUB, new Thresholds(1,1, new BigDecimal(100)));
		rankingThresholds2019.addThresholds(TipoTorneo.OPEN, new Thresholds(1,1, new BigDecimal(100)));
		rankingThresholds2019.addThresholds(TipoTorneo.MASTER, new Thresholds(1,1, new BigDecimal(100)));
		rankingThresholds2019.addThresholds(TipoTorneo.RADUNO_NAZIONALE, new Thresholds(1,1, new BigDecimal(100)));
		mappingScorers.put("2019", rankingThresholds2019);
		RankingThresholds rankingThresholds2020 = new RankingThresholds();
		rankingThresholds2020.addThresholds(TipoTorneo.CAMPIONATO, new Thresholds(5,1, new BigDecimal(100)));
		rankingThresholds2020.addThresholds(TipoTorneo.INTERCLUB, new Thresholds(5,1, new BigDecimal(20)));
		rankingThresholds2020.addThresholds(TipoTorneo.OPEN, new Thresholds(5,4, new BigDecimal(20)));
		rankingThresholds2020.addThresholds(TipoTorneo.MASTER, new Thresholds(5,3, new BigDecimal(25)));
		rankingThresholds2020.addThresholds(TipoTorneo.RADUNO_NAZIONALE, new Thresholds(5,1, new BigDecimal(33.3)));
		mappingScorers.put("2020", rankingThresholds2020);
		XStream xStream = new XStream();
		String thresholds = xStream.toXML(mappingScorers);
		
		System.out.println(thresholds);
		
		File thresholdsFile = new File("C:\\Users\\mds\\Desktop\\RankingThresholds.xml");
		
		Map mappa = (Map) xStream.fromXML(thresholdsFile);
		
		System.out.println(mappa);
	}
	
}
