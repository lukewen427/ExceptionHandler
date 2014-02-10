package ncl.ac.uk.esc.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.ncl.cs.esc.partitiontool.readInfo;

import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRes;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRestructure;

/**
 * Servlet implementation class securityLevel
 */
public class securityLevel extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			ArrayList<String> blockinfo=new ArrayList<String>();
			HashMap<String,ArrayList<String>>blockSecurity=new HashMap<String,ArrayList<String>>();
			String[] theblockId=request.getParameterValues("blockId");
			String[] getlocation=request.getParameterValues("location");
			String[] getclearance=request.getParameterValues("clearance");
			String[] gettype=request.getParameterValues("theType");
			String[] getcpu=request.getParameterValues("CPU");
			String workflowId=request.getParameter("workflowId");
			for(int i=0;i<theblockId.length;i++){
				ArrayList temp=new ArrayList();
				String[] theId=theblockId[i].split(" ");
				int thelocation;
				if(getlocation[i].equals("low")){
					thelocation=0;
				}else{
					thelocation=1;
				}
				int theclearance;
				if(getclearance[i].equals("low")){
					theclearance=0;
				}else{
					theclearance=1;
				}
					
				temp.add(thelocation);
				temp.add(theclearance);
				temp.add(gettype[i]);
				temp.add(getcpu[i]);
                blockinfo=(ArrayList) (((ArrayList)temp).clone());
				blockSecurity.put(theId[1], blockinfo);
			}
			String[] datalocation=request.getParameterValues("datalocation");
			String[] gettdataype=request.getParameterValues("Type");
			String getsize[]=request.getParameterValues("Size");
			String getlongevity[]=request.getParameterValues("Longevity");
			String[] sourceblockId=request.getParameterValues("sourceBlockId");
			String[] destinationBlockId=request.getParameterValues("destinationBlockId");
			ArrayList<ArrayList<String>> connections=new ArrayList<ArrayList<String>>();
			connections=(ArrayList<ArrayList<String>>) request.getSession().getAttribute("connections");
			for(int a=0;a<sourceblockId.length;a++){
				for(int x=0;x<connections.size();x++){
					ArrayList<String> connection=connections.get(x);
		//			System.out.println(connection);
					String sourceid=connection.get(0);
					String destinationid=connection.get(1);
					if(sourceblockId[a].equals(sourceid)&& destinationBlockId[a].equals(destinationid)){
						
						int thedatalocation;
						if(datalocation[a].equals("low")){
							thedatalocation=0;
						}else{
							thedatalocation=1;
						}
						connection.add(String.valueOf(thedatalocation));
						connection.add(gettdataype[a]);
						connection.add(getsize[a]);
						connection.add(getlongevity[a]);
					}
				}
			}
			
	//		System.out.println(blockSecurity);
//		System.out.println(workflowId);
	//		System.out.println(connections);
	//	new readInfo(blockSecurity,workflowId,connections);
	}	
}
