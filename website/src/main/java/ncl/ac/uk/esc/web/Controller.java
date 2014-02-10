package ncl.ac.uk.esc.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.ncl.cs.esc.connection.myConnection;

/**
 * Servlet implementation class Controller
 */
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L; 
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	   String[] username = request.getParameterValues("j_username");		
	   String[] password= request.getParameterValues("j_password");		

	     myConnection connection=new myConnection();
	     boolean userverified=false;
	     try {
	    	  userverified= connection.getVerify(username[0],password[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	     if(userverified==true){
	    	  getWorkflowInfo showWorkflow=new getWorkflowInfo();
	    	  showWorkflow.doGet(request, response);
	       }else{
	    	   HttpSession session=request.getSession();
	    	   RequestDispatcher view = request.getRequestDispatcher("Login.jsp");
	    	    session.setAttribute("retry","yes"); 
		       view.forward(request, response);
	       }
	}

	

}
