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
	
	public ArrayList<String> getInitialBlocks(ArrayList<ArrayList<String>> connections);
	public HashMap<String,ArrayList<Object>> tranferSecurityCheck(HashMap<String,ArrayList<Object>>options,BlockSet blockset);
	
	/* the map has stored the partitions and the links of each partition of the options
	ArrayList<Object> include two sub objects: HashMap<String, Object> and ArrayList<String>
	HashMap<String, Object> : String means the order Object is the partitions
	ArrayList<String> the links of each partition
	*
	*/
	public HashMap<String,ArrayList<Object>> Maps(HashMap<String,ArrayList<Object>>options,
			ArrayList<ArrayList<String>> connections, BlockSet blockset,DataBlockSet databBlockSet);
	
	public HashMap<String,ArrayList<Object>> cycleBreak( HashMap<String, ArrayList<Object>> Maps);
}
