package uk.ac.ncl.cs.esc.workflow.restructure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pipeline.core.drawing.DrawingException;
import org.pipeline.core.drawing.layout.BlockModelPosition;
import org.pipeline.core.drawing.model.DefaultDrawingModel;
import org.pipeline.core.xmlstorage.XmlDataStore;
import org.pipeline.core.xmlstorage.XmlStorageException;
import org.pipeline.core.xmlstorage.io.XmlDataStoreStreamReader;

import uk.ac.ncl.cs.esc.connection.getConnection;
import uk.ac.ncl.cs.esc.connection.myConnection;
import uk.ac.ncl.cs.esc.deployment.DpartitionSet;
import uk.ac.ncl.cs.esc.deployment.singalPartition;
import uk.ac.ncl.cs.esc.partitiontool.Block;
import uk.ac.ncl.cs.esc.partitiontool.BlockSet;
import uk.ac.ncl.cs.esc.partitiontool.DataBlock;

import com.connexience.server.api.API;
import com.connexience.server.api.APIConnectException;
import com.connexience.server.api.APIInstantiationException;
import com.connexience.server.api.APIParseException;
import com.connexience.server.api.APISecurityException;
import com.connexience.server.api.IDocument;
import com.connexience.server.api.IDynamicWorkflowService;
import com.connexience.server.api.IFolder;
import com.connexience.server.api.IObject;
import com.connexience.server.api.IWorkflow;
import com.connexience.server.api.IWorkflowInvocation;
import com.connexience.server.api.IWorkflowParameterList;
import com.connexience.server.api.IXmlMetaData;
import com.connexience.server.api.impl.InkspotWorkflowParameterList;
import com.connexience.server.ejb.util.WorkflowEJBLocator;
import com.connexience.server.workflow.blocks.processor.DataProcessorBlock;
import com.connexience.server.workflow.json.JSONDrawingExporter;
import com.connexience.server.workflow.xmlstorage.DocumentRecordWrapper;

public class WorkflowRes implements WorkflowRestructure {

	ArrayList< byte[]> data=new ArrayList< byte[]>();
	
	public JSONObject getWorkflowAsJsonObject(String workflowId) throws Exception {
		getConnection parser=new myConnection();
		parser.createAPI();
		IWorkflow workflow=parser.getAPI().getWorkflow(workflowId);
		ByteArrayOutputStream buffer=new ByteArrayOutputStream();
		parser.getAPI().download(workflow, buffer);
		 buffer.flush();
		 buffer.close();
		 ByteArrayInputStream inStream=new ByteArrayInputStream(buffer.toByteArray());
		 XmlDataStoreStreamReader reader =new XmlDataStoreStreamReader(inStream);
		 XmlDataStore wfData =reader.read();
		 DefaultDrawingModel drawing =new DefaultDrawingModel();
		 drawing.recreateObject(wfData);
		 JSONDrawingExporter exporter = new JSONDrawingExporter(drawing);
		 return exporter.saveToJson();
	}
	public HashMap<String,String> Blocklist(String workflowId) throws Exception {
		HashMap<String,String> Blocklist=new HashMap<String,String>();
		JSONObject dataObject= getWorkflowAsJsonObject( workflowId);
		JSONObject blocks = dataObject.getJSONObject("blocks");
		JSONArray blockArray = blocks.getJSONArray("blockArray");
        int blockCount = blocks.getInt("blockCount");
        for (int i = 0; i < blockCount; i++)
        {
        	Blocklist.put((String) blockArray.getJSONObject(i).get("guid"),(String) blockArray.getJSONObject(i).get("label") );
        	
        }
		return Blocklist;
	}
	public ArrayList<String> getPors(String workflowId,String sourceId, String endId) throws Exception{
		
		
		ArrayList<String>ports=new ArrayList<String>();
		
		JSONObject dataObject= getWorkflowAsJsonObject( workflowId);
		JSONObject connection=dataObject.getJSONObject("connections");
		JSONArray connectionArray=connection.getJSONArray("connectionArray");
		int connectionCount=connection.getInt("connectionCount");
		for(int i=0;i<connectionCount;i++){
			String destinationPort= (String) connectionArray.getJSONObject(i).get("destinationPortName");
			String sourcePort=(String) connectionArray.getJSONObject(i).get("sourcePortName");
			String destinationBlockGuid= (String) connectionArray.getJSONObject(i).get("destinationBlockGuid");
			String sourceBlockGuid=(String) connectionArray.getJSONObject(i).get("sourceBlockGuid");
			if(sourceBlockGuid.equals(sourceId)&& destinationBlockGuid.equals(endId)){
				
				ArrayList<String>temp=new ArrayList<String>();
				temp.add(sourcePort);
				temp.add(destinationPort);
				ports=(ArrayList<String>) temp.clone();
			}
		}
		return ports;
	}
	public ArrayList<String> getOutputports(String workflowId,String blockId) throws Exception{
		ArrayList<String>outputports=new ArrayList<String>();
		String serviceId=getBlockServiceId(blockId,workflowId);
		outputports.add(blockId);
		outputports.add(serviceId);
		JSONObject dataObject= getWorkflowAsJsonObject( workflowId);
		JSONObject connection=dataObject.getJSONObject("connections");
		JSONArray connectionArray=connection.getJSONArray("connectionArray");
		int connectionCount=connection.getInt("connectionCount");
		for(int i=0;i<connectionCount;i++){
		 String sourcePort=(String) connectionArray.getJSONObject(i).get("sourcePortName");
		 String sourceBlockGuid=(String) connectionArray.getJSONObject(i).get("sourceBlockGuid");
		 if(sourceBlockGuid.equals(blockId)){
			 outputports.add(sourcePort);
		 }
		}
		return outputports;
	}
	public ArrayList<String> getInputports(String workflowId,String blockId) throws Exception{
		ArrayList<String>inputports=new ArrayList<String>();
		String serviceId=getBlockServiceId(blockId,workflowId);
		inputports.add(blockId);
		inputports.add(serviceId);
		JSONObject dataObject= getWorkflowAsJsonObject( workflowId);
		JSONObject connection=dataObject.getJSONObject("connections");
		JSONArray connectionArray=connection.getJSONArray("connectionArray");
		int connectionCount=connection.getInt("connectionCount");
		for(int i=0;i<connectionCount;i++){
		 String destinationPort= (String) connectionArray.getJSONObject(i).get("destinationPortName");
		 String destinationBlockGuid= (String) connectionArray.getJSONObject(i).get("destinationBlockGuid");
		 if(destinationBlockGuid.equals(blockId)){
				inputports.add(destinationPort);
		 }
		}
		return inputports;
		
	}
	
	public ArrayList<String> getEndports(String workflowId,String blockId) throws Exception{
		
		ArrayList<String>ports=new ArrayList<String>();
		
		JSONObject dataObject= getWorkflowAsJsonObject( workflowId);
		JSONObject connection=dataObject.getJSONObject("connections");
		JSONArray connectionArray=connection.getJSONArray("connectionArray");
		int connectionCount=connection.getInt("connectionCount");
		for(int i=0;i<connectionCount;i++){
		//	String destinationPort= (String) connectionArray.getJSONObject(i).get("destinationPortName");
			String sourcePort=(String) connectionArray.getJSONObject(i).get("sourcePortName");
		//	String destinationBlockGuid= (String) connectionArray.getJSONObject(i).get("destinationBlockGuid");
			String sourceBlockGuid=(String) connectionArray.getJSONObject(i).get("sourceBlockGuid");
			if(sourceBlockGuid.equals(blockId)){
				
				ArrayList<String>temp=new ArrayList<String>();
				temp.add(sourcePort);
				
				ports=(ArrayList<String>) temp.clone();
			}
		}
		return ports;
		
	}
	
	public HashMap<String,ArrayList<String>> ConnectionMap(String workflowId) throws Exception {
	    
	    ArrayList<String> destinationBlocks=new ArrayList<String>();
	    HashMap<String,ArrayList<String>> connectionmap=new HashMap<String,ArrayList<String>>();
		JSONObject dataObject= getWorkflowAsJsonObject( workflowId);
		JSONObject connection=dataObject.getJSONObject("connections");
		JSONArray connectionArray=connection.getJSONArray("connectionArray");
		int connectionCount=connection.getInt("connectionCount");
		for(int i=0;i<connectionCount;i++){
		String destinationBlockGuid= (String) connectionArray.getJSONObject(i).get("destinationBlockGuid");
		String sourceBlockGuid=(String) connectionArray.getJSONObject(i).get("sourceBlockGuid");
		 ArrayList<String> temp=new ArrayList<String>();
		 
		if(connectionmap.containsKey(sourceBlockGuid)){
			temp.clear();
			temp=connectionmap.get(sourceBlockGuid);
			temp.add(destinationBlockGuid);
			destinationBlocks=(ArrayList)temp.clone();
			connectionmap.put(sourceBlockGuid, destinationBlocks);
		 }else{
			 temp.add(destinationBlockGuid);
			 destinationBlocks=(ArrayList)temp.clone();
			connectionmap.put(sourceBlockGuid, destinationBlocks);
		 }
		  temp.clear();
		}
	//	System.out.println(connectionmap);
		return connectionmap;
	}
	public HashMap<String,String> Workflowlist() throws Exception {
		getConnection parser=new myConnection();
		parser.createAPI();
		HashMap<String,String> workflowmap=new HashMap<String,String>();
		List<IWorkflow> workflows = parser.getAPI().listWorkflows();
		 for(IWorkflow w : workflows){
			 String workflowname=w.getName();
			 String workflowId=w.getId();
			 workflowmap.put(workflowId, workflowname);
		 }
		return workflowmap;
	}
	
	public HashMap<String,String> fileUpload(HashMap<String, ByteArrayOutputStream> theresults,API api) throws Exception{
		HashMap<String,String> DocumentSet=new HashMap<String,String>();
		Set<String> resultSet=theresults.keySet();
		Iterator<String> getDocument=resultSet.iterator();
		while(getDocument.hasNext()){
			IDocument tempFile=null;
			String BlockName=(String) getDocument.next();
			ByteArrayOutputStream inStream=theresults.get(BlockName);
			byte[] inString = inStream.toByteArray();
			tempFile = (IDocument)api.createObject(IDocument.XML_NAME);
			tempFile.setName(BlockName + System.currentTimeMillis());
			tempFile = api.saveDocument(api.getUserFolder(api.getUserContext()), tempFile);
			api.upload(tempFile, new ByteArrayInputStream(inString));
			String fileId=tempFile.getId();
			DocumentSet.put(BlockName, fileId);
		}
		return DocumentSet;
	}
	
	
	
	public  HashMap<String,ByteArrayOutputStream> CreateWorkflow(String cloudName,ArrayList<Object> partition,String partitionName,
		ArrayList<ArrayList<String>> connections,BlockSet blockset,ArrayList<ArrayList<String>>inputs,
													HashMap<String, ByteArrayOutputStream> theresults) throws Exception{
		getConnection parser=new myConnection();
		parser.createCloudAPI(cloudName);
		API api=parser.getAPI();
		ArrayList<Object>Links=new ArrayList<Object>();
		HashMap<String,String> resultInfo=fileUpload(theresults,api);
		ArrayList<Block> Blocks=new ArrayList<Block>();
		for(int a=1;a<partition.size();a++){
			Object block=partition.get(a);
			if(block instanceof DataBlock){
				Links.add(block);
			}
			if(block instanceof Block){
				Blocks.add((Block)block);
			}
		}
	//	singalPartition thePartition=Dpartitionset.getSingalPartition(partition);
		int b=0;
		DefaultDrawingModel drawing = new DefaultDrawingModel();
		
		// only include data block in this partition
		if(Blocks.isEmpty()){
			for(int h=0;h<Links.size();h++){
				DataBlock thedata=(DataBlock) Links.get(h);
				String destination=thedata.getdestinationblockId();
				String documentId=resultInfo.get(destination);
				String serviceId="blocks-core-io-csvimport";
				IDynamicWorkflowService service = parser.getService("blocks-core-io-csvimport");
				DataProcessorBlock Block = parser.createBlock(service);
				createblock(b,Block,documentId,serviceId,api,drawing,partitionName);
				b++;
				String theserviceId="blocks-core-io-csvexport";
				IDynamicWorkflowService service3=parser.getService(theserviceId);
				DataProcessorBlock exportBlock = parser.createBlock(service3);
				createblock(b,exportBlock,documentId,theserviceId,api,drawing,destination);
				drawing.connectPorts(Block.getOutput("imported-data"),exportBlock.getInput("input-data"));
			}
			
		}else{
			// only service blocks include in the partition
			if(Links.isEmpty()){
				for(int a=0;a<Blocks.size();a++){
					Block theblock=(Block) partition.get(a);
					String blockId=theblock.getBlockId();
					String serviceId=theblock.getserviceId();
					DataProcessorBlock serviceBlock;
					
					// the block is the load block
					if(serviceId.equals("blocks-core-io-csvimport")){
						String documentId=null;
						IDynamicWorkflowService service = parser.getService("blocks-core-io-csvimport");
						serviceBlock = parser.createBlock(service);
						createblock(b,serviceBlock,documentId,serviceId,api,drawing,partitionName);
						b++;
					}else{
						String documentId=resultInfo.get(blockId);
						String theserviceId="blocks-core-io-csvimport";
						IDynamicWorkflowService service = parser.getService("blocks-core-io-csvimport");
						DataProcessorBlock Block = parser.createBlock(service);
						createblock(b,Block,documentId,theserviceId,api,drawing,partitionName);
						b++;
						IDynamicWorkflowService service1 = parser.getService(serviceId);
						serviceBlock = parser.createBlock(service1);
						createblock(b,serviceBlock,documentId,serviceId,api,drawing,partitionName);
						b++;
						String inputportName=null;
						for(int fx=0;fx<inputs.size();fx++){
							ArrayList<String>theinput=inputs.get(fx);
							if(blockId.equals(theinput.get(1))){
								inputportName=theinput.get(5);
							}
						}
						drawing.connectPorts(Block.getOutput("imported-data"), serviceBlock.getInput(inputportName));
					}
					
					// add export block
				    String documentId=null;
					String outputPort=null;
					String inputPort=null;
					ArrayList<String> export=getNextNode(blockId,partition, blockset, connections);
					if(!export.isEmpty()){
						for(int hh=0;hh<export.size();hh++){
							String theserviceName=export.get(hh);
							String theserviceId="blocks-core-io-csvexport";
							IDynamicWorkflowService service3=parser.getService(theserviceId);
							DataProcessorBlock exportBlock = parser.createBlock(service3);
							createblock(b,exportBlock,documentId,theserviceId,api,drawing,theserviceName);
							b++;
							for(int fx=0;fx<connections.size();fx++){
								ArrayList<String> connection=connections.get(fx);
								String startNode=connection.get(0);
								String endNode=connection.get(1);
								if(startNode.equals(blockId)&&endNode.equals(theserviceName)){
									outputPort=connection.get(4);
									inputPort=connection.get(5);
									break;
								}
							}
							drawing.connectPorts(serviceBlock.getOutput(outputPort),exportBlock.getInput(inputPort));
						}
						
					}	
				}
			}else{
				
				ArrayList<Block> initialBlocks=new ArrayList<Block>();

					for(int a=0;a<Links.size();a++){
						DataBlock link=(DataBlock) Links.get(a);
						String source=link.getsourceblockId();
						String destination=link.getdestinationblockId();
						Block sourceBlock=blockset.getBlock(source);
						Block destinationBlock=blockset.getBlock(destination);
						if(!Blocks.contains(sourceBlock)&&Blocks.contains(destinationBlock)){
							initialBlocks.add(destinationBlock);
						}
					}
					
					for(int i=0;i<Blocks.size();i++){
						Block theBlock=Blocks.get(i);
						String Blockid=theBlock.getBlockId();
						boolean isStart=true;
						for(int a=0;a<Links.size();a++){
							DataBlock link=(DataBlock) Links.get(a);
							String destination=link.getdestinationblockId();
							if(Blockid.equals(destination)){
								isStart=false;
								break;
							}
						}
						if(isStart){
							initialBlocks.add(theBlock);
						}
					}
					
					// start deploy from one of the the initial block of the partition
					for(int h=0;h<initialBlocks.size();h++){
						Block startBlock=initialBlocks.get(h);
						String blockId=startBlock.getBlockId();
						String serviceId=startBlock.getserviceId();
						DataProcessorBlock serviceBlock;
						if(serviceId.equals("blocks-core-io-csvimport")){
							String documentId=null;
							String serviceName=null;
							IDynamicWorkflowService service = parser.getService("blocks-core-io-csvimport");
							 serviceBlock = parser.createBlock(service);
							createblock(b,serviceBlock,documentId,serviceId,api,drawing,serviceName);
							b++;
						}else{
							// add the load block for the start block
							String documentId=resultInfo.get(blockId);
							String serviceName=null;
							String theserviceId="blocks-core-io-csvimport";
							IDynamicWorkflowService service = parser.getService("blocks-core-io-csvimport");
							DataProcessorBlock Block = parser.createBlock(service);
							createblock(b,Block,documentId,theserviceId,api,drawing,serviceName);
							b++;
							
							IDynamicWorkflowService service1 = parser.getService(serviceId);
							serviceBlock = parser.createBlock(service1);
							createblock(b,serviceBlock,documentId,serviceId,api,drawing,serviceName);
							b++;
							
							String inputportName=null;
							for(int fx=0;fx<inputs.size();fx++){
							
								ArrayList<String>theinput=inputs.get(fx);
								
								if(blockId.equals(theinput.get(1))){
									inputportName=theinput.get(5);
								}
							}
							
							drawing.connectPorts(Block.getOutput("imported-data"), serviceBlock.getInput(inputportName));
						}
						// create the offspring blocks as following 
						offspringNodes( serviceBlock,startBlock,Links,Blocks,blockset,connections,partition,
																	drawing,b,parser,api,new ArrayList<String>());
					
					}
					
			}
		}
		//  get the results
		HashMap<String, ByteArrayOutputStream> result=new HashMap<String, ByteArrayOutputStream>();
		boolean finished=false;
	     getResult(drawing,partitionName,parser,api,result,finished);
	     
	     while(finished==false){
	    	 try {
	    		 Thread.sleep(500);
	    	 } catch (Exception e){}
	     }
		return result;
	}
	
	/* create the workflow and execute the workflow*/
	private void getResult(DefaultDrawingModel drawing,String partitionName,getConnection parser,API api,HashMap<String, ByteArrayOutputStream> result,boolean finished) throws Exception{
		
		JSONDrawingExporter exporter = new JSONDrawingExporter(drawing); 
		IWorkflow wf = parser.createWorkflow(partitionName, drawing);
		IWorkflowParameterList params = (InkspotWorkflowParameterList)api.createObject(IWorkflowParameterList.XML_NAME);
		IWorkflowInvocation invocation = api.executeWorkflow(wf, params);
		 while(invocation.getStatus().equals(IWorkflowInvocation.WORKFLOW_RUNNING) || invocation.getStatus().equals(IWorkflowInvocation.WORKFLOW_WAITING)){
	    	 try {
	    		 Thread.sleep(1000);
	    	 } catch (Exception e){}
	    	 System.out.println("Checking status");
	    	 invocation =api.getWorkflowInvocation(invocation.getInvocationId());
	     }
		  List<IObject> results = api.getFolderContents(invocation);
	//	  HashMap<String,ByteArrayOutputStream> result=new HashMap<String,ByteArrayOutputStream>();
		  for (IObject r : results) {
			  if (r instanceof IDocument) {
				  IDocument d = (IDocument) r;
				  String getName= d.getName();
				  String[] thename=getName.split("\\.");
				  ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				  api.download(d, outStream);
				  outStream.flush();
				  result.put(thename[0], outStream);
			  }
		  }
		  
	} 
	
	/* this method is used the create the offspring nodes the start nodes*/
	private void offspringNodes(DataProcessorBlock serviceBlock,Block startBlock,ArrayList<Object>Links,ArrayList<Block> Blocks,BlockSet blockset,ArrayList<ArrayList<String>> connections,
			ArrayList<Object>partition,DefaultDrawingModel drawing,int b,getConnection parser,API api,ArrayList<String> visited) throws Exception{
		
		String blockId=startBlock.getBlockId();
		visited.add( blockId);
		for(int a=0;a<Links.size();a++){
			DataBlock data=(DataBlock) Links.get(a);
			String source=data.getsourceblockId();
			String destination=data.getdestinationblockId();
			Block destinationBlock=blockset.getBlock(destination);
			String outputPort=null;
			String inputPort=null;
			if(blockId.equals(source)){
				
				if(Blocks.contains(destinationBlock)&&!visited.contains(destination)){
					String serviceName=null;
					String documentId=null;
					
					String offspringSeviceId=destinationBlock.getserviceId();
					IDynamicWorkflowService service = parser.getService(offspringSeviceId);
					DataProcessorBlock Block = parser.createBlock(service);
					createblock(b,Block,documentId,offspringSeviceId,api,drawing,serviceName);
					b++;
					for(int fx=0;fx<connections.size();fx++){
						ArrayList<String> connection=connections.get(fx);
						String startNode=connection.get(0);
						String endNode=connection.get(1);
						if(startNode.equals(blockId)&&endNode.equals(destination)){
							outputPort=connection.get(4);
							inputPort=connection.get(5);
							break;
						}
					}
					
					drawing.connectPorts(serviceBlock.getOutput(outputPort),Block.getInput(inputPort));
					offspringNodes(Block,destinationBlock,Links,Blocks,blockset,connections,partition,drawing,b, parser,api,visited);
				}
				
				if(!Blocks.contains(destinationBlock)){
					String theserviceName=destination;
					String theserviceId="blocks-core-io-csvexport";
					IDynamicWorkflowService service3=parser.getService(theserviceId);
					DataProcessorBlock exportBlock = parser.createBlock(service3);
					createblock(b,exportBlock,null,theserviceId,api,drawing,theserviceName);
					b++;
					for(int fx=0;fx<connections.size();fx++){
						ArrayList<String> connection=connections.get(fx);
						String startNode=connection.get(0);
						String endNode=connection.get(1);
						if(startNode.equals(blockId)&&endNode.equals(theserviceName)){
							outputPort=connection.get(4);
							inputPort=connection.get(5);
							break;
						}
					}
					drawing.connectPorts(serviceBlock.getOutput(outputPort),exportBlock.getInput(inputPort));
				}
			}
		}
			
	}
/*	public  HashMap<String,ByteArrayOutputStream> CreateWorkflow(String cloud,ArrayList<Object>partition,
				String thePartitionNum,ArrayList<ArrayList<String>> connections,BlockSet blockset,
				           DpartitionSet Dpartitionset,HashMap<String, ByteArrayOutputStream> theresults) throws Exception{
		System.out.println("Start deploy");
		getConnection parser=new myConnection();
		parser.createCloudAPI(cloud);
		API api=parser.getAPI();
		HashMap<String,String> resultInfo=fileUpload(theresults,api);

		boolean Blockcontained=false;
		ArrayList<Object>Links=new ArrayList<Object>();
		for(int a=0;a<partition.size()-1;a++){
			Object block=partition.get(a);
			if(block instanceof DataBlock){
				Links.add(block);
			}
			if(block instanceof Block){
				Blockcontained=true;
			}
		}
		singalPartition thePartition=Dpartitionset.getSingalPartition(partition);
		System.out.println(partition);
		int b=0;
		DefaultDrawingModel drawing = new DefaultDrawingModel();
		if(!Links.isEmpty()){
			if(Blockcontained==true){
				ArrayList<Block> initialBlocks=new ArrayList<Block>();
				for(int x=0;x<Links.size();x++){
					boolean iscontain=true;
					DataBlock data=(DataBlock) Links.get(x);
					String source=data.getsourceblockId();
					String destination=data.getdestinationblockId();
					for(int i=0;i<Links.size();i++){
						if(i!=x){
							DataBlock newData=(DataBlock)Links.get(i);
							String newdestination=newData.getdestinationblockId();
							if(source.equals(newdestination)){
								iscontain=false;
								break;
							}
						}
						
					}
					if(iscontain==true){
						Block startBlock=blockset.getBlock(source);
						if(!initialBlocks.contains(startBlock)){
							if(partition.contains(startBlock)){
								initialBlocks.add(startBlock);
							}else{
								Block theStartBlock=blockset.getBlock(destination);
								initialBlocks.add(theStartBlock);
							}
						}
						
					}
				}
				
				
				for(int h=0;h<initialBlocks.size();h++){
					Block getstartBlock=initialBlocks.get(h);
					String blockId=getstartBlock.getBlockId();
					String serviceId=getstartBlock.getserviceId();
					DataProcessorBlock serviceBlock;
					if(serviceId.equals("blocks-core-io-csvimport")){
						String documentId=null;
						String serviceName=null;
						IDynamicWorkflowService service = parser.getService("blocks-core-io-csvimport");
						 serviceBlock = parser.createBlock(service);
						createblock(b,serviceBlock,documentId,serviceId,api,drawing,serviceName);
						b++;
					}else{
						String documentId=resultInfo.get(blockId);
						String serviceName=null;
						String theserviceId="blocks-core-io-csvimport";
						IDynamicWorkflowService service = parser.getService("blocks-core-io-csvimport");
						DataProcessorBlock Block = parser.createBlock(service);
						createblock(b,Block,documentId,theserviceId,api,drawing,serviceName);
						b++;
						
						IDynamicWorkflowService service1 = parser.getService(serviceId);
					
						 serviceBlock = parser.createBlock(service1);
						createblock(b,serviceBlock,documentId,serviceId,api,drawing,serviceName);
						b++;
						ArrayList<ArrayList<String>>inputs=thePartition.getInputs();
						String inputportName=null;
						for(int fx=0;fx<inputs.size();fx++){
						
							ArrayList<String>theinput=inputs.get(fx);
							
							if(blockId.equals(theinput.get(1))){
								inputportName=theinput.get(5);
							}
						}
						
						drawing.connectPorts(Block.getOutput("imported-data"), serviceBlock.getInput(inputportName));
					}
				
					
					Block startBlock=getstartBlock;
					DataProcessorBlock createstartBlock=serviceBlock;
					DataProcessorBlock createendBlock=serviceBlock;
					ArrayList<Block> endBlockSet=getEndBlockSet(blockId,Links,blockset,partition);
					if(!endBlockSet.isEmpty()){
						String startBlockId=startBlock.getBlockId();
						String documentId=null;
						String outputPort=null;
						String inputPort=null;
						ArrayList<String> export=getNextNode(startBlockId,partition, blockset, connections);
						add export node
						if(!export.isEmpty()){
							for(int hh=0;hh<export.size();hh++){
								String theserviceName=export.get(hh);
								String theserviceId="blocks-core-io-csvexport";
								IDynamicWorkflowService service3=parser.getService(theserviceId);
								DataProcessorBlock exportBlock = parser.createBlock(service3);
								createblock(b,exportBlock,documentId,theserviceId,api,drawing,theserviceName);
								b++;
								for(int fx=0;fx<connections.size();fx++){
									ArrayList<String> connection=connections.get(fx);
									String startNode=connection.get(0);
									String endNode=connection.get(1);
									if(startNode.equals(startBlockId)&&endNode.equals(theserviceName)){
										outputPort=connection.get(4);
										inputPort=connection.get(5);
										break;
									}
								}
								drawing.connectPorts(createstartBlock.getOutput(outputPort),exportBlock.getInput(inputPort));
							}
						}
						
						add next node in the same partition
						ArrayList<Object> thesteps=new ArrayList<Object>();
						ArrayList<Object> step=new ArrayList<Object>();
						ArrayList<Object> startNodeInfo=new ArrayList<Object>();
			//			ArrayList<Block> getNewBlockSet=new ArrayList<Block>();
						for(int xx=0;xx<endBlockSet.size();xx++){
			//				ArrayList<Object> temp1=new ArrayList<Object>();
							Block endBlock=endBlockSet.get(xx);
							String endBlockId=endBlock.getBlockId();
							String serviceName=null;
							String endBlockServiceId=endBlock.getserviceId();
							IDynamicWorkflowService endBlockservice = parser.getService(endBlockServiceId);
							 createendBlock = parser.createBlock(endBlockservice);
							createblock(b,createendBlock,documentId,endBlockServiceId,api,drawing,serviceName);
							b++;
							for(int fx=0;fx<connections.size();fx++){
								ArrayList<String> connection=connections.get(fx);
								String startNode=connection.get(0);
								String endNode=connection.get(1);
								if(startNode.equals(startBlockId)&&endNode.equals(endBlockId)){
									outputPort=connection.get(4);
									inputPort=connection.get(5);
									break;
								}
							}
							drawing.connectPorts(createstartBlock.getOutput(outputPort),createendBlock.getInput(inputPort));
						ArrayList<Block>temp2=getEndBlockSet(endBlockId,Links,blockset,partition);
							if(!temp2.isEmpty()){
								startNodeInfo.add(endBlock);
								startNodeInfo.add(createendBlock);
								step=(ArrayList<Object>) startNodeInfo.clone();
								thesteps.add(step);
							}
						}
						Recursive until the all blocks is created 
						goDeployment( thesteps, connections,blockset,partition,drawing, b, parser, api,Links);
						
					}else{
						String documentId=null;
						String outputPort=null;
						String inputPort=null;
						
						ArrayList<String> export=getNextNode(blockId,partition, blockset, connections);
						if(!export.isEmpty()){
							for(int hh=0;hh<export.size();hh++){
								String theserviceName=export.get(hh);
								String theserviceId="blocks-core-io-csvexport";
								IDynamicWorkflowService service3=parser.getService(theserviceId);
								DataProcessorBlock exportBlock = parser.createBlock(service3);
								createblock(b,exportBlock,documentId,theserviceId,api,drawing,theserviceName);
								b++;
								for(int fx=0;fx<connections.size();fx++){
									ArrayList<String> connection=connections.get(fx);
									String startNode=connection.get(0);
									String endNode=connection.get(1);
									if(startNode.equals(blockId)&&endNode.equals(theserviceName)){
										outputPort=connection.get(4);
										inputPort=connection.get(5);
										break;
									}
								}
								drawing.connectPorts(createendBlock.getOutput(outputPort),exportBlock.getInput(inputPort));
							}
						}
					}
					
				}
			}else{
				for(int hf=0;hf<Links.size();hf++){
					DataBlock thedata=(DataBlock) Links.get(hf);
					String destination=thedata.getdestinationblockId();
			//		System.out.println(destination);
			//		System.out.println(resultInfo);
					String documentId=resultInfo.get(destination);
			//		System.out.println(documentId);
					String serviceId="blocks-core-io-csvimport";
					IDynamicWorkflowService service = parser.getService("blocks-core-io-csvimport");
					DataProcessorBlock Block = parser.createBlock(service);
					createblock(b,Block,documentId,serviceId,api,drawing,thePartitionNum);
					b++;
					String theserviceId="blocks-core-io-csvexport";
					IDynamicWorkflowService service3=parser.getService(theserviceId);
					DataProcessorBlock exportBlock = parser.createBlock(service3);
					createblock(b,exportBlock,documentId,theserviceId,api,drawing,destination);
					b++;
					drawing.connectPorts(Block.getOutput("imported-data"),exportBlock.getInput("input-data"));
				}
				
			}
		}else{
			for(int x=0;x<partition.size()-1;x++){
				Block theblock=(Block) partition.get(x);
				String blockId=theblock.getBlockId();
				String serviceId=theblock.getserviceId();
				DataProcessorBlock serviceBlock;
				if(serviceId.equals("blocks-core-io-csvimport")){
					String documentId=null;
					IDynamicWorkflowService service = parser.getService("blocks-core-io-csvimport");
					serviceBlock = parser.createBlock(service);
					createblock(b,serviceBlock,documentId,serviceId,api,drawing,thePartitionNum);
					b++;
					
				}else{
					String documentId=resultInfo.get(blockId);
					String theserviceId="blocks-core-io-csvimport";
					IDynamicWorkflowService service = parser.getService("blocks-core-io-csvimport");
					DataProcessorBlock Block = parser.createBlock(service);
					createblock(b,Block,documentId,theserviceId,api,drawing,thePartitionNum);
					b++;
					IDynamicWorkflowService service1 = parser.getService(serviceId);
					serviceBlock = parser.createBlock(service1);
					createblock(b,serviceBlock,documentId,serviceId,api,drawing,thePartitionNum);
					b++;
					ArrayList<ArrayList<String>>inputs=thePartition.getInputs();
					String inputportName=null;
					for(int fx=0;fx<inputs.size();fx++){
						ArrayList<String>theinput=inputs.get(fx);
						if(blockId.equals(theinput.get(1))){
							inputportName=theinput.get(5);
						}
					}
					drawing.connectPorts(Block.getOutput("imported-data"), serviceBlock.getInput(inputportName));
				
					}
				    String documentId=null;
					String outputPort=null;
					String inputPort=null;
					ArrayList<String> export=getNextNode(blockId,partition, blockset, connections);
					if(!export.isEmpty()){
						for(int hh=0;hh<export.size();hh++){
							String theserviceName=export.get(hh);
							String theserviceId="blocks-core-io-csvexport";
							IDynamicWorkflowService service3=parser.getService(theserviceId);
							DataProcessorBlock exportBlock = parser.createBlock(service3);
							createblock(b,exportBlock,documentId,theserviceId,api,drawing,theserviceName);
							b++;
							for(int fx=0;fx<connections.size();fx++){
								ArrayList<String> connection=connections.get(fx);
								String startNode=connection.get(0);
								String endNode=connection.get(1);
								if(startNode.equals(blockId)&&endNode.equals(theserviceName)){
									outputPort=connection.get(4);
									inputPort=connection.get(5);
									break;
								}
							}
							drawing.connectPorts(serviceBlock.getOutput(outputPort),exportBlock.getInput(inputPort));
						}
						
					}	
			 }
				
		}
	    	JSONDrawingExporter exporter = new JSONDrawingExporter(drawing); 
	      IWorkflow wf = parser.createWorkflow(thePartitionNum, drawing);
	      IWorkflowParameterList params = (InkspotWorkflowParameterList)api.createObject(IWorkflowParameterList.XML_NAME);
		  IWorkflowInvocation invocation = api.executeWorkflow(wf, params);
		  while(invocation.getStatus().equals(IWorkflowInvocation.WORKFLOW_RUNNING) || invocation.getStatus().equals(IWorkflowInvocation.WORKFLOW_WAITING)){
		    	 try {
		    		 Thread.sleep(1000);
		    	 } catch (Exception e){}
		    	 System.out.println("Checking status");
		    	 invocation =api.getWorkflowInvocation(invocation.getInvocationId());
		     }
		  List<IObject> results = api.getFolderContents(invocation);
		  HashMap<String,ByteArrayOutputStream> result=new HashMap<String,ByteArrayOutputStream>();
		  for (IObject r : results) {
			  if (r instanceof IDocument) {
				  IDocument d = (IDocument) r;
				  String getName= d.getName();
				  String[] thename=getName.split("\\.");
				  ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				  api.download(d, outStream);
				  outStream.flush();
				  result.put(thename[0], outStream);
			  }
		  }
		return  result;
	}*/
	
	private ArrayList<Block> getEndBlockSet(String nextBlock,ArrayList<Object>Links,BlockSet blockset,ArrayList<Object>partition){
		
		ArrayList<Block> endBlockSet=new ArrayList<Block>();
		for(int xx=0;xx<Links.size();xx++){
			DataBlock theLink=(DataBlock) Links.get(xx);
			String sourceBlock=theLink.getsourceblockId();
			if(nextBlock.equals(sourceBlock)){
				String endblockId=theLink.getdestinationblockId();
				Block getendBlock=blockset.getBlock(endblockId);
				if(partition.contains(getendBlock)){
					endBlockSet.add(getendBlock);
				}
			 }
			}
			return endBlockSet;
	}
	
	/*the next block not in the same partition*/
	
	private ArrayList<String> getNextNode(String nextBlock,ArrayList<Object>partition,BlockSet blockset,ArrayList<ArrayList<String>> connections){
		ArrayList<String> nextNodeSet=new ArrayList<String>();
		for(int a=0;a<connections.size();a++){
			ArrayList<String> temp=connections.get(a);
			String source=temp.get(0);
			if(nextBlock.equals(source)){
				String end=temp.get(1);
				Block endNode=blockset.getBlock(end);
				if(!partition.contains(endNode)){
					nextNodeSet.add(end);
				}
			}
		}
		return nextNodeSet;
	}
	
	private void createblock(int b,DataProcessorBlock Block,String documentId,String serviceId,API api,DefaultDrawingModel drawing,String theServiceName ) throws Exception{
		
		if(theServiceName==null){
			theServiceName="out";
		}
		if(serviceId.equals("blocks-core-io-csvimport")){
			if(documentId==null){
				documentId="ff8080813a022d6e013a0268e5310159";
			}
			
			 IDocument doc = api.getDocument(documentId);
			 DocumentRecordWrapper wrapper = new DocumentRecordWrapper(doc);
			 Block.getEditableProperties().add("Source", wrapper);
		}
		
		if(serviceId.equals("blocks-core-io-csvexport")){
			Block.getEditableProperties().add("FileName", theServiceName+".csv");
	       
		}
		 drawing.addBlock(Block);
	        BlockModelPosition p = new BlockModelPosition();
	      	p.setHeight(60);
	        p.setWidth(60);
	        p.setTop(100);
	        p.setLeft(50+b*100);
	        drawing.getDrawingLayout().addLocationData(Block, p);
	}

	public IDocument saveDocument(IDocument document,String partitionNum, getConnection parser) throws APIInstantiationException, Exception{
		
		IDocument newDoc=(IDocument) parser.getAPI().createObject(partitionNum);
		newDoc.setDescription(document.getDescription());
		newDoc.setName(document.getName());
		newDoc=parser.getAPI().saveDocument(newDoc);
		String num=partitionNum.substring(9);
		int pNum=Integer.parseInt(num);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data.get(pNum-2));
		parser.getAPI().upload(newDoc, inputStream);
		inputStream.close();
		return newDoc;
	}
	
	public ArrayList<ArrayList<String>> getSource(String workflowId) throws Exception{
		ArrayList<ArrayList<String>> reSourceSet=new ArrayList<ArrayList<String>>();
		
		JSONObject dataObject= getWorkflowAsJsonObject( workflowId);
		JSONObject blocks = dataObject.getJSONObject("blocks");
		JSONArray blockArray = blocks.getJSONArray("blockArray");
	//	JSONArray sourceArray=blocks.getJSONArray("jsonValue");
        int blockCount = blocks.getInt("blockCount");
     
        String serviceId=null;
        for(int i=0;i<blockCount;i++){  
        	ArrayList<String> resource=new ArrayList<String>();
        	 serviceId=(String) blockArray.getJSONObject(i).get("serviceId");
        		ArrayList<String> temp=new ArrayList<String>();
        		
        	 if(serviceId.equals("blocks-core-io-csvimport")){
        	     JSONObject value= blockArray.getJSONObject(i);
        	     JSONObject property=value.getJSONObject("properties");
        		 JSONArray propertyArray=property.getJSONArray("propertyArray");
        		 int propertyCount=property.getInt("propertyCount");
       	     	 for(int a=0;a<propertyCount;a++){
       	     	
       	     		JSONObject jsonValue=propertyArray.getJSONObject(a);
       	     		if(jsonValue.has("jsonValue")){
       	     			JSONObject thesource=jsonValue.getJSONObject("jsonValue");
       	     			String id=thesource.getString("id");
       	     			String name=thesource.getString("name");
       	     			temp.add(id);
       	     			temp.add(name);
       	     		}
       	   	
       		 }
        		 String getBlockId=(String) blockArray.getJSONObject(i).get("guid");
        		 temp.add(getBlockId);
        		 resource=(ArrayList<String>) temp.clone();
        	 }
        	 if(!resource.isEmpty()){
        		 reSourceSet.add(resource);
        	 }
        }
		return reSourceSet;
	}
	
	public ArrayList<ArrayList<String>> getConnection(String workflowId) throws Exception{
		ArrayList<ArrayList<String>> connections=new ArrayList<ArrayList<String>>();
		 ArrayList<String> theconnection=new ArrayList<String>();
		JSONObject dataObject= getWorkflowAsJsonObject( workflowId);
		JSONObject connection=dataObject.getJSONObject("connections");
		JSONArray connectionArray=connection.getJSONArray("connectionArray");
		int connectionCount=connection.getInt("connectionCount");
		for(int i=0;i<connectionCount;i++){
			ArrayList<String> temp=new ArrayList<String>();
			String destinationBlockGuid= (String) connectionArray.getJSONObject(i).get("destinationBlockGuid");
			String sourceBlockGuid=(String) connectionArray.getJSONObject(i).get("sourceBlockGuid");
			String destinationPortName=(String) connectionArray.getJSONObject(i).get("destinationPortName");
			String sourcePortName=(String) connectionArray.getJSONObject(i).get("sourcePortName");
			String destinationName=getBlockName(destinationBlockGuid,workflowId);
			String sourceBlockName=getBlockName(sourceBlockGuid,workflowId);
			temp.add(sourceBlockGuid);
			temp.add(destinationBlockGuid);
			temp.add(sourceBlockName);
			temp.add(destinationName);
			temp.add(sourcePortName);
			temp.add(destinationPortName);
			theconnection=(ArrayList<String>) temp.clone();
			connections.add(theconnection);
		}
		
		return connections;
		
	}
	
	public String getBlockName(String BlockId,String workflowId) throws Exception{
		String name = null;
		JSONObject dataObject= getWorkflowAsJsonObject( workflowId);
		JSONObject blocks = dataObject.getJSONObject("blocks");
		JSONArray blockArray = blocks.getJSONArray("blockArray");
        int blockCount = blocks.getInt("blockCount");
       
        for (int i = 0; i < blockCount; i++)
        {
        	
         String getBlockId=(String) blockArray.getJSONObject(i).get("guid");
         if(getBlockId.equals(BlockId)){
        	  name=(String) blockArray.getJSONObject(i).get("label");
         }	
         
        }
		if(name!=null){
			return name;
		}else{
			throw new Exception("The Block is not included in this workflow");
		}
	}
	public String getBlockServiceId(String BlockId,String workflowId) throws Exception {
		
		JSONObject dataObject= getWorkflowAsJsonObject( workflowId);
		JSONObject blocks = dataObject.getJSONObject("blocks");
		JSONArray blockArray = blocks.getJSONArray("blockArray");
        int blockCount = blocks.getInt("blockCount");
        String serviceId=null;
        for (int i = 0; i < blockCount; i++)
        {
        	
         String getBlockId=(String) blockArray.getJSONObject(i).get("guid");
         if(getBlockId.equals(BlockId)){
        	  serviceId=(String) blockArray.getJSONObject(i).get("serviceId");
        	 
         }	
         
        }
		if(serviceId!=null){
			return serviceId;
		}else{
			throw new Exception("The Block is not included in this workflow");
		}
	}
}

