/*
 * ListServices.java
 */

package com.connexience.server.service.cmd;

import com.connexience.server.util.*;
import com.connexience.server.rmi.*;

import java.util.*;

/**
 * This command lists the services running on a server
 * @author hugo
 */
public class ListServices {
    public static void main(String[] args){
        String host;
        if(args.length==1){
            host = args[0];
        } else {
            host = "localhost";
        }

        try {
            IServiceHost server = (IServiceHost)RegistryUtil.lookup(host, "ServiceHost");
            IServiceHostConnection connection = server.openConnection("", "");
            List<ServiceDescriptor> services = connection.listServices();
            System.out.println("Services running on: " + host);
            for(int i=0;i<services.size();i++){
                if(services.get(i).isRunning()){
                    System.out.println(i + ") " + services.get(i).getName() + ": RUNNING");
                } else {
                    System.out.println(i + ") " + services.get(i).getName() + ": STOPPED");
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
