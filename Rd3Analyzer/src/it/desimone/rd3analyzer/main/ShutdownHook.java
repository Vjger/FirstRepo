package it.desimone.rd3analyzer.main;

import it.desimone.rd3analyzer.database.ThreadStatusDao;
import it.desimone.utils.MyLogger;

public class ShutdownHook extends Thread {

	public void run(){
		MyLogger.getLogger().info("[INIZIO] ShutdownHook");
		ThreadStatusDao.flagAsStopped(ThreadLoaderSchede.THREAD_NAME);
		ThreadStatusDao.flagAsStopped(ThreadTorneoRanking.THREAD_NAME);
		MyLogger.getLogger().info("[FINE] ShutdownHook");
	}
	
}
