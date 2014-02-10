import java.io.IOException;


public class start {
	public start(){
		startServer();
	}
	public void startServer(){
		 Process proc=null;
			 String cmd=" /Users/zhenyuwen/jboss-as-7.1.1.Final/bin/standalone.sh";
			//	 String cmd=" /home/zhenyu/jboss/bin/jboss-cli.sh --connect command=:shutdown";
			 try {
				proc = Runtime.getRuntime().exec(cmd);
				System.out.println("machine start");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
