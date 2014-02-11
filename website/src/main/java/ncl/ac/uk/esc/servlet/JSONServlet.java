package ncl.ac.uk.esc.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ncl.ac.uk.esc.servlet.MachinePool.getStatue;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;



public class JSONServlet extends HttpServlet {
	
	 private static final long serialVersionUID = 1L;

		private ArrayList<String> currentService;
	    // This will store all received articles
	    List<Machine> machines = new LinkedList<Machine>();
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException{
	    	ObjectMapper mapper = new ObjectMapper();
	    	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    	machines.clear();
	    	machines=getStatue.getMachines();
	  
				   if(machines.isEmpty()){
					   
				   }else{
					//   System.out.println(machines.toString());
					   response.setContentType("application/json"); 
					   mapper.writeValue(response.getOutputStream(), machines);
				   }
				   
			   }   
	}
