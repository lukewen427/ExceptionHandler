
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;





public class TestClient {
	private static boolean connected=false;
	public static void main(String [] arg) throws Exception{
		InetAddress IPAddress = InetAddress.getByName("10.14.1.190");
	//	InetAddress IPAddress = InetAddress.getByName("10.14.128.219");
		
		String sendData="working";
		Socket clientSocket = new Socket(IPAddress,4444);
		connected =true;
		while(true){
			
			PrintWriter out = new PrintWriter(  
                    new BufferedWriter(new OutputStreamWriter(  
                            clientSocket.getOutputStream())), true);
			
			if(connected==true){
				
				out.println(sendData);
			}
		//	out.println(sendData);
			BufferedReader input =
		            new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String in=input.readLine();
			
			if(in.equals("stop")){
				out.println("stop");
				new shutDown();
				connected=false;
			}
			if(in.equals("start")){
				out.println("start");
				new start();
				connected=true;
			}
		}
		
		
	}
}
