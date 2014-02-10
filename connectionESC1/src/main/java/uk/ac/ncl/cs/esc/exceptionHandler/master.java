package uk.ac.ncl.cs.esc.exceptionHandler;

import uk.ac.ncl.cs.esc.exception.wrapper.ExceptionHandler;
import uk.ac.ncl.cs.esc.exception.wrapper.escPool;

public class master {
	public int askforDeloy(String cloud) throws Exception{
		escPool test=new escPool();
		int feedback;
		if(test.isAvailable(cloud)){
			feedback=0;
		}else{
			 feedback=1;
		}
		
		return feedback;
	}
}
