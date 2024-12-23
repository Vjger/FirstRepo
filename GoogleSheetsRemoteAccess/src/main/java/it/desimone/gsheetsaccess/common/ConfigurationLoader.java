package it.desimone.gsheetsaccess.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import it.desimone.gsheetsaccess.common.Configurator;
import it.desimone.gsheetsaccess.googleaccess.GoogleSheetsAccess;
import it.desimone.utils.DateUtils;
import it.desimone.utils.MyLogger;

public class ConfigurationLoader {

	public static final String CONFIGURAZIONI_SHEET_NAME = "CONFIGURAZIONI";
	public static final String TORNEI_SHEET_NAME = "CONFIGURAZIONI";
	private static ConfigurationData configurationData = new ConfigurationData();
	private ConfigurationLoader() {}
	
	static {
		GoogleSheetsAccess googleSheetsAccess = new GoogleSheetsAccess();
		try {
			List<List<Object>> configurazioni = googleSheetsAccess.leggiSheet(Configurator.getConfigurationSheetId(), CONFIGURAZIONI_SHEET_NAME+"!A2:B");
			if (CollectionUtils.isNotEmpty(configurazioni)) {
				Map<String, String> mappaConfigurazioni = new HashMap<String, String>();
				for (List<Object> row: configurazioni) {
					String nomeParametro =  (String)row.get(0);
					String valoreParametro =  (String)row.get(1);
					mappaConfigurazioni.put(nomeParametro, valoreParametro);
				}
			}
			List<List<Object>> tornei = googleSheetsAccess.leggiSheet(Configurator.getConfigurationSheetId(), TORNEI_SHEET_NAME+"!A2:B");
			if (CollectionUtils.isNotEmpty(tornei)) {
				Map<Integer, String> mappaTornei = new HashMap<Integer, String>();
				for (List<Object> row: tornei) {
					Integer anno =  Integer.parseInt((String)row.get(0));
					String  fileId =  (String)row.get(1);
					mappaTornei.put(anno, fileId);
				}
				configurationData.setTournamentsSheetId(mappaTornei);
			}
		} catch (IOException e) {
			MyLogger.getLogger().severe("Errore nell'acquisizione della configurazione: "+e.getMessage());
		} 
	}
	
	

	
}
