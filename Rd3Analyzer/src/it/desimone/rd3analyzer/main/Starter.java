package it.desimone.rd3analyzer.main;

import it.desimone.rd3analyzer.database.ThreadStatusDao;
import it.desimone.utils.MyLogger;

public class Starter {

	public static void main(String[] args) {
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		
		Thread threadLoaderSchede = new ThreadLoaderSchede();
		MyLogger.getLogger().finer("Lancio thread con id <<"+threadLoaderSchede.getId()+">>");
		ThreadStatusDao.flagAsActive(ThreadLoaderSchede.THREAD_NAME);
		threadLoaderSchede.start();

		Thread threadTorneoRanking = new ThreadTorneoRanking();
		MyLogger.getLogger().finer("Lancio thread con id <<"+threadTorneoRanking.getId()+">>");
		ThreadStatusDao.flagAsActive(ThreadTorneoRanking.THREAD_NAME);
		threadTorneoRanking.start();

	}

}
