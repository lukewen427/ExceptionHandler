package uk.ac.ncl.cs.esc.deployment;

import java.io.ByteArrayOutputStream;
import java.util.List;

import uk.ac.ncl.cs.esc.connection.getConnection;
import uk.ac.ncl.cs.esc.connection.myConnection;

import com.connexience.server.api.API;
import com.connexience.server.api.IDocument;
import com.connexience.server.api.IObject;
import com.connexience.server.api.IWorkflow;
import com.connexience.server.api.IWorkflowInvocation;

public class testDeployment {

	public static void main(String args[]) throws Exception{
	/*	getConnection parser=new myConnection();
		parser.createAPI();
		API api=parser.getAPI();
		String workflowId="8ac2c2303c3904e5013c3fe284750241";
		IWorkflow wf=api.getWorkflow(workflowId);
		IDocument dc=api.getDocument(workflowId);
		IWorkflowInvocation invocation=api.executeWorkflow(wf,dc);
		 while(invocation.getStatus().equals(IWorkflowInvocation.WORKFLOW_RUNNING) || invocation.getStatus().equals(IWorkflowInvocation.WORKFLOW_WAITING)){
	    	 try {
	    		 Thread.sleep(1000);
	    	 } catch (Exception e){}
	    	 System.out.println("Checking status");
	    	 invocation =api.getWorkflowInvocation(invocation.getInvocationId());
	     }
		  List<IObject> results = api.getFolderContents(invocation);
		  System.out.println(results);
		  for (IObject r : results) {
			  if (r instanceof IDocument) {
				  IDocument d = (IDocument) r;
				
				
		//		  String[] thename=getName.split("\\.");
		//		  ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			//	  api.download(d, outStream);
			//	  outStream.flush();
				  
			  }
		  }*/
		
		testDeployment  t=new testDeployment ();
		  t.callTread();
		  t.callok();
		 
	}
	public void callok(){
		 System.out.println("ok");
	}
	
	public void callTread(){
		testexample test=new testexample("running");
		Thread t=new Thread(test);
		t.start();
		
	//	System.out.println(test.getStatue());
		while((test.getStatue()).equals("running")){
		
			try {
	    		 Thread.sleep(100);
	    	 } catch (Exception e){}
		}
		System.out.println("finish");
	}
}
