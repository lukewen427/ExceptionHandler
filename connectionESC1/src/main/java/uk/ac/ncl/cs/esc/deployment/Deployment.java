package uk.ac.ncl.cs.esc.deployment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import uk.ac.ncl.cs.esc.exceptionHandler.dataCenter.dataStorage;
import uk.ac.ncl.cs.esc.exceptionHandler.exceptionHandler;
import uk.ac.ncl.cs.esc.partitiontool.BlockSet;
import uk.ac.ncl.cs.esc.partitiontool.Cloud;
import uk.ac.ncl.cs.esc.partitiontool.CloudSet;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRes;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRestructure;

public class Deployment implements Runnable {
	
	ArrayList<Object> partition;
	CloudSet cloudset;
	int partitionid;
	String staute="checking";
	String cloudName;
	ArrayList<ArrayList<String>> connections;
	BlockSet blockset;
	ArrayList<ArrayList<String>>inputs;
	ArrayList<String> heads;
	HashMap<String,ByteArrayOutputStream>newresults=new HashMap<String,ByteArrayOutputStream>();
	
	public Deployment(ArrayList<Object> partition,CloudSet cloudset,int partitionid,
		ArrayList<ArrayList<String>>inputs,BlockSet blockset,ArrayList<ArrayList<String>> connections,ArrayList<String> heads){
		this.partition=partition;
		this.cloudset=cloudset;
		this.partitionid=partitionid;
	     this.connections= connections;
	     this.blockset=blockset;
	     this.inputs=inputs;
	     this.heads=heads;
	    
	}
	
	public boolean checkCloud(){
		cloudCheck isAvailable =new cloudCheck(partition,cloudset);
		int  cloud=(Integer) partition.get(0);
		 cloudName="Cloud"+cloud;
		Cloud thecloud=cloudset.getCloud(cloudName);
		String cloudip=thecloud.getip();
		if(isAvailable.checkCloud(cloudip)){
			return true;
		}else{
			return false;
		}
	}
	
	private synchronized void resultsStoring(HashMap<String,ByteArrayOutputStream> newResults){
		
		dataStorage.setData(newResults);
	}
	
	
	public String checkStautes(){
		return staute;
	}
	
	public void run(){
		
		if(checkCloud()){
			String partitionName="Partition"+partitionid;
			WorkflowRestructure deployworkflow=new WorkflowRes();
			try {
				staute="running";
				System.out.println("running partition "+partitionName);
				HashMap<String,ByteArrayOutputStream> results=dataStorage.getData();
				newresults=deployworkflow.CreateWorkflow(cloudName,partition, partitionName,
							 connections,blockset,inputs, results,heads);
				while(newresults.isEmpty()){
					 try {
			    		 Thread.sleep(500);
			    	 } catch (Exception e){}
				}
			} catch (Exception e) {
				staute="fail";
				e.printStackTrace();
			}
			
				staute="finish";
		}else{
			staute="fail";
		}
		
		if(staute.equals("finish")){
			System.out.println("Start writting results");
			 resultsStoring(newresults);
		 } 
	}
}
