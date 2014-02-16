package ncl.ac.uk.esc.exceptiongenerator;

import java.io.IOException;
import java.io.InputStream;

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
		String userName=null;
		 try {
			 if(t.getName().equals("10.8.149.12")){
				 password="5lovesunny";
				 path=" /home/zhenyu/jboss/bin/standalone.sh";
				 userName="zhenyu";
			 }else{
				 password="escience";
				 path=" /home/hugo/jboss/bin/standalone.sh";
				 userName="hugo";
			 }
			 JSch jsch=new JSch(); 
			Session session=jsch.getSession(userName, t.getName(),22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			 ChannelExec channel = (ChannelExec) session.openChannel("exec");
			 channel.setCommand(path);
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
