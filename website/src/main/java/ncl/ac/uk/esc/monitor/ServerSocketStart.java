package ncl.ac.uk.esc.monitor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;



public class ServerSocketStart extends Thread {
    private int port;
    private int maxConnections;
    public ServerSocketStart(int aListenPort, int maxConnections){
        this.port=aListenPort;
        this.maxConnections=maxConnections;
    }
    public void run() {
    		  
        ServerSocketFactory serverSocketFactory=ServerSocketFactory.getDefault();
        try {
            ServerSocket serverSocket=serverSocketFactory.createServerSocket(port);
            //serverSocketFactory.
            Socket request = null;
            System.out.println("++++++ ServerSocket starting ++++++");
           connectionPool connectionPool= new connectionPool();
           Thread threadPool=new Thread(connectionPool);
          	threadPool.start();
            while(true) {  
                request = serverSocket.accept();
             //   System.out.println(request.isConnected());
                connection newCon=new connection(request,connectionPool);
                Thread threadwork=new Thread(newCon);
                threadwork.start();
                connectionPool.addConnection(newCon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

