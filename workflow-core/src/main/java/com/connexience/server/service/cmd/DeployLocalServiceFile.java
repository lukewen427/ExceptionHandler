/*
 * DeployLocalServiceFile.java
 */

package com.connexience.server.service.cmd;

import com.connexience.server.util.RegistryUtil;
import com.connexience.server.rmi.*;

import java.io.*;

/**
 * Standalone command tp deploy a zip file into the service directory.
 * @author hugo
 */
public class DeployLocalServiceFile {
    public static void main(String[] args){
        String fileName;
        String serviceName;
        if(args.length==1){
            fileName = args[0];
            File serviceFile = new File(fileName);
            if(serviceFile.exists()){
                try {
                    IServiceHost server = (IServiceHost)RegistryUtil.lookup("localhost", "ServiceHost");
                    IServiceHostConnection connection = server.openConnection("", "");
                    connection.deployLocalServiceFile(serviceFile, false);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            } else {
                System.out.println("Cannot find file: " + fileName);
            }
        } else {
            System.out.println("Needs two arguments: ServiceName, FileName");
            System.exit(1);
        }
    }
}