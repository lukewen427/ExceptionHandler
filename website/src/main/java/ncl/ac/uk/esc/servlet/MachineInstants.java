package ncl.ac.uk.esc.servlet;

import java.util.ArrayList;

public class MachineInstants {
	 protected  ArrayList<Machine> Machines=new ArrayList<Machine>();
	public MachineInstants()
	{
		Machine machine1=new Machine("10.8.149.12","OFF","1");
		Machine machine2=new Machine("10.66.66.176","OFF","0");
		Machines.add(machine1);
		Machines.add(machine2);
	}
	
	public ArrayList<Machine> getMachines(){
		return Machines;
	}
}
