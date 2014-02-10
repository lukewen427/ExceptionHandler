package uk.ac.ncl.cs.esc.workflow.restructure;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.connexience.server.api.API;
import com.connexience.server.api.IWorkflow;

import uk.ac.ncl.cs.esc.connection.myConnection;

public class testMethod {

	  public static void main(String[] args) throws Exception{
		  WorkflowRestructure call=new WorkflowRes();
		//  myConnection parser=new myConnection();
	//	api= parser.getAPI();
	//	List<IWorkflow> workflows = api.listWorkflows();
	//	JSONObject drawingJson;
		//System.out.println(workflows);
		  ArrayList<String> theBlockservice=new ArrayList<String>();
		  theBlockservice.add("blocks-core-io-importfile");
		  theBlockservice.add("blocks-core-io-parsecsv");
		  theBlockservice.add("blocks-core-io-csvexport");
		  String cloud="C1";
	//	call.CreateWorkflow(theBlockservice,cloud);
	//	call.Blocklist("ff8080813a6e77ec013a6ee74fb50002");
  //         for(IWorkflow w : workflows){
     //     	String workflowId=w.getId();
   //        	System.out.println(w.getName());
    //        	drawingJson=call.getWorkflowAsJsonObject(workflowId);
    //        	System.out.println(drawingJson.toString(3));
      //    	call.Blocklist(workflowId);
 //           	call.ConnectionMap(workflowId);
  //       }
		
	  }
}
