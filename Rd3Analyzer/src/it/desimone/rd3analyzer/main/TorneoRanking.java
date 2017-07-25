package it.desimone.rd3analyzer.main;

import it.desimone.rd3analyzer.RankingPrestigious;
import it.desimone.rd3analyzer.StoricoClassifica;
import it.desimone.rd3analyzer.database.TorneoPrestigiousDao;
import it.desimone.utils.Configurator;
import it.desimone.utils.MyLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class TorneoRanking {

	private static DateFormat dateFormatFULL = new SimpleDateFormat("EEEEEEEEE dd MMMMMMMMM yyyy", Locale.ITALIAN);
	private static DateFormat dateFormatSHORT = new SimpleDateFormat("MMMMMMMMM yyyy", Locale.ITALIAN);
	private static DateFormat dateFormatMONTH = new SimpleDateFormat("MMMMMMMMM", Locale.ITALIAN);
	private static DateFormat dateFormatITALY = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALIAN);
	
	private static FilenameFilter filenameFilter = new FilenameFilter() {
		
		@Override
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith("html");
		}
	};
	
	public static void main(String[] args) {
		try{
			if (args == null || args.length != 2){
				elaboraRankingPrestigious();
			}else{
				Integer mese = Integer.valueOf(args[0]);
				Integer anno = Integer.valueOf(args[1]);
				elaboraRankingPrestigiousDataIndicata(mese, anno);
			}
		}catch(Exception e){
			MyLogger.getLogger().severe("L'APPLICAZIONE DI ELABORAZIONE DELLA RANKING E' ANDATA IN ECCEZIONE: "+e.getMessage());
		}
	}
	
	public static void elaboraRankingPrestigious(){
		//Verifico se è il primo giorno del mese: se lo è, e son passate le 3 di mattina, verifico che esista
		//la classifica del mese precedente nello storico: se non esiste la genero e poi genero quella del mese in corso.
		
		Calendar now = Calendar.getInstance();
		
		if (now.get(Calendar.DAY_OF_MONTH) >= 1 && now.get(Calendar.HOUR_OF_DAY) > 2){
			now.add(Calendar.MONTH, -1);
			File file = new File(Configurator.getPathClassificaPrestigious()+File.separator+"classificaPrestigious_"+now.get(Calendar.YEAR)+"_"+(now.get(Calendar.MONTH)+1)+".html");
			if (!file.exists()){
				estraiClassifica(now, file);
			}
			now.add(Calendar.MONTH, +1);
		}
		File file = new File(Configurator.getPathClassificaPrestigious()+File.separator+"classificaPrestigious.html");
		estraiClassifica(now, file);	
	}
	
	public static void elaboraRankingPrestigiousDataIndicata(Integer mese, Integer anno){

		Calendar now = Calendar.getInstance();
		now.set(Calendar.MONTH, mese-1);
		now.set(Calendar.YEAR, anno);
		
		File file = new File(Configurator.getPathClassificaPrestigious()+File.separator+"classificaPrestigious_"+anno+"_"+mese+".html");
		estraiClassifica(now, file);
	}
	
	public static void estraiClassifica(Calendar calendar, File file) {
		TorneoPrestigiousDao torneoPrestigiousDao = new TorneoPrestigiousDao();
		Integer mese = calendar.get(Calendar.MONTH)+1;
		Integer anno = calendar.get(Calendar.YEAR);
		
		MyLogger.getLogger().info("Estrazione classifica di "+mese+" "+anno);
		
		List<RankingPrestigious> rankingPrestigious = torneoPrestigiousDao.estraiRankingDelMese(mese,anno);
		Integer totalePartiteGiocate = 0;
		for (RankingPrestigious ranking: rankingPrestigious){
			totalePartiteGiocate += ranking.getNumeroPartiteGiocate();
		}
		File folderClassifiche = new File(Configurator.getPathClassificaPrestigious());

		File[] classifiche = folderClassifiche.listFiles(filenameFilter);
		StoricoClassifica[] listaClassifiche = new StoricoClassifica[classifiche.length];
		for (int i = 0; i < classifiche.length; i++){
			String[] date = (classifiche[i].getName()).split("_");
			Calendar soloPerMese = Calendar.getInstance();
			if (date != null && date.length >= 2){
				soloPerMese.set(Calendar.MONTH, Integer.valueOf(date[2].substring(0, date[2].indexOf(".")))-1);
				listaClassifiche[i] = new StoricoClassifica(dateFormatMONTH.format(soloPerMese.getTime())+" "+date[1],classifiche[i].getName());
			}else{
				soloPerMese.set(Calendar.MONTH, Integer.valueOf(mese)-1);
				listaClassifiche[i] = new StoricoClassifica(dateFormatMONTH.format(soloPerMese.getTime())+" "+anno,classifiche[i].getName());
			}
		}
		
		Writer writer = null;
		try {
			writer = new FileWriter(file);
			//writer = new StringWriter();
			//OutputStream outputStream = new FileOutputStream(file);
			//writer = new OutputStreamWriter(outputStream);
			
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("totalePartiteGiocate", totalePartiteGiocate);
			velocityContext.put("rankingPrestigious", rankingPrestigious);
			velocityContext.put("adesso", dateFormatITALY.format(Calendar.getInstance().getTime()));
			velocityContext.put("calendar", calendar);
			velocityContext.put("meseanno", dateFormatSHORT.format(calendar.getTime()));
			velocityContext.put("dataInizio", dateFormatFULL.format(calendar.getTime()));
			velocityContext.put("listaClassifiche", listaClassifiche);
			velocityContext.put("dominio", Configurator.getDominio());
			//velocityContext.put("dateFormat", DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.ITALY).format(calendar.getTime()));
//			Template template = Velocity.getTemplate("C:/templateClassificaPrestigious.vm");

		    Velocity.setProperty("file.resource.loader.path", Configurator.PATH_CONFIGURATION);
		    Velocity.setProperty("runtime.log.logsystem.class", it.desimone.utils.MyLogger.class);
			Velocity.mergeTemplate("templateClassificaPrestigious.vm", "UTF-8", velocityContext,  writer);
			
			//outputStream.flush();
			//outputStream.close();
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			MyLogger.getLogger().severe("IOException nell'elaborazione del file "+file.getAbsolutePath()+": "+e.getMessage());
		}

		MyLogger.getLogger().info("[FINE ELABORAZIONE: Estratto File "+file.getName()+"]");
	}

}
