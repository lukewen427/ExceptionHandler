package uk.ac.ncl.cs.esc.deployment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import uk.ac.ncl.cs.esc.partitiontool.Block;
import uk.ac.ncl.cs.esc.partitiontool.BlockSet;
import uk.ac.ncl.cs.esc.partitiontool.CloudSet;
import uk.ac.ncl.cs.esc.partitiontool.DataBlock;
import uk.ac.ncl.cs.esc.partitiontool.DataBlockSet;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRes;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRestructure;

public class deployOrder {
	
	ArrayList<Object> selectedOpiton=new ArrayList<Object>();
	ArrayList<ArrayList<String>> connections=new ArrayList<ArrayList<String>>();
	DpartitionSet Dpartitionset=new DpartitionSet();
	HashMap<String,ArrayList<Object>> partitionMap=new HashMap<String,ArrayList<Object>>();
	BlockSet blockset;
	CloudSet  cloudSet;
	DataBlockSet datablockset;
	String workflowId;
	public deployOrder(ArrayList<Object> selectedOpiton,ArrayList<ArrayList<String>> connections,
	BlockSet blockset,CloudSet cloudset,DataBlockSet datablockset, String workflowId, HashMap<String,ArrayList<Object>> partitionMap){
		this.selectedOpiton=selectedOpiton;
		this.connections=connections;
		this.blockset=blockset;
		this.cloudSet=cloudset;
		this.datablockset=datablockset;
		this.workflowId=workflowId;	 
		this.partitionMap=partitionMap;
	}
	
	/*the links between partitions*/
   public  ArrayList<Object> getLinkPartition(){
	  HashMap<Object,Object>LinkedBlock=new  HashMap<Object,Object>();
	  ArrayList<Object> theLinked=new ArrayList<Object>();
	  ArrayList<Object> theLink=new ArrayList<Object>();
	  for(int a=0;a<selectedOpiton.size();a++){
		  ArrayList<Object> singalPartition=(ArrayList<Object>) selectedOpiton.get(a);
		
		  for(int i=0;i<singalPartition.size()-1;i++){
			  Object block=singalPartition.get(i);
			  if(block instanceof DataBlock){
				  String sourceid=((DataBlock) block).getsourceblockId();
				  String destinationid=((DataBlock) block).getdestinationblockId();
				  Block getsourceBlock= blockset.getBlock(sourceid);
				  Block getdestinationBlock=blockset.getBlock(destinationid);
				  if(singalPartition.contains(getsourceBlock)&&!singalPartition.contains(getdestinationBlock)){
					  
					  LinkedBlock.put(getsourceBlock,getdestinationBlock);
				  }
				  if(!singalPartition.contains(getsourceBlock)&&singalPartition.contains(getdestinationBlock)){
					  
						LinkedBlock.put(getsourceBlock,getdestinationBlock);
					}
				  if(!singalPartition.contains(getsourceBlock)&&!singalPartition.contains(getdestinationBlock)){
						theLinked.add(block);
						LinkedBlock.put(getsourceBlock,getdestinationBlock);
					}
			  }
		  }
	  }
	   theLink.add(LinkedBlock);
	   theLink.add(theLinked);
         return theLink;
	}
   
   public DpartitionSet getDpartitionSet(){
	   return Dpartitionset;
   }
   public  HashMap<Integer,ArrayList<Object>> deployOrder(){
	   
	   ArrayList<String> startNodeSet=new ArrayList<String>();
	   ArrayList<String> endNodeSet=new ArrayList<String>();
	   for(int a=0;a<connections.size();a++){
		   boolean isStartNode=true;
		   boolean isEndNode=true;
		   ArrayList<String> singalLink=connections.get(a);
		   String sourceId=singalLink.get(0);
		   String destinationId=singalLink.get(1);
		   for(int i=0;i<connections.size();i++){
			   ArrayList<String> compare=connections.get(i);
			   String comparedestinationId=compare.get(1);
			   String comparesourceId=compare.get(0);
			   if(i!=a){
				   if(sourceId.equals(comparedestinationId)){
					   isStartNode=false;
					   break;
				   }
				   if(destinationId.equals(comparesourceId)){
					   isEndNode=false;
				   }
			   }
		   }
		   if(isStartNode==true){
			   startNodeSet.add(sourceId);
		   }
		   if(isEndNode==true){
			   endNodeSet.add(destinationId);
		   }
	   }
	   
	   ArrayList<Object>StartPartitions=new ArrayList<Object>();
	   for(int i=0;i<startNodeSet.size();i++){
		   String nodeName=startNodeSet.get(i);
		   Block startBlock=blockset.getBlock(nodeName);
		   ArrayList<Object> startPartition=getPartition(startBlock);
		   if(startPartition!=null){
			   singalPartition InstancePartition=PartitionInstance( startPartition);
			   Dpartitionset.addPartition(InstancePartition);
			   StartPartitions.add(startPartition);
		   }
	   }
	   HashMap<Integer,ArrayList<Object>> initial=new HashMap<Integer,ArrayList<Object>>();
	   initial.put(1, StartPartitions);
	   ArrayList<Object> Links=getLinkPartition();
	   HashMap<Integer,ArrayList<Object>> order=createOrder(initial,StartPartitions,Links,1,Dpartitionset);
	   return order;
   }
   public singalPartition PartitionInstance( ArrayList<Object> startPartition){
	   singalPartition InstancePartition=new singalPartition();
	   boolean noBlock=true;
	   for(int a=0;a<startPartition.size();a++){
		   Object theblock=startPartition.get(a);
		   if(theblock instanceof Block){
			   noBlock=false;
			   break;
		   }
	   }
	   
	   if(noBlock==true){
		   InstancePartition.setName("Data");
	   }else{
		   InstancePartition.setElements(startPartition);
		   ArrayList<ArrayList<String>>Outputs=getOutputs(startPartition);
		   if(!Outputs.isEmpty()){
			   InstancePartition.setOutputs(Outputs);
		   }
		   
		   ArrayList<ArrayList<String>>Inputs=getInputs(startPartition);
		   if(!Inputs.isEmpty()){
			   InstancePartition.setInputs(Inputs);
		   }
	   }
	
	   return InstancePartition;
   }
   
   public ArrayList<ArrayList<String>> getInputs(ArrayList<Object> partition){
	   ArrayList<ArrayList<String>> Inputs=new ArrayList<ArrayList<String>>();
	   
	   for(int a=0;a<partition.size();a++){
		   Object block=partition.get(a);
		   if(block instanceof Block){
			   String blockid=((Block) block).getBlockId();
			 
			   for(int i=0;i<connections.size();i++){
				   ArrayList<String> connection=connections.get(i);
				   if(blockid.equals(connection.get(1))){
					   String sourceId=connection.get(0);
					   Block soureceBlock=blockset.getBlock(sourceId);
					   if(!partition.contains(soureceBlock)){
				//		   System.out.println(blockid);
						
						   Inputs.add(connection);
					   }
				   }
			   }
		   }
	   }
	   return Inputs;
   }
   public ArrayList<ArrayList<String>> getOutputs(ArrayList<Object> partition){
	   ArrayList<ArrayList<String>> Outputs=new ArrayList<ArrayList<String>>();
	   for(int a=0;a< partition.size();a++){
		   Object block=partition.get(a);
		   if(block instanceof Block){
			   String blockid=((Block) block).getBlockId();
			   for(int i=0;i<connections.size();i++){
				   ArrayList<String> connection=connections.get(i);
				   if(blockid.equals(connection.get(0))){
					   String destinationId=connection.get(1);
					   Block destinationIdBlock=blockset.getBlock(destinationId);
					   if(!partition.contains(destinationIdBlock)){
						   Outputs.add(connection);
					   }
				   }
			   }
		   }
	   }
	   return Outputs;
   }
   public HashMap<Integer,ArrayList<Object>> createOrder( HashMap<Integer,ArrayList<Object>> order,
		   								ArrayList<Object> prePartition, ArrayList<Object> Links,int h, DpartitionSet Dpartitionset){
	  // singalPartition InstancePartition=new singalPartition();
	   ArrayList<Object> newPartition=new ArrayList<Object>();
	   HashMap<Object,Object>LinkedBlock=new  HashMap<Object,Object>();
	   ArrayList<Object>theLinked=new ArrayList<Object>();
	   LinkedBlock=(HashMap<Object,Object>) Links.get(0);
	   theLinked=(ArrayList<Object>) Links.get(1);
	   Set<Object> sourceBlcok=LinkedBlock.keySet();
	   ArrayList<Object>singaldataBlock=new ArrayList<Object>();
	   for(int a=0;a<prePartition.size();a++){
		   ArrayList<Object> partition=(ArrayList<Object>) prePartition.get(a);
		   for(int i=0;i<partition.size()-1;i++){
			   Object block=partition.get(i);
			   if(sourceBlcok.contains(block)){
				   Block destinationBlock=(Block) LinkedBlock.get(block);
				   String sourceBlockId=((Block)block).getBlockId();
				   String desitnationBlockId=((Block)destinationBlock).getBlockId();
				   DataBlock getData=datablockset.getDataBlock(sourceBlockId, desitnationBlockId);
				   if(theLinked.contains(getData)){
					   ArrayList<Object>temp=new ArrayList<Object>();
					   temp=getPartition(getData);
					   singaldataBlock=(ArrayList<Object>) temp.clone();
					   singalPartition InstancePartition=PartitionInstance(singaldataBlock);
					   Dpartitionset.addPartition(InstancePartition);
					   newPartition.add(singaldataBlock);
				   }else{
					   Block getdestinationBlock=(Block) LinkedBlock.get(block);
					   ArrayList<Object>temp=new ArrayList<Object>();
					   temp=getPartition(getdestinationBlock);
						   ArrayList<Object> getNewPartition=(ArrayList<Object>) temp.clone();
						   singalPartition InstancePartition=PartitionInstance(getNewPartition);
						   Dpartitionset.addPartition(InstancePartition);
						   newPartition.add(getNewPartition);
				   }
			   }
			   if(theLinked.contains(block)){
				   String destinationId=((DataBlock) block).getdestinationblockId();
				   ArrayList<Object>temp=new ArrayList<Object>();
				   Block newBlock=blockset.getBlock(destinationId);
				   temp =getPartition(newBlock);
				   ArrayList<Object> getNewPartition=(ArrayList<Object>) temp.clone();
				   singalPartition InstancePartition=PartitionInstance(getNewPartition);
				   Dpartitionset.addPartition(InstancePartition);
				   newPartition.add(getNewPartition);
			   }
		   }
	   }
	   if(newPartition.isEmpty()){
		   return order;
	   }else{
		   
		   h++;
		  order.put(h, newPartition);
	   }
	   
	   return createOrder(order,newPartition,Links,h,Dpartitionset);
   }
   
   public ArrayList<Object> getPartition(Object block){
	   for(int a=0;a<selectedOpiton.size();a++){
		   ArrayList<Object> partition=(ArrayList<Object>) selectedOpiton.get(a);
		   if(partition.contains(block)){
			   return partition;
			   
		   }
	   }
	   return null;
	   
   }
	   	
}
