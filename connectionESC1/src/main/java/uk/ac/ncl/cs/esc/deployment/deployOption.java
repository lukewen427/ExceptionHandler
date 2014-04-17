package uk.ac.ncl.cs.esc.deployment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import uk.ac.ncl.cs.esc.exceptionHandler.exceptionHandler;
import uk.ac.ncl.cs.esc.exceptionHandler.dataCenter.dataStorage;
import uk.ac.ncl.cs.esc.partitiontool.Block;
import uk.ac.ncl.cs.esc.partitiontool.BlockSet;
import uk.ac.ncl.cs.esc.partitiontool.CloudSet;



public class deployOption implements Runnable {
	
	HashMap<Integer,ArrayList<Object>> thegraph;
	ArrayList<Object>links;
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
			 ArrayList<String> initials,ArrayList<String> terminals,ArrayList<Object> links){
		this.thegraph =thegraph;
		this.cloudset=cloudset;
		this.connections=connections;
		this.deploygraph=deploygraph;
		this.deploygraphcost=deploygraphcost;
		this.deploylinkcost=deploylinkcost;
		this.initials=initials;
		this.terminals=terminals;
		this.NodePath=NodePath;
		this.blockset=blockset;
		this.links=links;
	}

	public void run() {
	//	System.out.println("the deploy path"+NodePath);
		for(int i=0;i<=NodePath.size();i++){
			ArrayList<String> path=null;
			if(i==NodePath.size()){
				path=NodePath.get(i-1);
			}else{
				path=NodePath.get(i);
			}
			
			String running=null;
		
			// deploy the partition
			if(i==NodePath.size()){
				running=path.get(1);
				Deployment( running,false);
			}
			if(i==0){
				running=path.get(0);
				Deployment(running,true);
			}
			if(i<NodePath.size()&&i>0){
				running=path.get(0);
				Deployment(running,false);
			}
			System.out.println("running party "+running);
			while(!runningPartitions.isEmpty()){
				Iterator<Deployment> keys=runningPartitions.keySet().iterator();
				while(keys.hasNext()){
					Deployment excu=keys.next();
					Thread t=runningPartitions.get(excu);
					String pName=t.getName();
					int node=Integer.valueOf(pName);
			
					while(excu.checkStautes().equals("running")||excu.checkStautes().equals("checking")){
						 try {
				    		 Thread.sleep(1000);
				    	 } catch (Exception e){}
					}
					if(excu.checkStautes().equals("fail")){
						removePartition(excu);
						String exceptionNode=running;
						new exceptionHandler( deploygraph,deploylinkcost,
								deploygraphcost, NodePath, thegraph,
								 exceptionNode,initials, terminals, cloudset,blockset,connections,links);
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
					if(!isContain(inputs,bLink)){
						inputs.add((ArrayList<String>)bLink.clone());
					}
				}
			}
		
		return inputs;
	}
	
	private boolean isContain(ArrayList<ArrayList<String>> inputs,ArrayList<String> bLink){
		
		if(inputs.isEmpty()){
			return false;
		}else{
			boolean isContain=false;
			for(int a=0;a<inputs.size();a++){
				ArrayList<String> input=inputs.get(a);
				if((input.get(0).equals(bLink.get(0)))&&(input.get(1).equals(bLink.get(1)))){
					
					return true;
				}
			}
			return isContain;
		}
		
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
		System.out.println(node);
		System.out.println(inputs);
		
		ArrayList<Object> partition=thegraph.get(node);
		ArrayList<String> heads=null;
		if(inputs.isEmpty()){
			 heads=headBlocks(partition);
		}else{
			heads=headBlocks(node);
		}
	//	System.out.println(partition);
		System.out.println(heads);
		Deployment excu=new Deployment(partition,cloudset,node,inputs,blockset,connections,heads);
		Thread t= new Thread(excu);
		t.setName(String.valueOf(node));
		t.start();
		addNewPartition(excu,t);
		
	}

private ArrayList<String> headBlocks(ArrayList<Object> partition){
	
	ArrayList<String> headBlocks=new ArrayList<String>();
	
	for(int a=1;a<partition.size();a++){
		Object block=partition.get(a);
		if(block instanceof Block){
			boolean isHead=true;
			String BlockId=((Block)block).getBlockId();
			for(int i=0;i<connections.size();i++){
				ArrayList<String> connect=connections.get(i);
				if(BlockId.equals(connect.get(1))){
					isHead=false;
				}
			}
			if(isHead){
				headBlocks.add(BlockId);
			}
		}
	}
	
	return headBlocks;
}

private ArrayList<String> headBlocks(int node){
	
	ArrayList<String> headBlocks=new ArrayList<String>();
	
	for(int i=0;i<links.size();i++){
		ArrayList<Object> link=(ArrayList<Object>) links.get(i);
		ArrayList<Integer> pLink=(ArrayList<Integer>) link.get(0);
		ArrayList<String> bLink=(ArrayList<String>) link.get(1);
		int destination=pLink.get(1);
		if(node==destination){
			String head=bLink.get(1);
			if(!headBlocks.contains(head)){
				headBlocks.add(head);
			}
			
		}
	}
	return headBlocks;
}

	private boolean checkSource(int parentsNode){
		
		ArrayList<ArrayList<String>>getinputs=getInputs(parentsNode);
		HashMap<String,ByteArrayOutputStream> results=dataStorage.getData();
		Set<String>dataSet=results.keySet();
		for(int i=0;i<getinputs.size();i++){
			ArrayList<String> input=getinputs.get(i);
			String inputNode=input.get(0)+","+input.get(1);
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
