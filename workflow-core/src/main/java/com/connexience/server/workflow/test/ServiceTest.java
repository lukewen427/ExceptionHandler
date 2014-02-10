/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.connexience.server.workflow.test;

import com.connexience.server.workflow.rpc.*;
import org.pipeline.core.xmlstorage.prefs.*;
import java.io.*;

/**
 *
 * @author hugo
 */
public class ServiceTest {
    public static void main(String[] args){
        try {
            // Load public and private keys
            if(!PreferenceManager.loadKeystoreFromHomeDir(".inkspot", "ServiceHost.keystore")) {
                System.out.println("Need to fetch the keystore from the server first");
                System.exit(1);
            }

            RPCClient client = new RPCClient("http://localhost:8080/WorkflowServer/WorkflowServlet");
            client.setSecurityMethod(RPCClient.PRIVATE_KEY_SECURITY);
            client.setPrivateKey(PreferenceManager.getPrivateKey());
            client.setSigningUserId(PreferenceManager.getCertificateOwnerId());

            CallObject call = new CallObject("SVCFindRunningService");
            call.getCallArguments().add("ServiceName", "DataService");

            client.syncCall(call);
            if(call.getStatus()==CallObject.CALL_EXECUTED_OK){
                PrintWriter writer = new PrintWriter(System.out);
                call.getReturnArguments().debugPrint(writer, 5);
                writer.flush();
                writer.close();
            } else {
                System.out.println("Error: " + call.getStatusMessage());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
