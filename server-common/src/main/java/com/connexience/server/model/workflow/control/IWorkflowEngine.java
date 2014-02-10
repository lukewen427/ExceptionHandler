/*
 * IWorkflowEngine.java
 */

package com.connexience.server.model.workflow.control;

import com.connexience.server.model.security.*;
import java.rmi.*;

/**
 * This interface provides a control link to a workflow engine
 * @author nhgh
 */
public interface IWorkflowEngine extends Remote {
    /** Open a control connection to this workflow engine */
    public IWorkflowEngineControl openControlConnection(Ticket ticket) throws RemoteException;

    /** Open a non authenticated control connection */
    public IWorkflowEngineControl openControlConnection() throws RemoteException;

    /** Shutdown the engine */
    public void shutdown() throws RemoteException;
}