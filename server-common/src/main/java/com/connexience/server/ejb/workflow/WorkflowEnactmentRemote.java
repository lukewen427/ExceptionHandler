/*
 * WorkflowEnactmentRemote.java
 */

package com.connexience.server.ejb.workflow;

import com.connexience.server.model.workflow.*;
import com.connexience.server.model.security.*;
import com.connexience.server.*;

import javax.ejb.Remote;

/**
 * This interface defines the functionality of the workflow enactment bean. This
 * runs in its own application space and uses a different database to the
 * core server database.
 * @author nhgh
 */
@Remote
public interface WorkflowEnactmentRemote {
    /** Execute a workflow. The invocation ID is returned if the workflow started */
    public String startWorkflow(Ticket ticket, WorkflowInvocationMessage invocationMessage) throws ConnexienceException;

    /** Resubmit a workflow */
    public String resubmitWorkflow(Ticket ticket, String invocationId) throws ConnexienceException;
}