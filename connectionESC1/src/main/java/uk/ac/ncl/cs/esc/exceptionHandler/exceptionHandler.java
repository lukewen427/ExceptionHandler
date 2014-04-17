package uk.ac.ncl.cs.esc.exceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import uk.ac.ncl.cs.esc.deployment.deployOption;
import uk.ac.ncl.cs.esc.partitiontool.BlockSet;
import uk.ac.ncl.cs.esc.partitiontool.CloudSet;

public class exceptionHandler {

	ArrayList<String> initials;
	 ArrayList<String> terminals;
	 HashMap<String,ArrayList<Integer>> deploygraph;
	 ArrayList<Object>deploylinkcost;
	 HashMap<String,Integer>deploygraphcost;
	 HashMap<Integer,ArrayList<Object>> graph;
	 CloudSet cloudset;
	 String exceptionNode;
	 ArrayList<ArrayList<String>> excPath;
	 BlockSet blockset;
	 ArrayList<ArrayList<String>> connections;
	 ArrayList<Object> links;

	public exceptionHandler(HashMap<String,ArrayList<Integer>> deploygraph,ArrayList<Object>deploylinkcost,
			HashMap<String,Integer>deploygraphcost,ArrayList<ArrayList<String>> excPath,HashMap<Integer,ArrayList<Object>> thegraph,
			String exceptionNode,ArrayList<String> initials,ArrayList<String> terminals,CloudSet cloudset,BlockSet blockset,
			ArrayList<ArrayList<String>> connections,ArrayList<Object> links){
		
		this.deploygraph=deploygraph;
		this.deploylinkcost=deploylinkcost;
		this.initials=initials;
		this.terminals=terminals;
		this.deploygraphcost=deploygraphcost;
		this.exceptionNode=exceptionNode;
		this.graph=thegraph;
		this.cloudset=cloudset;
		this.excPath=excPath;
		this.blockset=blockset;
		this.connections=connections;
		this.links=links;
	}
	public ArrayList<ArrayList<String>> getPath(){
		
		  router getRouter= new router( deploygraph, deploylinkcost,deploygraphcost,excPath,graph,exceptionNode,initials,terminals,cloudset);
	        ArrayList<ArrayList<String>> getPath=getRouter.getPath();
	        return getPath;
	}

	public void reDeployment(){
		ArrayList<ArrayList<String>>NodePath=getPath();
		deployOption deploy=new deployOption( graph,  NodePath, cloudset, deploylinkcost,
				deploygraph, blockset, connections,deploygraphcost,initials, terminals,links);
			deploy.Stop();
			Thread t=new Thread(deploy);
			t.setName("deployment");
			t.start();
	}
}
