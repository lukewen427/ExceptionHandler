/*
 * KeyTest.java
 */

package com.connexience.server.workflow.test;

import com.connexience.server.workflow.rpc.*;
import org.pipeline.core.xmlstorage.prefs.*;
import java.io.*;
/**
 * Tests acquisition of key data
 * @author hugo
 */
public class KeyTest {

    public static void main(String[] args){
        try {
            File keystoreFile = new File(System.getProperty("user.home") + File.separator + ".inkspot", "Keystore.dat");
            if(!PreferenceManager.loadKeystoreFromFile(keystoreFile)){
                PreferenceManager.fetchKeystoreFromServer("localhost", 8080, keystoreFile, "h.g.hiden@ncl.ac.uk", "V1an1W");
            }

            RPCClient client = new RPCClient("http://localhost:8080/WorkflowServer/WorkflowServlet");
            CallObject call = new CallObject("DUMMY");
            client.setSecurityMethod(RPCClient.PRIVATE_KEY_SECURITY);
            client.setPrivateKey(PreferenceManager.getPrivateKey());
            client.setSigningUserId(PreferenceManager.getCertificateOwnerId());
            client.syncCall(call);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
