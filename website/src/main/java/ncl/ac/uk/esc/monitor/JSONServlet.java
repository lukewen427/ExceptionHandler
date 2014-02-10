package ncl.ac.uk.esc.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ncl.ac.uk.esc.monitor.connectionPool.getStatue;

public class JSONServlet extends HttpServlet {
	
	 private static final long serialVersionUID = 1L;
	 private Set<String> Machines=null;
		private ArrayList<String> currentService;
	    // This will store all received articles
	    List<Machine> machines = new LinkedList<Machine>();
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException{
	    	ObjectMapper mapper = new ObjectMapper();
	    	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    	machines.clear();
	    	Machines=getStatue.getMachine();
			currentService=getStatue.getService();
			
			if(Machines==null){
				//  request.getRequestDispatcher("/Display.jsp").forward(request, response);
				 
			   }else{
				  
				   if(currentService.isEmpty()){
					   for(String theMachine: Machines){
						   Machine machine=new Machine(theMachine,"OFF","1");
						   machines.add(machine);
					   }
				   }else{
					   for(String theMachine: Machines){
						   if(currentService.contains(theMachine)){
							   Machine machine=new Machine(theMachine,"ON","1");
							   machines.add(machine);
						   }else{
							   Machine machine=new Machine(theMachine,"OFF","1");
							   machines.add(machine);
						   }
					   }
				   }
				   
				   if(machines.isEmpty()){
					   
				   }else{
					//   System.out.println(machines.toString());
					   response.setContentType("application/json"); 
					   mapper.writeValue(response.getOutputStream(), machines);
				   }
				   
			   }
		/*	
	       Machine machine=new Machine("192.168.1.1","OFF","1");
	        Machine machine1=new Machine("192.168.2.1","OFF","1");
	        machines.add(machine);
	        machines.add(machine1);
	        response.setContentType("application/json"); 
		    mapper.writeValue(response.getOutputStream(), machines);*/
	    }
	    
}
