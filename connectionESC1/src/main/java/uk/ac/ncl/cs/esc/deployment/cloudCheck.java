package uk.ac.ncl.cs.esc.deployment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



import uk.ac.ncl.cs.esc.partitiontool.Cloud;
import uk.ac.ncl.cs.esc.partitiontool.CloudSet;


public class cloudCheck {

	ArrayList<Object> partition;
	CloudSet cloudset;
	String url="/Users/zhenyuwen/git/ExceptionHandler/website/statues.txt";
	
	public cloudCheck(ArrayList<Object> partition,CloudSet cloudset){
		this.partition=partition;
		
	}
	
	public boolean checkCloud(String cloudip){
		
		HashMap<String,String>machines=getCloud();
		String staute=machines.get(cloudip);
		if(staute.equals("ON")){
			return true;
		}else{
			return false;
		}
		
	}
	
	private HashMap<String,String>  getCloud(){
		getClouds clouds=new getClouds();
		HashMap<String,String> machines = null;
		try {
			machines=clouds.getHttp(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return machines;
	}
	


	public static void main(String[] arg) throws IOException{
	//	getDeployment test=new getDeployment();
	//	test.getCloud();
	}
}
