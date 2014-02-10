


import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

public class ssh {
	

	
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
	
	public static void main(String [] arg) throws TaskExecFailException{
		new ssh().SshStart();
	}
	
}
