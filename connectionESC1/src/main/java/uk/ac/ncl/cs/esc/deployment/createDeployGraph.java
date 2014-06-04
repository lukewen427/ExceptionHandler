package uk.ac.ncl.cs.esc.deployment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class createDeployGraph {
	HashMap<String,ArrayList<Integer>> startNodes;
	 HashMap<String,ArrayList<Integer>> endNodes;
	 HashMap<Integer,Integer> partitioncost;
	 ArrayList<Object> linkcost;
	 HashMap<String,ArrayList<Object>> optionLinks;
	
	 ArrayList<Object> Links=new ArrayList<Object>();
	 // the deploy links
	 ArrayList<Object> deployLinks=new ArrayList<Object>();
	 // String is the partition set of deployment, ArrayList<Object> is the partitions
	 HashMap<String,ArrayList<Integer>> deploygraph =new HashMap<String,ArrayList<Integer>>();
	 HashMap<String,Integer> deploygraphcost=new HashMap<String,Integer>();
	 ArrayList<Object> deploylinkcost=new ArrayList<Object>();
	 ArrayList<String> initialNodes=new ArrayList<String>();
	 ArrayList<String> finishNodes=new ArrayList<String>();

	 
public createDeployGraph(HashMap<Integer,Integer> partitioncost, ArrayList<Object> linkcost,
		HashMap<String,ArrayList<Object>> optionLinks, HashMap<String,ArrayList<Integer>> terminalNodes,
		HashMap<String,ArrayList<Integer>> rootNodes){
		
	this.partitioncost=partitioncost;
	this.linkcost=linkcost;
	this.startNodes=rootNodes;
	this.endNodes=terminalNodes;
	this.optionLinks=optionLinks;
	partitionCombine();
	}
// return new graph 
public  HashMap<String,ArrayList<Integer>> getdeployGraph(){
	return deploygraph;
}
// return deploy links of new nodes
public ArrayList<Object> getDeployLinks(){
	return deployLinks;
}
// return deploy cost of each node
public HashMap<String,Integer> getDeployCostGraph(){
	return deploygraphcost;
}
// return deploy cost of each link
public ArrayList<Object> getDeployLinkCost(){
	return deploylinkcost;
}
public ArrayList<String> getRootNodes(){
	return initialNodes;
}

public ArrayList<String> getTerminialNodes(){
	return finishNodes;
}
// from the leaf nodes to root nodes 
private void partitionCombine(){
	
	Iterator<String> optionNames=endNodes.keySet().iterator();
	
	while(optionNames.hasNext()){
		String optionName=optionNames.next();
		ArrayList<Integer> terminial=endNodes.get(optionName);
	//	ArrayList<Integer> start=startNodes.get(optionName);
		ArrayList<Object> thelinks= optionLinks.get(optionName);
	//	System.out.println(optionName);
	//     System.out.println(thelinks);
		LinkedList<ArrayList<Integer>> order= new LinkedList<ArrayList<Integer>>();
		 
		 createOrder(terminial,thelinks,order,new ArrayList<Integer>((ArrayList<Integer>)terminial.clone()));
//		 System.out.println(order);
		 LinkedList<ArrayList<Integer>>newOrder= modify(order);
	//	 System.out.println(newOrder);
		 ArrayList<String>connection=createDeployMap(newOrder);
		 createLinks(connection);
	}
//	System.out.println(deploygraph);
	 createDeployLink();
	//System.out.println(deployLinks);
	calculateDeployNodeCost();
	calculateDeployLinkCost();
	findNodes();
 }

private LinkedList<ArrayList<Integer>> modify(LinkedList<ArrayList<Integer>> order){
	LinkedList<ArrayList<Integer>> newOrder=new LinkedList<ArrayList<Integer>>();
	ArrayList<Integer>deploy=new ArrayList<Integer>();
	for(int a=0;a<order.size();a++){
		ArrayList<Integer> temp=order.get(a);
		ArrayList<Integer> newtemp=removeDuplicate(temp,deploy);
		newOrder.add((ArrayList<Integer>) newtemp.clone());
	}
//	System.out.println(newOrder);
	return newOrder;
}
private void createLinks( ArrayList<String>connection){
	
	for(int a=0;a<connection.size()-1;a++){
		ArrayList<String> temp=new ArrayList<String>();
		String source=connection.get(a);
		String destination=connection.get(a+1);
		temp.add(source);
		temp.add(destination);
		if(!linkisContained(source,destination)){
			Links.add(temp.clone());
		}
	}
}
private boolean linkisContained(String source,String destination){
	for( Object link:Links){
		String s=((ArrayList<String>)link).get(0);
		String d=((ArrayList<String>)link).get(1);
		if(source.equals(s)&&destination.equals(d)){
			return true;
		}
	}
	return false;
}
private ArrayList<String> createDeployMap(LinkedList<ArrayList<Integer>> order){
	ArrayList<String> connection=new ArrayList<String>();
	for(int a=order.size()-1;a>=0;a--){
		ArrayList<Integer> newPartition=order.get(a);
	//	ArrayList<Integer> newPartition=removeDuplicate(Partition,deployed);
		if(!newPartition.isEmpty()){
			if(deploygraph.isEmpty()){
				
				deploygraph.put("P"+1, newPartition);
				if(order.size()==1){
					initialNodes.add("P"+1);
					finishNodes.add("P"+1);
				}
				connection.add("P"+1);
			}else{
				if(iscontain(newPartition)==null){
					int size=deploygraph.size();
					deploygraph.put("P"+(size+1), newPartition);
					if(order.size()==1){
						initialNodes.add("P"+(size+1));
						finishNodes.add("P"+(size+1));
					}
					connection.add("P"+(size+1));
				}else{
					connection.add(iscontain(newPartition));
				}
				/*if(iscontain(newPartition)!=null){
					connection.add(iscontain(newPartition));
				}*/
			}
		}
		
	}

	return connection;
}

private ArrayList<Integer> removeDuplicate(ArrayList<Integer> newPartition,ArrayList<Integer>deployed){
	if(deployed.isEmpty()){
		for(int a=0;a<newPartition.size();a++){
			int node=newPartition.get(a);
			deployed.add(node);
		}
		return newPartition;
	}else{
		ArrayList<Integer> removed=new ArrayList<Integer>();
		for(int a=0;a<newPartition.size();a++){
			int node=newPartition.get(a);
			if(!deployed.contains(node)){
				removed.add(node);
				deployed.add(node);
			}
		}
		return removed;
	}
}

private void createDeployLink(){
	
	for(int a=0;a<Links.size();a++){
		ArrayList<Object> singalDeployLink=new ArrayList<Object>();
		ArrayList<String> link=(ArrayList<String>) Links.get(a);
		singalDeployLink.add(link.clone());
		String s=link.get(0);
		String d=link.get(1);
		ArrayList<Integer> sPartition=deploygraph.get(s);
		ArrayList<Integer> dPartition=deploygraph.get(d);
		ArrayList<Object> temp=new ArrayList<Object>();
		for(int i=0;i<sPartition.size();i++){
			 int source=sPartition.get(i);
			for(int x=0;x<dPartition.size();x++){
				int destination=dPartition.get(x);
				for(int h=0;h<linkcost.size();h++){
					ArrayList<Object> Plink=(ArrayList<Object>) linkcost.get(h);
					int slink=((ArrayList<Integer>)Plink.get(0)).get(0);
					int dlink=((ArrayList<Integer>)Plink.get(0)).get(1);
					if(source==slink&&destination==dlink){
						temp.add(Plink.clone());
					}
				}
			}
		}
		singalDeployLink.add(temp.clone());
		deployLinks.add(singalDeployLink.clone());
	}
	
}
public void findNodes(){
	
	for(int a=0;a<deploylinkcost.size();a++){
		ArrayList<Object> temp=(ArrayList<Object>) deploylinkcost.get(a);
		ArrayList<String> tempLink=(ArrayList<String>) temp.get(0); 
		String source=tempLink.get(0);
		String destination=tempLink.get(1);
		boolean isStart=true;
		boolean isEnd=true;
		for(int i=0;i<deploylinkcost.size();i++){
			ArrayList<Object> temp1=(ArrayList<Object>) deploylinkcost.get(i);
			ArrayList<String> tempLink1=(ArrayList<String>) temp1.get(0); 
			String source1=tempLink1.get(0);
			String destination1=tempLink1.get(1);
			if(source.equals(destination1)){
				isStart=false;
			}
			if(destination.equals(source1)){
				isEnd=false;
			}
		}
		
		if(isStart){
			if(!initialNodes.contains(source)){
				initialNodes.add(source);
			}
			
		}
		if(isEnd){
			if(!finishNodes.contains(destination)){
				finishNodes.add(destination);
			}
			
		}
	}
	
}

private void calculateDeployNodeCost(){

	Iterator <String> Names=deploygraph.keySet().iterator();
	
	while(Names.hasNext()){
		String Name=Names.next();
	//	System.out.println(Name);
		int cost=0;
		ArrayList<Integer> partition=deploygraph.get(Name);
		for(int a=0;a<partition.size();a++){
			int node=partition.get(a);
			cost=partitioncost.get(node)+cost;
		}
		deploygraphcost.put(Name, cost);
	}
}

private void calculateDeployLinkCost(){
	
	for(int a=0;a<deployLinks.size();a++){
		ArrayList<Object> link=(ArrayList<Object>) deployLinks.get(a);
		ArrayList<String> linkName=(ArrayList<String>) link.get(0);
		ArrayList<Object> linkcon=(ArrayList<Object>)link.get(1);
		
		
		ArrayList<Object> singaldeployLinkcost=new ArrayList<Object>();
		singaldeployLinkcost.add(linkName);
	
		int cost=0;
		for(int i=0;i<linkcon.size();i++){
			ArrayList<Object> templink=(ArrayList<Object>) linkcon.get(i);
			
			int thecost=(Integer) templink.get(1);
			cost=thecost+cost;
		//	System.out.println(cost);
		}
		
		singaldeployLinkcost.add(cost);
		deploylinkcost.add(singaldeployLinkcost.clone());
	}
	
}

/*private void AddLinks(ArrayList<Object> partitionLinks,ArrayList<String> newLinks,int source,int destination){

	ArrayList<Object> createNewLink=new ArrayList<Object>();
	ArrayList<Integer> createLink=new ArrayList<Integer>();
	if(partitionLinks.isEmpty()){
		createNewLink.add(newLinks.clone());
		createLink.add(source);
		createLink.add(destination);
		createNewLink.add(createLink.clone());
		partitionLinks.add((ArrayList<Object>)createNewLink.clone());
	}else{
		boolean isinclude=false;
		for(int a=0;a<partitionLinks.size();a++){
			ArrayList<Object> link=(ArrayList<Object>) partitionLinks.get(a);
			ArrayList<String> nameLink=(ArrayList<String>) link.get(0);
			if(nameLink.get(0).equals(newLinks.get(0))&&nameLink.get(1).equals(newLinks.get(1))){
				isinclude=true;
				boolean iscontained=false;
				for(int i=1;i<link.size();i++){
					ArrayList<Integer> gettempLink=(ArrayList<Integer>) link.get(i);
					if(source==gettempLink.get(0)&& destination==gettempLink.get(1)){
						iscontained=true;
						break;
					}
				}
				if(!iscontained){
					createLink.add(source);
					createLink.add(destination);
					link.add(createLink.clone());
				}
			}
		}
		if(!isinclude){
			createNewLink.add(newLinks.clone());
			createLink.add(source);
			createLink.add(destination);
			createNewLink.add(createLink.clone());
			partitionLinks.add((ArrayList<Object>)createNewLink.clone());
		}
		
	}
	
}*/

/*private ArrayList<String> findPartition(int source, int destination){
	
	ArrayList<String> thePLink=new ArrayList<String>();
	
	Iterator <String> Names=deploygraph.keySet().iterator();
	
	while(Names.hasNext()){
		String Name=Names.next();
	//	System.out.println(Name);
		ArrayList<Integer> partition=deploygraph.get(Name);
		Iterator <String> temp=deploygraph.keySet().iterator();
		while(temp.hasNext()){
			String tempName=temp.next();
		//	System.out.println(tempName);
			if(!Name.equals(tempName)){
				ArrayList<Integer> tempPartition=deploygraph.get(tempName);
				if(partition.contains(source)&&tempPartition.contains(destination)){
					thePLink.add(Name);
					thePLink.add(tempName);
				}
			}
		}
	//	System.out.println("*****");
	}
	
	return thePLink;
}*/
private String iscontain(ArrayList<Integer> newPartition){
	
	Iterator <String> Names=deploygraph.keySet().iterator();
	String reName = null;
	while(Names.hasNext()){
	   String Name=Names.next();
		ArrayList<Integer> partition=deploygraph.get(Name);
		
		if(newPartition.size()==partition.size()){
			boolean iscontain=true;
			for(int a=0;a<partition.size();a++){
				int node=partition.get(a);
				if(!newPartition.contains(node)){
					iscontain=false;
					break;
				}
			}
			if(iscontain==true){
				return Name;
			}
		}
	}
	return reName;
}

private void createOrder(ArrayList<Integer>offspringNodes,ArrayList<Object> thelinks,
							LinkedList<ArrayList<Integer>> order,ArrayList<Integer> visited){
	
	if(offspringNodes.isEmpty()){
		return;
	}else{
		order.add((ArrayList<Integer>)offspringNodes.clone());
		ArrayList<Integer> nodeCollection=new ArrayList<Integer>();
		
		for(int a=0;a<offspringNodes.size();a++){
			int offspring=offspringNodes.get(a);
	//		System.out.println(offspring);
		//	visited.add(offspring);
			for(int i=0;i<thelinks.size();i++){
				ArrayList<Integer> pLink=(ArrayList<Integer>) thelinks.get(i);
				int source=pLink.get(0);
				int destination=pLink.get(1);
				if(destination==offspring){
					if(!visited.contains(source)){
						
						ArrayList<Integer> brothers=findBrothers(source,thelinks);
						if(isready(offspringNodes,brothers)){
							if(!nodeCollection.contains(source)){
								nodeCollection.add(source);
								visited.add(source);
							}
						}else{
							nodeCollection.add(offspring);
						}
					}
				}
			}
		//	System.out.println(nodeCollection);
		}
		createOrder(nodeCollection,thelinks,order,visited);
	}	
 }

private boolean isready(ArrayList<Integer>offspringNodes,ArrayList<Integer> brothers){
	int size=brothers.size();
	int acount=0;
	for(int a=0;a<offspringNodes.size();a++){
		int offspring=offspringNodes.get(a);
		if(brothers.contains(offspring)){
			acount++;
		}
	}
	if(acount==size){
		return true;
	}
	return false;
}

private ArrayList<Integer> findBrothers(int startNode,ArrayList<Object> thelinks){
	ArrayList<Integer> brother=new ArrayList<Integer>();
	for(int i=0;i<thelinks.size();i++){
		ArrayList<Integer> pLink=(ArrayList<Integer>) thelinks.get(i);
		int source=pLink.get(0);
		int destination=pLink.get(1);
		if(startNode==source){
			if(!brother.contains(destination)){
				brother.add(destination);
			}
		}
	}
	return brother;
}
/*
	private ArrayList<Integer> DFS(int startNode,ArrayList<Object> thelinks,ArrayList<Integer> stack){
		
		stack.add(startNode);
		for(int i=0;i<thelinks.size();i++){
			ArrayList<Integer> pLink=(ArrayList<Integer>) thelinks.get(i);
			int source=pLink.get(0);
			int destination=pLink.get(1);
			if(source==startNode){
				if(!stack.contains(destination)){
					DFS(destination,thelinks,stack);
				}
			}
		}
		return stack;
	}*/
}
