package uk.ac.ncl.cs.esc.deployment;

import java.util.ArrayList;


public class DpartitionSet {
	ArrayList<singalPartition> partitionSet =new ArrayList<singalPartition>();
	public void addPartition(singalPartition partition){
		partitionSet.add(partition);
	}
	public singalPartition getSingalPartition(ArrayList<Object> comparePartition){
		singalPartition thePartition=null;
		for(int a=0;a<partitionSet.size();a++){
			singalPartition partition=partitionSet.get(a);
			ArrayList<Object> partitionElement=partition.getElement();
			if(comparePartition.equals(partitionElement)){
				thePartition=partition;
			}
		}
		return thePartition;
	}
	
	public ArrayList<singalPartition> getPartitionSet(){
		return partitionSet;
	}
}
