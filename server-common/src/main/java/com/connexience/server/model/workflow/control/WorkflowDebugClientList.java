/*
 * WorkflowDebugClientList.java
 */
package com.connexience.server.model.workflow.control;
import com.connexience.server.*;
import com.connexience.server.ejb.util.EJBLocator;
import com.connexience.server.ejb.util.WorkflowEJBLocator;
import com.connexience.server.util.WorkflowUtils;
import com.connexience.server.util.*;
import com.connexience.server.model.security.*;
import com.connexience.server.model.workflow.*;
import java.util.*;

/**
 * This class contains a list of debug client references
 * @author hugo
 */
public class WorkflowDebugClientList implements Runnable {
    private HashMap<String,IWorkflowDebugClient> clients = new HashMap<String, IWorkflowDebugClient>();
    private Thread checkThread;
    
    public WorkflowDebugClientList() {
        checkThread = new Thread(this);
        checkThread.setDaemon(true);
        checkThread.start();
    }
    
    /** Open a debug client to a workflow invocation */
    public IWorkflowDebugClient getDebugConnection(Ticket ticket, String invocationId, String contextId) throws ConnexienceException {
        IWorkflowDebugClient debugger = null;
        WorkflowInvocationFolder invocation = invocation = WorkflowEJBLocator.lookupWorkflowManagementBean().getInvocationFolder(ticket, invocationId);
        
        if(!clients.containsKey(invocationId + "-" + contextId)){
            if(invocation!=null){
                if(invocation.getInvocationStatus()==WorkflowInvocationFolder.INVOCATION_WAITING_FOR_DEBUGGER){
                    IWorkflowEngineControl control = null;
                    try {
                        control = WorkflowUtils.connectToWorkflowEngine(ticket, invocation);
                    } catch (Exception e){
                        throw new ConnexienceException("Error connecting to workflow engine: " + e.getMessage(), e);
                    }


                    try {
                        debugger = control.openDebugger(invocationId, contextId);
                    } catch (Exception e){
                        throw new ConnexienceException("Error opening debug connection", e);
                    }
                    clients.put(invocationId + "-" + contextId, debugger);
                    return debugger;
                } else {
                    throw new ConnexienceException("Only workflows that are waiting for a debugger can be debugged");
                }
            } else {
                throw new ConnexienceException("No such invocation");
            }
        } else {
            debugger = clients.get(invocationId + "-" + contextId);
        }
        
        if(debugger!=null){
            if(invocation.getInvocationStatus()==WorkflowInvocationFolder.INVOCATION_WAITING_FOR_DEBUGGER){
                return debugger;
            } else {
                removeDebugConnection(ticket, invocationId, contextId);
                throw new ConnexienceException("Workflow invocation finished");
            }
        } else {
            throw new ConnexienceException("Cannot obtain debugger");
        }
    }
    
    /** Remove a debugger */
    public void removeDebugConnection(Ticket ticket, String invocationId, String contextId) throws ConnexienceException {
        if(clients.containsKey(invocationId + "-" + contextId)){
            IWorkflowDebugClient client = null;
            try {
                client = clients.get(invocationId + "-" + contextId);
                client.close();
            } catch (Exception e){
                throw new ConnexienceException("Error closing debug connection: " + e.getMessage(), e);
            } finally {
                client = null;
                clients.remove(invocationId + "-" + contextId);
            }
        }
    }

    @Override
    public void run() {
        boolean run = true;
        while(run){
            Iterator<IWorkflowDebugClient> i = clients.values().iterator();
            ArrayList<IWorkflowDebugClient> clientsToRemove = new ArrayList<IWorkflowDebugClient>();
            IWorkflowDebugClient client;
            while(i.hasNext()){
                client = i.next();
                try {
                    if(!client.isConnected()){
                        clientsToRemove.add(client);
                    }
                } catch (Exception e){
                    clientsToRemove.add(client);
                }
            }
            
            for(IWorkflowDebugClient c : clientsToRemove){
                clients.values().remove(c);
            }
            
            try {
                Thread.sleep(10000);
            } catch(InterruptedException e){
                run = false;
            }
        }
    }
    
    
}