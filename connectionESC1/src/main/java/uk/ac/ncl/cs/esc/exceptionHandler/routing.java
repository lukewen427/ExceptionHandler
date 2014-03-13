package uk.ac.ncl.cs.esc.exceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class routing {
	
	//HashMap<Integer,ArrayList<Object>>  graph;
	ArrayList<Object> costLink;
	ArrayList<Integer> exceptionPartition;
	HashMap<String,ArrayList<Integer>> terminalNodes;
	ArrayList<Integer> set;
	ArrayList<Object>cheapestPath;
	 HashMap<Integer,Integer> partitioncost;
	int [][] dist;
	int [][] path;
	int [][] pathcost;
	// for the first time deployment, exceptionPartition is the initial partitions of the cheapest option
	public routing( ArrayList<Object> costLink,ArrayList<Integer> exceptionPartition,
			HashMap<String,ArrayList<Integer>> terminalNodes, HashMap<Integer,Integer> partitioncost){
	//	this.graph=thegraph;
		this.costLink=costLink;
		this.exceptionPartition=exceptionPartition;
		this.terminalNodes=terminalNodes;
		this.set= terminalSet();
		int size=partitioncost.size();
		dist=new int[size+1][size+1];
		path=new int[size+1][size+1];
		pathcost=new int[size+1][size+1];
		this.partitioncost= partitioncost;
		findCheapestPath();
	}
	
	public ArrayList<Object> getNewRouting(){
		return cheapestPath;
	}
	// the cheapest path between two the exception set and leaves
	public void findCheapestPath(){
		ArrayList<Object>thepath=new ArrayList<Object>();
		ArrayList<Integer> reachedterminalNode=new ArrayList<Integer>();
		for(int a=0;a<exceptionPartition.size();a++){
			int node=exceptionPartition.get(a);
			thepath=cheapestPath(node,thepath,reachedterminalNode);
		}
		
		if(found(reachedterminalNode)){
			this.cheapestPath=(ArrayList<Object>) thepath.clone();
		}else{
			// if there are more than one exception nodes and terminal nodes
			Floyd();
			Iterator<String> keys=terminalNodes.keySet().iterator();
			ArrayList<Object> foundPath=new ArrayList<Object>();
			int min=0;
			while(keys.hasNext()){
				String optionName=keys.next();
				ArrayList<Integer>temp=terminalNodes.get(optionName);	
				thecosts(temp,min,foundPath);
			}
			this.cheapestPath=(ArrayList<Object>) foundPath.clone();
		}
	}
	
	private void thecosts(ArrayList<Integer> terminalPartition,int min,ArrayList<Object> foundPath){
		int cost=0;
		ArrayList<Integer> visited=new ArrayList<Integer>();
		ArrayList<Object> pathCollection=new ArrayList<Object>();
		for(int a=0;a<exceptionPartition.size();a++){
			int startNode=exceptionPartition.get(a);
			for(int i=0;i<terminalPartition.size();i++){
				int endNode=terminalPartition.get(i);
				int Value=returnPathCost(startNode,endNode);
				if(Value!=0){
					ArrayList<Integer> getPath=returnPath(startNode,endNode,new ArrayList<Integer>());
					pathCollection.add((ArrayList<Integer>)getPath.clone());
					if(!visited.contains(endNode)){
						visited.add(endNode);
					}
				
				}
			}
		}
		
		if(visited.size()!=terminalPartition.size()){
			return;
		}else{
			ArrayList<Object> newPath= pathCombine(pathCollection);
			ArrayList<Integer> allNodes=new ArrayList<Integer>();
			for(int x=0;x<newPath.size();x++){
				ArrayList<Integer> tempPath=(ArrayList<Integer>) newPath.get(x);
				int i=tempPath.get(0);
				int j=tempPath.get(1);
				if(!allNodes.contains(i)){
					allNodes.add(i);
				}
				if(!allNodes.contains(j)){
					allNodes.add(j);
				}
				int thecost=pathcost[i][j];
				cost=thecost+cost;
				for(int h=0;h<allNodes.size();h++){
					int partition=allNodes.get(h);
					int partitionCost=partitioncost.get(partition);
					cost=partitionCost+cost;
				}
				
				if(min==0){
					if(cost!=0){
						min=cost;
						foundPath=(ArrayList<Object>) tempPath.clone();
					}
				}else{
					if(cost!=0 && min>cost){
						min=cost;
						foundPath=(ArrayList<Object>) tempPath.clone();
					}
				}
			}
		}
	
	}
	
	// remove the duplicate path 
	private ArrayList<Object> pathCombine(ArrayList<Object> pathCollection){
		ArrayList<Object> newPath=new ArrayList<Object>();
		
		for(int a=0;a<pathCollection.size();a++){
			ArrayList<Integer> getPath=(ArrayList<Integer>) pathCollection.get(a);
			for(int i=0;i<getPath.size()-2;i++){
				ArrayList<Integer> temp=new ArrayList<Integer> ();
				int source=getPath.get(i);
				int des=getPath.get(i+1);
				temp.add(source);
				temp.add(des);
				if(newPath.isEmpty()){
					newPath.add(new ArrayList<Integer>((ArrayList<Integer>) temp.clone()));
				}else{
					if(!pathIncluded(temp,newPath)){
						newPath.add(new ArrayList<Integer>((ArrayList<Integer>) temp.clone()));
					}
				}
				
			}
		}
		return newPath;
	}
	//  cheapest cost of each node 
	private void Floyd(){
		// transfer the path to array
		for(int a=0;a<costLink.size();a++){
			ArrayList<Object>link=(ArrayList<Object>) costLink.get(a);
			ArrayList<Integer> thelink=(ArrayList<Integer>) link.get(0);
			int thecost=(Integer) link.get(1);
			int source =thelink.get(0);
			int destination=thelink.get(1);
			dist[source][destination]=thecost;
			pathcost[source][destination]=thecost;
		}
		
		int size=partitioncost.size();
		// the shortest path between each node
		for(int k=1;k<=size;k++){
			for(int i=1;i<=size;i++){
			 for(int j=1;j<=size;j++){
				 if(dist[i][k]+dist[k][j]<dist[i][j]){
					 dist[i][j]=dist[i][k]+dist[k][j];
					 path[i][j]=k;
				 }
			 }
			}
		}
		
	}
	
	public int returnPathCost(int start,int end){
		return dist[start][end];
	}
	
	public ArrayList<Integer> returnPath(int start,int end,ArrayList<Integer> singalPath){
		if(start==end){
			return singalPath;
		}
		if(path[start][end]==0){
			singalPath.add(end);
		}else{
			returnPath(start,path[start][end],singalPath);
			returnPath(path[start][end],end,singalPath);
		}
		return singalPath;
		
	}
	private boolean found(ArrayList<Integer> reachedterminalNode){
		Iterator<String> keys=terminalNodes.keySet().iterator();
		
		while(keys.hasNext()){
			boolean iscontained=true;
			String optionName=keys.next();
			ArrayList<Integer>temp=terminalNodes.get(optionName);	
			for(int a=0;a<temp.size();a++){
				int partitionName=temp.get(a);
				if(!reachedterminalNode.contains(partitionName)){
					iscontained=false;
					break;
				}
			}
			if(iscontained){
				return iscontained;
			}
		}
		return false;
	}
	public ArrayList<Object> cheapestPath(int node,ArrayList<Object> path,ArrayList<Integer> reachedterminalNode){
		ArrayList<Integer> thepath=getCheaptestPath(node);
		if(!pathIncluded(thepath,path)){
			path.add(thepath);
		}
		int endNode=thepath.get(1);
		if(iscontained(endNode)){
			 reachedterminalNode.add(endNode);
			return path;
		}else{
			return cheapestPath(endNode,path,reachedterminalNode);
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
