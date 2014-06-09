package uk.ac.ncl.cs.esc.partitiontool;

import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.ncl.cs.esc.deployment.HEFT.deployment;
import uk.ac.ncl.cs.esc.deployment.HEFT.deploymentIm;
import uk.ac.ncl.cs.esc.partitiontool.prepareDeployment.workflowInfo;

public class operating {
	partitionWorkflow partition;
	HashMap<Block,Integer> option;
	ArrayList<Object> partitions;
	ArrayList<Object> links;
	
	public operating(workflowInfo workflowinfo) throws Exception{
		// map the blocks to clouds
		this.partition= new partitionWorkflowImp(workflowinfo);
		this.option= partition.mappingCloud();
	//	System.out.println(option);
		// partition the workflow 
		this.partitions=partition.workflowSplit(option);
		this.links=partition.getLinks();
		deploymentIm deploy=new deploymentIm();
		setDeploy(deploy,workflowinfo);
		deploy.createpartitionGraph();
		deploy.createDeployGraph();
	}
	
	private void setDeploy(deploymentIm deploy,workflowInfo workflowinfo){
		deploy.setOption(option);
		deploy.setParitions(partitions);
		deploy.setWorkflowIn(workflowinfo);
		deploy.setpartitionLinks(links);
		
	}
	
	
}
