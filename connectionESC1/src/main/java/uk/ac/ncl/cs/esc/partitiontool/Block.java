package uk.ac.ncl.cs.esc.partitiontool;

public class Block {
	String blockId;
	int location;
	int clearance;
	String type;
	String serviceId;
	int cpu;
	public Block(String blockId,int location,int clearance,String type,
										String serviceId,int cpu){
		this.blockId=blockId;
		this.location=location;
		this.clearance=clearance;
		this.type=type;
		this.serviceId=serviceId;
		this.cpu=cpu;
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
	public int cpu(){
		
		return cpu;
	}
}
