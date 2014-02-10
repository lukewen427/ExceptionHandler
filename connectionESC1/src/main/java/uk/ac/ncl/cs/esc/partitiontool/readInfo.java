package uk.ac.ncl.cs.esc.partitiontool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uk.ac.ncl.cs.esc.deployment.getDeploymentInfo;
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
			Set<DataBlock> DataBlocks=getDataBlocks();
			partitionTool partition=new partitionToolimp();
			if(partition.workflowChecking(theBlockSet,connections)){
				ArrayList<Object>theOptionSet=partition.allOptions(theBlockSet,DataBlocks,cloudSet);
				BlockSet blockset=new BlockSet(theBlockSet);
				CloudSet cloudset=new CloudSet(cloudSet);
				DataBlockSet datablockset=new DataBlockSet(DataBlocks);
			 HashMap<String,ArrayList<Object>> partitionMap=partition.workflowPartition(theOptionSet,connections,blockset);	
			 String []  order=partition.findBestOption(partitionMap,connections,blockset,cloudset);
			 ArrayList<Object> findBestOption=partitionMap.get(order[0]);
		     new getDeploymentInfo(findBestOption,connections,blockset,cloudset,datablockset,workflowId, partitionMap, order);
			}else{
					throw new Exception("This is invalid workflow");
				 }
	//		ArrayList<Object> options=partition.allOptions(theBlockSet,cloudSet);
		//	WorkflowRestructure getWorkflowInfo=new WorkflowRes();
		
		//	HashMap<String,ArrayList<String>> connection=getWorkflowInfo.ConnectionMap(workflowId);
	//		if(partition.workflowChecking(theBlockSet,connection)){
	//		  HashMap<String,ArrayList<Object>> partitionMap=partition.workflowPartition(theBlockSet,connection,options);
	//		  ArrayList<Object> findBestOption=partition.findBestOption(partitionMap,connection,theBlockSet,cloudSet);
	//		  new getDeploymentInfo(findBestOption,connection,theBlockSet,cloudSet,workflowId);
			  
	//		}else{
	//			throw new Exception("This is invalid workflow");
		//	}
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
		cloud0=new Cloud("Cloud0","0","esc1",5,5,5,5);
		cloud1=new Cloud("Cloud1","1","esc2",10,5,5,10);
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
			String serviceId=parser.getBlockServiceId(blockid, workflowId);
			ArrayList element=blocks.get(blockid);	
	//		System.out.println(element);
			int location=Integer.valueOf((String) element.get(0));
	//		int location=(Integer)element.get(0);
			int clearance=Integer.valueOf((String) element.get(1));
	//		int clearance=(Integer) element.get(1);
			String type=(String) element.get(2);
			int cpu=Integer.valueOf((String) element.get(3));
			theblock=new Block(blockid,location,clearance,type,serviceId,cpu);
			theBlockSet.add(theblock);	
		}
		return theBlockSet;
	}
}
