package uk.ac.ncl.cs.esc.deployment;

import java.util.ArrayList;

public class singalPartition {
	String partitionName;
	ArrayList<ArrayList<String>> inputs=new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> outputs=new ArrayList<ArrayList<String>>();
	ArrayList<Object> elements=new ArrayList<Object>();
	public void setName(String name){
		this.partitionName=name;
	}
	public void setInputs(ArrayList<ArrayList<String>>inputs){
		this.inputs=inputs;
	}
	public void setOutputs(ArrayList<ArrayList<String>>outputs){
		this.outputs=outputs;
	}
	public void setElements(ArrayList<Object> partition){
		this.elements=partition;
	}
	public ArrayList<ArrayList<String>> getInputs(){
		return inputs;
	}
	public ArrayList<ArrayList<String>> getOutputs(){
		return outputs;
	}
	public String getPartitionName(){
		return partitionName;
	}
	public ArrayList<Object> getElement(){
		return elements;
	}
}
