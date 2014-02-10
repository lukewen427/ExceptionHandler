package ncl.ac.uk.esc.exceptiongenerator;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class shutdownServer extends Thread {
	
	private Boolean stop = false;
	public void run(){
		 JSch jsch=new JSch(); 
		 try {
			Session session=jsch.getSession("zhenyu", "10.8.149.12",22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword("5lovesunny");
			session.connect();
			 ChannelExec channel = (ChannelExec) session.openChannel("exec");
			  channel.setCommand(" /home/zhenyu/jboss/bin/jboss-cli.sh --connect command=:shutdown");
			 ((ChannelExec) channel).setErrStream(System.err);
			    InputStream in = null;
				try {
					in = channel.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    channel.connect();
			    System.out.println("Unix system connected...");
			    byte[] tmp = new byte[1024];
			    while (!stop){
			        while (in.available() > 0 || !stop) {
			            int i = in.read(tmp, 0, 1024);
			            if (i < 0) {
			                break;
			            }
			 //           String line = new String(tmp, 0, i);
			  //          System.out.println("Unix system console output: " +line);
			        }
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
