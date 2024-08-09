package it.desimone.gsheetsaccess.htmlpublisher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.googleaccess.GoogleSheetsAccess;
import it.desimone.utils.MyLogger;

public class LastUpdateData {

	public static final String LAST_UPDATE_SHEET = "LAST_UPDATE";
	private Map<Integer, Date> lastUpdateMap;
	private static LastUpdateData instance;
	
	private LastUpdateData () {}
	
	public static LastUpdateData getInstance() {
		if (instance == null) {	
			instance = new LastUpdateData();
			GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
			try {
				List<List<Object>> rows = googleSheetsAccess.leggiSheet(Configurator.getBlackListSheetId(), LAST_UPDATE_SHEET+"!A2:B");
				if (CollectionUtils.isNotEmpty(rows)) {
					Map<Integer, Date> lum = new HashMap<Integer, Date>();
					for (List<Object> row: rows) {
						Integer anno =  Integer.parseInt((String)row.get(0));
						Date lastUpdateDate =  (Date)row.get(1);
		
						lum.put(anno, lastUpdateDate);
					}
					instance.setLastUpdateMap(lum);
				}
				MyLogger.getLogger().info("LAST_UPDATE_SHEET: "+instance);
			} catch (IOException e) {
				MyLogger.getLogger().severe("Errore nell'acquisizione della last update list: "+e.getMessage());
			} catch (Exception e) {
				MyLogger.getLogger().severe("Errore nell'elaborazione della last update list: "+e.getMessage());
			}
		}
		return instance;
	}

	public Map<Integer, Date> getLastUpdateMap() {
		return lastUpdateMap;
	}

	public void setLastUpdateMap(Map<Integer, Date> lastUpdateMap) {
		this.lastUpdateMap = lastUpdateMap;
	}

	public Date getLastTournamentDate(Integer year){
		return lastUpdateMap.get(year);
	}
}
