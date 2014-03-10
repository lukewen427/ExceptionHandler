package uk.ac.ncl.cs.esc.exceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class exceptionHandler implements Runnable {

	HashMap<Integer,ArrayList<Object>> graph;
	ArrayList<Object> links;
	// used to store the running partitions
	Hashtable<Integer,Thread>  runningPartitions=new Hashtable<Integer,Thread>();
	
	
	public exceptionHandler( HashMap<Integer,ArrayList<Object>> graph, ArrayList<Object> links){
		this.graph=graph;
		this.links=links;
	}
	public void getException(int partitionName){
		
	}

	public void run() {
		
		
	}
	
	public synchronized void addNewPartition(int partitionName,Thread t){
		
		 runningPartitions.put(partitionName, t);
	}
	
	public synchronized void removePartition(int partitionName){
		
		runningPartitions.remove(partitionName);
	}
}
