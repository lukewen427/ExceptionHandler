package uk.ac.ncl.cs.esc.partitiontool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uk.ac.ncl.cs.esc.deployment.createDeployGraph;
import uk.ac.ncl.cs.esc.deployment.createPartitionGraph;
import uk.ac.ncl.cs.esc.deployment.deployOption;
import uk.ac.ncl.cs.esc.deployment.getDeploymentInfo;
import uk.ac.ncl.cs.esc.exceptionHandler.exceptionHandler;
import uk.ac.ncl.cs.esc.exceptionHandler.router;
import uk.ac.ncl.cs.esc.exceptionHandler.routing;
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
				
			// used to find the typical block
				BlockSet blockset=new BlockSet(theBlockSet);
				// used to find the typical cloud
				CloudSet cloudset=new CloudSet(cloudSet);
				// used to find the typical data
				DataBlockSet datablockset=new DataBlockSet(DataBlocks);
		     HashMap<String,ArrayList<Object>> partitionMap=partition.workflowPartition(theOptionSet,connections,blockset,datablockset);	
		     HashMap<String,ArrayList<Object>> validOptions=partition.tranferSecurityCheck(partitionMap, blockset);
		     HashMap<String, ArrayList<Object>> Maps=partition.Maps(validOptions,connections,  blockset, datablockset);
		     // ArrayList<Object> includes the links of partitions and the partition instances 
		     HashMap<String, ArrayList<Object>> validMap=partition.cycleBreak(Maps);
		     
		     // get initial partitions of each option
		     HashMap<String,ArrayList<Object>> startNodes=partition.getStartNodes(validMap);
		     // get end partitions of each option
		     HashMap<String,ArrayList<Object>> endNodes=partition.getEndNodes(validMap);
		    
		 //     get the cost of each partition and each link of the partitions 
	        costCalculation cost=new costCalculation( validMap, blockset,cloudset,datablockset);
	      
	  //    get the cheapest option 
		   
	 	    HashMap<String,ArrayList<Object>> costSet=cost.getCost();
	
		    // create a partition graph to deal the exceptions 
		    createPartitionGraph graph=new createPartitionGraph( costSet,validMap,endNodes,startNodes);
	        HashMap<Integer,ArrayList<Object>> thegraph=graph.getGraph();
	        ArrayList<Object> links=graph.getLinks();
	        HashMap<String,ArrayList<Object>> optionLinks=graph.getoptionLinks();
	       
	        // root Partitions represent in graph
	        HashMap<String,ArrayList<Integer>> rootNodes=graph.getrootPartition();
	       
	        // terminal Partitions represent in graph
	        HashMap<String,ArrayList<Integer>> terminalNodes=graph.getterminialPartition();
	      
	        // the cost of each partitions in the graph
	        HashMap<Integer,Integer> partitioncost=graph.getPartitioncost();
	    
	        // the cost of each link between partitions
	        ArrayList<Object> linkcost=graph.getLinkCost();
	        createDeployGraph thedeploygraph=new createDeployGraph(partitioncost,linkcost,optionLinks,terminalNodes,rootNodes);
	        HashMap<String,ArrayList<Integer>> deploygraph=thedeploygraph.getdeployGraph();
	        ArrayList<Object> deployLinks=thedeploygraph.getDeployLinks();
	        HashMap<String,Integer>deploygraphcost=thedeploygraph.getDeployCostGraph();
	        ArrayList<Object>deploylinkcost=thedeploygraph.getDeployLinkCost();
	        ArrayList<String> terminals=thedeploygraph.getTerminialNodes();
	        ArrayList<String> initials=thedeploygraph.getRootNodes();
	     //   System.out.println(deploylinkcost);
	    //    System.out.println(terminals);
	      
	        router getRouter=  new router( deploygraph, deploylinkcost,deploygraphcost,null,thegraph,null,initials,terminals,cloudset);
	        ArrayList<ArrayList<String>> getPath=getRouter.getPath();
	 
			Thread deploy=new Thread(new deployOption(thegraph, getPath, cloudset, deploylinkcost,
					deploygraph, blockset, connections,deploygraphcost,initials, terminals));
			deploy.setName("deloyment");
			deploy.start();
	
			}else{
					throw new Exception("This is invalid workflow");
				 }
	
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	private Set<DataBlock> getDataBlocks(){
		DataBlock dataBlock=null;
		Set<DataBlock>theDataBlockSet=new HashSet<DataBlock>();
		for(int a=0;a<connections.size();a++){
		ArrayList<String>connection=connections.get(a);
		 String name="D"+(a+1);
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
		cloud1=new Cloud("Cloud1","1","10.8.149.12",10,5,5,10);
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
