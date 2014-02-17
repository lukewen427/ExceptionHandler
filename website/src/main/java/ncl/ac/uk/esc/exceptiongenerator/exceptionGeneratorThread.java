package ncl.ac.uk.esc.exceptiongenerator;


import java.util.ArrayList;

import ncl.ac.uk.esc.servlet.MachinePool;


public class exceptionGeneratorThread implements Runnable {
	Thread t;
	long startTime;
	ArrayList<ArrayList<String>> actions;
	startServer start;
	shutdownServer stop;
	MachinePool pool;
	public exceptionGeneratorThread (String name,long startTime,ArrayList<ArrayList<String>> actions,MachinePool pool){
		this.pool=pool;
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
			
				if(actionTime-runingTime<100 &&actionTime-runingTime>-100){
					String active=action.get(1);
					if(active.equals("turnon")){
						// start=new startServer(t.getName());
						sendStartMessage(t.getName());
					}else{
						sendStopMessage(t.getName());
					}
					i++;
				}else{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	private void sendStartMessage(String name)  {
		/*	try {
				SshStart();
			} catch (TaskExecFailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			if(stop instanceof shutdownServer ){
				if(stop.t.isAlive()){
					stop.setStop(true);
				}
			}
			
			start=new startServer(name);
			pool.changeStatus(name);
			
		}
		private void sendStopMessage(String name) {
			/*try {
				SshShutdown();
			} catch (TaskExecFailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			if(start instanceof startServer){
				if(start.t.isAlive()){
					start.setStop(true);
				}
			}
		
			stop=new shutdownServer(name);
			pool.changeStatus(name);
		}

}
