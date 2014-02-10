/*
 * WorkflowAPIProvider.java
 */

package com.connexience.server.workflow.api;
import com.connexience.server.api.*;
import com.connexience.server.model.security.*;

/**
 * This class uses an RPCClient to provide the core API programming model to the
 * applet and rich client interfaces
 * @author nhgh
 */
public interface WorkflowAPIProvider {
    /** Create a new non authenticated link */
    public API createApi();

    /** Create a new API with a ticket */
    public API createApi(Ticket ticket) throws Exception;

    /** Create a non authenticated workflow control api */
    public WorkflowAPI createWorkflowApi();

    /** Create a workflow control api with a ticket */
    public WorkflowAPI createWorkflowApi(Ticket ticket) throws Exception;


    /** Release a client api */
    public void release(API api);

    /** Release a workflow control api */
    public void release(WorkflowAPI workflowApi);

    /** Get the download location */
    public String getDownloadUrl();
}