/*
 * WorkflowLockRemote.java
 */

package com.connexience.server.ejb.workflow;

import com.connexience.server.model.workflow.*;
import com.connexience.server.model.workflow.notification.*;
import com.connexience.server.model.security.*;
import com.connexience.server.*;

import javax.ejb.Remote;
import java.util.*;

/**
 * This interface defines the functionality of the workflow locking bean that
 * allows workflows to kick off subworkflows and then wait for completion.
 * @author hugo
 */
@Remote
public interface WorkflowLockRemote {
    /** Create a workflow lock */
    public WorkflowLock createWorkflowLock(Ticket ticket, WorkflowInvocationFolder parentWorkflow) throws ConnexienceException;

    /** Save changes to a workflow lock */
    public WorkflowLock saveWorkflowLock(Ticket ticket, WorkflowLock lock) throws ConnexienceException;

    /** Remove a workflow lock */
    public void removeWorkflowLock(Ticket ticket, long lockId) throws ConnexienceException;

    /** Get a workflow lock by id */
    public WorkflowLock getLock(Ticket ticket, long lockId) throws ConnexienceException;
    
    /** Attach a workflow invocation to a lock */
    public WorkflowLockMember attachInvocationToLock(Ticket ticket, long lockId, WorkflowInvocationFolder invocation) throws ConnexienceException;

    /** Update the status of a lock member */
    public void updateLockMember(Ticket ticket, WorkflowInvocationFolder invocation) throws ConnexienceException;

    /** Check to see if a lock can be released */
    public boolean isLockFinished(Ticket ticket, long lockId) throws ConnexienceException;

    /** How many failed runs are there in a lock */
    public int getNumberOfFailedInvocationsInLock(Ticket ticket, long lockId) throws ConnexienceException;

    /** How many invocations are left in a lock */
    public int getNumberOfRemainingInvocationsInLock(Ticket ticket, long lockId) throws ConnexienceException;
    
    /** Attempt to notify the lock holder that a lock has completed */
    public void notifyLockHolderOfCompletion(Ticket ticket, long lockId) throws ConnexienceException;

    /** Get a list of the members of a workflow lock */
    public List getLockMembers(Ticket ticket, long lockId) throws ConnexienceException;

    /** Get a list of all locks */
    public List listAllLocks(Ticket ticket) throws ConnexienceException;

    /** Get a list of locks for a user */
    public List listUserLocks(Ticket ticket, String userId) throws ConnexienceException;

    /** Get a list of locks for an invocation */
    public List listInvocationLocks(Ticket ticket, String invocationId) throws ConnexienceException;
    
    /** Check to see if all of the invocations in a lock have completed. If they have, notify
     * the holder */
    public void notifyLockHolderIfComplete(Ticket ticket, long lockId) throws ConnexienceException;
}