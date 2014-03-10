package uk.ac.ncl.cs.esc.deployment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.ncl.cs.esc.exceptionHandler.exceptionHandler;
import uk.ac.ncl.cs.esc.partitiontool.Cloud;
import uk.ac.ncl.cs.esc.partitiontool.CloudSet;

public class Deployment implements Runnable {
	
	ArrayList<Object> partition;
	CloudSet cloudset;
	int partitionName;
	
	public Deployment(ArrayList<Object> partition,CloudSet cloudset,int partitionName){
		this.partition=partition;
		this.cloudset=cloudset;
		this.partitionName=partitionName;
	}
	public boolean checkCloud(){
		cloudCheck isAvailable =new cloudCheck(partition,cloudset);
		int  cloud=(Integer) partition.get(0);
		String cloudName="Cloud"+cloud;
		Cloud thecloud=cloudset.getCloud(cloudName);
		String cloudip=thecloud.getip();
		if(isAvailable.checkCloud(cloudip)){
			return true;
		}else{
			return false;
		}
	}
	
	public HashMap<String, ByteArrayOutputStream> deploy(){
		
		HashMap<String,ByteArrayOutputStream>results=new HashMap<String,ByteArrayOutputStream>();
		
		if(checkCloud()){
			
		}else{
			
		}

		return results;
	}
	
	public void run(){
		
	}
}
