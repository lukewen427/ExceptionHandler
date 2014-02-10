package ncl.ac.uk.esc.monitor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;



public class connectionPool implements Runnable {
	
	
	 /*store the available thread*/
	 protected  Hashtable<String, connection> threads=new Hashtable<String, connection>();
	 protected  ArrayList<String> currentService=new ArrayList<String>();
	 private int maxThread=20;
	 private  Set<String> machines;
	 /*the running thread*/
	
	@Override
	public void run() {
		
		while(true){
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
			}
	   }
	
	public connectionPool(){
		new getStatue(this);
	}
	
	public  synchronized Set<String> getMachine(){
		if(threads.isEmpty()){
			machines=null;
		}else{
			machines=threads.keySet();
		}
		return machines;
	}
	
	public  synchronized void addConnection(connection con){
		if(threads.size()<maxThread){
			String inet=con.getAddress().getHostAddress();
			threads.put(inet, con);
		}
	}
	
	public synchronized void addService(String service){
		if(!currentService.contains(service)){
			currentService.add(service);
		}else{
			
		}
	}
	public synchronized void removeService(String service){
		if(currentService.contains(service)){
			currentService.remove(service);
		}else{
			
		}
	}
	public ArrayList<String> getService(){
		return currentService;
	}
	public  Hashtable<String, connection> getConnections(){
		return threads;
	}
	
	public synchronized void removeConnection(String inet){
		
		threads.remove(inet);
	}
	
	public static class getStatue{
		static connectionPool thePool;
		static Hashtable<String, connection> connections;
		public static Set<String> machines;
		public static ArrayList<String>service;
		getStatue(connectionPool thePool){
			this.thePool=thePool;
		//	System.out.println(connections);
		}
		public static void SetMachine(){
			machines=thePool.getMachine();
		}
		public static Set<String>  getMachine(){
			SetMachine();
			return machines;
		}
		public static void Setconnections(){
			connections=thePool.getConnections();
			
		}
		public static Hashtable<String, connection> getConnections(){
			Setconnections();
			return connections;
		}
		public static ArrayList<String> getService(){
			service=thePool.getService();
			return service;
		}
	}
	
}


