/*
 * ShowServiceLog.java
 */

package com.connexience.server.service.cmd;

import com.connexience.server.util.*;
import com.connexience.server.rmi.*;
import com.connexience.server.workflow.util.*;

/**
 * Displays the contexnts of the service log file
 * @author hugo
 */
public class ShowServiceLog {
    public static void main(String[] args){
        String host = "localhost";
        String serviceName = "";

        if(args.length==2){
            host = args[0];
            serviceName = args[1];
        } else if (args.length==1) {
            host = "localhost";
            serviceName = args[0];

        } else {
            System.out.println("Incorrect number of arguments");
            System.exit(1);
        }

        try {
            IServiceHost server = (IServiceHost)RegistryUtil.lookup(host, "ServiceHost");
            IServiceHostConnection connection = server.openConnection("", "");
            RMIInputStream stream = new RMIInputStream(connection.openLogFile(serviceName));
            StreamDumper dumper = new StreamDumper(stream, System.out);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }
}