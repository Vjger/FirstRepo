package it.desimone.rd3analyzer.main;

import it.desimone.rd3analyzer.database.ConnectionManager;
import it.desimone.rd3analyzer.database.ThreadStatusDao;
import it.desimone.utils.Configurator;
import it.desimone.utils.MyLogger;

public class ThreadLoaderSchede extends Thread{
	
	public static final String THREAD_NAME = "LoaderSchedeRD3";
	private volatile boolean running = true;
	private volatile Integer tempoAttesa;
	public ThreadLoaderSchede(){
		setName(THREAD_NAME);
	}

	public void run(){
		MyLogger.getLogger().info("[INIZIO] ThreadLoaderSchede");
		while (running){
			LoaderSchede.main(null);
			try {
				tempoAttesa = Configurator.getTempoAttesaElaborazioneSchede();
				//ConnectionManager.closeConnection();
				sleep(tempoAttesa*60*1000);
				running = ThreadStatusDao.isRunning(THREAD_NAME);
			} catch (InterruptedException e) {
				MyLogger.getLogger().info("Thread interrotto: "+e.getMessage());
				ThreadStatusDao.flagAsStopped(THREAD_NAME);
			}
		}
		ThreadStatusDao.flagAsStopped(THREAD_NAME);
		MyLogger.getLogger().info("[FINE] ThreadLoaderSchede");
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	
}
