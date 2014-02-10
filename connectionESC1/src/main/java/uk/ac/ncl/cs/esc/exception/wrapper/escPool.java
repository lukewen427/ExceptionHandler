package uk.ac.ncl.cs.esc.exception.wrapper;

public class escPool {
	String firstserver;
	String secondserver;
	long runingtime1;
	long runingtime2;
	Thread one;
	Thread two;
	public escPool(){
		this.firstserver="eSC1";
		this.secondserver="eSC2";
		this.runingtime1=20000;
		this.runingtime2=10000;
	}
	public void startServer(){
		 one=new CreateServer(runingtime1,firstserver);
		one.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 two=new CreateServer(runingtime2,secondserver);
		two.start();
	}
	
	public boolean isAvailable(String cloud){
		boolean isAva;
		if(cloud.equals("C0")){
			isAva=one.isAlive();
		}else{
			isAva=two.isAlive();
		}
		return isAva;
	}
	public  static void main(String arc[]){
		escPool test=new escPool();
		test.startServer();
	}
}
