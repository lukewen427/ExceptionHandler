package uk.ac.ncl.cs.esc.partitiontool;

public class DataBlock {
	String name;
	int location;
	String sourceblockId;
	String destinationblockId;
	int size;
	int longevity;
	public DataBlock(String name,int location,String sourceblockId,
									String destinationblockId,int size,int longevity){
			this.name=name;
			this.location=location;
			this.sourceblockId=sourceblockId;
			this.destinationblockId=destinationblockId;
			this.size=size;
			this.longevity=longevity;
	}
	public String getDataBlockName(){
		return name;
	}
	public int getDatalocation(){
		return location;
	}
	public String getsourceblockId(){
		
		return sourceblockId;
	}
	public String getdestinationblockId(){
		return destinationblockId;
	}
	public int getSize(){
		return size;
	}
	public int getlongevity(){
		return longevity;
	}
}
