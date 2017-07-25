package it.desimone.utils;

import java.util.Map;
import java.util.Set;

public class DaemonTest {

    public static void main(String[] args) {
    	WorkerThread wt = new WorkerThread();
        wt.start();
        try {
            Thread.sleep(7500);
        } catch (InterruptedException e) {}
        wt.setRunning(false);
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e) {}
        Map<Thread, StackTraceElement[]> threadsJVM = Thread.getAllStackTraces();
        Set<Thread> threads = threadsJVM.keySet();
        for (Thread thrJVM: threads){
            System.out.println("JVM - ID: "+thrJVM.getId()+" - Name: "+thrJVM.getName());
            if (thrJVM.getName().equals("pippo")){
            	System.out.println("Thread pippo state: "+thrJVM.getState());
            	((WorkerThread) thrJVM).setRunning(false);
            }
        }
        System.out.println("Main Thread ending") ;
    }

}
class WorkerThread extends Thread {

	private boolean running = true;
    public WorkerThread() {
        setDaemon(false) ;   // When false, (i.e. when it's a user thread),
                // the Worker thread continues to run.
                // When true, (i.e. when it's a daemon thread),
                // the Worker thread terminates when the main 
                // thread terminates.
        setName("pippo");
        System.out.println("ID: "+getId()+" - Name: "+getName());
    }

    public void setRunning(boolean running){
    	this.running = running;
    }
    public void run() {
        int count=0 ;
        while (running) {
            System.out.println("Hello from Worker "+count++) ;
            try {
                sleep(1000);
            } catch (InterruptedException e) {}
        }
        System.out.println("WorkerThread ending") ;
    }
}
