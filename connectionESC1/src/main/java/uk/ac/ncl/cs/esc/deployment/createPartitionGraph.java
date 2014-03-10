package uk.ac.ncl.cs.esc.deployment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class createPartitionGraph {
	HashMap<String,ArrayList<Object>> costSet;
	// 
	HashMap<String, ArrayList<Object>> validMap;
	// keys are the name of the partition, values are the partitions
	HashMap<Integer,ArrayList<Object>> graph =new HashMap<Integer,ArrayList<Object>>();
	ArrayList<Object> newLink=new ArrayList<Object>();
	int Number=1;
	public createPartitionGraph(HashMap<String,ArrayList<Object>> costSet, HashMap<String, ArrayList<Object>> validMap){
		this.costSet=costSet;
		this.validMap=validMap;
		 createGraph();
	}
	
	public HashMap<Integer,ArrayList<Object>> getGraph(){
		return graph;
	}
	
	public ArrayList<Object> getLinks(){
		return newLink;
	}
	private void createGraph(){
		Iterator<String> optionNames=validMap.keySet().iterator();
		while(optionNames.hasNext()){
			ArrayList<Object>option=validMap.get(optionNames.next());
			HashMap<Integer,ArrayList<Object>> partitions=(HashMap<Integer,ArrayList<Object>>) option.get(0);
			ArrayList<Object>connections=new ArrayList<Object>();
			connections=(ArrayList<Object>)((ArrayList<Object>) option.get(1)).clone();
			
			ArrayList<Object>copyConnections= new ArrayList<Object>();
			//copyConnections=(ArrayList<Object>) connections.clone();
			
		for(int x=0;x<connections.size();x++){
			ArrayList<Object>h=(ArrayList<Object>) connections.get(x);
			ArrayList<Object>newcopy=new ArrayList<Object>();
			ArrayList<Integer> pLink=(ArrayList<Integer>) h.get(0);
			ArrayList<String>bLink=(ArrayList<String>)h.get(1);
			newcopy.add(pLink.clone());
			newcopy.add(bLink.clone());
			copyConnections.add(newcopy.clone());				
			}
			
			addPartitions(partitions,connections,copyConnections);
			addLinks(connections);
		}
	}
	
	private void addLinks(ArrayList<Object>connections){
		if(newLink.isEmpty()){
			for(int a=0;a<connections.size();a++){
				ArrayList<Object> thelink=(ArrayList<Object>) connections.get(a);
				
				newLink.add(thelink.clone());
			}
		}else{
			for(int a=0;a<connections.size();a++){
				ArrayList<Object> thelink=(ArrayList<Object>) connections.get(a);
				ArrayList<Integer> pLink=(ArrayList<Integer>) thelink.get(0);
				int source=pLink.get(0);
				int destination=pLink.get(1);
				boolean isStored=false;
				for(int i=0;i<newLink.size();i++){
					ArrayList<Object> templink=(ArrayList<Object>)newLink.get(i);
					ArrayList<Integer>temppLink=(ArrayList<Integer>) templink.get(0);
					int temps=temppLink.get(0);
					int tempd=temppLink.get(1);
					if(source==temps&&destination==tempd){
						isStored=true;
						break;
					}
				}
				if(!isStored){
					newLink.add(thelink.clone());
				}
			}
		}
	}
	
	private void addPartitions(HashMap<Integer,ArrayList<Object>> partitions,ArrayList<Object>connections,ArrayList<Object>copyConnections){
		Iterator<Integer> partitionNames=partitions.keySet().iterator();
		while(partitionNames.hasNext()){
			int thePartitionName=partitionNames.next();
			ArrayList<Object> partition=partitions.get(thePartitionName);
			if(graph.isEmpty()){
				graph.put(Number, partition);
				changeConnections(copyConnections,connections,Number,thePartitionName);
				Number++;
			}else{
				int theNumber=isContained(partition);
				if(theNumber==0){
					graph.put(Number, partition);
					changeConnections(copyConnections,connections,Number,thePartitionName);
					Number ++;
				}else{
					changeConnections(copyConnections,connections,theNumber,thePartitionName);
				}
				
				
			}
		}
	}
	
	
	
	private void changeConnections(ArrayList<Object>copyConnections,ArrayList<Object>connections,int name,int thePartitionName){
		for(int a=0;a<copyConnections.size();a++){
			
			
			
			ArrayList<Object>connectioncopy=(ArrayList<Object>)copyConnections.get(a);
			ArrayList<Integer> partitionLinkcopy=(ArrayList<Integer>) connectioncopy.get(0);
			
			
			if(partitionLinkcopy.get(0)==thePartitionName){
				ArrayList<Object>connection=(ArrayList<Object>) connections.get(a);
				ArrayList<Integer> partitionLink=(ArrayList<Integer>) connection.get(0);
					partitionLink.remove(0);
					partitionLink.add(0, name);
			//	partitionLinkcopy.remove(0);
			//	partitionLinkcopy.add(0, name);
			}
			if(partitionLinkcopy.get(1)==thePartitionName){
				ArrayList<Object>connection=(ArrayList<Object>) connections.get(a);
				ArrayList<Integer> partitionLink=(ArrayList<Integer>) connection.get(0);
			//	partitionLinkcopy.remove(1);
			//	partitionLinkcopy.add(1,name);
     			partitionLink.remove(1);
				partitionLink.add(1,name);
			}
		}
	}
	private int isContained(ArrayList<Object> partition){
		
		Iterator<Integer> partitionNameSet=graph.keySet().iterator();
		while(partitionNameSet.hasNext()){
			int partitionName=partitionNameSet.next();
			ArrayList<Object> temp=graph.get(partitionName);
			if(temp.size()==partition.size()){
				boolean isSame=true;
				for(int a=0;a<temp.size();a++){
					Object block=partition.get(a);
					Object tempblock=temp.get(a);
					if(!block.equals(tempblock)){
						isSame=false;
						break;
					}
				}
				if(isSame){
					return partitionName;
				}
			}
		}
		
		return 0;
	}
}
