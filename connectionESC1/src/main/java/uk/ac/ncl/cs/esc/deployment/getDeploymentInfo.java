package uk.ac.ncl.cs.esc.deployment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.connexience.server.api.IDocument;
import com.connexience.server.api.IXmlMetaData;

import uk.ac.ncl.cs.esc.exceptionHandler.master;
import uk.ac.ncl.cs.esc.messaging.Mytool;
import uk.ac.ncl.cs.esc.messaging.Producer;
import uk.ac.ncl.cs.esc.partitiontool.Block;
import uk.ac.ncl.cs.esc.partitiontool.BlockSet;
import uk.ac.ncl.cs.esc.partitiontool.Cloud;
import uk.ac.ncl.cs.esc.partitiontool.CloudSet;
import uk.ac.ncl.cs.esc.partitiontool.DataBlock;
import uk.ac.ncl.cs.esc.partitiontool.DataBlockSet;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRes;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRestructure;


public class getDeploymentInfo {
	ArrayList<Object> thebestoption=new ArrayList<Object>();
	ArrayList<ArrayList<String>> connections=new ArrayList<ArrayList<String>>();
	HashMap<String,ArrayList<Object>> partitionMap=new HashMap<String,ArrayList<Object>>();
	BlockSet blockset;
	CloudSet  cloudSet;
	DataBlockSet datablockset;
	String workflowId;
	 String []  order;
	HashMap<String,List<IDocument>> documentSet=new HashMap<String,List<IDocument>>();
	ArrayList<ArrayList<String>> partitionLinks=new ArrayList<ArrayList<String>>();
//	ArrayList<ArrayList<String>> Links=new ArrayList<ArrayList<String>>();
	public getDeploymentInfo(ArrayList<Object>bestoption,ArrayList<ArrayList<String>> connections,
		BlockSet blockset,CloudSet cloudset,DataBlockSet datablockset, String workflowId, HashMap<String,ArrayList<Object>> partitionMap, String []  order) throws Exception{
			this.thebestoption=bestoption;
			this.connections=connections;
			this.blockset=blockset;
			this.cloudSet=cloudset;
			this.datablockset=datablockset;
			this.workflowId=workflowId;	 
			this.partitionMap=partitionMap;
			this.order=order;
			deployment();	
	}
	
	public void deployment() throws Exception{
		
		deployOrder createOrder=new deployOrder(thebestoption,connections,
			 blockset,cloudSet,datablockset, workflowId,  partitionMap);
		
		HashMap<Integer,ArrayList<Object>> getdeployOrder= createOrder.deployOrder();
		DpartitionSet Dpartitionset=createOrder.getDpartitionSet();
	
		int f=0;
		HashMap<String,ByteArrayOutputStream> theResultSet=new HashMap<String,ByteArrayOutputStream>();
		HashMap<String,ByteArrayOutputStream> ResultSet=new HashMap<String,ByteArrayOutputStream>();
		ArrayList<Object>recordResults=new ArrayList<Object>();
		for(int a=0;a<getdeployOrder.size();a++){
			ArrayList<Object> PartitionSet=getdeployOrder.get(a+1);
			ArrayList<Object> resultSet=deploy(PartitionSet,f,theResultSet,Dpartitionset);
			
			if(resultSet.get(0) instanceof Integer ){
				if((Integer)resultSet.get(0)==1){
					System.out.println("Exception type 1");
					rescheduleType1(recordResults,a+1,getdeployOrder);
					break;
				}else{
					System.out.println("Exception type 2");
					rescheduleType2(recordResults,a+1,getdeployOrder);
					break;
				}
				
				
			}
			if( (resultSet.get(0)) instanceof HashMap){
				 theResultSet.clear();
					for(int i=0;i<resultSet.size();i++){
						HashMap<String,ByteArrayOutputStream> temp=(HashMap<String, ByteArrayOutputStream>) resultSet.get(i);
						Set<String>keySet=temp.keySet();
						Iterator<String> getOutput=keySet.iterator();
						while(getOutput.hasNext()){
							String key=getOutput.next();
							theResultSet.put(key, temp.get(key));
						}
					}
					
					ResultSet=(HashMap<String, ByteArrayOutputStream>) theResultSet.clone();
					recordResults.add(ResultSet);
					f=f+PartitionSet.size();
			}
		   
		}
	}
	
	public void redeployment2(ArrayList<Object> option,String exceptionType,HashMap<String,ByteArrayOutputStream> theResultSet,
																ArrayList<Object> endBlocks, ArrayList<Object> ObjectSet,int f)throws Exception{
		deployOrder createOrder=new deployOrder(option,connections,
				 blockset,cloudSet,datablockset, workflowId,  partitionMap);
		HashMap<Integer,ArrayList<Object>> getdeployOrder= createOrder.deployOrder();
		DpartitionSet Dpartitionset=createOrder.getDpartitionSet();
		int count=0;
		for(int a=1;a<getdeployOrder.size();a++){
			ArrayList<Object> PartitionSet=getdeployOrder.get(a);
			ArrayList<Block> theSet=(ArrayList<Block>) endBlocks.get(0);
			for(int b=0;b<theSet.size();b++){
				Block block=theSet.get(b);
				for(int x=0;x<PartitionSet.size();x++){
					ArrayList<Object>partition=(ArrayList<Object>) PartitionSet.get(x);
					if(partition.contains(block));
					count=a;
					break;
				 }
				}
		}
		int thesize=getdeployOrder.size();
		
		
		ArrayList<Object>modify=(ArrayList<Object>) getdeployOrder.get(count);
		 for(int z=0;z<modify.size();z++){
			 ArrayList<Object>singalPartition=(ArrayList<Object>) modify.get(z);
			 for(int x=0;x<singalPartition.size();x++){
				 if(ObjectSet.contains(singalPartition.get(x))){
					 singalPartition.remove(x);
				 }
			 }
		 }
		 
		 HashMap<String,ByteArrayOutputStream> ResultSet=new HashMap<String,ByteArrayOutputStream>();
			ArrayList<Object>recordResults=new ArrayList<Object>();
		 for(int h=count;h<=thesize;h++){
			 ArrayList<Object> PartitionSet=getdeployOrder.get(h);
				ArrayList<Object> resultSet=null;
				if(exceptionType!=null){
					resultSet=redeploy(PartitionSet,f,theResultSet,Dpartitionset,exceptionType);
					exceptionType=null;
				}else{
					resultSet=deploy(PartitionSet,f,theResultSet,Dpartitionset);
				}
				
				if(resultSet.get(0) instanceof Integer ){
					if((Integer)resultSet.get(0)==1){
						System.out.println("Exception type 1");
					
						rescheduleType1(recordResults,h,getdeployOrder);
						
						break;
					}else{
						System.out.println("Exception type 2");
						rescheduleType2(recordResults,h,getdeployOrder);
						break;
					}
					
					
				}
				if( (resultSet.get(0)) instanceof HashMap){
					 theResultSet.clear();
						for(int i=0;i<resultSet.size();i++){
							HashMap<String,ByteArrayOutputStream> temp=(HashMap<String, ByteArrayOutputStream>) resultSet.get(i);
							Set<String>keySet=temp.keySet();
							Iterator<String> getOutput=keySet.iterator();
							while(getOutput.hasNext()){
								String key=getOutput.next();
								theResultSet.put(key, temp.get(key));
							}
						}
						
						ResultSet=(HashMap<String, ByteArrayOutputStream>) theResultSet.clone();
						recordResults.add(ResultSet);
						f=f+PartitionSet.size();
				}

		 }
	}
	public void redeployment(ArrayList<Object> option,String exceptionType) throws Exception{
		deployOrder createOrder=new deployOrder(option,connections,
				 blockset,cloudSet,datablockset, workflowId,  partitionMap);
			
			HashMap<Integer,ArrayList<Object>> getdeployOrder= createOrder.deployOrder();
			DpartitionSet Dpartitionset=createOrder.getDpartitionSet();
		
			int f=0;
			HashMap<String,ByteArrayOutputStream> theResultSet=new HashMap<String,ByteArrayOutputStream>();
			HashMap<String,ByteArrayOutputStream> ResultSet=new HashMap<String,ByteArrayOutputStream>();
			ArrayList<Object>recordResults=new ArrayList<Object>();
			for(int a=0;a<getdeployOrder.size();a++){
				ArrayList<Object> PartitionSet=getdeployOrder.get(a+1);
				ArrayList<Object> resultSet=null;
				if(exceptionType!=null){
					resultSet=redeploy(PartitionSet,f,theResultSet,Dpartitionset,exceptionType);
					exceptionType=null;
				}else{
					resultSet=deploy(PartitionSet,f,theResultSet,Dpartitionset);
				}
				
				if(resultSet.get(0) instanceof Integer ){
					if((Integer)resultSet.get(0)==1){
						System.out.println("Exception type 1");
					
						rescheduleType1(recordResults,a+1,getdeployOrder);
						
						break;
					}else{
						System.out.println("Exception type 2");
						rescheduleType2(recordResults,a+1,getdeployOrder);
						break;
					}
					
					
				}
				if( (resultSet.get(0)) instanceof HashMap){
					 theResultSet.clear();
						for(int i=0;i<resultSet.size();i++){
							HashMap<String,ByteArrayOutputStream> temp=(HashMap<String, ByteArrayOutputStream>) resultSet.get(i);
							Set<String>keySet=temp.keySet();
							Iterator<String> getOutput=keySet.iterator();
							while(getOutput.hasNext()){
								String key=getOutput.next();
								theResultSet.put(key, temp.get(key));
							}
						}
						
						ResultSet=(HashMap<String, ByteArrayOutputStream>) theResultSet.clone();
						recordResults.add(ResultSet);
						f=f+PartitionSet.size();
				}
			}
	} 
	
	public void rescheduleType1(ArrayList<Object>recordResults,int breakpoint,HashMap<Integer,ArrayList<Object>> originalOption ) throws Exception{
		
		if(breakpoint==1){
			ArrayList<Object> proccesedPartition=originalOption.get(breakpoint);
			ArrayList<Object>partition=(ArrayList<Object>) proccesedPartition.get(0);
				int securityLevel=(Integer) partition.get(partition.size()-1);
				
				if(securityLevel==1){
					System.out.println("Discard the workflow");
				}
				else{
					ArrayList<Block> getBlocks=new ArrayList<Block>();
					for(int i=0;i<partition.size()-1;i++){
						Object theblock=partition.get(i);
						if(theblock instanceof Block){
							getBlocks.add((Block)theblock);
						}
					}
					
					if(!getBlocks.isEmpty()){
						ArrayList<Object> thereplacement=new ArrayList<Object>();
						for(int a=1;a<order.length;a++){
							ArrayList<Object>option=partitionMap.get(order[a]);
							for(int x=0;x<option.size();x++){
								ArrayList<Object>thepartition=(ArrayList<Object>) option.get(x);
								int compareSecurityLevel=(Integer) thepartition.get(thepartition.size()-1);
								int selected=0;
								for(int z=0;z<getBlocks.size();z++){
									Block block=(Block) getBlocks.get(z);
									if(thepartition.contains(block)&&compareSecurityLevel!=securityLevel){
									
										selected++;
									}
								 }
								if(selected==getBlocks.size()&&compareSecurityLevel!=securityLevel){
									
									thereplacement=(ArrayList<Object>) option.clone();
									break;
								}
							}
							if(!thereplacement.isEmpty()){
								break;
							}
						}
						
						if(!thereplacement.isEmpty()){
							redeployment(thereplacement,"Type1");
						}else{
							System.out.println("Discard the workflow");
						}
					}else{
						System.out.println("Can not find a replacement partition, the workflow has to be discarded");
					}
			}
		}else
		{
			ArrayList<Object> newPartition=new ArrayList<Object>();
			int b=breakpoint-2;
			ArrayList<Object> proccesedPartition=originalOption.get(b);
			HashMap<String,ByteArrayOutputStream> secondlatestResult=(HashMap<String, ByteArrayOutputStream>) recordResults.get(recordResults.size()-2);
			Set<String>BlockNames=secondlatestResult.keySet();
			ArrayList<Block> theSet=new ArrayList<Block>();
			Iterator<String> BlockIds=BlockNames.iterator();
			ArrayList<Object>endBlocks=new ArrayList<Object>();
			while(BlockIds.hasNext()){
				String theBlockId=BlockIds.next();
				for(int a=0;a<connections.size();a++){
					ArrayList<String> link=connections.get(a);
					String sourceId=link.get(0);
					String destinationId=link.get(1);
					if(theBlockId.equals(destinationId)){
						Block endBlock=blockset.getBlock(destinationId);
						String SelectedBlock=sourceId;
						Block theBlock=blockset.getBlock(SelectedBlock);
						for(int i=0;i<proccesedPartition.size();i++){
							ArrayList<Block> temp=new ArrayList<Block>();
							ArrayList<Object>partition=(ArrayList<Object>) proccesedPartition.get(i);
							if(partition.contains(theBlock)){
								int cloud=(Integer) partition.get(partition.size()-1);
							//	temp.add(theBlock);
								temp.add(endBlock);
								theSet=(ArrayList<Block>) temp.clone();
								endBlocks.add(theSet);
								endBlocks.add(cloud);
							}
						}
					}
				}
			}
			System.out.println(endBlocks);
			newPartition=replacement(endBlocks);
			/*store all processed blocks*/
			 int f=0;
			 ArrayList<Object> ObjectSet= new ArrayList<Object>();
			 for(int hf=1;hf<b;hf++){
				ArrayList<Object> tempOrder=originalOption.get(hf);
				f=f+tempOrder.size();
				for(int s=0;s<tempOrder.size();s++){
					ArrayList<Object> tempPartition=(ArrayList<Object>) tempOrder.get(s);
					for(int i=0;i<tempPartition.size()-1;i++){
						ObjectSet.add(tempPartition.get(i));
					}
				}
			 }
	
			 if(newPartition.isEmpty()){
				 System.out.println("Discard the workflow");
			 }else{
				 redeployment2(newPartition,"Type1",secondlatestResult,endBlocks,ObjectSet,f);
			 }
		 }
	}
	
	public void rescheduleType2(ArrayList<Object>recordResults,int breakpoint,HashMap<Integer,ArrayList<Object>> originalOption ) throws Exception{
		if(breakpoint==1){
			ArrayList<Object> proccesedPartition=originalOption.get(breakpoint);
			
			ArrayList<Object>partition=(ArrayList<Object>) proccesedPartition.get(0);
				int securityLevel=(Integer) partition.get(partition.size()-1);
				
				if(securityLevel==1){
					System.out.println("Discard the workflow");
				}
				else{
					ArrayList<Block> getBlocks=new ArrayList<Block>();
					for(int i=0;i<partition.size()-1;i++){
						Object theblock=partition.get(i);
						if(theblock instanceof Block){
							getBlocks.add((Block)theblock);
						}
					}
					
					if(!getBlocks.isEmpty()){
						ArrayList<Object> thereplacement=new ArrayList<Object>();
						for(int a=1;a<order.length;a++){
							ArrayList<Object>option=partitionMap.get(order[a]);
							for(int x=0;x<option.size();x++){
								ArrayList<Object>thepartition=(ArrayList<Object>) option.get(x);
								int compareSecurityLevel=(Integer) thepartition.get(thepartition.size()-1);
								int selected=0;
								for(int z=0;z<getBlocks.size();z++){
									Block block=(Block) getBlocks.get(z);
									if(thepartition.contains(block)&&compareSecurityLevel!=securityLevel){
									
										selected++;
									}
								 }
								if(selected==getBlocks.size()&&compareSecurityLevel!=securityLevel){
									
									thereplacement=(ArrayList<Object>) option.clone();
									break;
								}
							}
							if(!thereplacement.isEmpty()){
								break;
							}
						}
						
						if(!thereplacement.isEmpty()){
							redeployment(thereplacement,"Type2");
						}else{
							System.out.println("Discard the workflow");
						}
					}else{
						System.out.println("Can not find a replacement partition, the workflow has to be discarded");
					}
			}
		}else
		{
			ArrayList<Object> newPartition=new ArrayList<Object>();
			int b=breakpoint-1;
			ArrayList<Object> proccesedPartition=originalOption.get(b);
			HashMap<String,ByteArrayOutputStream> latestResult=(HashMap<String, ByteArrayOutputStream>) recordResults.get(recordResults.size()-1);
			Set<String>BlockNames=latestResult.keySet();
			ArrayList<Block> theSet=new ArrayList<Block>();
			Iterator<String> BlockIds=BlockNames.iterator();
			ArrayList<Object>endBlocks=new ArrayList<Object>();
			while(BlockIds.hasNext()){
				String theBlockId=BlockIds.next();
				for(int a=0;a<connections.size();a++){
					ArrayList<String> link=connections.get(a);
					String sourceId=link.get(0);
					String destinationId=link.get(1);
					if(theBlockId.equals(destinationId)){
						Block endBlock=blockset.getBlock(destinationId);
						String SelectedBlock=sourceId;
						Block theBlock=blockset.getBlock(SelectedBlock);
						for(int i=0;i<proccesedPartition.size();i++){
							ArrayList<Block> temp=new ArrayList<Block>();
							ArrayList<Object>partition=(ArrayList<Object>) proccesedPartition.get(i);
							if(partition.contains(theBlock)){
								int cloud=(Integer) partition.get(partition.size()-1);
							//	temp.add(theBlock);
								temp.add(endBlock);
								theSet=(ArrayList<Block>) temp.clone();
								endBlocks.add(theSet);
								endBlocks.add(cloud);
							}
						}
					}
				}
			}
			/*here an order only includes one partitioning,it should be more than one in practice*/
			 newPartition=replacement(endBlocks);
			
			  
			 /*store all processed blocks*/
			 int f=0;
			 ArrayList<Object> ObjectSet= new ArrayList<Object>();
			 for(int hf=1;hf<b;hf++){
				ArrayList<Object> tempOrder=originalOption.get(hf);
				f=f+tempOrder.size();
				for(int s=0;s<tempOrder.size();s++){
					ArrayList<Object> tempPartition=(ArrayList<Object>) tempOrder.get(s);
					for(int i=0;i<tempPartition.size()-1;i++){
						ObjectSet.add(tempPartition.get(i));
					}
				}
			 }
	
			 if(newPartition.isEmpty()){
				 System.out.println("Discard the workflow");
			 }else{
				 redeployment2(newPartition,"Type2",latestResult,endBlocks,ObjectSet,f);
			 }

		 }		
	} 
	
	private ArrayList<Object>replacement(ArrayList<Object> endBlocks){
		ArrayList<Object> thereplacement=new ArrayList<Object>();
		for(int a=1;a<order.length;a++){
			ArrayList<Object>option=partitionMap.get(order[a]);
			
			ArrayList<Block> theSet=(ArrayList<Block>) endBlocks.get(0);
			int cloud=(Integer) endBlocks.get(1);
			for(int x=0;x<option.size();x++){
				ArrayList<Object>thepartition=(ArrayList<Object>) option.get(x);
				int compareSecurityLevel=(Integer) thepartition.get(thepartition.size()-1);
				int selected=0;
				for(int b=0;b<theSet.size();b++){
					Block block=theSet.get(b);
					if(thepartition.contains(block)&&compareSecurityLevel==cloud){
						selected++;
					}
				}
				if(selected==theSet.size()){
					thereplacement=(ArrayList<Object>) option.clone();
					break;
				}
				if(!thereplacement.isEmpty()){
					break;
				}
			}
			
		}
		return thereplacement;
	}
		
	public  ArrayList<Object>deploy(ArrayList<Object> PartitionSet,int partitionNum,HashMap<String,ByteArrayOutputStream> resource,DpartitionSet Dpartitionset) throws Exception{
		 
		HashMap<String,ByteArrayOutputStream>results=new HashMap<String,ByteArrayOutputStream>();
		ArrayList<Object> resultSet=new ArrayList<Object>();
		for(int a=0;a<PartitionSet.size();a++){
			HashMap<String,ByteArrayOutputStream>temp=new HashMap<String,ByteArrayOutputStream>();
			WorkflowRestructure deployworkflow=new WorkflowRes();
			String cloud=null;
			int num=partitionNum+a+1;
			String thePartitionNum="Partition"+num;
			System.out.println(thePartitionNum);
			ArrayList<Object>partition=(ArrayList<Object>) PartitionSet.get(a);
			
			int securityLevel=(Integer) partition.get(partition.size()-1);
			if(securityLevel==0){
				cloud="C0";
			}else{
				cloud="C1";
			}
			
	/*	    	temp=deployworkflow.CreateWorkflow(cloud,partition,thePartitionNum,connections,blockset,Dpartitionset,resource);
				while(temp.isEmpty()){
					Thread.sleep(1000);
					temp=deployworkflow.CreateWorkflow(cloud,partition,thePartitionNum,connections,blockset,Dpartitionset,resource);
				}
				results=(HashMap<String, ByteArrayOutputStream>) temp.clone();
				resultSet.add(results); 
			*/
			master messaging =new master();
			int feedback=messaging.askforDeloy(cloud);
			/*no exception happened*/
			if(feedback==0){
				temp=deployworkflow.CreateWorkflow(cloud,partition,thePartitionNum,connections,blockset,Dpartitionset,resource);
				while(temp.isEmpty()){
					Thread.sleep(1000);
					temp=deployworkflow.CreateWorkflow(cloud,partition,thePartitionNum,connections,blockset,Dpartitionset,resource);
				}
				results=(HashMap<String, ByteArrayOutputStream>) temp.clone();
				resultSet.add(results);
			}
			/*The Running Cloud is failed*/
			if(feedback==1){
				resultSet.clear();
				resultSet.add(1);
			}	
			/*type2 exception*/
			if(feedback==2){
				resultSet.clear();
				resultSet.add(2);
			}
		} 
		return resultSet;
	}
	public  ArrayList<Object>redeploy(ArrayList<Object> PartitionSet,
			int partitionNum,HashMap<String,ByteArrayOutputStream> resource,DpartitionSet Dpartitionset,String exceptionType) throws Exception{
		
		HashMap<String,ByteArrayOutputStream>results=new HashMap<String,ByteArrayOutputStream>();
		ArrayList<Object> resultSet=new ArrayList<Object>();
			 for(int a=0;a<PartitionSet.size();a++){
					HashMap<String,ByteArrayOutputStream>temp=new HashMap<String,ByteArrayOutputStream>();
					WorkflowRestructure deployworkflow=new WorkflowRes();
					String cloud=null;
					int num=partitionNum+a+1;
					String thePartitionNum=exceptionType+"replacementPartition"+num;
					System.out.println(thePartitionNum);
					ArrayList<Object>partition=(ArrayList<Object>) PartitionSet.get(a);
					
					int securityLevel=(Integer) partition.get(partition.size()-1);
					if(securityLevel==0){
						cloud="C0";
					}else{
						cloud="C1";
					}
			
					/*no exception happened*/
						temp=deployworkflow.CreateWorkflow(cloud,partition,thePartitionNum,connections,blockset,Dpartitionset,resource);
						while(temp.isEmpty()){
							Thread.sleep(1000);
							temp=deployworkflow.CreateWorkflow(cloud,partition,thePartitionNum,connections,blockset,Dpartitionset,resource);
						}
						results=(HashMap<String, ByteArrayOutputStream>) temp.clone();
						resultSet.add(results);
				} 
		return resultSet;
	}
}
 