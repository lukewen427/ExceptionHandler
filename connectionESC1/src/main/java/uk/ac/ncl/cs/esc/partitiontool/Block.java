package uk.ac.ncl.cs.esc.partitiontool;

public class Block {
	String blockId;
	int location;
	int clearance;
	String type;
	String serviceId;
	int cpu;
	String name;
	public Block(String blockId,int location,int clearance,String type,
										String serviceId,int cpu,String name){
		this.blockId=blockId;
		this.location=location;
		this.clearance=clearance;
		this.type=type;
		this.serviceId=serviceId;
		this.cpu=cpu;
		this.name=name;
	}
	public String getBlockId(){
		return blockId;
	}
	public int getlocation(){
		return location;
	}
	public int getclearance(){
		return clearance;
	}
	public String gettype(){
		return type;
	}
	public String getserviceId(){
		return serviceId;
	}
	public String getBlockName(){
		return name;
	}
	public int cpu(){
		
		return cpu;
	}
}
