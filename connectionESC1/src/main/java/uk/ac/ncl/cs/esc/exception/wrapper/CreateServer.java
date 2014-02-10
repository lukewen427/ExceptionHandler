package uk.ac.ncl.cs.esc.exception.wrapper;

public class CreateServer extends Thread {
	long runingtime;
	String name;
	public CreateServer(long runingtime,String name){
		super(name);
		this.runingtime=runingtime;
		this.name=name;
	}
	public void run(){
		System.out.println("server "+name+" start runing");
		try{
			System.out.println(runingtime);
			Thread.sleep(runingtime);
		}catch(InterruptedException e){
			
		}
		System.out.println("server "+name+" shutdown");
	}
}
