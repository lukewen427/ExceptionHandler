package uk.ac.ncl.cs.esc.partitiontool;

public class Cloud {
	private String name;
	private String securityLevel;
	private String cloudip;
	private int StorageCost;
	private int TransferIn;
	private int TransferOut;
	private int CPUCost;
	public Cloud(String name,String securityLevel,String cloudip,int StorageCost,
			int TransferIn,int TranferOut,int CPUCost){
		this.name=name;
		this.securityLevel=securityLevel;
		this.cloudip=cloudip;
		this.StorageCost=StorageCost;
		this.TransferIn=TransferIn;
		this.TransferOut= TranferOut;
		this.CPUCost=CPUCost;
	}
	public String getCloudname(){
		
		return name;
	}
	
	public String getCloudsecurityLevel(){
		
		return securityLevel;
	}
	
	
	public String getip(){
		
		return cloudip;
	}
	public int getStoragecost(){
		
		return StorageCost;
	}
	
	public int getTransferin(){
		
		return TransferIn;
	}
	public int getTransferout(){
		return TransferOut;
		
	}
	public int getCPUcost(){
		return CPUCost;
	}
	
}
