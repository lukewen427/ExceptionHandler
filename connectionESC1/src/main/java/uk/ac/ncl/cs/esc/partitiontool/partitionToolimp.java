package uk.ac.ncl.cs.esc.partitiontool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class partitionToolimp implements partitionTool{
	boolean Cycle;
	
	
	public ArrayList<Object> allOptions(Set<Block> blockSet,Set<DataBlock> DataBlocks,
			Set<Cloud> cloudSet) {
		
		Iterator<Cloud> theclouds=cloudSet.iterator();
		Random rand=new Random();
		int cloud0secure = 0;
		int cloud1secure=0;
		while(theclouds.hasNext()){
			Cloud getCloud=theclouds.next();
			String cloudname=getCloud.getCloudname();
			if(cloudname.equals("Cloud0")){
				cloud0secure=Integer.parseInt(getCloud.getCloudsecurityLevel());
				
			}else{
				cloud1secure=Integer.parseInt(getCloud.getCloudsecurityLevel());
				
			}
		}
		ArrayList<Object> theoptions=new  ArrayList<Object>();
		String url="/Users/zhenyuwen/git/ExceptionHandler/website/";
		File file=new File(url+"options.txt");
		 ArrayList<Object> options=new  ArrayList<Object>();
		 int cloudNumber=cloudSet.size();
			int BlockNumber=blockSet.size();
			int DataBlockNumber=DataBlocks.size();
			int theBlockNum=BlockNumber+DataBlockNumber;
		if(file.length()==0){
			/*the arraylist store all of the possible deployment options*/
			
			
			/*calculate the total number of the options*/
			int total=(int) Math.pow(cloudNumber, theBlockNum);
			int a=1;
			int b,f=3;
		
			ArrayList <Integer>option=new  ArrayList<Integer>();
			while(a<=total){
				int x=0;
				while(x<theBlockNum){
					f++;
					b=rand.nextInt(f);
					if(b%2==0){
						option.add(0);
					}
					if(b%2!=0){
						option.add(1);
					}
					x++;
				}
				if(option.size()<theBlockNum){
					 
					 option.clear();
				 }
				if(option.size()==theBlockNum){
					 if(!options.isEmpty()){
						 if(options.contains(option)){
							 option.clear();
						 }
						 if(!options.contains(option)){
							 if(!option.isEmpty()){
								 ArrayList option1=(ArrayList) (((ArrayList)option).clone());
									options.add(option1);
									option.clear();
									a++;
							 }
						 }
					 }else{
						 if(!option.isEmpty()){
							 ArrayList option1=(ArrayList) (((ArrayList)option).clone());
								options.add(option1);
								option.clear();
								a++;
						 }
					 }
				}
				
			}
			
			BufferedWriter bw;
			try {
				bw = new   BufferedWriter(new FileWriter(file));
				for(int ff=0;ff<options.size();ff++){
					ArrayList <Integer>theoption=(ArrayList<Integer>) options.get(ff);
				
						String write=theoption.toString();
						bw.write(write);
						bw.newLine();
				}
				bw.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			options= getOption(file);
		}
		
		HashMap<Object,Integer> singalOption=new HashMap<Object,Integer>();
		for(int i=0;i<options.size();i++){
			HashMap<Object,Integer> tempOption=new HashMap<Object,Integer>();
			ArrayList<Integer> temp=(ArrayList<Integer>) options.get(i);
			Iterator<Block> blocks=blockSet.iterator();
			boolean failed=false;
			int count=0;
			while(blocks.hasNext()){
				int Csercure=temp.get(count);
				Block getBlock=blocks.next();
				int Blocation=getBlock.getlocation();
				if(Csercure>=Blocation){
					tempOption.put(getBlock, Csercure);
					count++;
				}else{
					failed=true;
					break;
				}
			}
			
			Iterator<DataBlock> theDataBlocks=DataBlocks.iterator();
			while(failed==false && theDataBlocks.hasNext()){
				int Csercure=temp.get(count);
				DataBlock getDataBlock=theDataBlocks.next();
				int Blocation=getDataBlock.getDatalocation();
				if(Csercure>=Blocation){
					tempOption.put(getDataBlock, Csercure);
					count++;
				}else{
					break;
				}
			}
			if(tempOption.size()==theBlockNum){
				singalOption=(HashMap<Object, Integer>) tempOption.clone();
				theoptions.add(singalOption);
			}
		}
		return theoptions;
	}
	
	private ArrayList<Object> getOption(File file){
		ArrayList<Object>options=new ArrayList<Object>();
		try {
			FileInputStream fis=new FileInputStream(file);
			InputStreamReader isr=new InputStreamReader(fis, "UTF-8");
	        BufferedReader br = new BufferedReader(isr);
	        String line="";
	        String[] arrs=null;
	        
	        while ((line=br.readLine())!=null) {
	        	ArrayList<Integer> temp=new ArrayList<Integer>();
	            arrs=line.split(",");
	            for(int xf=0;xf<arrs.length;xf++){
	            	String b;
	            	b=String.valueOf(arrs[xf].charAt(1));
	            	int value=Integer.valueOf(b);
	            	temp.add(value);
	            }
	         
	            if(!temp.isEmpty()){
	            	options.add((ArrayList<Integer>)temp.clone());
	            }
	        }
	        br.close();
	        isr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return options;
	}
	
	/*check the rules: no read high and no write down*/
	public boolean workflowChecking (Set<Block> theBlockSet,ArrayList<ArrayList<String>> connections) {
		Boolean verified=true;
		Iterator<Block> getBlocks=theBlockSet.iterator();
		while(getBlocks.hasNext()){
			Block theBlock=getBlocks.next();
			String id=theBlock.getBlockId();
			int SBclearance;
			int SBlocation;
			int writeData;
			int readData;
			for(int a=0;a<connections.size();a++){
				ArrayList<String>connection=connections.get(a);
				String sourceBlockId=connection.get(0);
				String destinationBlockId=connection.get(1);
				if(id.equals(sourceBlockId)){
					SBlocation=theBlock.getlocation();
					writeData=Integer.valueOf(connection.get(6));
					if(SBlocation>writeData){
						verified=false;
						break;
					}
				}
				if(id.equals(destinationBlockId)){
					SBclearance=theBlock.getclearance();
					readData=Integer.valueOf(connection.get(6));
					if(SBclearance<readData){
						
						verified=false;
						break;
					}
				}
			}
		}

		return verified;
	}

	public HashMap<String, ArrayList<Object>> workflowPartition( ArrayList<Object>theOptionSet,
			ArrayList<ArrayList<String>> connections,BlockSet blockset,DataBlockSet databBlockSet) {
		HashMap<String, ArrayList<Object>> partitionMap=new HashMap<String, ArrayList<Object>>();
			
			ArrayList<String> startNodes=getInitialBlocks(connections);
			for(int a=0;a<theOptionSet.size();a++){
			//	System.out.println("Option"+(a+1));
				HashMap<Object,Integer> singalOption=(HashMap<Object, Integer>) theOptionSet.get(a);
				ArrayList<Object>option=getParitions(singalOption,connections, blockset,databBlockSet, 
											startNodes,new ArrayList<Object>(),new ArrayList<Object>());
			//	System.out.print(option);
				String OptionNum="Option"+(a+1);
				partitionMap.put(OptionNum, option);
	
		/*		for(int x=0;x<option.size();x++){
					ArrayList<Object>thepartition=(ArrayList<Object>) option.get(x);
					System.out.println(thepartition.get(0));
					for(int h=1;h<thepartition.size();h++){
						
						Object f=thepartition.get(h);
						if(f instanceof Block){
							System.out.print((((Block) f).getBlockId()));
							System.out.print("  ");
						}else{
							System.out.print(((DataBlock)f).getsourceblockId());
							System.out.print("*");
							System.out.print(((DataBlock)f).getdestinationblockId());
							System.out.print("  ");
						}
						
					}
					System.out.println("  ");
				}*/
			
			}
			
			// useless code
			
			/*for(int a=0;a<theOptionSet.size();a++){
		//		System.out.println("Partition"+(a+1));
				HashMap<Object,Integer> singalOption=(HashMap<Object, Integer>) theOptionSet.get(a);
	//			ArrayList<Object> singaloptiontemp=new ArrayList<Object>();
				ArrayList<Object> cloud0block=new ArrayList<Object>();
				ArrayList<Object> cloud0datablock=new ArrayList<Object>();
				ArrayList<Object> cloud1block=new ArrayList<Object>();
				ArrayList<Object> cloud1datablock=new ArrayList<Object>();
				Set<Object> keySet=singalOption.keySet();
				Iterator<Object> blocks=keySet.iterator();
				while(blocks.hasNext()){
					Object theblock=blocks.next();
					if(theblock instanceof Block){
						Block getBlock=(Block) theblock;
						int cloud=singalOption.get(getBlock);
						if(cloud==0){
							cloud0block.add(getBlock);
						}else{
							cloud1block.add(getBlock);
						}
							
					}else{
						DataBlock getDatablock=(DataBlock) theblock;
							int cloud=singalOption.get(getDatablock);
							if(cloud==0){
								cloud0datablock.add(getDatablock);
							}else{
								cloud1datablock.add(getDatablock);
							}
					}
				}
				ArrayList<Object> cloud0Partition=new ArrayList<Object>();
				ArrayList<Object> cloud0singalPart=new ArrayList<Object>();
				if(cloud0block.isEmpty()){
					if(!cloud0datablock.isEmpty()){
						for(int x=0;x<cloud0datablock.size();x++){
							ArrayList<Object> temp=new ArrayList<Object>();
							DataBlock thedatablock=(DataBlock)cloud0datablock.get(x);
							temp.add(thedatablock);
							cloud0singalPart=(ArrayList<Object>) temp.clone();
							if(!cloud0singalPart.isEmpty()){
								cloud0singalPart.add(0);
								cloud0Partition.add(cloud0singalPart);
							}
						}
					}
				}else{
					ArrayList<Object> addedBlocks=new ArrayList<Object>();
					for(int x=0;x<cloud0block.size();x++){
						ArrayList<Object> temp=new ArrayList<Object>();
						Block theblock=(Block) cloud0block.get(x);
						temp.add(theblock);
						String id=theblock.getBlockId();
						for(int f=0;f<cloud0datablock.size();f++){
							DataBlock thedatablock=(DataBlock) cloud0datablock.get(f);
							String datasource=thedatablock.getsourceblockId();
							String datadestination=thedatablock.getdestinationblockId();
							if(id.equals(datasource)){
								temp.add(thedatablock);
								addedBlocks.add(thedatablock);
							}
							if(id.equals(datadestination)){
								temp.add(thedatablock);
								addedBlocks.add(thedatablock);
							}
						}
						cloud0singalPart=(ArrayList<Object>) temp.clone();
						if(!cloud0singalPart.isEmpty()){
							cloud0singalPart.add(0);
							cloud0Partition.add(cloud0singalPart);
						}	
					}
					if(!cloud0datablock.isEmpty()){
						for(int xf=0;xf<cloud0datablock.size();xf++){
							ArrayList<Object> temp=new ArrayList<Object>();
							DataBlock tempBlock=(DataBlock) cloud0datablock.get(xf);
							if(!addedBlocks.contains(tempBlock)){
								temp.add(tempBlock);
								cloud0singalPart=(ArrayList<Object>) temp.clone();
								cloud0singalPart.add(0);
								cloud0Partition.add(cloud0singalPart);
							}
						}
					}
				}	
				
				
				ArrayList<Object> cloud1Partition=new ArrayList<Object>();
				ArrayList<Object> cloud1singalPart=new ArrayList<Object>();
				if(cloud1block.isEmpty()){
					if(!cloud1datablock.isEmpty()){
						for(int x=0;x<cloud1datablock.size();x++){
							ArrayList<Object> temp=new ArrayList<Object>();
							DataBlock thedatablock=(DataBlock)cloud1datablock.get(x);
							temp.add(thedatablock);
							cloud1singalPart=(ArrayList<Object>) temp.clone();
							if(!cloud1singalPart.isEmpty()){
								cloud1singalPart.add(1);
								cloud1Partition.add(cloud1singalPart);
							}
						}
					}
				}else{
					ArrayList<Object> addedBlocks=new ArrayList<Object>();
					for(int x=0;x<cloud1block.size();x++){
						ArrayList<Object> temp=new ArrayList<Object>();
						Block theblock=(Block) cloud1block.get(x);
						temp.add(theblock);
						String id=theblock.getBlockId();
						for(int f=0;f<cloud1datablock.size();f++){
							DataBlock thedatablock=(DataBlock) cloud1datablock.get(f);
							String datasource=thedatablock.getsourceblockId();
							String datadestination=thedatablock.getdestinationblockId();
							if(id.equals(datasource)){
								temp.add(thedatablock);
								addedBlocks.add(thedatablock);
							}
							if(id.equals(datadestination)){
								temp.add(thedatablock);
								addedBlocks.add(thedatablock);
							}
						}
						cloud1singalPart=(ArrayList<Object>) temp.clone();
						if(!cloud1singalPart.isEmpty()){
							cloud1singalPart.add(1);
							cloud1Partition.add(cloud1singalPart);
						}	
					}
					if(!cloud1datablock.isEmpty()){
						for(int xf=0;xf<cloud1datablock.size();xf++){
							ArrayList<Object> temp=new ArrayList<Object>();
							DataBlock tempBlock=(DataBlock) cloud1datablock.get(xf);
							if(!addedBlocks.contains(tempBlock)){
								temp.add(tempBlock);
								cloud1singalPart=(ArrayList<Object>) temp.clone();
								cloud1singalPart.add(1);
								cloud1Partition.add(cloud1singalPart);
							}
						}
					}
				}
				ArrayList<Object> finalcloud0Partition=null;
				if(cloud0Partition.isEmpty()){
					finalcloud0Partition=null;
				}else{
			//		System.out.println(cloud0Partition);
					finalcloud0Partition=cloudCombination(cloud0Partition);
			//		System.out.println("afterfffffff");
			//		System.out.println(finalcloud0Partition);
			//		System.out.println("Cloud1");
				}
				
				ArrayList<Object> finalcloud1Partition=null;
				if(cloud1Partition.isEmpty()){
					 finalcloud1Partition=null;
				}else{
			//		System.out.println(cloud1Partition);
					 finalcloud1Partition=cloudCombination(cloud1Partition);
			//		 System.out.println("afterhhhhh");
			//		 System.out.println(finalcloud1Partition);
				}
				
				
				
				ArrayList<Object> temp1=new ArrayList<Object>();
				if(finalcloud0Partition!=null){
					for(int sf=0;sf<finalcloud0Partition.size();sf++){
						ArrayList<Object> getsingalPartition=(ArrayList<Object>) finalcloud0Partition.get(sf);
						temp1.add(getsingalPartition);
					}
				}
				if(finalcloud1Partition!=null){
					for(int sf=0;sf<finalcloud1Partition.size();sf++){
						ArrayList<Object> getsingalPartition=(ArrayList<Object>) finalcloud1Partition.get(sf);
						temp1.add(getsingalPartition);
					}
				}
				
				singaloption=(ArrayList<Object>) temp1.clone();
			boolean valid=partitionSecurity(singaloption);
			boolean transfervalid=transferCheck(singaloption,blockset);
			if(valid==true&&transfervalid==true){
				String partitionNum="Partition"+(partCount);
				partitionMap.put(partitionNum, singaloption);
				partCount++;
			  }	
			}*/
			
			return partitionMap;
	}

	private  ArrayList<Object> getParitions(HashMap<Object,Integer> singalOption,
								ArrayList<ArrayList<String>> connections,BlockSet blockset,DataBlockSet databBlockSet,
										ArrayList<String> waitingNodes,ArrayList<Object> visited,ArrayList<Object>option){
		// option is used to store partitions
		if(waitingNodes.isEmpty()){
		
			return option;
		}
	
		ArrayList<String> offspringNodes=new ArrayList<String>();
		ArrayList<Object> collection=new ArrayList<Object>();
		for(int a=0;a<waitingNodes.size();a++){
			String Node=waitingNodes.get(a);
			Block block=blockset.getBlock(Node);
	
			int cloudblock=singalOption.get(block);
			if(visited.contains(block)){
				
			}else{
				visited.add(block);
				ArrayList<Object> partition=new ArrayList<Object>();
			
				if(option.isEmpty()){
					partition.add(cloudblock);
					partition.add(block);
				}else{
				//	ArrayList<Object> newPartition=new ArrayList<Object>();
					for(Object party:option){
						ArrayList<Object> theParty=(ArrayList<Object>)party;
						if(theParty.contains(block)){
							partition=(ArrayList<Object>) theParty.clone();
							option.remove(party);
							break;
						}
					}
					if(partition.isEmpty()){
						partition.add(cloudblock);
						partition.add(block);
					}
					
				}
				
				for(ArrayList<String> connection:connections){
					String sourceNode=connection.get(0);
					String destinationNode=connection.get(1);
					if(Node.equals(sourceNode)){
						DataBlock link= databBlockSet.getDataBlock(Node, destinationNode);
						int linkcloud=singalOption.get(link);
						Block offspringblock=blockset.getBlock(destinationNode);
						int offspringCloud=singalOption.get(offspringblock);
						if(cloudblock==linkcloud){
							// the data is the same partition with parents block
							partition.add(link);
						
							if(linkcloud==offspringCloud){
								//  data and offspringblock in same partition
								partition.add(offspringblock);
				
							}else{
								// data and offspring block is not in same partition 
								ArrayList<Object> newPartition=new ArrayList<Object>();
								newPartition.add(offspringCloud);
								newPartition.add(offspringblock);	
								collection.add(new ArrayList<Object>((ArrayList<Object>)newPartition.clone()));
							//	option.add(new ArrayList<Object>((ArrayList<Object>)newPartition.clone()));
				
							}
							
						}else{
							ArrayList<Object> newPartition=new ArrayList<Object>();
							newPartition.add(linkcloud);
							newPartition.add(link);
							if(linkcloud==offspringCloud){
								newPartition.add(offspringblock);
								collection.add(new ArrayList<Object>((ArrayList<Object>)newPartition.clone()));
							//	option.add(new ArrayList<Object>((ArrayList<Object>)newPartition.clone()));
				
							}else{
								ArrayList<Object> thenewPartition=new ArrayList<Object>();
								thenewPartition.add(offspringCloud);
								thenewPartition.add(offspringblock);	
								
								collection.add(new ArrayList<Object>((ArrayList<Object>)newPartition.clone()));
							//	option.add(new ArrayList<Object>((ArrayList<Object>)newPartition.clone()));
							//	option.add(new ArrayList<Object>((ArrayList<Object>)thenewPartition.clone()));
							}
							
						}
						if(!offspringNodes.contains(destinationNode)){
							offspringNodes.add(destinationNode);
						}
					}
					
				}
				
				collection.add(new ArrayList<Object>((ArrayList<Object>)partition.clone()));
			//	option.add(new ArrayList<Object>((ArrayList<Object>)partition.clone()));
			}
			
		}
	
			combination(option,collection);
			return getParitions(singalOption, connections,blockset, databBlockSet,offspringNodes,
					visited,option);
	}
	
	private void combination(ArrayList<Object>option,ArrayList<Object> collection){
	//	System.out.println(collection.size());
		ArrayList<Object> temp=new ArrayList<Object>();
		for(int a=0;a<collection.size();a++){
			ArrayList<Object>party=(ArrayList<Object>) collection.get(a);
			if(temp.isEmpty()){
				temp.add(party.clone());
			}else{
				boolean iscontain=false;
				for(int i=0;i<temp.size();i++){
					ArrayList<Object>partition=(ArrayList<Object>) temp.get(i);
					if(checkContain(party,partition)){
						getConbine(party,partition);
						iscontain=true;
					}
				}
				if(iscontain==false){
					temp.add(party.clone());
				}
			}
		}
		
		// adding to option
		for(int h=0;h<temp.size();h++){
			ArrayList<Object> partition=(ArrayList<Object>) temp.get(h);
			if(!partition.isEmpty()){
				option.add(partition.clone());
			}
			
		}
		
	}
	
/*	private ArrayList<Object> copy(ArrayList<Object>original){
		ArrayList<Object> copy=new ArrayList<Object>();
		for(int a=0;a<original.size();a++){
			Object block=original.get(a);
			copy.add(block);
		}
		return copy;
	}*/
	
	private void getConbine(ArrayList<Object>party,ArrayList<Object>party2){
	
			for(int a=0;a<party.size();a++){
				Object block=party.get(a);
				if(!party2.contains(block)){
					party2.add(block);
				}
			}
	}
	private boolean checkContain(ArrayList<Object>party,ArrayList<Object>party2){
		
			for(int a=0;a<party2.size();a++){
				Object block=party2.get(a);
				if(party.contains(block)){
					return true;
				}
			}
		return false;
	}
	private boolean partitionSecurity(ArrayList<Object>singaloption){
		boolean validPartition=true;
		for(int a=0;a<singaloption.size();a++){
			ArrayList<Object> singalPartition=(ArrayList<Object>) singaloption.get(a);
		
			int securityLevel=(Integer) singalPartition.get(singalPartition.size()-1);
			for(int i=0;i<singalPartition.size()-1;i++){
				Object block=singalPartition.get(i);
				if(block instanceof Block){
					int blocksecurity=((Block) block).getlocation();
					if(blocksecurity>securityLevel){
						validPartition=false;
						break;
					}
				}else{
					int datablocksecurity=((DataBlock)block).getDatalocation();
					if(datablocksecurity>securityLevel){
						validPartition=false;
						break;
					}
				}
				if(validPartition==false){
					break;
				}
			}
		}
		return validPartition;
	}
	
	private boolean transferCheck(ArrayList<Object>singaloption,BlockSet blockset){
		boolean validtransfer=true;
		for(int a=0;a<singaloption.size();a++){
			ArrayList<Object>tempPartition=(ArrayList<Object>) singaloption.get(a);
				for(int i=0;i<tempPartition.size();i++){
					Object block=tempPartition.get(i);
					if(block instanceof DataBlock){
						String sourceid=((DataBlock) block).getsourceblockId();
						String destinationid=((DataBlock) block).getdestinationblockId();
						Block getsourceBlock= blockset.getBlock(sourceid);
						Block getdestinationBlock=blockset.getBlock(destinationid);
						if(tempPartition.contains(getsourceBlock)&&!tempPartition.contains(getdestinationBlock)){
							int currentSecurity=getsourceBlock.getlocation();
							for(int f=0;f<singaloption.size();f++){
								ArrayList<Object>compare=(ArrayList<Object>)singaloption.get(f);
								if(compare.contains(getdestinationBlock)){
									int nextSecurity=(Integer) compare.get(0);
									if(currentSecurity>nextSecurity){
										validtransfer=false;
										break;
									}
								}
							}
						}
					}
					if(validtransfer==false){
						break;
					}
				}
				if(validtransfer==false){
					break;
				}
		}
		return validtransfer;
	}
	
	
	public ArrayList<String> getInitialBlocks(ArrayList<ArrayList<String>> connections) {
		
		ArrayList<String> startBlocks=new ArrayList<String>();
		for(ArrayList<String> connection:connections){
			boolean isStartNode=true;
			String sourceNode=connection.get(0);
			for(ArrayList<String> thelink:connections){
				String destinationNode=thelink.get(1);
				if(sourceNode.equals(destinationNode)){
					isStartNode=false;
					break;
				}
			}
			if(isStartNode){
				startBlocks.add(sourceNode);
			}
		}
		return startBlocks;
	}

	public HashMap<String, ArrayList<Object>> tranferSecurityCheck(
			HashMap<String, ArrayList<Object>> options, BlockSet blockset) {
		HashMap<String,ArrayList<Object>> validOptions=new HashMap<String,ArrayList<Object>> ();
		Iterator<String> keySet=options.keySet().iterator();
		int a=1;
		while(keySet.hasNext()){
			ArrayList<Object> option=options.get(keySet.next());
			if(transferCheck(option,blockset)){
				String Name="Option"+a;
				validOptions.put(Name, option);
				a++;
			}
		}
		return validOptions;
	}
/* this method is used to check the cycle in the options, if there is cycle in a option which mean it is not atomic.
 *  We use DFT to trversal each node, when the   */
	public HashMap<String, ArrayList<Object>> cycleBreak( HashMap<String, ArrayList<Object>> Maps) {
		HashMap<String, ArrayList<Object>> validMap=new HashMap<String, ArrayList<Object>>();
		Iterator<String> keySet=Maps.keySet().iterator();
		while(keySet.hasNext()){
			String optionName=keySet.next();
			ArrayList<Object> option=Maps.get(optionName);
			ArrayList<Object> connection=(ArrayList<Object>) option.get(1);
			
			if(!cycleCheck(connection)){
				validMap.put(optionName, option);
			}
		}
		return validMap;
	}
	// to check are there any cycle includ in the partitioned workflow 
	private boolean cycleCheck(ArrayList<Object> connection){
		
		ArrayList<Integer> StartNodes=new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> links=new ArrayList<ArrayList<Integer>>();
		for(int a=0;a<connection.size();a++){
			boolean isStart=true;
			  ArrayList<Object> link=(ArrayList<Object>) connection.get(a);
			  ArrayList<Integer> partitionLink=(ArrayList<Integer>) link.get(0);
			  int source=partitionLink.get(0);
			  links.add(partitionLink);
			  for(int i=0;i<connection.size();i++){
				  ArrayList<Object> templink=(ArrayList<Object>) connection.get(i);
				  ArrayList<Integer> temppartitionLink=(ArrayList<Integer>) link.get(0);
				  int destination=temppartitionLink.get(1);
				  if(source==destination){
					  isStart=false;
				  }
			  }
			  if(isStart){
				  StartNodes.add(source);
			  }
		}
		
		boolean isCycle = false;
		for(int node:StartNodes){
			DFT(node,links,new ArrayList<Integer>(),new ArrayList<Integer>());
			isCycle=isCycle();
			if(isCycle){
				break;
			}
		}
		
		return isCycle;
	}
	
	
	private void DFT(int startNode,ArrayList<ArrayList<Integer>> links,ArrayList<Integer> visited,ArrayList<Integer> running){
		
		visited.add(startNode);
		running.add(startNode);
		for(ArrayList<Integer> link:links){
			int source=link.get(0);
			int destination=link.get(1);
			if(source==startNode){
				if(!visited.contains(destination)){
					DFT(destination,links,visited,running);
				}else if(running.contains(destination)){
					hasCycle(true);
					return;
				}
			}
		}
		for(int j=0;j<running.size();j++){
			if(running.get(j)==startNode){
				running.remove(j);
				break;
			}
		}
		if(running.isEmpty()){
			hasCycle(false);
		}
	}
	private boolean isCycle(){
		return Cycle;
	}
	private void hasCycle(boolean iscycle){
		this.Cycle=iscycle;
	}
	// return the initial partition of a paritioned workflow 
	private ArrayList<Object> initialPartition(ArrayList<String>startNodes,BlockSet blockset,ArrayList<Object> option){
		ArrayList<Object> startPartitions=new ArrayList<Object>();
		for(String node:startNodes){
			Block block=blockset.getBlock(node);
			for(int a=0;a<option.size();a++){
				ArrayList<Object>partition=(ArrayList<Object>) option.get(a);
				if(partition.contains(block)){
					startPartitions.add(partition);
				}
			}
		}
		return startPartitions;
	}

	/* the map has stored the partitions and the links of each partition of the options
	ArrayList<Object> include two sub objects: HashMap<String, Object> and ArrayList<String>
	HashMap<String, Object> : String means the order Object is the partitions 
	ArrayList<String> the links of each partition 
	*/
	public HashMap<String, ArrayList<Object>> Maps(HashMap<String,ArrayList<Object>>options,
			ArrayList<ArrayList<String>> connections, BlockSet blockset,
			DataBlockSet dataBlockSet) {
		HashMap<String,ArrayList<Object>> Map=new HashMap<String,ArrayList<Object>> ();
		Iterator<String> keySet=options.keySet().iterator();
	
		while(keySet.hasNext()){
			String Name=keySet.next();
			ArrayList<Object> option=options.get(Name);
		//	System.out.println(Name);
		//	System.out.println(option);
			ArrayList<Object> order= linkedPartitions(option, connections,blockset,dataBlockSet);
			
			Map.put(Name,order);
		
		}
		return Map;
	}
	
//	Object :ArrayList<ArrayList<Object>> to store the links of each partition and HashMap to store the paritions 
	private ArrayList<Object>linkedPartitions(ArrayList<Object>option,ArrayList<ArrayList<String>> connections, BlockSet blockset,
			DataBlockSet dataBlockSet){
		
		ArrayList<String>startNodes=getInitialBlocks(connections);
		ArrayList<Object> startPartitions=initialPartition(startNodes,blockset, option);
		ArrayList<Object> Visited = new ArrayList<Object>();
		HashMap<Integer, Object> partitions=new HashMap<Integer, Object> ();
		if(partitions.isEmpty()){
			int number=partitions.size()+1;
			for(int a=0;a<startPartitions.size();a++){
				partitions.put(number+a, startPartitions.get(a));
			//	System.out.println(number+a);
			//	System.out.println(startPartitions.get(a));
				Visited.add(startPartitions.get(a));
			}
		}
		
		ArrayList<Object>Map=traversal(option,connections,blockset,dataBlockSet,startPartitions,
									new ArrayList<ArrayList<Object>>(),partitions,new ArrayList<Object>(),Visited);
		
		
		return Map;
	}
	
	private ArrayList<Object>traversal(ArrayList<Object>option,ArrayList<ArrayList<String>> connections, BlockSet blockset,DataBlockSet dataBlockSet,
			ArrayList<Object> parents,ArrayList<ArrayList<Object>>partitionLinks,HashMap<Integer, Object> partitions,ArrayList<Object> Map,ArrayList<Object>Visited){
		
		if(parents.isEmpty()){
			Map.add(partitions);
			Map.add(partitionLinks);
			return Map;
		}else{
			ArrayList<Object> offspringPartitions=new ArrayList<Object>();
			
			for(Object node:parents){
				ArrayList<Object> theNode=(ArrayList<Object>)node;
				int name=getkey(theNode,partitions);
				for(int a=1;a<theNode.size();a++){
					Object block=theNode.get(a);
					 
					 if(block instanceof DataBlock){
						
						  String sourceid=((DataBlock) block).getsourceblockId();
						  String destinationid=((DataBlock) block).getdestinationblockId();
						  Block getdestinationBlock=blockset.getBlock(destinationid);
						  
						  if(!theNode.contains(getdestinationBlock)){
							  for(int h=0;h<option.size();h++){
								  ArrayList<Object> thepartition=(ArrayList<Object>) option.get(h);
								  if(thepartition.contains(getdestinationBlock)){
									  ArrayList<Object> singalLink=new ArrayList<Object>();
									  ArrayList<String>link=findconnection(sourceid,destinationid,connections);
									
									  if(isVisited(Visited,thepartition)){
										  
										  int theName=getkey(thepartition,partitions);
										  if(name!=theName){
											  ArrayList<Integer> thelink=new ArrayList(Arrays.asList(name,theName));
									
											  singalLink.add(thelink);
											  singalLink.add(link);
											  
												  partitionLinks.add(new ArrayList<Object>(singalLink));
											  
										  }
									  }else{
										  if(!isadded(offspringPartitions,thepartition)){
											  offspringPartitions.add(new ArrayList<Object>(thepartition));	 
											  Visited.add(new ArrayList<Object>(thepartition));
											  int named=partitions.size()+1;
											  partitions.put(named, thepartition);
										//	  System.out.println(named);
										//		System.out.println(thepartition);
											  ArrayList<Integer> thelink=new ArrayList(Arrays.asList(name,named));
											
											 
											  singalLink.add(thelink);
											  singalLink.add(link);
											  
											  if(!singalLink.isEmpty()){
												 
													  partitionLinks.add(new ArrayList<Object>(singalLink));
												  
											  }
										  }
										
									  }
									
								  }
							  }
						  }
					 }else{
							String blockid=((Block)block).getBlockId();
							
						for (int i = 0; i < connections.size(); i++) {
							ArrayList<String> connection = connections.get(i);
							String sourceid = connection.get(0);
							String destinationid = connection.get(1);
						//	Block getdestinationBlock = blockset.getBlock(destinationid);
							if(sourceid.equals(blockid)){
								ArrayList<Object> singalLink = new ArrayList<Object>();
								ArrayList<String> link = findconnection(sourceid,
										destinationid, connections);
								DataBlock data = dataBlockSet.getDataBlock(
										sourceid, destinationid);
								if (!theNode.contains(data)) {
									for (int h = 0; h < option.size(); h++) {
										ArrayList<Object> thepartition = (ArrayList<Object>) option.get(h);
										if (thepartition.contains(data)) {

											if (isVisited(Visited, thepartition)) {
												int theName = getkey(thepartition,
														partitions);
												if (name != theName) {
													ArrayList<Integer> thelink = new ArrayList(
															Arrays.asList(name,theName));

													singalLink.add(thelink);
													singalLink.add(link);

													partitionLinks.add(new ArrayList<Object>(singalLink));

												}

											}else{
												 if(!isadded(offspringPartitions,thepartition)){
													  offspringPartitions.add(new ArrayList<Object>(thepartition));	 
													  Visited.add(new ArrayList<Object>(thepartition));
													  int named=partitions.size()+1;
													  partitions.put(named, thepartition);
											//		  System.out.println(named);
												//		System.out.println(thepartition);
													  ArrayList<Integer> thelink=new ArrayList(Arrays.asList(name,named));
													
											
													  singalLink.add(thelink);
													  singalLink.add(link);
													  if(!singalLink.isEmpty()){
															  partitionLinks.add(new ArrayList<Object>(singalLink));
														  
													  }
												  }

											}
										}
									}
								}
							}
						}

					   }
					}
			}
	//		System.out.println( partitionLinks);
			return traversal(option,connections,blockset,dataBlockSet,offspringPartitions,partitionLinks,partitions,Map,Visited);
		}
	
	}
	
	private boolean isLinked(ArrayList<ArrayList<Object>>partitionLinks,ArrayList<String>link){
		for(int a=0;a<partitionLinks.size();a++){
			ArrayList<Object> singalLink=partitionLinks.get(a);
			ArrayList<String> temp=(ArrayList<String>) singalLink.get(1);
			if((link.get(0).equals(temp.get(0)))&&(link.get(1).equals(temp.get(1)))){
				
				return true;
			}
		}
		return false;
	}
	
	private boolean isadded(ArrayList<Object>offspring,ArrayList<Object>thepartition){
		for(Object partition:offspring){
			ArrayList<Object> temp=(ArrayList<Object>) partition;
			Object node=thepartition.get(1);
			if(temp.contains(node)){
				return true;
			}
		}
		return false;
	}
	private boolean isVisited(ArrayList<Object>Visited, ArrayList<Object> thepartition){
		
		for(Object partition:Visited){
			ArrayList<Object> temp=(ArrayList<Object>) partition;
			Object node=thepartition.get(1);
			if(temp.contains(node)){
				return true;
			}
		}
		return false;
	}
	private int getkey(ArrayList<Object> partition,HashMap<Integer, Object> partitions){
		for(int i: partitions.keySet()){
			Object node=partition.get(1);
			ArrayList<Object> singalPartition=(ArrayList<Object>) partitions.get(i);
			if(singalPartition.contains(node)){
				return i;
			}
		}
		return 0;
	}

	
	private ArrayList<String> findconnection(String sourceid,String destinationid,ArrayList<ArrayList<String>> connections){
		ArrayList<String> getConnection=new ArrayList<String>();
		for(ArrayList<String>connection:connections){
			if(sourceid.equals(connection.get(0))&&destinationid.equals(connection.get(1))){
				getConnection=(ArrayList<String>) connection.clone();
			}
		}
		return getConnection;
	}
	
	public HashMap<String,ArrayList<Object>> getStartNodes(HashMap<String, ArrayList<Object>> validMap){
		HashMap<String,ArrayList<Object>> startNodes=new HashMap<String, ArrayList<Object>>();
		
		Iterator<String> partitionNames=validMap.keySet().iterator();
		while(partitionNames.hasNext()){
			ArrayList<Object> Nodes=new ArrayList<Object>();
			String partitionName=partitionNames.next();
			
			ArrayList<Object> option=validMap.get(partitionName);
			HashMap<Integer,ArrayList<Object>> partitions=(HashMap<Integer, ArrayList<Object>>) option.get(0);
			ArrayList<Object> links=(ArrayList<Object>) option.get(1);
			ArrayList<Integer> startpartition=new ArrayList<Integer>();
			if(links.isEmpty()){
				 startpartition.add(partitions.keySet().iterator().next());
			}else{
				 startpartition=getStartPartition(links);
			}
			
			for(int a=0;a<startpartition.size();a++){
				int theNode=startpartition.get(a);
				ArrayList<Object> singalPartition=partitions.get(theNode);
				
				if(Nodes.isEmpty()){
					Nodes.add(singalPartition);
				}else{
					if(!isIncluded(Nodes,singalPartition)){
						Nodes.add(singalPartition);
					}
				}
			}
			
			startNodes.put(partitionName, new ArrayList<Object>((ArrayList<Object>) Nodes.clone()));
		}
		return startNodes;
		
	}
	
	private boolean isIncluded(ArrayList<Object> Nodes,ArrayList<Object> singalPartition){
		boolean isContain=true;
		for(int a=0;a<Nodes.size();a++){
			ArrayList<Object> temp=(ArrayList<Object>) Nodes.get(a);
			if(temp.size()==singalPartition.size()){
				for(int i=0;i<temp.size();i++){
					if(!(temp.get(i)).equals(singalPartition.get(i))){
						isContain=false;
						break;
					}
				}
				if(isContain){
					return true;
				}
			}
		}
		return false;
	}
	
	private ArrayList<Integer> getStartPartition(ArrayList<Object> links){
		ArrayList<Integer> startNodes=new ArrayList<Integer>();
		
		for(int a=0;a<links.size();a++){
			ArrayList<Object> link=(ArrayList<Object>) links.get(a);
			ArrayList<Integer>pLink=(ArrayList<Integer>) link.get(0);
			int source=pLink.get(0);
			boolean isStart=true;
			for(int i=0;i<links.size();i++){
				ArrayList<Object> linktemp=(ArrayList<Object>) links.get(i);
				ArrayList<Integer> pLinktemp=(ArrayList<Integer>) linktemp.get(0);
				int destination=pLinktemp.get(1);
				if(source==destination){
					isStart=false;
					break;
				}
			}
			
			if(isStart){
				startNodes.add(source);
			}
		}
		return startNodes;
	}
	public HashMap<String,ArrayList<Object>> getEndNodes(HashMap<String, ArrayList<Object>> validMap){
		HashMap<String,ArrayList<Object>> endNodes=new HashMap<String, ArrayList<Object>>();
		
		Iterator<String> partitionNames=validMap.keySet().iterator();
		
		while(partitionNames.hasNext()){
			ArrayList<Object> Nodes=new ArrayList<Object>();
			String partitionName=partitionNames.next();
			ArrayList<Object> option=validMap.get(partitionName);
			HashMap<Integer,ArrayList<Object>> partitions=(HashMap<Integer, ArrayList<Object>>) option.get(0);
			ArrayList<Object> links=(ArrayList<Object>) option.get(1);
			if(links.isEmpty()){
				Iterator<Integer> node=partitions.keySet().iterator();
				while(node.hasNext()){
					int name=node.next();
					Nodes.add(partitions.get(name));
				}
			}else{
				ArrayList<Integer> endpartition=getEndPartition(links);
				for(int a=0;a<endpartition.size();a++){
					int theNode=endpartition.get(a);
					ArrayList<Object> singalPartition=partitions.get(theNode);
					Nodes.add(singalPartition);
				}
			}
			
			endNodes.put(partitionName, new ArrayList<Object>((ArrayList<Object>) Nodes.clone()));
		}
		return endNodes;
		
	}
	
	private ArrayList<Integer> getEndPartition(ArrayList<Object> links){
		ArrayList<Integer> endNodes=new ArrayList<Integer>();
		for(int a=0;a<links.size();a++){
			ArrayList<Object> link=(ArrayList<Object>) links.get(a);
			ArrayList<Integer>pLink=(ArrayList<Integer>) link.get(0);
			int destination=pLink.get(1);
			boolean isEnd=true;
			for(int i=0;i<links.size();i++){
				ArrayList<Object> linktemp=(ArrayList<Object>) links.get(i);
				ArrayList<Integer> pLinktemp=(ArrayList<Integer>) linktemp.get(0);
				int source=pLinktemp.get(0);
				if(source==destination){
					isEnd=false;
					break;
				}
			}
			
			if(isEnd){
				if(!endNodes.contains(destination)){
					endNodes.add(destination);
				}
				
			}
		}
		
		return endNodes;
	}

	public HashMap<String, ArrayList<Object>> additionalOption(HashMap<String, ArrayList<Object>> validMap,HashMap<String,ArrayList<Object>> startNodes) {
		Iterator <String> keys=validMap.keySet().iterator();
		
		while(keys.hasNext()){
			 HashMap<String, ArrayList<Object>> backup=new HashMap<String,ArrayList<Object>>();
			String keyName=keys.next();
			ArrayList<Object> option=validMap.get(keyName);
			HashMap<Integer,ArrayList<Object>> partitions=(HashMap<Integer,ArrayList<Object>>) option.get(0);
			ArrayList<Object> connects=(ArrayList<Object>) option.get(1);
			ArrayList<Object> startNode=startNodes.get(keyName);
			ArrayList<Integer> theStarts=new ArrayList<Integer>();
			for(int a=0;a<startNode.size();a++){
				ArrayList<Object> node=(ArrayList<Object>) startNode.get(a);
				theStarts.add((Integer)node.get(0));
			}
			Traversal(partitions,connects,theStarts,backup,validMap,keyName);
		
		}
		return null;
	}
	
	private void Traversal(HashMap<Integer,ArrayList<Object>> partitions,ArrayList<Object> connects,ArrayList<Integer> startNode, 
			HashMap<String, ArrayList<Object>> backup,HashMap<String, ArrayList<Object>> validMap,String keyName){
		ArrayList<Integer> endNode=new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> links=new ArrayList<ArrayList<Integer>>();
		for(int a=0;a<startNode.size();a++){
			int partitionName=startNode.get(a);
			for(int i=0;i<connects.size();i++){
				ArrayList<Object> link=(ArrayList<Object>) connects.get(i);
				ArrayList<Integer> PLink=(ArrayList<Integer>) link.get(0);
				if(partitionName==PLink.get(0)){
					int nextNode=PLink.get(1);
					if(!endNode.contains(nextNode)){
						links.add(PLink);
						endNode.add(nextNode);
					}
				}
			}
		}
		
		if(endNode.isEmpty()){
			return;
		}else{
			findNewNode(links,partitions,validMap,backup,startNode,keyName);
			Traversal(partitions,connects,endNode,backup,validMap,keyName);
		}
	}
	
	private void findNewNode(ArrayList<ArrayList<Integer>> links,HashMap<Integer,ArrayList<Object>> partitions,
								HashMap<String, ArrayList<Object>> validMap,HashMap<String, ArrayList<Object>> backup,ArrayList<Integer> startNode,String keyName){
	
		for(int a=0;a<startNode.size();a++){
			int nodeName=startNode.get(a);
			ArrayList<Object> startPartition=partitions.get(nodeName);
			HashMap<Integer,ArrayList<Object>> collection=new HashMap<Integer,ArrayList<Object>> ();
			for(int i=0;i<links.size();i++){
					ArrayList<Integer> link=links.get(a);
					if(nodeName==link.get(0)){
						ArrayList<Object> partition=partitions.get(link.get(1));
						int cloud=(Integer) partition.get(0);
						Set<Integer> keysSet=collection.keySet();
						if(keysSet.contains(cloud)){
							ArrayList<Object> temp=(ArrayList<Object>) collection.get(cloud).clone();
							temp.add(partition);
							collection.put(cloud, temp);
						}else{
							collection.put(cloud, partition);
						}
					}
		    	}
			 findPartition(startPartition,keyName,validMap,collection);
			}
		}
	
	private void findPartition(ArrayList<Object> startPartition,String keyName,HashMap<String, ArrayList<Object>> validMap,HashMap<Integer,ArrayList<Object>> collection){
		
		Iterator <String> keys=validMap.keySet().iterator();
		while(keys.hasNext()){
			String name=keys.next();
			if(!name.equals(keyName)){
				Set<Integer> thePartition=collection.keySet();
				Iterator<Integer> offspring=thePartition.iterator();
				int cloud=(Integer) startPartition.get(0);
				while(offspring.hasNext()){
					ArrayList<Object> partition=collection.get(offspring.next());
					for(int a=0;a<partition.size();a++){
						ArrayList<Object> singlePartition=(ArrayList<Object>) partition.get(a);
						for(int i=1;i<singlePartition.size();i++){
							Object block=singlePartition.get(i);
								
							}
						}
				}
			 }
			}
	}
	
	private boolean CheckingContained(String keyName,HashMap<String, ArrayList<Object>> validMap,String name,Object block){
		
			if(!name.equals(keyName)){
				ArrayList<Object> option=validMap.get(name);
				HashMap<Integer,ArrayList<Object>> partitions=(HashMap<Integer,ArrayList<Object>>) option.get(0);
				 while(partitions.keySet().iterator().hasNext()){
					 ArrayList<Object> tempPartiton=partitions.get(partitions.keySet().iterator().next());
				 }
			}
	    }
}
