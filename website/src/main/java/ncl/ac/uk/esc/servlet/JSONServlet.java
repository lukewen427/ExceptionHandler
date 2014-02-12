package ncl.ac.uk.esc.servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ncl.ac.uk.esc.exceptiongenerator.loadExceptions;
import ncl.ac.uk.esc.servlet.MachinePool.getStatue;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;



public class JSONServlet extends HttpServlet {
	
	 
	 private static final long serialVersionUID = 1L;
	 long startTime;
	   
	    List<Machine> machines = new LinkedList<Machine>();
	  
	    public void init(){
		startTime=System.currentTimeMillis(); 
		/*send the startTime to the exception generator*/
		new loadExceptions(startTime);
		new MachinePool().start();
	     }
	
	   protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException{
	    	
	    	machines.clear();
	    	machines=getStatue.getMachines();
	    	ObjectMapper mapper = new ObjectMapper();
	    	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				   if(machines.isEmpty()){
					   
				   }else{
					//   System.out.println(machines.toString());
					   response.setContentType("application/json"); 
					   mapper.writeValue(response.getOutputStream(), machines);
				   }
				   
			   }   
	}
