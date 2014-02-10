/*
 * RPCClientWorkflowAPI.java
 */

package com.connexience.server.workflow.api.rpc;

import com.connexience.server.model.workflow.control.WorkflowEngineStatusData;
import com.connexience.server.workflow.rpc.*;
import com.connexience.server.api.*;
import com.connexience.server.model.security.*;
import com.connexience.server.util.SerializationUtils;
import com.connexience.server.workflow.api.*;
import com.connexience.server.workflow.service.*;
import com.connexience.server.workflow.xmlstorage.WorkflowLockWrapper;

import java.io.*;
import java.util.*;
import org.pipeline.core.xmlstorage.XmlStorageException;

/**
 * This class provides a workflow api client using workflow RPC as the communication
 * mechanism.
 * @author nhgh
 */
public class RPCClientWorkflowAPI implements WorkflowAPI, Serializable {
    /** RPCClient used for communication */
    private RPCClient client;

    /** Security ticket */
    private Ticket ticket;

    /** Number of times to try resending a call */
    int retryCount = 20;
    
    /** Starting waiting interval at first failure */
    int initialRetryInterval = 1000;
    
    /** Maximum waiting interval after ramping up */
    int maxWaitInterval = 10000;
    
    /** Interval multiplication factor for each failed attempt */
    double retryMultiplier = 2;
    
    public RPCClientWorkflowAPI(RPCClient existingClient) {
        client = new RPCClient(existingClient.getServerUrl());
    }

    public RPCClientWorkflowAPI(RPCClient existingClient, Ticket ticket) throws RPCException {
        this.client = new RPCClient(existingClient.getServerUrl());
        client.setSecurityMethod(RPCClient.TICKET_SECURITY);
        client.setTicket(ticket);
        this.ticket = ticket;
    }

    public RPCClient getClient(){
        return client;
    }
    
    /** Terminate the RPC client object */
    public void terminateClient(){
        client.close();
    }
    
    /** Send ticket details */
    private void sendTicket(boolean force) throws RPCException {
        // Do an authentication
        if(force || client.getSessionId()==null ||client.getSessionId().trim().equalsIgnoreCase("")){
            if(ticket!=null){
                CallObject call = new CallObject("APISetTicket");
                call.getCallArguments().add("UserID", ticket.getUserId());
                call.setAuthenticationRequired(false);
                client.syncCall(call);
                client.setSessionId(call.getReturnArguments().stringValue("SessionID", null));
            }
        }
    }
    
    public void setInitialRetryInterval(int initialRetryInterval) {
        this.initialRetryInterval = initialRetryInterval;
    }

    public int getInitialRetryInterval() {
        return initialRetryInterval;
    }

    public void setMaxWaitInterval(int maxWaitInterval) {
        this.maxWaitInterval = maxWaitInterval;
    }

    public int getMaxWaitInterval() {
        return maxWaitInterval;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryMultiplier(double retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
    }

    public double getRetryMultiplier() {
        return retryMultiplier;
    }    

    public IWorkflowInvocation saveWorkflowInvocation(IWorkflowInvocation invocation) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISaveWorkflowInvocation");

        try {
            call.getCallArguments().add("Invocation", new InkspotObjectTransport(invocation));
        } catch (Exception e){
            throw new APIConnectException("Error saving invocation data: " + e.getMessage());
        }

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IWorkflowInvocation)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Invocation")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error saving workflow invocation: " + e.getMessage());
            }    
        } else {
            throw new APIConnectException("Could not send call after retrying");
        }
        
    }
    
    /** Set the engine ID for an invocation ID */
    public void setInvocationEngineId(String invocationId, String engineId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISetWorkflowEngineID");
        call.getCallArguments().add("InvocationID", invocationId);
        call.getCallArguments().add("EngineID", engineId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Could not send call after retrying" + call.getStatusMessage());
        }
    }

    @Override
    public void notifyEngineStartup(String engineId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APINotifyEngineStartup");
        call.getCallArguments().add("EngineID", engineId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Could not send call after retrying" + call.getStatusMessage());
        }
    }

    @Override
    public void notifyEngineShutdown(String engineId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APINotifyEngineShutdown");
        call.getCallArguments().add("EngineID", engineId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Could not send call after retrying" + call.getStatusMessage());
        }
    }
    
    

    /** Send the blog debugging data back to the server */
    public void updateServiceLog(String invocationId, String contextId, String outputData, String statusText, String statusMessage) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIUpdateServiceLog");
        call.getCallArguments().add("InvocationID", invocationId);
        call.getCallArguments().add("ContextID", contextId);
        if(outputData!=null){
            call.getCallArguments().add("OutputData", outputData);
        }
        call.getCallArguments().add("StatusText", statusText);
        call.getCallArguments().add("StatusMessage", statusMessage);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Could not send call after retrying" + call.getStatusMessage());
        }
    }

    /** Update just the message part of a service log */
    public void updateServiceLogMessage(String invocationId, String contextId, String statusText, String statusMessage) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIUpdateServiceLogMessage");
        call.getCallArguments().add("InvocationID", invocationId);
        call.getCallArguments().add("ContextID", contextId);
        call.getCallArguments().add("StatusText", statusText);
        call.getCallArguments().add("StatusMessage", statusMessage);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Could not send call after retrying" + call.getStatusMessage());
        }        
    }
    
    
    /** Get the latest document versions for a list of IDs */
//    @Override
    public List<IDocumentVersion> getLatestVersions(List<String> documentIds) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetLatestVersions");
        call.getCallArguments().add("DocumentCount", documentIds.size());
        for(int i=0;i<documentIds.size();i++){
            call.getCallArguments().add("Document" + i + "ID", documentIds.get(i));
        }

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }


        if(success){
            try {
                ArrayList<IDocumentVersion> results = new ArrayList<IDocumentVersion>();

                int size = call.getReturnArguments().intValue("VersionCount", 0);
                for(int i=0;i<size;i++){
                    results.add((IDocumentVersion)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Version" + i)).getObject());
                }

                return results;

            } catch (Exception e){
                throw new APIConnectException("Error accessing returned version list: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** Get the XML definition for a service */
    public DataProcessorServiceDefinition getService(String serviceId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("getService");
        call.getCallArguments().add("ServiceID", serviceId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            if(call.getReturnArguments().containsName("ServiceXML")){
                DataProcessorServiceDefinition def = new DataProcessorServiceDefinition();
                try {
                    def.loadXmlString(call.getReturnArguments().stringValue("ServiceXML", null));
                    if(call.getReturnArguments().containsName("VersionNumber")){
                        def.setVersionNumber(call.getReturnArguments().intValue("VersionNumber", 0));
                    }
                    return def;
                } catch (Exception e){
                    throw new APIConnectException("Error parsing service XML: " + e.getMessage(), e);
                }
            } else {
                throw new APIConnectException("No service found");
            }
        } else {
            throw new APIConnectException("Could not send call after retrying" + call.getStatusMessage());
        }
    }

    /** Get the XML definition for a specific version of a service */
    public DataProcessorServiceDefinition getService(String serviceId, String versionId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("getService");
        call.getCallArguments().add("ServiceID", serviceId);
        call.getCallArguments().add("VersionID", versionId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }
        
        if(success){
            if(call.getReturnArguments().containsName("ServiceXML")){
                DataProcessorServiceDefinition def = new DataProcessorServiceDefinition();
                try {
                    def.loadXmlString(call.getReturnArguments().stringValue("ServiceXML", null));
                    if(call.getReturnArguments().containsName("VersionNumber")){
                        def.setVersionNumber(call.getReturnArguments().intValue("VersionNumber", 0));
                    }
                    return def;
                } catch (Exception e){
                    throw new APIConnectException("Error parsing service XML: " + e.getMessage(), e);
                }
            } else {
                throw new APIConnectException("No service found");
            }
        } else {
            throw new APIConnectException("Could not send call after retrying" + call.getStatusMessage());
        }
    }

    public void logWorkflowDequeued(String invocationId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APILogWorkflowDequeued");
        call.getCallArguments().add("InvocationID", invocationId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public void logWorkflowExecutionStarted(String invocationId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APILogWorkflowExecutionStarted");
        call.getCallArguments().add("InvocationID", invocationId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public void logWorkflowComplete(String invocationId, String status) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
           CallObject call = new CallObject("APILogWorkflowComplete");
           call.getCallArguments().add("InvocationID", invocationId);
           call.getCallArguments().add("Status", status);

           CallSender sender = new CallSender(client, call);
           sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
           boolean success;
           try {
               success = sender.syncCall();
           } catch (RPCException rpce){
               throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
           }

           if(!success){
               throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
           }
       }


    /** Create an empty invocation folder */
    public IWorkflowInvocation createInvocationFolder(String workflowId, String invocationId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APICreateInvocationFolder");
        call.getCallArguments().add("WorkflowID", workflowId);
        call.getCallArguments().add("InvocationID", invocationId);
        
        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }
        
        if(success){
            try {
                return (IWorkflowInvocation)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Invocation")).getObject();
            } catch (Exception ex){
                throw new APIConnectException("Error parsing invocation data: " + ex.getMessage(), ex);
            }
        } else {
            throw new APIConnectException("Error executing call: " + call.getStatusMessage());
        }
    }

    /** Get the time on the server */
    public Date getServerTime() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetServerTime");
        
        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            return call.getReturnArguments().dateValue("ServerTime", new Date());
        } else {
            throw new APIConnectException("Error executing call: " + call.getStatusMessage());
        }
    }

    @Override
    public void setCurrentBlockStreamingProgress(String invocationId, String contextId, long totalBytes, long bytesStreamed) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISetStreamingProgress");
        call.getCallArguments().add("InvocationID", invocationId);
        call.getCallArguments().add("ContextID", contextId);
        call.getCallArguments().add("TotalBytes", totalBytes);
        call.getCallArguments().add("StreamedBytes", bytesStreamed);
        
        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing call: " + call.getStatusMessage());
        }
    }

    @Override
    public WorkflowLockWrapper createWorkflowLock(String invocationId, String contextId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException{
        CallObject call = new CallObject("APICreateWorkflowLock");
        call.getCallArguments().add("InvocationID", invocationId);
        call.getCallArguments().add("ContextID", contextId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }
        
        if(success){
            try {
                return (WorkflowLockWrapper)call.getReturnArguments().xmlStorableValue("WorkflowLock");
            } catch (Exception ex){
                throw new APIConnectException("Error parsing local data: " + ex.getMessage(), ex);
            }
        } else {
            throw new APIConnectException("Error executing call: " + call.getStatusMessage());
        }
    }

    public IWorkflowInvocation executeWorkflow(IWorkflow workflow, IWorkflowParameterList parameters, long lockId, String folderName) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIExecuteWorkflowWithParameters");
        call.getCallArguments().add("WorkflowID", workflow.getId());
        call.getCallArguments().add("AttachToLock", true);
        call.getCallArguments().add("LockID", lockId);
        call.getCallArguments().add("FolderName", folderName);
        
        try {
            call.getCallArguments().add("Parameters", new InkspotObjectTransport(parameters));
        } catch (Exception e){
            throw new APIConnectException("Error serializing parameters: " + e.getMessage(), e);
        }

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                InkspotObjectTransport transport = (InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("WorkflowInvocation");
                return (IWorkflowInvocation)transport.getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned workflow: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public void removeWorkflowLock(long lockId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIRemoveWorkflowLock");
        call.getCallArguments().add("LockID", lockId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public void setWorkflowLockStatus(long lockId, String status) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISetWorkflowLockStatus");
        call.getCallArguments().add("LockID", lockId);
        call.getCallArguments().add("Status", status);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }
    
    @Override
    public void refreshLockStatus(long lockId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIRefreshWorkflowLockStatus");
        call.getCallArguments().add("LockID", lockId);
        

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }    

    @Override
    public void setCurrentBlock(String invocationId, String contextId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISetCurrentBlock");
        call.getCallArguments().add("InvocationID", invocationId);
        call.getCallArguments().add("ContextID", contextId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public void setWorkflowStatus(String invocationId, String workflowStatus) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISetWorkflowStatus");
        call.getCallArguments().add("InvocationID", invocationId);
        call.getCallArguments().add("Status", workflowStatus);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }
    
    
}