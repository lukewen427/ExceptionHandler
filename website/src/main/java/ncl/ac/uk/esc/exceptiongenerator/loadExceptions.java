package ncl.ac.uk.esc.exceptiongenerator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class loadExceptions {
	long startTime;
	/*get the time from the server*/
	public loadExceptions( long startTime){
		
		this.startTime=startTime;
		System.out.println(System.currentTimeMillis()-startTime);
	}
	/*create thread for each machine to generate exceptions*/
	public void createThread(){
		HashMap<String,ArrayList<ArrayList<String>>> machines = null;
		try {
			 machines=readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(machines.isEmpty()){
			
		}else{
			Set<String>machineName=machines.keySet();
			Iterator<String>Names=machineName.iterator();
			while(Names.hasNext()){
				String singalMachine=Names.next();
				ArrayList<ArrayList<String>> actions=machines.get(singalMachine);
				new exceptionGeneratorThread(singalMachine,startTime,actions);
			}
		}
		
		
	}
	
	/*read the exceptions which have predefined in a file*/
	public HashMap<String,ArrayList<ArrayList<String>>> readFile() throws IOException{
		String url="/Users/zhenyuwen/git/ExceptionHandler/website/";
			BufferedReader br= new BufferedReader(new InputStreamReader(
							new FileInputStream(url+"exceptions.txt")));
			String data;
			/* 
			 * classify exceptions by different machines store in a map
			 * key is the machine ip value is a list store the times and actions
			*/
			HashMap<String,ArrayList<ArrayList<String>>> machines=new HashMap<String,ArrayList<ArrayList<String>>>();
			Set<String>machineName=machines.keySet();
			while((data = br.readLine())!=null){
				String[] machineArr=data.split(" ");
				String name=machineArr[0];
				if(machineName.contains(name)){
					ArrayList<ArrayList<String>> theMachine=machines.get(name);
					ArrayList<String> exception=new ArrayList<String>();
					exception.add(machineArr[1]);
					exception.add(machineArr[2]);
					theMachine.add(exception);
					machines.put(name, theMachine);
				}else{
					ArrayList<ArrayList<String>> newMachine=new ArrayList<ArrayList<String>>();
					ArrayList<String> exception=new ArrayList<String>();
					exception.add(machineArr[1]);
					exception.add(machineArr[2]);
					newMachine.add(exception);
					machines.put(name, newMachine);
				}
			}
			return machines;
	}
	
	public static void main(String [] args) throws IOException{
		loadExceptions test=new loadExceptions(System.currentTimeMillis());
				test.createThread();
	}
}
