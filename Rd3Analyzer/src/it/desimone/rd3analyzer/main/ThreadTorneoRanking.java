package it.desimone.rd3analyzer.main;

import it.desimone.rd3analyzer.database.ThreadStatusDao;
import it.desimone.utils.Configurator;
import it.desimone.utils.MyLogger;

public class ThreadTorneoRanking extends Thread {

	public static final String THREAD_NAME = "RankingPrestigiousRD3";
	private volatile boolean running = true;
	private volatile Integer tempoAttesa;
	
	public ThreadTorneoRanking(){
		setName(THREAD_NAME);
	}
	
	public void run(){
		
		MyLogger.getLogger().info("[INIZIO] ThreadTorneoRanking");
		while (running){
			TorneoRanking.main(null);
			try {
				tempoAttesa = Configurator.getTempoAttesaElaborazioneRanking();
				//ConnectionManager.closeConnection();
				sleep(tempoAttesa*60*1000);
				running = ThreadStatusDao.isRunning(THREAD_NAME);
			} catch (InterruptedException e) {
				MyLogger.getLogger().info("Thread interrotto: "+e.getMessage());
				ThreadStatusDao.flagAsStopped(THREAD_NAME);
			}
		}
		ThreadStatusDao.flagAsStopped(THREAD_NAME);
		MyLogger.getLogger().info("[FINE] ThreadTorneoRanking");
		
	}

}
