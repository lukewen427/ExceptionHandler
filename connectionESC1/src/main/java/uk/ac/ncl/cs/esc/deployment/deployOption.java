package uk.ac.ncl.cs.esc.deployment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class deployOption implements Runnable {
	// the selected option, or rescheduled option 
	ArrayList<Object> option;
	ArrayList<Object>links;
	HashMap<Integer,HashMap<String,ByteArrayOutputStream>> results;
	int exceptionPartition;
	 public deployOption(  HashMap<Integer,ArrayList<Object>> thegraph ,ArrayList<Object> links,int exceptionPartition){
		this.option=option;
		this.links=links;
		this.exceptionPartition=exceptionPartition;
	}

	public void run() {
		
	}
	
	private void deployment(){
		ArrayList<Integer>executedPartition=new ArrayList<Integer>();
		//first time deploy the don't have exception partition, so it is 0
		if(exceptionPartition==0){
			
		}{
			
		}
	}
	
	private ArrayList<Object> initialPartitions(){
		ArrayList<Object> startNodes=new ArrayList<Object>();
		
		return startNodes;
	}
}
