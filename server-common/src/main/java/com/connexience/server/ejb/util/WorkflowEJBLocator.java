/*
 * WorkflowEJBLocator.java
 */

package com.connexience.server.ejb.util;

import com.connexience.server.*;
import com.connexience.server.ejb.workflow.*;

import javax.naming.*;

/**
 * This bean provides utility methods for looking up the various workflow
 * management and execution beans.
 *
 * @author nhgh
 */
public abstract class WorkflowEJBLocator
{
  private static WorkflowEnactmentRemote wfEnactmentRemote = null;
  private static WorkflowManagementRemote wfManagementRemote = null;
  private static WorkflowLockRemote wfLockRemote = null;

  /**
   * Get hold of a workflow management bean
   */
  public static WorkflowManagementRemote lookupWorkflowManagementBean() throws ConnexienceException
  {
    try
    {
      if (wfManagementRemote == null)
      {
        Context c = new InitialContext();
        wfManagementRemote = (WorkflowManagementRemote) c.lookup("java:global/ejb/WorkflowManagementBean");
      }
      return wfManagementRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate workflow management bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of a workflow execution bean
   */
  public static WorkflowEnactmentRemote lookupWorkflowEnactmentBean() throws ConnexienceException
  {
    try
    {
      if (wfEnactmentRemote == null)
      {
        Context c = new InitialContext();
        wfEnactmentRemote = (WorkflowEnactmentRemote) c.lookup("java:global/ejb/WorkflowEnactmentBean");
      }
      return wfEnactmentRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate workflow enactment bean: " + ne.getMessage());
    }
  }

  /**
   * Get hold of a workflow lock bean
   */
  public static WorkflowLockRemote lookupWorkflowLockBean() throws ConnexienceException
  {
    try
    {
      if (wfLockRemote == null)
      {
        Context c = new InitialContext();
        wfLockRemote = (WorkflowLockRemote) c.lookup("java:global/ejb/WorkflowLockBean");
      }
      return wfLockRemote;
    }
    catch (NamingException ne)
    {
      throw new ConnexienceException("Cannot locate workflow lock bean: " + ne.getMessage());
    }
  }
}