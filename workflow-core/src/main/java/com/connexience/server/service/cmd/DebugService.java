/*
 * DebugService.java
 */

package com.connexience.server.service.cmd;
import com.connexience.server.util.*;
import com.connexience.server.rmi.*;

/**
 * Start a service with the debugger enabled
 * @author hugo
 */
public class DebugService {
    public static void main(String[] args){
        String host = "localhost";
        String serviceName = "";
        int port = 5005;

        if(args.length==3){
            host = args[0];
            port = Integer.parseInt(args[1]);
            serviceName = args[2];

        } else if (args.length==2) {
            host = "localhost";
            port = Integer.parseInt(args[0]);
            serviceName = args[1];

        } else if (args.length==1){
            host = "localhost";
            port = 5005;
            serviceName = args[0];

        } else {
            System.out.println("Incorrect number of arguments");
            System.exit(1);
        }

        try {
            IServiceHost server = (IServiceHost)RegistryUtil.lookup(host, "ServiceHost");
            IServiceHostConnection connection = server.openConnection("", "");
            connection.startServiceDebug(serviceName, port);
            System.exit(0);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }
}
