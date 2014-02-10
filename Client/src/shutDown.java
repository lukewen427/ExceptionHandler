

import java.io.IOException;

public class shutDown {
	public shutDown() throws IOException{
		shutDownserver();
	}
	public void shutDownserver() throws IOException{
	
		 Process proc=null;
		 String cmd=" /Users/zhenyuwen/jboss-as-7.1.1.Final/bin/jboss-cli.sh --connect command=:shutdown";
	//	 String cmd=" /home/zhenyu/jboss/bin/jboss-cli.sh --connect command=:shutdown";
		 proc = Runtime.getRuntime().exec(cmd);
		 System.out.println("machine down");
	}
}
