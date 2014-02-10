/*
 * FetchKeystore.java
 */

package com.connexience.server.service.cmd;

import org.pipeline.core.xmlstorage.prefs.*;
import java.io.*;

/**
 * This command fetches the keystore from the remote server and saves them
 * in the storage directory
 * @author hugo
 */
public class FetchKeystore {
    public static void main(String[] args){
        try {
            System.out.print("Server [localhost]: ");
            String server = System.console().readLine();
            if(server.isEmpty()){
                server = "localhost";
            }

            System.out.print("Port [8080]: ");
            String p = System.console().readLine();
            int port;
            if(p.isEmpty()){
                port = 8080;
            } else {
                port = Integer.parseInt(p);
            }

            System.out.print("Username []: ");
            String username = System.console().readLine();

            System.out.print("Password []: ");
            String password = new String(System.console().readPassword());


            String filename = System.getProperty("user.home") + "/.inkspot/ServiceHost.keystore";
            System.out.print("Filename [" + filename + "]: ");
            String nf = System.console().readLine();
            if(!nf.isEmpty()){
                filename = nf;
            }
            
            File keystoreFile = new File(filename);
            PreferenceManager.fetchKeystoreFromServer(server, port, keystoreFile, username, password);
            System.out.println("Fetched keystore for user ID: " + PreferenceManager.getCertificateOwnerId());
            System.out.println("Certificate: " + PreferenceManager.getCertificate());

        } catch (Exception e){
            System.out.println("Error fetching keystore: " + e.getMessage());
        }
    }
}