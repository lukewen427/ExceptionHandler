package uk.ac.ncl.cs.esc.partitiontool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface partitionTool {
	/*get all of the deploy options */
	public ArrayList<Object> allOptions(Set<Block> blockSet,Set<DataBlock> DataBlocks,Set<Cloud> cloudSet);
	public boolean workflowChecking (Set<Block> theBlockSet,ArrayList<ArrayList<String>> connections);
	public HashMap<String,ArrayList<Object>> workflowPartition( ArrayList<Object>theOptionSet,
			ArrayList<ArrayList<String>> connections, BlockSet blockset,DataBlockSet databBlockSet) ;
	public String []  findBestOption(HashMap<String,ArrayList<Object>> partitionMap,
										ArrayList<ArrayList<String>> connections,BlockSet blockset, CloudSet cloudset);
	public ArrayList<String> getInitialBlocks(ArrayList<ArrayList<String>> connections);
}
