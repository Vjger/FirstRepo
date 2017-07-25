package it.desimone.rd3analyzer.main;

import it.desimone.rd3analyzer.ThreadStatus;
import it.desimone.rd3analyzer.database.ThreadStatusDao;
import it.desimone.utils.Configurator;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Console {
	
	private static String riepilogo;
    private static Scanner sc = new Scanner(System.in);
	static{
		StringBuilder buffer = new StringBuilder();
		buffer.append("----------------------\n");
		buffer.append("[0]: Help\n");
		buffer.append("[1]: Avvio elaborazione in background\n");
		buffer.append("[2]: Stop elaborazione in background\n");
		buffer.append("[3]: Verifica status dell'elaborazione in background\n");
		buffer.append("[4]: Avvio elaborazione delle schede partita manualmente\n");
		buffer.append("[5]: Avvio elaborazione della ranking manualmente\n");
		buffer.append("[6]: ESCI\n");
		buffer.append("----------------------\n");
		buffer.append("EFFETTUA LA TUA SCELTA:");
		riepilogo = buffer.toString();
	}

	public static void main(String[] args) {
		if (args == null || args.length == 0){
			input();
		}else{
			try{
				int scelta = Integer.valueOf(args[0]);
		    	effettuaScelta(scelta);
				sc.close();
				System.out.println("[FINE ELABORAZIONE]");
			}catch(InputMismatchException ime){
				System.out.println("[Scelta errata]");
			}
		}
	}

	private static void input(){
		printRiepilogo();
		try{
			int scelta = sc.nextInt();
			//sc.close();
	    	effettuaScelta(scelta);
	    	if (scelta != 6) input();
		}catch(InputMismatchException ime){
			System.out.println("[Scelta errata]");
		}
	}
	
	private static void effettuaScelta(Integer scelta){    
	    switch (scelta) {
		case 0:
			System.out.println("[DA IMPLEMENTARE]");
			break;
		case 1:
			avvioElaborazioneInBackground();
			break;
		case 2:
			stoppoElaborazioneInBackground();
			break;
		case 3:
			statusElaborazioneInBackground();
			break;	
		case 4:
			avvioElaborazioneSchedePartitaManuale();
			break;	
		case 5:
			avvioElaborazioneRankingManuale();
			break;	
		case 6:
			sc.close();
			System.out.println("[CONSOLE CHIUSA]");
			break;
		default:
			System.out.println("Scelta non prevista");
			break;
		}
	}
	
	private static void printRiepilogo(){
		System.out.println(riepilogo);
	}
	

	private static Thread[] searchThreadsRunning(){
		Thread[] threads = new Thread[2];
		
		Map<Thread, StackTraceElement[]> mapThreadsJVM = Thread.getAllStackTraces();
		Set<Thread> threadsJVM = mapThreadsJVM.keySet();
	    for (Thread thrJVM: threadsJVM){
	    	if (thrJVM.getName().equals(ThreadLoaderSchede.THREAD_NAME)){
	    		threads[0] = thrJVM;
	    	}else if (thrJVM.getName().equals(ThreadTorneoRanking.THREAD_NAME)){
	    		threads[1] = thrJVM;
	    	}
	    	System.out.println("[Thread JVM] ID: <<"+thrJVM.getId()+">> Name <<"+thrJVM.getName()+">>");
	    }
    	ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    	for (ThreadInfo threadInfo: threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds())){
    		System.out.println(threadInfo);
    	}
	    return threads;
	}
	
	private static void avvioElaborazioneInBackground(){
		//Verifico che i thread non siano già in piedi.
		Map<String, ThreadStatus> statusThreads = ThreadStatusDao.leggiStatusThreads();
		ThreadStatus loaderSchedeStatus = statusThreads.get(ThreadLoaderSchede.THREAD_NAME);
		ThreadStatus rankingStatus 		= statusThreads.get(ThreadTorneoRanking.THREAD_NAME);
		if (loaderSchedeStatus != null && loaderSchedeStatus.isActive()){
			System.out.println("[Il processo di caricamento delle schede partita è già in esecuzione]");
		}else if (loaderSchedeStatus != null && loaderSchedeStatus.isSuspended()){
			System.out.println("[Il processo di caricamento delle schede partita è in fase di sospensione: si deve attendere che venga conclusa]");
		}else if (rankingStatus != null && rankingStatus.isActive()){
			System.out.println("[Il processo di elaborazione della ranking è già in esecuzione]");
		}else if (rankingStatus != null && rankingStatus.isSuspended()){
			System.out.println("[Il processo di elaborazione della ranking è in fase di sospensione: si deve attendere che venga conclusa]");
		}else{
			Starter.main(null);
		}
	}
	
	private static void stoppoElaborazioneInBackground(){
		//Verifico che i thread siano effettivamente in piedi.
		Map<String, ThreadStatus> statusThreads = ThreadStatusDao.leggiStatusThreads();
		ThreadStatus loaderSchedeStatus = statusThreads.get(ThreadLoaderSchede.THREAD_NAME);
		ThreadStatus rankingStatus 		= statusThreads.get(ThreadTorneoRanking.THREAD_NAME);
		if (loaderSchedeStatus == null){
			System.out.println("[Non risulta alcun processo di caricamento delle schede partita]");
		}else if (!loaderSchedeStatus.isActive()){
			System.out.println("[Il processo di caricamento delle schede partita risulta già essere stato chiuso il "+loaderSchedeStatus.getDataAggiornamento()+"]");
		}else{
			System.out.println("[Chiusura del processo di caricamento delle schede partita in corso...]");
			try{
				ThreadStatusDao.flagAsSuspended(ThreadLoaderSchede.THREAD_NAME);
			}catch(Exception e){
				String message = e.getMessage();
				boolean locked = message != null && message.contains("locked");
				System.out.println("[Non è stato possibile stoppare il processo "+(locked?"in quanto attualmente in esecuzione: riprovare":"")+"]");
			}
		}
		
		if (rankingStatus == null){
			System.out.println("[Non risulta alcun processo di elaborazione della ranking]");
		}else if (!rankingStatus.isActive()){
			System.out.println("[Il processo di elaborazione della ranking risulta già essere stato chiuso il "+rankingStatus.getDataAggiornamento()+"]");
		}else{
			System.out.println("[Chiusura del processo di elaborazione della ranking in corso...]");
			try{
				ThreadStatusDao.flagAsSuspended(ThreadTorneoRanking.THREAD_NAME);
			}catch(Exception e){
				String message = e.getMessage();
				boolean locked = message != null && message.contains("locked");
				System.out.println("[Non è stato possibile stoppare il processo "+(locked?"in quanto attualmente in esecuzione: riprovare":"")+"]");
			}
		}		
	}
	
	private static void statusElaborazioneInBackground(){
		Map<String, ThreadStatus> statusThreads = ThreadStatusDao.leggiStatusThreads();
		ThreadStatus loaderSchedeStatus = statusThreads.get(ThreadLoaderSchede.THREAD_NAME);
		ThreadStatus rankingStatus 		= statusThreads.get(ThreadTorneoRanking.THREAD_NAME);
		
		if (loaderSchedeStatus == null){
			System.out.println("[Non risulta alcun processo di caricamento delle schede partita]");
		}else{
			System.out.println("[Il processo di caricamento delle schede partita è "+ loaderSchedeStatus.getStatoThread()+" dal "+loaderSchedeStatus.getDataAggiornamento()+"]");
		}	
		
		if (rankingStatus == null){
			System.out.println("[Non risulta alcun processo di elaborazione della ranking]");
		}else{
			System.out.println("[Il processo di elaborazione della ranking è "+ rankingStatus.getStatoThread()+" dal "+rankingStatus.getDataAggiornamento()+"]");
		}		
	}
	
	private static void avvioElaborazioneSchedePartitaManuale(){
		String urlSchedaStagione = Configurator.getUrlSchedaStagione();
		Boolean soloPartiteNuove = Configurator.caricaSoloLeNuove();
		System.out.println("[L'attuale configurazione prevede che vengano elaborate "+ (soloPartiteNuove?"le sole partite nuove":"tutte le partite ")+" della seguente pagina: "+urlSchedaStagione+"]");
		System.out.print("[Confermi? (S/N)]");
		String scelta = sc.next();
		if (scelta.equalsIgnoreCase("S")){
			System.out.println("[INIZIO ELABORAZIONE]");
			LoaderSchede loaderSchede = LoaderSchede.getInstance();
			loaderSchede.caricaSchedePrestigious(urlSchedaStagione, soloPartiteNuove);
			System.out.println("[FINE ELABORAZIONE]");
		}		
	}
	
	private static void avvioElaborazioneRankingManuale(){
		System.out.println("[L'elaborazione prevede l'estrazione della ranking per il mese in corso; vuoi dare tu invece il periodo? (S/N)");
		String scelta = sc.next();
		if (scelta.equalsIgnoreCase("S")){
			System.out.println("Indica mese ed anno nel formato M/AAAA (Es: 8/2014))");
			String periodo = sc.next();
			String[] meseanno = periodo.split("/");
			if (meseanno != null && meseanno.length == 2){
				try{
					Integer mese = Integer.valueOf(meseanno[0]);
					Integer anno = Integer.valueOf(meseanno[1]);
					System.out.println("[INIZIO ELABORAZIONE]");
					TorneoRanking.main(meseanno);
					System.out.println("[FINE ELABORAZIONE]");
				}catch (NumberFormatException ne){
					System.out.println("[Formato errato]");
				}
			}else{
				System.out.println("[Formato errato]");
			}
		}else if (scelta.equalsIgnoreCase("N")){
			System.out.println("[INIZIO ELABORAZIONE]");
			TorneoRanking.main(null);
			System.out.println("[FINE ELABORAZIONE]");
		}
	}
}
