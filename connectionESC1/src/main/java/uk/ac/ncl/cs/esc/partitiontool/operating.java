package uk.ac.ncl.cs.esc.partitiontool;

import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.ncl.cs.esc.partitiontool.prepareDeployment.workflowInfo;

public class operating {
	
	public operating(workflowInfo workflowinfo) throws Exception{
		// map the blocks to clouds
		partitionWorkflow partition= new partitionWorkflowImp(workflowinfo);
		HashMap<Block,Integer> option= partition.mappingCloud();
	//	System.out.println(option);
		// partition the workflow 
		ArrayList<Object> partitions=partition.workflowSplit( option);
		
		
	}
	
	
}
