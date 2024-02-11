package it.desimone.gsheetsaccess.ranking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.googleaccess.GoogleSheetsAccess;
import it.desimone.utils.DateUtils;
import it.desimone.utils.MyLogger;

public class BlackListData {

	public static final String BLACKLIST_SHEET = "BLACKLIST";
	private List<BlackListPlayer> blackListPlayers;
	private static BlackListData instance;
	private BlackListData() {}
	
	public static BlackListData getInstance() {
		if (instance == null) {
			instance = new BlackListData();
			GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
			try {
				List<List<Object>> rows = googleSheetsAccess.leggiSheet(Configurator.getBlackListSheetId(), BLACKLIST_SHEET+"!A2:D");
				if (CollectionUtils.isNotEmpty(rows)) {
					List<BlackListPlayer> blp = new ArrayList<BlackListPlayer>();
					for (List<Object> row: rows) {
						Integer idAnagrafica 			=  Integer.parseInt((String)row.get(0));
						String anniEsclusi = null;
						if (row.size() >1) anniEsclusi 	= (String) row.get(1);
						String dataInizioEsclusione = null;
						if (row.size() >2) dataInizioEsclusione 	= (String) row.get(2);
						String dataFineEsclusione = null;
						if (row.size() >3) dataFineEsclusione 	= (String) row.get(3);
						BlackListPlayer blackListPlayer = new BlackListPlayer();
						blackListPlayer.setIdAnagrafica(idAnagrafica);
						if (StringUtils.isNotEmpty(anniEsclusi)) {
							String[]years = anniEsclusi.split(",");
							blackListPlayer.setForbiddenYears(Arrays.asList(years));
						}
						if (StringUtils.isNotEmpty(dataInizioEsclusione)) {
							blackListPlayer.setStartExclusion(DateUtils.parseItalianDate(dataInizioEsclusione));
						}
						if (StringUtils.isNotEmpty(dataFineEsclusione)) {
							blackListPlayer.setEndExclusion(DateUtils.parseItalianDate(dataFineEsclusione));
						}
						blp.add(blackListPlayer);
					}
					instance.setBlackListPlayers(blp);
				}
				MyLogger.getLogger().info("BLACKLIST: "+instance);
			} catch (IOException e) {
				MyLogger.getLogger().severe("Errore nell'acquisizione della blacklist: "+e.getMessage());
			} catch (Exception e) {
				MyLogger.getLogger().severe("Errore nell'elaborazione della blacklist: "+e.getMessage());
			}
		}
		return instance;
	}
	
	public List<BlackListPlayer> getBlackListPlayers() {
		return blackListPlayers;
	}

	public void setBlackListPlayers(List<BlackListPlayer> blackListPlayers) {
		this.blackListPlayers = blackListPlayers;
	}

	public boolean isForbiddenPlayer(int idAnagrafica, String year) {
		boolean result = false;
		if (CollectionUtils.isNotEmpty(blackListPlayers)) {
			for (BlackListPlayer blackListPlayer: blackListPlayers) {
				if (idAnagrafica == blackListPlayer.getIdAnagrafica() && blackListPlayer.isForbiddenYear(year)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}
	
	public boolean isDisqualifiedPlayer(int idAnagrafica, Date start, Date end) {
		boolean result = false;
		if (CollectionUtils.isNotEmpty(blackListPlayers)) {
			for (BlackListPlayer blackListPlayer: blackListPlayers) {
				if (idAnagrafica == blackListPlayer.getIdAnagrafica() && blackListPlayer.isExcludedPeriod(start, end)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "BlackListData [blackListPlayers=" + blackListPlayers + "]";
	}
	
}
