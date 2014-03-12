package uk.ac.ncl.cs.esc.deployment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class deployOption implements Runnable {
	
	HashMap<Integer,ArrayList<Object>> thegraph;
	ArrayList<Object>links;
	HashMap<Integer,HashMap<String,ByteArrayOutputStream>> results;
	ArrayList<Integer>exceptionPartition;
	// when first load the exceptionpartition is the root partitions of the cheapest option
	 public deployOption( HashMap<Integer,ArrayList<Object>> thegraph ,ArrayList<Object> links, ArrayList<Integer> exceptionPartition){
		this.thegraph =thegraph;
		this.links=links;
		this.exceptionPartition=exceptionPartition;
	}

	public void run() {
		
	}
	
	private void deployment(){
		ArrayList<Integer>executedPartition=new ArrayList<Integer>();
		
		for(int a=0;a<exceptionPartition.size();a++){
			int partitionName=exceptionPartition.get(a);
			ArrayList<Object> partitionArrayList=thegraph.get(partitionName);
		}
		
	}
	
	private ArrayList<Object> initialPartitions(){
		ArrayList<Object> startNodes=new ArrayList<Object>();
		
		return startNodes;
	}
}
