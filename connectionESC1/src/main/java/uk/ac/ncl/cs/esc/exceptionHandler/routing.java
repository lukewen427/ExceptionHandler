package uk.ac.ncl.cs.esc.exceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class routing {
	
	HashMap<Integer,ArrayList<Object>>  graph;
	ArrayList<Object> costLink;
	ArrayList<Integer> exceptionPartition;
	HashMap<String,ArrayList<Integer>> terminalNodes;
	ArrayList<Integer> set;
	// for the first time deployment, exceptionPartition is the initial partitions of the cheapest option
	public routing( HashMap<Integer,ArrayList<Object>> thegraph, ArrayList<Object> costLink,
	      ArrayList<Integer> exceptionPartition,HashMap<String,ArrayList<Integer>> terminalNodes){
		this.graph=thegraph;
		this.costLink=costLink;
		this.exceptionPartition=exceptionPartition;
		this.terminalNodes=terminalNodes;
		this.set= terminalSet();
		
		findCheapestPath();
	}
	
	public ArrayList<Integer> getNewRouting(){
		return null;
	}
	// the cheapest path between two the exception set and leaves
	public void findCheapestPath(){
		ArrayList<Object>thepath=new ArrayList<Object>();
		
		for(int a=0;a<exceptionPartition.size();a++){
			
			int node=exceptionPartition.get(a);
			thepath=cheapestPath(node,thepath);
		}
		System.out.println(thepath);
	}
	
	public ArrayList<Object> cheapestPath(int node,ArrayList<Object> path){
		ArrayList<Integer> thepath=getCheaptestPath(node);
		if(!pathIncluded(thepath,path)){
			path.add(thepath);
		}
		int endNode=thepath.get(1);
		if(iscontained(endNode)){
			return path;
		}else{
			return cheapestPath(endNode,path);
		}
	}
	
	private boolean pathIncluded(ArrayList<Integer>newpath,ArrayList<Object> path){
		int source=newpath.get(0);
		int destination=newpath.get(1);
		for(int a=0;a<path.size();a++){
			ArrayList<Integer> thepath=(ArrayList<Integer>) path.get(a);
			if(source==thepath.get(0)&&destination==thepath.get(1)){
				return true;
			}
		}
		return false;
	}
	private ArrayList<Integer> getCheaptestPath(int node){
		int min=0;
		int num=0;
		for(int a=0;a<costLink.size();a++){
			ArrayList<Object>link=(ArrayList<Object>) costLink.get(a);
			ArrayList<Integer> thelink=(ArrayList<Integer>) link.get(0);
			int thecost=(Integer) link.get(1);
			int source =thelink.get(0);
		//	int destination=thelink.get(1);
			if(node==source){
				if(min==0){
					min=thecost;
					num=a;
				}else{
					if(thecost<min){
						min=thecost;
						num=a;
					}
				}
			}
		}
		
		ArrayList<Integer> selectedPah=(ArrayList<Integer>)((ArrayList<Object>) costLink.get(num)).get(0);
		return selectedPah;
	}
	// check the node is contained in the terminal set
	private boolean iscontained(int node){
	
		if(set.contains(node)){
			return true;
		}else{
			return false;
		}
	}
	
	// create terminal set
	private ArrayList<Integer> terminalSet(){
		ArrayList<Integer> set=new ArrayList<Integer>();
		Iterator<String> keys=terminalNodes.keySet().iterator();
		while(keys.hasNext()){
			String optionName=keys.next();
			ArrayList<Integer>temp=terminalNodes.get(optionName);	
			for(int a=0;a<temp.size();a++){
				int partitionName=temp.get(a);
				if(!set.contains(partitionName)){
					set.add(partitionName);
				}
			}
		}
		return set;
	}
}
