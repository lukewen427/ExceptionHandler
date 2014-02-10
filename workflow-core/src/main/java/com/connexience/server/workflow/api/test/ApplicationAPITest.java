/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.connexience.server.workflow.api.test;

import com.connexience.server.workflow.api.*;
import java.net.*;
import com.connexience.server.workflow.api.rpc.*;
import com.connexience.server.workflow.rpc.*;
import com.connexience.server.model.security.*;
import java.io.*;

/**
 *
 * @author hugo
 */
public class ApplicationAPITest {
    public static void main(String[] args){
        try {
            final String appId = "8aa7b3ad27d544e40127d955fff5000e";
            final String dataId = "40288b8c2844f89a0128457263dd0005";

            final RPCClient client = new RPCClient("http://localhost:8080/WorkflowServer/WorkflowServlet");
            
            CallInvocationListener listener = new CallInvocationListener() {

            public void callSucceeded(CallObject call) {
                client.setSessionId(call.getReturnArguments().stringValue("SessionID", ""));
                Ticket ticket = new Ticket();
                ticket.setId(call.getReturnArguments().stringValue("TicketID", ""));
                ticket.setOrganisationId(call.getReturnArguments().stringValue("OrganisationID", ""));
                ticket.setUserId(call.getReturnArguments().stringValue("UserID", ""));
                try {
                    RPCClientApi api = new RPCClientApi(client, ticket);
                    System.out.println("User: " + api.getUserContextId());
                    ApplicationHttpClient httpClient = new ApplicationHttpClient(api, appId);
                    
                    URL u = new URL("http://localhost:8080/DataServiceWeb/query.jsp?startrow=1&endrow=100&dataid=" + dataId);
                    HttpURLConnection connection = httpClient.getConnection(u);
                    InputStream inStream = connection.getInputStream();
                    int value;
                    while((value=inStream.read())!=-1){
                        System.out.write(value);
                    }
                    System.out.flush();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            public void callFailed(CallObject call) {
                System.out.println("Call failed: " + call.getStatusMessage());
            }
        };


            CallObject call = new CallObject("authenticate");
            call.getCallArguments().add("Username", "h.g.hiden@ncl.ac.uk");
            call.getCallArguments().add("Password", "V1an1W");
            call.setAuthenticationRequired(false);
            client.asyncCall(call, listener);
            
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
