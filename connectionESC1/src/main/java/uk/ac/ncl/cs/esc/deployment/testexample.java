package uk.ac.ncl.cs.esc.deployment;

public class testexample implements Runnable{
	
	String statues;
public testexample(String statues){
	this.statues=statues;
}

public String getStatue(){
	
	return statues;
}

public void Setruning(){
	statues="running";
}

public void setStop(){
	statues="stop";
}
public void run() {
	Setruning();
	try {
		 Thread.sleep(5000);
	 } catch (Exception e){}
	setStop();
 }
}
