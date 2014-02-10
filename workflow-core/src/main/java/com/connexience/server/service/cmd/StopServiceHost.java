/*
 * StopServiceHost.java
 */

package com.connexience.server.service.cmd;

import com.connexience.server.util.*;
import com.connexience.server.rmi.*;

/**
 * Terminate the service host process on the target machine
 * @author hugo
 */
public class StopServiceHost {
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
            connection.shutdownHost();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}