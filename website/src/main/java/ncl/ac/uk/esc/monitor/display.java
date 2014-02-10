package ncl.ac.uk.esc.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ncl.ac.uk.esc.exceptiongenerator.exceptionGenerator;
import ncl.ac.uk.esc.monitor.connectionPool.getStatue;

public class display extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<String> Machines=null;
	private ArrayList<String> currentService;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 Machines=getStatue.getMachine();
		 currentService=getStatue.getService();
		
		   if(Machines==null){
			  request.getRequestDispatcher("/Monitor.jsp").forward(request, response);
			 
		   }else{
			//   exceptionGenerator a=new exceptionGenerator();
			//	 a.shutDown();
			   HttpSession session=request.getSession();
		 	   session.setAttribute("Machine",Machines);
		 	    session.setAttribute("Service",currentService);
		 	   response.sendRedirect("/website/Monitor.jsp");
		   }
	  
	}

}
