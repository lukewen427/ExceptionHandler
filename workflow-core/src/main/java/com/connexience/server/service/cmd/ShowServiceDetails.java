/*
 * ShowServiceDetails.java
 */

package com.connexience.server.service.cmd;

import com.connexience.server.util.*;
import com.connexience.server.rmi.*;
import com.connexience.server.workflow.util.*;

/**
 * Show the service document details for a service if it was downloaded from
 * the main data store.
 * @author hugo
 */
public class ShowServiceDetails {
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
            ServiceDocumentDetails details = connection.getServiceDocumentDetails(serviceName);
            System.out.println("File name: " + details.getName());
            System.out.println("Version number: " + details.getVersionNumber());
            System.out.println("File ID: " + details.getDocumentId());
            System.out.println("Version ID: " + details.getVersionId());
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }
}