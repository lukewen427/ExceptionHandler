/*
 * ServiceInstanceLookup.java
 */

package com.connexience.server.service.util;

import com.connexience.server.api.*;
import com.connexience.server.model.service.*;
import com.connexience.server.workflow.rpc.*;

import java.net.*;

/**
 * This class looks up a running service and returns some valid connection
 * data
 * @author hugo
 */
public class ServiceInstanceLookup {
    /** Client object */
    private RPCClient client;

    /** Service name */
    private String name;

    public ServiceInstanceLookup(API api, String name) {
        URL serverUrl = api.getServerUrl();
        String url = "http://" + serverUrl.getHost() + ":" + serverUrl.getPort() + "/WorkflowServer/WorkflowServlet";
        this.client = new RPCClient(url);
        client.setSecurityMethod(RPCClient.SESSION_SECURITY);
        client.setSessionIdRequired(false);
        this.name = name;
    }

  public ServiceInstanceLookup(String server, int port, String name) {
          String url = "http://" + server + ":" + port + "/WorkflowServer/WorkflowServlet";
          this.client = new RPCClient(url);
          client.setSecurityMethod(RPCClient.SESSION_SECURITY);
          client.setSessionIdRequired(false);
          this.name = name;
      }


    /** Get hold of the service instance */
    public ServiceInstance getServiceInstance() throws Exception {
        CallObject call = new CallObject("SVCFindRunningService");
        call.getCallArguments().add("ServiceName", name);
        client.syncCall(call);
        if(call.getStatus()==CallObject.CALL_EXECUTED_OK){
            ServiceInstance instance = new ServiceInstance();
            instance.setHostId(call.getReturnArguments().stringValue("HostID", ""));
            instance.setRegisteredIpAddress(call.getReturnArguments().stringValue("RegisteredIPAddress", null));
            instance.setName(call.getReturnArguments().stringValue("Name", ""));
            instance.setRmiPort(call.getReturnArguments().intValue("RMIPort", 1099));
            instance.setRunning(call.getReturnArguments().booleanValue("Running", false));
            instance.setId(call.getReturnArguments().longValue("ID", 0));
            return instance;
        } else {
            throw new Exception("Call error: " + call.getStatusMessage());
        }
    }

    /** Get hold of a service instance on a specific HostID. This is used
     * because the RMI port of the service may have changed if the server
     * was restarted */
    public ServiceInstance getServiceInstance(String hostId) throws Exception {
        CallObject call = new CallObject("SVCGetRunningServiceOnHost");
        call.getCallArguments().add("ServiceName", name);
        call.getCallArguments().add("HostID", hostId);
        client.syncCall(call);
        if(call.getStatus()==CallObject.CALL_EXECUTED_OK){
            ServiceInstance instance = new ServiceInstance();
            instance.setHostId(call.getReturnArguments().stringValue("HostID", ""));
            instance.setRegisteredIpAddress(call.getReturnArguments().stringValue("RegisteredIPAddress", null));
            instance.setName(call.getReturnArguments().stringValue("Name", ""));
            instance.setRmiPort(call.getReturnArguments().intValue("RMIPort", 1099));
            instance.setRunning(call.getReturnArguments().booleanValue("Running", false));
            instance.setId(call.getReturnArguments().longValue("ID", 0));
            return instance;
        } else {
            throw new Exception("Call error: " + call.getStatusMessage());
        }
    }
}