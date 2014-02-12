package ncl.ac.uk.esc.servlet;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import ncl.ac.uk.esc.monitor.connection;
import ncl.ac.uk.esc.monitor.connectionPool;
import ncl.ac.uk.esc.monitor.connectionPool.getStatue;

public class MachinePool extends Thread {

	private ArrayList<Machine> Machines;
	
	protected  Hashtable<String, Machine> threads=new Hashtable<String, Machine>();
	public MachinePool(){
		Machines=new MachineInstants().getMachines();
	
		addMachine(Machines);
		new getStatue(this);
	}
	public void run() {
		
	while(true){
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
			}
	}
	
	public synchronized Hashtable<String, Machine> getThreads(){
		return threads;
	}
	public  synchronized void changeStatus(String machineName){
		Machine theMachine=threads.get(machineName);
		String Statue=theMachine.getStatue();
		if(Statue.equals("OFF")){
			theMachine.setStatue("ON");
		}else{
			theMachine.setStatue("OFF");
		}
		threads.put(machineName, theMachine);
	}
	public void addMachine(ArrayList<Machine> Machines){
		for(Machine machine : Machines){
			
			String Ip=machine.getIp();
			threads.put(Ip, machine);
		}
	}
	
	public static class getStatue{
		static MachinePool thePool;
		static Hashtable<String, Machine> thread;
		public static ArrayList<Machine> machines;
		getStatue(MachinePool thePool){
			this.thePool=thePool;
			setThread();
		}
		public static void setThread(){
			thread=thePool.getThreads();
		}
		
		
		
		public static void setMahines(){
			if(thread.isEmpty()){
				machines=null;
			}else{
				machines=new ArrayList<Machine>(thread.values());
			}
		}
		
		public static ArrayList<Machine> getMachines(){
			 setMahines();
			return machines;
		}
	
	}
}
