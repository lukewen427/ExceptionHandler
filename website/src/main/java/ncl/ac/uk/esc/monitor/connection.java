package ncl.ac.uk.esc.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class connection implements  Runnable {

	protected Socket connection;
	private InetAddress inet;
	private int port;
	private connectionPool thePool;
	public connection(Socket s,connectionPool pool){
		this.connection=s;
		this.thePool=pool;
		setPort();
		setAddress();
	}
	
	public void setPort(){
		this.port=connection.getPort();
		
	}
	public int getPort(){
		return port;
	}
	public void setAddress(){
		this.inet=connection.getInetAddress();
	}
	public InetAddress getAddress(){
		return inet;
	}
	public Socket getSocket(){
		return connection;
	}

	@Override
	public void run() {
		while(true){
			
			try {
				
				
					BufferedReader input =
				            new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String in=input.readLine();
					
				//	System.out.println(in);
					if(in.equals("working")){
						thePool.addService(inet.getHostAddress());
					}
					if(in.equals("start")){
						thePool.addService(inet.getHostAddress());
					}
					if(in.equals("stop")){
						System.out.println("i am out");
						thePool.removeService(inet.getHostAddress());
						
					}
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
