package ncl.ac.uk.esc.monitor;

import java.util.Hashtable;

public class ThreadPool {
	 public static final int MAX_THREADS = 200;
	 protected int maxThreads;
	 /*to stop all threads*/
	 protected boolean stopThePool;
	 /*name of the ThreadPool*/
	 protected String name="ThreadPool";
	 /*threads is used to store threads and their controlRunable*/
	 protected  Hashtable threads=new Hashtable();
	 
//	 protected ControlRunnable[] pool = null;
	 public ThreadPool(){
		 this.maxThreads=MAX_THREADS;
	 }
	 public static ThreadPool createThreadPool(boolean jmx){
		 return new ThreadPool();
	 }
	 
	/* public synchronized void start(){
		 
		 stopThePool=false;
		 pool= new ControlRunnable[maxThreads];
	 }*/
	/* public static class ControlRunnable implements Runnable {
		 
		 ThreadPool where this thread will be returned
		 private ThreadPool p;
		 
		Stop this thread
		 private boolean shouldTerminate;
		 Acative the execution of the action
		 private boolean shouldRun;
		 
		 Start a new thread 
		 ControlRunnable(ThreadPool p){
			 shouldTerminate = false;
             shouldRun = false;
	         this.p = p;
	        
		 }
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		 }
		}*/
	 
	 /*public void addThread(Thread t, ControlRunnable cr){
			threads.put(t,cr);
		}*/
	 public void addThread(String machineName,Thread t){
		threads.put(name, t);
	 }
	 public void removeThread(Thread t){
		  threads.remove(t);
	 }
}
