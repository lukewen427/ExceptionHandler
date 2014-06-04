package ncl.ac.uk.esc.exceptiongenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class startServer implements Runnable {
	private Boolean stop = false;
	Thread t;
	public startServer(String name){
		t=new Thread(this,name);
		t.start();
	}
	public void run(){
		System.out.println(t.getName()+"starting");
		String password = null;
		String path = null;
		String path2=null;
		String userName=null;
		 try {
			 
				 password="escience";
				 path=" /home/hugo/jboss/bin/standalone.sh";
				 path2=" /home/hugo/code/webflow/WorkflowCloud/bin/startserver.sh";
				 userName="hugo";
			 
			 JSch jsch=new JSch(); 
			Session session=jsch.getSession(userName, t.getName(),22);
			
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			
		//	 ChannelExec channel = (ChannelExec) session.openChannel("exec");
	//		 channel.setCommand(path);
		    	Channel channel=session.openChannel("shell");
		    	OutputStream ops = channel.getOutputStream();
		    	PrintStream ps=new PrintStream(ops,true);
		    	channel.connect();
		    	
		//	 ((ChannelExec) channel).setErrStream(System.err);
			    InputStream in = null;
				try {
					in = channel.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    ps.println(path);
			    ps.println(path2);
			    System.out.println("Unix system connected...");
			    System.out.println("Service is starting...");
			  long  startTime=System.currentTimeMillis(); 
			    byte[] tmp = new byte[1024];
			    while (!stop){
			        while (in.available() > 0 ) {
			            int i = in.read(tmp, 0, 1024);
			            if (i < 0) {
			                break;
			            }
			   //         String line = new String(tmp, 0, i);
			      //      System.out.println("Unix system console output: " +line);
			        }
			        long  finishTime=System.currentTimeMillis();
		//	        System.out.println(finishTime-startTime);
			        if(in.read(tmp, 0, 1024)<0){
			        	break;
			        }
			        if (channel.isClosed()){
			            break;
			        }
			        try {
			            Thread.sleep(1000);
			        } catch (Exception ee){
			            //ignore
			        }
			    }
			    channel.disconnect();
			    session.disconnect();     
			
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	 public void setStop(Boolean stop){
		 this.stop=stop;
	 }
	
}
