package ncl.ac.uk.esc.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ncl.ac.uk.esc.exceptiongenerator.exceptionGenerator;



public class background extends HttpServlet {
	
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		 BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        
	      String json = null;
	        
	        if(br != null){
	           json=br.readLine();
	        }
	       
	        String[] JsonArray=json.split("&");
	        
	        	String[] ip=JsonArray[0].split("=");
	        	String[] statue=JsonArray[1].split("=");
	        	String[] security=JsonArray[2].split("=");
	        	
	        	Machine machine=new Machine(ip[1],statue[1],security[1]);
	      
		 	exceptionGenerator test=new exceptionGenerator();
		 	if(machine.getStatue().equals("ON")){
		 		test.shutDown(machine.getIp());
		 	}else{
		 		test.turnOn(machine.getIp());
		 	}
		
		}
}
