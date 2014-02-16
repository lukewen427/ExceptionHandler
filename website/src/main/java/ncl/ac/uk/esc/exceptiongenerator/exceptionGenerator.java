/*package ncl.ac.uk.esc.exceptiongenerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import ncl.ac.uk.esc.monitor.connection;
import ncl.ac.uk.esc.monitor.connectionPool.getStatue;


public class exceptionGenerator {
	startServer start;
	shutdownServer stop;
	private Hashtable<String, connection> connectionPool;

	public exceptionGenerator(){
		connectionPool=getStatue.getConnections();
		
	}
	public void shutDown(String machine) throws IOException{
		
		connection connection=connectionPool.get(machine);
	
		sendStopMessage(connection);
		
	}
	public void turnOn(String machine) throws IOException{
		connection connection=connectionPool.get(machine);
		
		sendStartMessage(connection);
	}
	
	private void sendStartMessage(connection connection) throws IOException {
		try {
			SshStart();
		} catch (TaskExecFailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(stop instanceof shutdownServer ){
			if(stop.isAlive()){
				stop.setStop(true);
			}
		}
		
		start=new startServer();
		start.start();
		Socket serverSocket=connection.getSocket();
		PrintWriter out = new PrintWriter(  
                new BufferedWriter(new OutputStreamWriter(  
                        serverSocket.getOutputStream())), true);
	
		out.println("start");
		
	}
	private void sendStopMessage(connection connection) throws IOException{
		try {
			SshShutdown();
		} catch (TaskExecFailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(start instanceof startServer){
			if(start.isAlive()){
				start.setStop(true);
			}
		}
	
		stop=new shutdownServer();
		stop.start();
		Socket serverSocket=connection.getSocket();
		PrintWriter out = new PrintWriter(  
                new BufferedWriter(new OutputStreamWriter(  
                        serverSocket.getOutputStream())), true);
		out.println("stop");
	}
	
	public void SshShutdown() throws TaskExecFailException{
		SSHExec ssh = null;
		ConnBean cb = new ConnBean("10.8.149.12", "zhenyu","5lovesunny");
		 ssh = SSHExec.getInstance(cb);   
		  CustomTask ct1 = new ExecCommand(" /home/zhenyu/jboss/bin/jboss-cli.sh --connect command=:shutdown");
		  ssh.connect();
		  ssh.exec(ct1);
		  ssh.disconnect();

	}
	
	public void SshStart() throws TaskExecFailException{
		SSHExec ssh = null;
		ConnBean cb = new ConnBean("10.8.149.12", "zhenyu","5lovesunny");
		 ssh = SSHExec.getInstance(cb);   
		  CustomTask ct1 = new ExecCommand(" /home/zhenyu/jboss/bin/standalone.sh");
		  ssh.connect();
		  ssh.exec(ct1);
		  ssh.disconnect();

	}
	
}
*/