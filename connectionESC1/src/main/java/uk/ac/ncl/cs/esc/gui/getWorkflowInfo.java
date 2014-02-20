package uk.ac.ncl.cs.esc.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONObject;

import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRes;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRestructure;

public class getWorkflowInfo {
	
	public HashMap<String,String> getWorkflows() throws Exception{
	
		
		 HashMap<String,String>workflowlist= new HashMap<String,String>();
			WorkflowRestructure workflow=new WorkflowRes();
			workflowlist=workflow.Workflowlist();
			return workflowlist;
			
	}
	
	/*public static  void main(String []args){
		getWorkflowInfo test=new getWorkflowInfo();
		try {
			test.getWorkflows();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
