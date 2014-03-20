package uk.ac.ncl.cs.esc.deployment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import uk.ac.ncl.cs.esc.exceptionHandler.exceptionHandler;
import uk.ac.ncl.cs.esc.partitiontool.BlockSet;
import uk.ac.ncl.cs.esc.partitiontool.CloudSet;


public class deployOption implements Runnable {
	
	HashMap<Integer,ArrayList<Object>> thegraph;
	ArrayList<Object>links;
	HashMap<String,ByteArrayOutputStream> results;
	CloudSet cloudset;
	Hashtable<Deployment,Thread>  runningPartitions=new Hashtable<Deployment,Thread>();
	BlockSet blockset;
	ArrayList<ArrayList<String>> connections;
	ArrayList<Integer> exceutedNode=new ArrayList<Integer>();
	ArrayList<ArrayList<String>> NodePath;
	 HashMap<String,ArrayList<Integer>> deploygraph;
	 HashMap<String,Integer>deploygraphcost;
	 ArrayList<Object>deploylinkcost;
	 ArrayList<String> initials;
	 ArrayList<String> terminals;
	 boolean STOP=false;
	
	// when first load the exceptionpartition is the root partitions of the cheapest option
	 public deployOption( HashMap<Integer,ArrayList<Object>> thegraph, ArrayList<ArrayList<String>> NodePath,CloudSet cloudset, ArrayList<Object>deploylinkcost,
			 HashMap<String,ArrayList<Integer>> deploygraph,BlockSet blockset,ArrayList<ArrayList<String>> connections,HashMap<String,Integer>deploygraphcost,
			 ArrayList<String> initials,ArrayList<String> terminals){
		this.thegraph =thegraph;
		this.cloudset=cloudset;
		this.connections=connections;
		this.deploygraph=deploygraph;
		this.deploygraphcost=deploygraphcost;
		this.deploylinkcost=deploylinkcost;
		this.initials=initials;
		this.terminals=terminals;
	}

	public void run() {
		
		for(int i=0;i<=NodePath.size();i++){
			ArrayList<String> path=NodePath.get(i);
			String source=path.get(0);
			String destination=path.get(1);
			while(!runningPartitions.isEmpty()){
				Iterator<Deployment> keys=runningPartitions.keySet().iterator();
				while(keys.hasNext()){
					Deployment excu=keys.next();
					Thread t=runningPartitions.get(excu);
					String pName=t.getName();
					int node=Integer.valueOf(pName);
					while(excu.checkStautes().equals("running")){
						 try {
				    		 Thread.sleep(1000);
				    	 } catch (Exception e){}
					}
					if(excu.checkStautes().equals("fail")){
						removePartition(excu);
						String exceptionNode=null;
						if(i<NodePath.size()){
							exceptionNode=source;
						}else{
							exceptionNode=destination;
						}
						new exceptionHandler( deploygraph,deploylinkcost,
								deploygraphcost, NodePath, thegraph,
								 exceptionNode,initials, terminals, cloudset,blockset,connections);
						break;
					}
					if(excu.checkStautes().equals("finish")){
						removePartition(excu);
						exceutedNode.add(node);
					}
				}
				if(STOP){
					break;
				}
			}
			if(STOP){
				break;
			}
			// deploy the partition
			if(i==NodePath.size()){
				Deployment( destination,false);
			}
			if(i==0){
				Deployment(source,true);
			}
			if(i<NodePath.size()&&i>0){
				Deployment(source,false);
			}
		}
		
	}
	
	
	public ArrayList<ArrayList<String>> getInputs(int partitionName){
		
		ArrayList<ArrayList<String>>inputs=new ArrayList<ArrayList<String>>();
			for(int i=0;i<links.size();i++){
				ArrayList<Object> link=(ArrayList<Object>) links.get(i);
				ArrayList<Integer> pLink=(ArrayList<Integer>) link.get(0);
				ArrayList<String> bLink=(ArrayList<String>) link.get(1);
				int destination=pLink.get(1);
				if(partitionName==destination){
					inputs.add((ArrayList<String>)bLink.clone());
				}
			}
		
		return inputs;
	}

//  deploy the partitions of a node
	private void Deployment(String Node,boolean isRoot){
		ArrayList<Integer> partitions=deploygraph.get(Node);
		for(int partition:partitions){
			if(isRoot){
				deploy(partition);
			}else{
				norootDeployment(partition);
			}
		}
	}

// no-root deployment
	private void norootDeployment(int Node){
		
	if(exceutedNode.contains(Node)){
		System.out.println("It was excuted");
		}else{
		  if(checkSource(Node)){
			deploy(Node);
			}
		 }
	}
// deploy the root partitions
private void deploy(int node){
		
		ArrayList<ArrayList<String>>inputs=getInputs(node);
		ArrayList<Object> partition=thegraph.get(node);
		Deployment excu=new Deployment(partition,cloudset,node,results,inputs,blockset,connections);
		Thread t= new Thread(excu);
		t.setName(String.valueOf(node));
		t.start();
		addNewPartition(excu,t);
		
	}

	private boolean checkSource(int parentsNode){
		
		ArrayList<ArrayList<String>>getinputs=getInputs(parentsNode);
		Set<String>dataSet=results.keySet();
		for(int i=0;i<getinputs.size();i++){
			ArrayList<String> input=getinputs.get(i);
			String inputNode=input.get(1);
			if(!dataSet.contains(inputNode)){
				return false;
			}
		}
		return true;
	}

	public void Stop(){
		STOP=true;
	}
	
	public synchronized void addNewPartition(Deployment excu,Thread t){
		
		 runningPartitions.put(excu, t);
	}
	
	public synchronized void removePartition(Deployment excu){
		
		runningPartitions.remove(excu);
	}
}
