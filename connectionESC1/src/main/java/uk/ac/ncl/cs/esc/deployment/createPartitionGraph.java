package uk.ac.ncl.cs.esc.deployment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class createPartitionGraph {
	HashMap<String,ArrayList<Object>> costSet;
	// 
	HashMap<String, ArrayList<Object>> validMap;
	// keys are the name of the partition, values are the partitions
	HashMap<Integer,ArrayList<Object>> graph =new HashMap<Integer,ArrayList<Object>>();
	 HashMap<String,ArrayList<Object>> startNodes;
	 HashMap<String,ArrayList<Object>> endNodes;
	 HashMap<Integer,Integer> costgraph =new HashMap<Integer,Integer>();
	ArrayList<Object> newLink=new ArrayList<Object>();
	ArrayList<Object> newLinkcost=new ArrayList<Object>();
	// the links of each option, after map is created 
	HashMap<String,ArrayList<Object>> optionLink=new HashMap<String,ArrayList<Object>>();
	// root partitions
	HashMap<String,ArrayList<Integer>> rootNodes=new HashMap<String,ArrayList<Integer>>();
	// terminal partitions
	HashMap<String,ArrayList<Integer>> terminalNodes=new HashMap<String,ArrayList<Integer>>();
	int Number=1;
	public createPartitionGraph(HashMap<String,ArrayList<Object>> costSet,HashMap<String, ArrayList<Object>> validMap,
			 HashMap<String,ArrayList<Object>> endNodes,	HashMap<String,ArrayList<Object>> startNodes){
		this.costSet=costSet;
		this.validMap=validMap;
		this.startNodes=startNodes;
		this.endNodes=endNodes;
		 createGraph();
	}
	
	public HashMap<Integer,ArrayList<Object>> getGraph(){
		return graph;
	}
	
	public ArrayList<Object> getLinks(){
		return newLink;
	}
	public HashMap<String,ArrayList<Integer>> getrootPartition(){
		return rootNodes;
	}
	public HashMap<String,ArrayList<Integer>> getterminialPartition(){
		return terminalNodes;
	}
	public HashMap<Integer,Integer> getPartitioncost(){
		return costgraph;
	}
	public ArrayList<Object> getLinkCost(){
		return newLinkcost;
	}
	
	public HashMap<String,ArrayList<Object>> getoptionLinks(){
		return optionLink;
	}
	private void createGraph(){
		Iterator<String> optionNames=validMap.keySet().iterator();
		while(optionNames.hasNext()){
			String optionName=optionNames.next();
			ArrayList<Object>option=validMap.get(optionName);
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
		
		ArrayList<Object>optioncost=costSet.get(optionName);
		HashMap<Integer,Integer>partitionscost=(HashMap<Integer, Integer>) optioncost.get(0);
		
		addPartitions(partitions,connections,copyConnections,partitionscost);
		 ArrayList<Object> connectioncost=( ArrayList<Object>) optioncost.get(1);
		 		
			setrootPartition(optionName);
			setterminialPartition(optionName);
			addLinks(connections, connectioncost);
			optionLinks(optionName,connections);
		}
	}
	
	private void optionLinks(String optionName,ArrayList<Object> connections){
		ArrayList<Object> newPLinks=new ArrayList<Object>();
		for(int a=0;a<connections.size();a++){
			ArrayList<Object> thelink= (ArrayList<Object>) connections.get(a);
			ArrayList<Integer> pLink=(ArrayList<Integer>) thelink.get(0);
			newPLinks.add(new ArrayList<Integer>((ArrayList<Integer>)pLink.clone()));
		}
	//	System.out.println(newPLinks);
		optionLink.put(optionName, (ArrayList<Object>) newPLinks.clone());
		
	}
	
	private void setrootPartition(String optionName){
		
		ArrayList<Object> nodes=startNodes.get(optionName);
		ArrayList<Integer> tempNodes=new ArrayList<Integer>();
		for(int a=0;a<nodes.size();a++){
			ArrayList<Object> singalPartition=(ArrayList<Object>) nodes.get(a);
			int partitionNum=isContained(singalPartition);
			tempNodes.add(partitionNum);
		}
		rootNodes.put(optionName, new ArrayList<Integer>((ArrayList<Integer>)tempNodes.clone()));
	}
	
	private void setterminialPartition(String optionName){
		ArrayList<Object> nodes=endNodes.get(optionName);
		ArrayList<Integer> tempNodes=new ArrayList<Integer>();
		for(int a=0;a<nodes.size();a++){
			ArrayList<Object> singalPartition=(ArrayList<Object>) nodes.get(a);
			int partitionNum=isContained(singalPartition);
			tempNodes.add(partitionNum);
		}
		terminalNodes.put(optionName, new ArrayList<Integer>((ArrayList<Integer>)tempNodes.clone()));
	}
	
	private void addLinks(ArrayList<Object>connections, ArrayList<Object>connectioncost){
		if(newLink.isEmpty()){
			for(int a=0;a<connections.size();a++){
				ArrayList<Object> thelink=(ArrayList<Object>) connections.get(a);
				ArrayList<Object> thelinkcost=(ArrayList<Object>) connectioncost.get(a);
				newLink.add(thelink.clone());
				newLinkcost.add(thelinkcost.clone());
			}
		}else{
			for(int a=0;a<connections.size();a++){
				ArrayList<Object> thelink=(ArrayList<Object>) connections.get(a);
				ArrayList<Object> thelinkcost=(ArrayList<Object>) connectioncost.get(a);
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
					newLinkcost.add(thelinkcost.clone());
				}
			}
		}
	}
	
	private void addPartitions(HashMap<Integer,ArrayList<Object>> partitions,ArrayList<Object>connections,
						ArrayList<Object>copyConnections,HashMap<Integer,Integer>partitionscost){
		Iterator<Integer> partitionNames=partitions.keySet().iterator();
		while(partitionNames.hasNext()){
			int thePartitionName=partitionNames.next();
			ArrayList<Object> partition=partitions.get(thePartitionName);
			if(graph.isEmpty()){
				graph.put(Number, partition);
				int getcost=partitionscost.get(thePartitionName);
				costgraph.put(Number, getcost);
				changeConnections(copyConnections,connections,Number,thePartitionName);
				Number++;
			}else{
				int theNumber=isContained(partition);
				
				if(theNumber==0){
					graph.put(Number, partition);
					int getcost=partitionscost.get(thePartitionName);
					costgraph.put(Number, getcost);
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
