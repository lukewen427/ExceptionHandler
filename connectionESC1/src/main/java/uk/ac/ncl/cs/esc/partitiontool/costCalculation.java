package uk.ac.ncl.cs.esc.partitiontool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class costCalculation {
	HashMap<String, ArrayList<Object>> Map;
	BlockSet blockset;
	CloudSet cloudset;
	DataBlockSet dataBlockSet;
	// costSet includes the cost of each partition and connection of each option
	HashMap<String,ArrayList<Object>> costSet=new HashMap<String,ArrayList<Object>> ();
	
 public costCalculation( HashMap<String, ArrayList<Object>> Map,BlockSet blockset,CloudSet cloudset,DataBlockSet dataBlockSet){
	this.Map=Map;
	this.blockset=blockset;
	this.cloudset=cloudset;
	this.dataBlockSet=dataBlockSet;
 }
 
 
 public HashMap<String,ArrayList<Object>> getCost(){
	 return costSet;
 }
 /*create the order of each option*/
 public String getOrder(){
	 Iterator<String> optionSet=Map.keySet().iterator();
	 int min=0;
	 String selected = null;
	 while(optionSet.hasNext()){
		 String optionName=optionSet.next();
		 ArrayList<Object> option=Map.get(optionName);
		 int theCost=optionCost(option,optionName);
		 if(min==0){
			 min=theCost;
		 }
		 if(theCost<min){
			 min=theCost;
			 selected=optionName;
		 }
	 }
	 return selected;
 }
 
 // the cost of each option
 
 private int optionCost(ArrayList<Object> option,String Name){
	 // include the cost of the partitions and transition
	 ArrayList<Object> thecosts=new ArrayList<Object>();
	 HashMap<Integer,Integer> partitionCost=new  HashMap<Integer,Integer>();
	 HashMap<ArrayList<Integer>,Integer> linkCost=new  HashMap<ArrayList<Integer>,Integer> ();
	 
	 HashMap<Integer,ArrayList<Object>> partitions=(HashMap<Integer, ArrayList<Object>>) option.get(0);
	 ArrayList<Object> connection=(ArrayList<Object>) option.get(1);
	 Iterator<Integer> keySet=partitions.keySet().iterator();
	 int total=0;
	 while(keySet.hasNext()){
		 int partitionName=keySet.next();
		 ArrayList<Object> singalPartition=partitions.get(partitionName);
		 int cost=singalPartitioncost(singalPartition);
		 //System.out.println(cost);
		 partitionCost.put(partitionName, cost);
		 total=cost+total;
	 }
	 thecosts.add(partitionCost);
	 
	 for(int a=0;a<connection.size();a++){
		 ArrayList<Object> link=(ArrayList<Object>) connection.get(a);
		 ArrayList<Integer> partitionLink=(ArrayList<Integer>) link.get(0);
		 ArrayList<String> blocklink=(ArrayList<String>) link.get(1);
		 int tranferCost=transferCost(partitionLink,blocklink,partitions);
		 linkCost.put(partitionLink, tranferCost);
		 total=tranferCost+total;
	 }
	 thecosts.add(linkCost);
	 
	 costSet.put(Name, thecosts);
	 return total;
 }
 
 private int transferCost(ArrayList<Integer> partitionLink,ArrayList<String> blocklink,HashMap<Integer,ArrayList<Object>> partitions){
	 int sourcePartitionName=partitionLink.get(0);
	 int destinationPartitionName=partitionLink.get(1);
	 ArrayList<Object>sourcePartition=partitions.get(sourcePartitionName);
	 ArrayList<Object>destinationPartition=partitions.get(destinationPartitionName);
	 String sourceCloudName="Cloud"+sourcePartition.get(0);
	 String destinationCloudName="Cloud"+destinationPartition.get(0);
	 Cloud sourceCloud=cloudset.getCloud(sourceCloudName);
	 Cloud destinationCloud=cloudset.getCloud(destinationCloudName);
	 int TransferOut=sourceCloud.getTransferout();
	 int TransferIn=destinationCloud.getTransferin();
	 String sourceId=blocklink.get(0);
	 String destinationId=blocklink.get(1);
	 DataBlock data=dataBlockSet.getDataBlock(sourceId, destinationId);
	 int size=data.getSize();
	 int cost=(TransferOut+TransferIn)*size;
	 return cost;
 }
 /* the cost of each Partition */
 private int singalPartitioncost(ArrayList<Object> partition){
	 
	 int storageCost=0;
	 int cpuCost=0;
	 for(int a=1;a<partition.size();a++){
		 int security=(Integer)partition.get(0);
		 String cloudName="Cloud"+security;
		 Cloud currentCloud=cloudset.getCloud(cloudName);
		 int theStorage=currentCloud.getStoragecost();
		 int thecpu=currentCloud.getCPUcost();
		 Object block=partition.get(a);
		 if(block instanceof DataBlock){
			 int datasize=((DataBlock) block).getSize();
				int longevity=((DataBlock) block).getlongevity();
				storageCost=storageCost+datasize*longevity*theStorage;
			
		 }else{
			    Block isBlock=(Block)block;
				int cpu=isBlock.cpu();
				cpuCost=cpuCost+(cpu*thecpu);
		 }
	 }
	 
	 
	 return storageCost+cpuCost;
 }
}
