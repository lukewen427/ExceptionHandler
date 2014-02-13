package ncl.ac.uk.esc.exceptiongenerator;

import java.util.ArrayList;

public class exceptionGeneratorThread implements Runnable {
	Thread t;
	long startTime;
	ArrayList<ArrayList<String>> actions;
	
	public exceptionGeneratorThread (String name,long startTime,ArrayList<ArrayList<String>> actions){
		this.startTime=startTime;
		this.actions=actions; 
		t=new Thread(this,name);
		t.start();
	}
	public void run() {
		
		while(true){
			for(int i=0;i<actions.size();){
				ArrayList<String> action=actions.get(i);
				long actionTime=(long) Integer.valueOf(action.get(0))*1000;
				long runingTime=System.currentTimeMillis()-startTime;
				if(actionTime-runingTime<100){
					System.out.println(action.get(1));
					i++;
				}else{
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}

}
