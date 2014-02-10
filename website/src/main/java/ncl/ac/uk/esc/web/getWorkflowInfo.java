package ncl.ac.uk.esc.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRes;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRestructure;

public class getWorkflowInfo extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] selectedworkflow=request.getParameterValues("selectedworkflow");
		
		if(selectedworkflow==null){
		 HashMap<String,String>workflowlist= new HashMap<String,String>();
		 try {
			 
			 JSONObject workflowInfo=new JSONObject();
			WorkflowRestructure workflow=new WorkflowRes();
			workflowlist=workflow.Workflowlist();
			Set<String> keyset=workflowlist.keySet();
			Iterator<String> iter=keyset.iterator();
			while(iter.hasNext()){
				String id=(String)iter.next();
				String name=(String)workflowlist.get(id);
				workflowInfo.put(id, name);
				}
			HttpSession session=request.getSession();
			    session.setAttribute("workflows",workflowlist);
	            response.sendRedirect("/website/Workflowlist.jsp");
		   } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	  }else{
		
		  String selectedId=selectedworkflow[0];
		  
		try {
			 HashMap<String, String> blocks = getBlocks(selectedId);
			 ArrayList<ArrayList<String>> connections=getConnection(selectedId);
			 System.out.println(connections);
			HttpSession session=request.getSession();
		    session.setAttribute("blocks",blocks);
		    session.setAttribute("connections", connections);
		    session.setAttribute("workflowId",selectedId);
	        response.sendRedirect("/website/getSecurityLevel.jsp");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	  }	 
	}
	
 public ArrayList<ArrayList<String>> getConnection (String workflowid) throws Exception{
	 ArrayList<ArrayList<String>> connection=new ArrayList<ArrayList<String>>();
	 WorkflowRestructure getconnection=new WorkflowRes();
	 connection=getconnection.getConnection(workflowid);
	return connection;
	 
 }	
 public HashMap<String,String> getBlocks(String workflowid) throws Exception{
		
		WorkflowRestructure blocklist=new WorkflowRes();
		HashMap<String,String> blocks=new HashMap<String,String>();
		blocks=blocklist.Blocklist(workflowid);
	    
	    return blocks;
	} 
    
}
