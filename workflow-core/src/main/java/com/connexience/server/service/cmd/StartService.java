/*
 * StartService.java
 */

package com.connexience.server.service.cmd;

import com.connexience.server.util.*;
import com.connexience.server.rmi.*;

import java.util.*;


/**
 * Start a specified service
 * @author hugo
 */
public class StartService {
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
            connection.startService(serviceName);
            System.exit(0);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }
}