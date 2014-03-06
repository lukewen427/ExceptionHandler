package ncl.ac.uk.esc.servlet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
	   String url= "/Users/zhenyuwen/git/ExceptionHandler/website/statues.txt";
	    List<Machine> machines = new LinkedList<Machine>();
	  
	    public void init(){
	    	MachinePool pool=new MachinePool();
	    	pool.start();
		startTime=System.currentTimeMillis(); 
		/*send the startTime to the exception generator*/
		new loadExceptions(startTime,pool);
		
	     }
	
	   protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException{
	    	
	    	machines.clear();
	    	machines=getStatue.getMachines();
	    	ObjectMapper mapper = new ObjectMapper();
	    	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				   if(machines.isEmpty()){
					   
				   }else{
				//	  System.out.println(machines.toString());
					   postHttp(url,machines.toString(),5);
					   response.setContentType("application/json"); 
					   mapper.writeValue(response.getOutputStream(), machines);
				   }
				   
			   }
	   
	 public  void postHttp(String url,String params, int timeout ){
		
		 OutputStream outs = null;
		
			 try {
				outs = new FileOutputStream(url);
				byte[] ob = params.getBytes();
					outs.write(ob);
			 	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
				
				
		
			
	 }
	}
