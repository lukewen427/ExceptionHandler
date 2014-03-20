package uk.ac.ncl.cs.esc.deployment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
	String staute;
	String cloudName;
	ArrayList<ArrayList<String>> connections;
	HashMap<String,ByteArrayOutputStream> results;
	BlockSet blockset;
	ArrayList<ArrayList<String>>inputs;
	HashMap<String,ByteArrayOutputStream>newresults=new HashMap<String,ByteArrayOutputStream>();
	
	public Deployment(ArrayList<Object> partition,CloudSet cloudset,int partitionid,HashMap<String,ByteArrayOutputStream> results,
			ArrayList<ArrayList<String>>inputs,BlockSet blockset,ArrayList<ArrayList<String>> connections){
		this.partition=partition;
		this.cloudset=cloudset;
		this.partitionid=partitionid;
	     this.results=results;
	     this.connections= connections;
	     this.blockset=blockset;
	     this.inputs=inputs;
	    
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
	
	public void deploy(){
		
		if(checkCloud()){
			String partitionName="Partition"+partitionid;
			WorkflowRestructure deployworkflow=new WorkflowRes();
			try {
				staute="running";
				newresults=deployworkflow.CreateWorkflow(cloudName,partition, partitionName,
							 connections,blockset,inputs, results);
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

	}
	
	private synchronized void resultsStoring(HashMap<String,ByteArrayOutputStream> newResults){
		
		if(!newResults.isEmpty()){
			Iterator<String> name=newResults.keySet().iterator();
			while(name.hasNext()){
				String singalParition=name.next();
				ByteArrayOutputStream data=newResults.get(singalParition);
				if(results.isEmpty()){
					results.put(singalParition, data);
				}else{
					Set<String> storedData=results.keySet();
					if(!storedData.contains(singalParition)){
						results.put(singalParition, data);
					}
				}
				
			}
		}
	}
	
	
	public String checkStautes(){
		return staute;
	}
	
	public void run(){
		
		 deploy();
		 if(staute.equals("finish")){
			 resultsStoring(newresults);
		 }
	}
}
