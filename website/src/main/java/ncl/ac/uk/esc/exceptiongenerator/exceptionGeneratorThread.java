package ncl.ac.uk.esc.exceptiongenerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import ncl.ac.uk.esc.monitor.connection;

public class exceptionGeneratorThread implements Runnable {
	Thread t;
	long startTime;
	ArrayList<ArrayList<String>> actions;
	startServer start;
	shutdownServer stop;
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
			
				if(actionTime-runingTime<100 &&actionTime-runingTime>-100){
					System.out.println(action);
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
			
		}

}
