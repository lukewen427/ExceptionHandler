/*
 * IWorkflowEngineControl.java
 */

package com.connexience.server.model.workflow.control;

import com.connexience.server.model.properties.*;

import java.rmi.*;
import java.util.*;

/**
 * This interface represents a control instance to a workflow engine
 * @author nhgh
 */
public interface IWorkflowEngineControl extends Remote {
    /** Get the running invocations for the logged on user */
    public ArrayList<WorkflowInvocationRecord> getRunningInvocations() throws RemoteException;

    /** Get a workflow invocation record */
    public WorkflowInvocationRecord getInvocation(String invocationId) throws RemoteException;
    
    /** Get the current status of the engine */
    public WorkflowEngineStatusData getStatusData() throws RemoteException;
    
    /** Shutdown the workflow engine */
    public void shutdown() throws RemoteException;

    /** Terminate an invocation */
    public void terminateInvocation(String invocationId) throws RemoteException;

    /** Flush the workflow library */
    public void flushLibrary() throws RemoteException;

    /** Notify an invocation that a lock has completed */
    public void lockCompleted(String invocationId, String contextId, long lockId, int numberOfFailures) throws RemoteException;
    
    /** Disconnect the JMS queue */
    public void disconnectJms() throws RemoteException;
    
    /** Connect the JMS queue */
    public void connectJms() throws RemoteException;
    
    /** Open a debug client to a workflow invocation */
    public IWorkflowDebugClient openDebugger(String invocationId, String contextId) throws RemoteException;
}