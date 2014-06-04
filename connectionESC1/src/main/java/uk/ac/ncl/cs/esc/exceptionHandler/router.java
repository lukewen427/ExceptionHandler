package uk.ac.ncl.cs.esc.exceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.ncl.cs.esc.deployment.cloudCheck;
import uk.ac.ncl.cs.esc.partitiontool.Cloud;
import uk.ac.ncl.cs.esc.partitiontool.CloudSet;

public class router {
	// create a graph to store all of the nodes, in order to find the alternative path
	int [][] dist;
	int [][] path;
	int [][] pathcost;
	ArrayList<String> initials;
	 ArrayList<String> terminals;
	 HashMap<String,ArrayList<Integer>> deploygraph;
	 ArrayList<Object>deploylinkcost;
	 HashMap<String,Integer>deploygraphcost;
	 HashMap<Integer,ArrayList<Object>> graph;
	 CloudSet cloudset;
	 String exceptionNode;
	 ArrayList<ArrayList<String>> excPath;
	 ArrayList<ArrayList<String>> foundpath=new ArrayList<ArrayList<String>>();
	 
	public router( HashMap<String,ArrayList<Integer>> deploygraph, ArrayList<Object>deploylinkcost,HashMap<String,Integer>deploygraphcost,ArrayList<ArrayList<String>> excPath,
			HashMap<Integer,ArrayList<Object>> thegraph,String exceptionNode,ArrayList<String> initials, ArrayList<String> terminals,CloudSet cloudset){
		this.deploygraph=deploygraph;
		this.deploylinkcost=deploylinkcost;
		int size=deploygraph.size();
		dist=new int [size+1][size+1];
		path=new int[size+1][size+1];
		pathcost=new int[size+1][size+1];
		this.initials=initials;
		this.terminals=terminals;
		this.deploygraphcost=deploygraphcost;
		this.exceptionNode=exceptionNode;
		this.graph=thegraph;
		this.cloudset=cloudset;
		this.excPath=excPath;
		try {
			findCheapestPath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<ArrayList<String>> getPath(){
		return foundpath;
	}
	
	// find the cheapest path
	public void findCheapestPath() throws Exception{
		
		Floyd();
		
		// if exceptionNode is null, it means deployment in the initial state, need find a initial path
		if(exceptionNode==null){
			 ArrayList<Integer> temppath=new ArrayList<Integer>();
				ArrayList<Integer> temp=new ArrayList<Integer>();
		
				 fromRoot(temppath,temp,initials);
			
		//	this.foundpath=(ArrayList<Integer>) temppath.clone();
				
				 pathtoString(temppath);
		}else{
			// find the alternative path
			// find parent nodes
			if(initials.contains(exceptionNode)){
				ArrayList<String> startNodes=new ArrayList<String>();
				for(String node:initials){
					if(!node.equals(exceptionNode)){
						startNodes.add(node);
					}
				}
				ArrayList<Integer> temppath=getPath(startNodes,exceptionNode);
				if(temppath.isEmpty()||temppath==null){
					throw new Exception("The workflow Can not be deployed");
				}else{
					// this.foundpath=(ArrayList<Integer>) temppath.clone();
					pathtoString(temppath);
				}
				
				
			}else{
				// the exception partition is not the root nodes
				// the parents node must be the node which has been executed. So there is only in parents node 
				String parents=getParentNodes(exceptionNode,excPath);
				ArrayList<String> brotherNodes=getBrotherNodes( parents,exceptionNode);	
			    ArrayList<Integer> temppath=alternativePath(brotherNodes,exceptionNode,parents);
			    if(temppath.isEmpty()||temppath==null){
					throw new Exception("The workflow Can not be deployed");
				}else{
					// this.foundpath=(ArrayList<Integer>) temppath.clone();
					pathtoString(temppath);
				}
				
			}
		}
		
	}
	

	private void pathtoString(ArrayList<Integer> temppath){
		for(int a=0;a<temppath.size()-1;a++){
			ArrayList<String> oneStep=new ArrayList<String>();
			String s="P"+temppath.get(a);
			String d="P"+temppath.get(a+1);
			oneStep.add(s);
			oneStep.add(d);
			foundpath.add((ArrayList<String>)oneStep.clone());
		}
	}
	
	// return the parent node of the exception nodes
	private String getParentNodes(String node,ArrayList<ArrayList<String>> excPath){
		
		String parents=null;
		for(int a=0;a<excPath.size();a++){
			ArrayList<String>link=(ArrayList<String>) excPath.get(a);
			String s= link.get(0);
			String d= link.get(1);
			if(d.equals(node)){
				parents=s;
			}
		}
		return parents;
	}
	// return the brother node the exception nodes
	private ArrayList<String> getBrotherNodes(String parents,String node){
		ArrayList<String> brotherNodes=new ArrayList<String>();
		
			for(int a=0;a<deploylinkcost.size();a++){
				ArrayList<Object>link=(ArrayList<Object>) deploylinkcost.get(a);
				ArrayList<String> thelink=(ArrayList<String>) link.get(0);
				String s= thelink.get(0);
				String d= thelink.get(1);
				if(s.equals(parents)){
					if(!d.equals(node)){
						brotherNodes.add(d);
					}
				}
			}
		
		return brotherNodes;
	}
	// 
	private ArrayList<Integer> getPath(ArrayList<String> startNodes,String node){
		
		 ArrayList<Integer> temppath=new ArrayList<Integer>();
		 ArrayList<Integer> temp=new ArrayList<Integer>();
		 fromRoot(temppath,temp,startNodes);
		 String getPartition="P"+temppath.get(0);
		 if(isDeployable(getPartition)){
			 return temppath;
		 }else{
			
				ArrayList<String> startNode=new ArrayList<String>();
				for(String thenode:startNodes){
					if(!thenode.equals(node)){
						startNode.add(thenode);
					}
				}
				if(startNode.isEmpty()){
					return null;
				}else{
					return getPath(startNode,node);
				}
			
		 }
	}
	// find a alternative path for exception node
	private ArrayList<Integer> alternativePath(ArrayList<String> brothers,String node,String parents ){
		ArrayList<Integer> path=getPath(brothers,node);
		// if the brother nodes also can not be satisfied, go the grandparents  
		if(path.isEmpty()||path==null){
			String newParents=getParentNodes(parents,excPath);
			ArrayList<String> newBrothers=getBrotherNodes( newParents,parents);
			return alternativePath(newBrothers,parents,newParents);
		}
		if((path.isEmpty()||path==null)&&parents==null){
			return null;
		}
		return path;
	}
	
	private boolean isDeployable(String newPartition){
		ArrayList<Integer> nodes=deploygraph.get(newPartition);
		for(int a=0;a<nodes.size();a++){
			int partitionName=nodes.get(a);
			ArrayList<Object> partition =graph.get(partitionName);
			boolean cloudisAvailable=checkingCloud(partition);
			if(!cloudisAvailable){
				return false;
			}
		}
		
		return true;
	}
	
	private boolean checkingCloud(ArrayList<Object> partition){
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
	private void fromRoot(ArrayList<Integer> temppath,ArrayList<Integer> temp,ArrayList<String> startNodes){
		
		int min=0;
		for(int a=0;a<startNodes.size();a++){
			String startNode=startNodes.get(a);
			// the cheapest cost from the startNdoe
	
			int cost=thecost(temp,startNode);
			
			if(min==0){
				min=cost;
				temppath.clear();
				for(int i=0;i<temp.size();i++){
					int node=temp.get(i);
					temppath.add(node);
				}
				//temppath=(ArrayList<Integer>) temp.clone();
				
			}else{
				if(cost<min){
					min=cost;
					temppath.clear();
					for(int i=0;i<temp.size();i++){
						int node=temp.get(i);
						temppath.add(node);
					}
				}
			}  
			
		}
	}
	private int thecost(ArrayList<Integer>temp,String startNode){
		int minPath=0;
	//	ArrayList<Object> pathCollection=new ArrayList<Object>();
		String[] s=startNode.split("P");
		int start=Integer.valueOf(s[1]);
		for(int a=0;a<terminals.size();a++){
			String[] endNode=terminals.get(a).split("P");
			int end=Integer.valueOf(endNode[1]);
			ArrayList<Integer> getPath=new ArrayList<Integer>();
		
			getPath.add(start);
			returnPath(start,end,getPath);
		//	System.out.println(getPath);
			int cost=getCostNodes(getPath);
			if(minPath==0){
				minPath=cost;
			//	temppath=(ArrayList<Integer>) temp.clone();
				// deep copy
				temp.clear();
				for(int i=0;i<getPath.size();i++){
					int node=getPath.get(i);
					temp.add(node);
				}
			}else{
				if(cost<minPath){
					minPath=cost;
			//		temppath=(ArrayList<Integer>) temp.clone();
					// deep copy
					temp.clear();
					for(int i=0;i<getPath.size();i++){
						int node=getPath.get(i);
						temp.add(node);
					}
				}
			}
			
		}
		return minPath;
	}
	
	private int getCostNodes(ArrayList<Integer> getPath){
		int cost=0;
		for(int a=0;a<getPath.size();a++){
			int node=getPath.get(a);
			String nodeName="P"+node;
			cost=deploygraphcost.get(nodeName)+cost;
		}
		return cost;
	}
	private void returnPath(int start,int end,ArrayList<Integer> singalPath){
		
		if(start==end){
			return;
		}
		if(path[start][end]==0){
		//	System.out.print(end+" ");
			singalPath.add(end);
		}else{
			returnPath(start,path[start][end],singalPath);
			returnPath(path[start][end],end,singalPath);
		}
		
	}
//  cheapest cost of each node 
	private void Floyd(){
		int size=deploygraph.size();
		// transfer the path to array
		for(int i=1;i<=size;i++){
			   for(int j=1;j<=size;j++){
				   dist[i][j]=(i==j)?0:1000000;
				   path[i][j]=0;
			   }
			
		}
		for(int a=0;a<deploylinkcost.size();a++){
			ArrayList<Object> link=(ArrayList<Object>) deploylinkcost.get(a);
			ArrayList<String> thelink=(ArrayList<String>) link.get(0);
			int thecost=(Integer) link.get(1);
			String s[]=thelink.get(0).split("P");
			String d[]=thelink.get(1).split("P");
			int source=Integer.valueOf(s[1]);
			int destination=Integer.valueOf(d[1]);
			dist[source][destination]=thecost;
			pathcost[source][destination]=thecost;
		 }
			
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
}
