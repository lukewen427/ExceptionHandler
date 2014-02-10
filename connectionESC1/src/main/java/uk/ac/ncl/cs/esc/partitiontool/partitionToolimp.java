package uk.ac.ncl.cs.esc.partitiontool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class partitionToolimp implements partitionTool{

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
		
		/*the arraylist store all of the possible deployment options*/
		ArrayList<Object> theoptions=new  ArrayList<Object>();
		int cloudNumber=cloudSet.size();
		int BlockNumber=blockSet.size();
		int DataBlockNumber=DataBlocks.size();
		int theBlockNum=BlockNumber+DataBlockNumber;
		/*calculate the total number of the options*/
		int total=(int) Math.pow(cloudNumber, theBlockNum);
		int a=1;
		int b,f=3;
		 ArrayList<Object> options=new  ArrayList<Object>();
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
			ArrayList<ArrayList<String>> connections,BlockSet blockset) {
		HashMap<String, ArrayList<Object>> partitionMap=new HashMap<String, ArrayList<Object>>();
			ArrayList<Object> singaloption=new ArrayList<Object>();
			int partCount=1;
			for(int a=0;a<theOptionSet.size();a++){
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
			}
			
			return partitionMap;
	}

	private ArrayList<Object> cloudCombination(ArrayList<Object> cloud0Partition){
	
		ArrayList<Object> finalcloud0Partition=new ArrayList<Object> ();
		ArrayList<Object> clone1=new ArrayList<Object> ();
		ArrayList<Object> clone2=new ArrayList<Object> ();
		for(int a=0;a<cloud0Partition.size();a++){
			ArrayList<Object> partition=(ArrayList<Object>) cloud0Partition.get(a);
		//	System.out.println(partition);
			if(!partition.isEmpty()){
			boolean noLink=true;
			for(int fx=0; fx<partition.size();fx++){
				Object block=partition.get(fx);
				if(block instanceof DataBlock){
					noLink=false;
					break;
				}
			}
			if(noLink==true){
				clone1=(ArrayList<Object>) partition.clone();
				finalcloud0Partition.add(clone1);
			}else{
				boolean Undo=false;
				for(int hf=0;hf<partition.size();hf++){
					Object block=partition.get(hf);
					if(block instanceof Block){
						for(int hc=0;hc<finalcloud0Partition.size();hc++){
							ArrayList<Object> gettemp=(ArrayList<Object>) finalcloud0Partition.get(hc);
							if(gettemp.contains(block)){
								Undo=true;
								break;
							}
						}
					}
					
				}
				if(Undo==false){
				ArrayList<Object> thepartition=singalCombinationCloud0(finalcloud0Partition,partition,cloud0Partition);
				if(thepartition!=null && !thepartition.isEmpty()){
					clone2=(ArrayList<Object>) thepartition.clone();
					finalcloud0Partition.add(clone2);
				}
			}
			}
		 }
		}
		return finalcloud0Partition;
	}

	private ArrayList<Object>singalCombinationCloud0(ArrayList<Object> finalcloud0Partition,ArrayList<Object> partition,
																ArrayList<Object> cloud0Partition){
		boolean noLink=true;
		for(int a=0; a<partition.size();a++){
			Object block=partition.get(a);
			if(block instanceof DataBlock){
				for(int hs=0;hs<cloud0Partition.size();hs++){
					ArrayList<Object> singalSet=(ArrayList<Object>) cloud0Partition.get(hs);
					if(singalSet.contains(block)){
						noLink=false;
						break;
					}
				}
			}
		}
		if(noLink==true){
			return partition;
		}else{
			ArrayList<Object> newpartition=(ArrayList<Object>) partition.clone();
			ArrayList<Object> changedcloud0=(ArrayList<Object>)cloud0Partition.clone();
				if(finalcloud0Partition.isEmpty()){
					for(int i=0;i<partition.size();i++){
						Object getBlock=partition.get(i);
						if(getBlock instanceof DataBlock){
							DataBlock thedatablock=(DataBlock) getBlock;
							for(int f=0;f<changedcloud0.size();f++){
								ArrayList<Object> tempPartition=(ArrayList<Object>) changedcloud0.get(f);
								boolean iscontain=false;
								for(int h=0;h<tempPartition.size();h++){
									Object gettempBlock=tempPartition.get(h);
									if(gettempBlock instanceof Block){
										if(partition.contains(gettempBlock)){
											iscontain=true;
											break;
										}
									}
								}
								if(iscontain==false){
									if(tempPartition.contains(thedatablock)){
										int size=newpartition.size();
										newpartition.remove(size-1);
										for(int fh=0;fh<tempPartition.size();fh++){
											Object getBlockAdd=tempPartition.get(fh);
											if(!newpartition.contains(getBlockAdd)){
												newpartition.add(getBlockAdd);
											}
										}
										changedcloud0.remove(f);
									}
									
								}else{
									changedcloud0.remove(f);
								}
							}
						}
					}
			
					return singalCombinationCloud0(finalcloud0Partition,newpartition, changedcloud0);
				}else{
					// System.out.println(newpartition);
				     for(int i=0;i<partition.size();i++){
								Object getBlock=partition.get(i);
								if(getBlock instanceof DataBlock){
									DataBlock thedatablock=(DataBlock) getBlock;
									for(int f=0;f<changedcloud0.size();f++){
										ArrayList<Object> tempPartition=(ArrayList<Object>)changedcloud0.get(f);
							//			System.out.println(tempPartition);
										boolean iscontain=false;
										for(int h=0;h<tempPartition.size();h++){
											Object gettempBlock=tempPartition.get(h);
											if(gettempBlock instanceof Block){
												if(partition.contains(gettempBlock)){
													iscontain=true;
													break;
												}
											}
										}
										if(iscontain==false){
								
											if(tempPartition.contains(thedatablock)){
												
												int size=newpartition.size();
												newpartition.remove(size-1);
												for(int fh=0;fh<tempPartition.size();fh++){
													Object getBlockAdd=tempPartition.get(fh);
													if(!newpartition.contains(getBlockAdd)){
														newpartition.add(getBlockAdd);
													}
												}
												changedcloud0.remove(f);
											}
										}else{
											changedcloud0.remove(f);
										}
									}
								}
							}
			
					return singalCombinationCloud0(finalcloud0Partition,newpartition, changedcloud0);
				}
		 }
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
									int nextSecurity=(Integer) compare.get(compare.size()-1);
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
	
	public String [] findBestOption(
			HashMap<String,ArrayList<Object>> partitionMap,ArrayList<ArrayList<String>> connections,BlockSet blockset,CloudSet cloudset) {
		int size=partitionMap.size();
		int Total[]=new int[size];
		int Order[]=new int[size];
		int Storage[]=new int[size];
		int CPU[]=new int[size];
		int TransferInCost[]=new int[size];
		int TransferOutCost[]=new int[size];
		String optionName []=new String[size];
		Set<String> partitionNum=partitionMap.keySet();
		Iterator getPartition=partitionNum.iterator();
		int h=0;
		while(getPartition.hasNext()){
			String parName=(String) getPartition.next();
			optionName[h]=parName;
			ArrayList<Object> partitions=partitionMap.get(parName);
			int storageCost=0;
			int cpuCost=0;
			int outgoing=0;
			int incoming=0;
			for(int a=0;a<partitions.size();a++){
				ArrayList<Object>transferdata=new ArrayList<Object>();
				ArrayList<Object>singalPartition=(ArrayList<Object>) partitions.get(a);
				for(int i=0;i<singalPartition.size()-1;i++){
					int security=(Integer) singalPartition.get(singalPartition.size()-1);
					String cloudName="Cloud"+security;
					Cloud currentCloud=cloudset.getCloud(cloudName);
					int theStorage=currentCloud.getStoragecost();
					int thecpu=currentCloud.getCPUcost();
					Object block=singalPartition.get(i);
					if(block instanceof DataBlock){
						int datasize=((DataBlock) block).getSize();
						int longevity=((DataBlock) block).getlongevity();
						storageCost=storageCost+datasize*longevity*theStorage;
						String sourceid=((DataBlock) block).getsourceblockId();
						String destinationid=((DataBlock) block).getdestinationblockId();
						Block getsourceBlock= blockset.getBlock(sourceid);
						Block getdestinationBlock=blockset.getBlock(destinationid);
						if(singalPartition.contains(getsourceBlock)&&!singalPartition.contains(getdestinationBlock)){
							int TransferOut=currentCloud.getTransferout();
							outgoing=outgoing+datasize*TransferOut;
							for(int f=0;f<partitions.size();f++){
								ArrayList<Object>compare=(ArrayList<Object>)partitions.get(f);
								if(compare.contains(getdestinationBlock)){
									int nextsecure=(Integer) compare.get(compare.size()-1);
									String nextCloudName="Cloud"+nextsecure;
									Cloud nextCloud=cloudset.getCloud(nextCloudName);
									int TransferIn=nextCloud.getTransferin();
									incoming=incoming+TransferIn*datasize;
								  }
								}
							}
						}else{
							Block isBlock=(Block)block;
							int cpu=isBlock.cpu();
							cpuCost=cpuCost+(cpu*thecpu);
						}
					}
				}
			Storage[h]=storageCost;
			TransferInCost[h]=incoming;
			TransferOutCost[h]=outgoing;
			CPU[h]=cpuCost;
			Total[h]=storageCost+incoming+outgoing+cpuCost;
			Order[h]=storageCost+incoming+outgoing+cpuCost;
			h++;
		}
		
		int theOrder[]=new int[size];
		Arrays.sort(Order);
		for (int i = 0; i < Order.length; i++) {
			 int sortedData=Order[i];
			 for(int xf=0;xf<size;xf++){
				 if(sortedData==Total[xf]){
					 boolean contain=false;
					 for(int af=0;af<theOrder.length;af++){
						 if(theOrder[af]==xf){
							 contain=true;
							 break;
						 }
					 }
					 if(contain==false){
						 theOrder[i]=xf;
					 }
				 }
			 } 
		}
		String orderName []=new String[size];
		for(int i=0;i<size;i++){
			orderName[i]=optionName[theOrder[i]];
		}
		
		return orderName;
	}

}
