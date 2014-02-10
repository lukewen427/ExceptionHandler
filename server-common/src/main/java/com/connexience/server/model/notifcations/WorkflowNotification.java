package com.connexience.server.model.notifcations;

import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.workflow.WorkflowInvocationFolder;
import com.connexience.server.model.messages.Message;
import com.connexience.server.model.messages.TextMessage;

/**
 * Created by IntelliJ IDEA.
 * User: martyn
 * Date: 16-Nov-2009
 * Time: 16:16:36
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowNotification extends Notification
{
  public WorkflowInvocationFolder workflow;

  public Status status;

  public static enum Status
  {
    COMPLETE, FAILED, IN_PROGRESS
  }

  public WorkflowNotification(Ticket ticket, WorkflowInvocationFolder workflow, WorkflowNotification.Status status)
  {
    setTicket(ticket);
    this.status = status;
    this.workflow = workflow;
  }

  public Message getMessage()
  {
    Message message = new Message();
    message.setMessage(workflow.getId() + " " + status.name());
    return message;
  }
}
