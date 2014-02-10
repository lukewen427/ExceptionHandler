/*
 * DeployServiceFromServer.java
 */

package com.connexience.server.service.cmd;

import com.connexience.server.util.RegistryUtil;
import com.connexience.server.rmi.*;

/**
 * This command deploys a file from the servers data store into the local
 * service host
 * @author hugo
 */
public class DeployServiceFromServer {
    public static void main(String[] args){
        String documentId;

        if(args.length==1){
            documentId = args[0];
            try {
                IServiceHost server = (IServiceHost)RegistryUtil.lookup("localhost", "ServiceHost");
                IServiceHostConnection connection = server.openConnection("", "");
                connection.deployServiceFromServer(documentId, false);
            } catch (Exception e){
                System.out.println(e.getMessage());
                System.exit(1);
            }

        } else {
            System.out.println("Needs one argument: DocumentID");
            System.exit(1);
        }
    }
}