package uk.ac.ncl.cs.esc.partitiontool;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uk.ac.ncl.cs.esc.deployment.createDeployGraph;
import uk.ac.ncl.cs.esc.deployment.createPartitionGraph;
import uk.ac.ncl.cs.esc.deployment.deployOption;
import uk.ac.ncl.cs.esc.exceptionHandler.router;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRes;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRestructure;

public class readInfo {
	HashMap<String,ArrayList<String>>blocks=new HashMap<String,ArrayList<String>>();
	String workflowId;
	ArrayList<ArrayList<String>> connections=new ArrayList<ArrayList<String>>();
	
	public readInfo(HashMap<String,ArrayList<String>>blocks,String workflowId,ArrayList<ArrayList<String>> connections){
		this.blocks=blocks;
		this.workflowId=workflowId;
		this.connections=connections;
		try {
			Set<Block>theBlockSet=getBlocks();
			Set<Cloud> cloudSet=getClouds();
			//DataBlocks are the dependencies of two service blocks
			Set<DataBlock> DataBlocks=getDataBlocks();
			partitionTool partition=new partitionToolimp();
			if(partition.workflowChecking(theBlockSet,connections)){
				// theOptionSet stores all the possible options of the blocks deployed on the clouds
				
				
				ArrayList<Object>theOptionSet=partition.allOptions(theBlockSet,DataBlocks,cloudSet);
		//		optionset(theOptionSet);
			//	System.out.println(theOptionSet);
			// used to find the typical block
				BlockSet blockset=new BlockSet(theBlockSet);
				// used to find the typical cloud
				CloudSet cloudset=new CloudSet(cloudSet);
				// used to find the typical data
				DataBlockSet datablockset=new DataBlockSet(DataBlocks);
		      HashMap<String,ArrayList<Object>> partitionMap=partition.workflowPartition(theOptionSet,connections,blockset,datablockset);
		//     printPartions( partitionMap);
		  //   System.out.println(partitionMap);
		      HashMap<String,ArrayList<Object>> validOptions=partition.tranferSecurityCheck(partitionMap, blockset);
		  
	         HashMap<String, ArrayList<Object>> Maps=partition.Maps(validOptions,connections,  blockset, datablockset);
	     //   System.out.println(Maps);
		     // ArrayList<Object> includes the links of partitions and the partition instances 
		     HashMap<String, ArrayList<Object>> validMap=partition.cycleBreak(Maps);
		     
	//	     pringValidMap(validMap);
		     
		     // get initial partitions of each option
		     HashMap<String,ArrayList<Object>> startNodes=partition.getStartNodes(validMap);
		 //    System.out.println(validMap);
		  //   System.out.println(startNodes);
		     
		     
		     HashMap<String,ArrayList<Object>> finalMap=partition.additionalOption(validMap,startNodes);
		     // get end partitions of each option
		  //   System.out.println(startNodes);
	/*	     HashMap<String,ArrayList<Object>> endNodes=partition.getEndNodes(validMap);
		//     System.out.println(endNodes);
		 //     get the cost of each partition and each link of the partitions 
	        costCalculation cost=new costCalculation(validMap, blockset,cloudset,datablockset);
	        
	        
	  //     System.out.println(validMap);
	  //    get the cheapest option 
		   
	 	    HashMap<String,ArrayList<Object>> costSet=cost.getCost();
	  //  	   System.out.println(costSet);
		    // create a partition graph to deal the exceptions 
		    createPartitionGraph graph=new createPartitionGraph(costSet,validMap,endNodes,startNodes);
	        HashMap<Integer,ArrayList<Object>> thegraph=graph.getGraph();
	      
	        ArrayList<Object> links=graph.getLinks();
	  //       System.out.println(links);
	   //     System.out.println(thegraph);
	//        System.out.println("start print paritions ");
	//       printMap(thegraph);
	        
	        HashMap<String,ArrayList<Object>> optionLinks=graph.getoptionLinks();
	   //     System.out.println(optionLinks);
	        // root Partitions represent in graph
	        HashMap<String,ArrayList<Integer>> rootNodes=graph.getrootPartition();
	   //      System.out.println(rootNodes);
	        // terminal Partitions represent in graph
	        HashMap<String,ArrayList<Integer>> terminalNodes=graph.getterminialPartition();
	 //        System.out.println(terminalNodes);
	        // the cost of each partitions in the graph
	       HashMap<Integer,Integer> partitioncost=graph.getPartitioncost();
	//       System.out.println(partitioncost);
	        // the cost of each link between partitions
	        ArrayList<Object> linkcost=graph.getLinkCost();
	 //       System.out.println(linkcost);
	 //       System.out.println(partitioncost);
	        createDeployGraph thedeploygraph=new createDeployGraph(partitioncost,linkcost,optionLinks,terminalNodes,rootNodes);
	        HashMap<String,ArrayList<Integer>> deploygraph=thedeploygraph.getdeployGraph();
	       
	       ArrayList<Object> deployLinks=thedeploygraph.getDeployLinks();
	       
	        HashMap<String,Integer>deploygraphcost=thedeploygraph.getDeployCostGraph();
	        ArrayList<Object>deploylinkcost=thedeploygraph.getDeployLinkCost();
	        ArrayList<String> terminals=thedeploygraph.getTerminialNodes();
	        ArrayList<String> initials=thedeploygraph.getRootNodes();
	//        System.out.println(deployLinks);
	//        System.out.println(deploylinkcost);
	       
	//       System.out.println(deploygraph);
	//        System.out.println(initials);
	  //      System.out.println(terminals);
	      
	        router getRouter=  new router( deploygraph, deploylinkcost,deploygraphcost,null,thegraph,null,initials,terminals,cloudset);
	        ArrayList<ArrayList<String>> getPath=getRouter.getPath();
	     
	       System.out.println(links);
	        
			Thread deploy=new Thread(new deployOption(thegraph, getPath, cloudset, deploylinkcost,
					deploygraph, blockset, connections,deploygraphcost,initials, terminals,links));
			deploy.setName("deloyment");
			deploy.start();*/
	
			}else{
					throw new Exception("This is invalid workflow");
				 }
	
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	private void optionset(ArrayList<Object>theOptionSet){
		for(int z=0;z<theOptionSet.size();z++){
			System.out.println("Option "+z);
			HashMap<Object,Integer> partitions=(HashMap<Object, Integer>) theOptionSet.get(z);
			Iterator<Object> key=partitions.keySet().iterator();
			while(key.hasNext()){
				Object block=key.next();
				if(block instanceof Block){
					String name=((Block) block).getBlockName();
					System.out.println(name);
				}else{
					String dataName=((DataBlock)block).getDataBlockName();
					System.out.println(dataName);
				}
			}
		}
	}
	
	private void printPartions( HashMap<String,ArrayList<Object>> partitionMap){
		Iterator <String>keys=partitionMap.keySet().iterator();
		while(keys.hasNext()){
			String optionName=keys.next();
			System.out.println(optionName);
			ArrayList<Object> partitions=partitionMap.get(optionName);
			for(int a=0;a<partitions.size();a++){
				ArrayList<Object>partition=(ArrayList<Object>) partitions.get(a);
				int cloud=(Integer) partition.get(0);
				System.out.println(cloud);
			for(int i=1;i<partition.size();i++){
					
					Object block=partition.get(i);
					if(block instanceof Block){
						String name=((Block) block).getBlockName();
						System.out.println(name);
					}else{
						String dataName=((DataBlock)block).getDataBlockName();
						System.out.println(dataName);
					}
				}
			}
		}
	}
	
	private void pringValidMap( HashMap<String, ArrayList<Object>> validMap){
		Iterator <String>keys=validMap.keySet().iterator();
		while(keys.hasNext()){
			String optionName=keys.next();
			System.out.println(optionName);
			ArrayList<Object> option=validMap.get(optionName);
			HashMap<Integer,ArrayList<Object>> partitions=(HashMap<Integer, ArrayList<Object>>) option.get(0);
			Iterator<Integer> newKeys=partitions.keySet().iterator();
			while(newKeys.hasNext()){
				int getPartition=newKeys.next();
				ArrayList<Object> partition=partitions.get(getPartition);
				int cloud=(Integer) partition.get(0);
				System.out.println(cloud);
			for(int i=1;i<partition.size();i++){
					
					Object block=partition.get(i);
					if(block instanceof Block){
						String name=((Block) block).getBlockName();
						System.out.println(name);
					}else{
						String dataName=((DataBlock)block).getDataBlockName();
						System.out.println(dataName);
					}
				}
			}
			
	
		}
	}
	private void printMap( HashMap<Integer,ArrayList<Object>> thegraph){
		Iterator <Integer>keys=thegraph.keySet().iterator();
		while(keys.hasNext()){
			int key=keys.next();
			System.out.println("paritionName "+key);
			ArrayList<Object> partition=thegraph.get(key);
		//	ArrayList<Object> partitions=(ArrayList<Object>) oneOption.get(0);
		
				int cloud=(Integer) partition.get(0);
				System.out.println(cloud);
			for(int i=1;i<partition.size();i++){
					
					Object block=partition.get(i);
					if(block instanceof Block){
						String name=((Block) block).getBlockName();
						System.out.println(name);
					}else{
						String dataName=((DataBlock)block).getDataBlockName();
						System.out.println(dataName);
					}
				}
				
			}
		
	}
	private Set<DataBlock> getDataBlocks(){
		DataBlock dataBlock=null;
		Set<DataBlock>theDataBlockSet=new HashSet<DataBlock>();
		for(int a=0;a<connections.size();a++){
		ArrayList<String>connection=connections.get(a);
		 String name=connection.get(2)+"-"+connection.get(3);
		 String sourceblockId=connection.get(0);
		 String destinationblockId=connection.get(1);
		 int location=Integer.valueOf(connection.get(6));
		 int size=Integer.valueOf(connection.get(8));
		 int longevity=Integer.valueOf(connection.get(9));
		 dataBlock=new DataBlock(name,location,sourceblockId,destinationblockId,size,longevity);
		 theDataBlockSet.add(dataBlock);
		}
		return theDataBlockSet;
	}
	private Set<Cloud> getClouds() {
		Set<Cloud> cloudSet=new HashSet<Cloud>();
		Cloud cloud0;
		Cloud cloud1;
		cloud0=new Cloud("Cloud0","0","10.66.66.176",5,5,5,5);
		cloud1=new Cloud("Cloud1","1","10.8.149.11",10,5,5,10);
		cloudSet.add(cloud0);
		cloudSet.add(cloud1);
		return cloudSet;
	}
	public Set<Block> getBlocks() throws Exception{
		Block theblock;
		WorkflowRestructure parser=new WorkflowRes();
		Set<String> BlockIds=blocks.keySet();
		Iterator <String> ids=BlockIds.iterator();
		Set<Block>theBlockSet=new HashSet<Block>();
		while(ids.hasNext()){
			String blockid=ids.next();
			String blockName=parser.getBlockName(blockid, workflowId);
			String serviceId=parser.getBlockServiceId(blockid, workflowId);
			ArrayList element=blocks.get(blockid);	
	//		System.out.println(element);
			int location=Integer.valueOf((String) element.get(0));
	//		int location=(Integer)element.get(0);
			int clearance=Integer.valueOf((String) element.get(1));
	//		int clearance=(Integer) element.get(1);
			String type=(String) element.get(2);
			int cpu=Integer.valueOf((String) element.get(3));
			theblock=new Block(blockid,location,clearance,type,serviceId,cpu,blockName);
			theBlockSet.add(theblock);	
		}
		return theBlockSet;
	}
}
