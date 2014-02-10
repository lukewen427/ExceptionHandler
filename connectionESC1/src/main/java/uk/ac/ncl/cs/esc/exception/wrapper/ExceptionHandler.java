package uk.ac.ncl.cs.esc.exception.wrapper;

public class ExceptionHandler {
	
	public int askDeploy() throws Exception{
		Exceptions getException=new Exceptions();
		 int a=0;
		String theresult=getException.ListExceptions();
		if(theresult.equals("Case 1")||theresult.equals("Case 2")){
			Thread.sleep(1000);
			  theresult=getException.ListExceptions();
			 
			while((theresult.equals("Case 1")||theresult.equals("Case 2"))&&a<3){
				Thread.sleep(1000);
				theresult=getException.ListExceptions();
				a++;
			}
			if(a==3){
				System.out.println("The workflow can not be deployed on this cloud");
				return 2;
			}
			
		}
		if(theresult.equals("Case 3")){
			System.out.println("The cloud is failed, the workflow can not be deployed on this cloud ");
			return 1;
		}
		if(theresult.equals("Case 4")){
		
			System.out.println("Go to excuting");
			return 0;
		}
		return 3;
	}
	
}
