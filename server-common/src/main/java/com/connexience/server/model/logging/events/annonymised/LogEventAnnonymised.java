package com.connexience.server.model.logging.events.annonymised;

import java.io.Serializable;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class LogEventAnnonymised implements Serializable
{
  private int id;

  private Long timestamp;

  private int principalId;

  public LogEventAnnonymised()
  {
  }

  public LogEventAnnonymised(Long timestamp, int principalId)
  {
    this.timestamp = timestamp;
    this.principalId = principalId;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public Long getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(Long timestamp)
  {
    this.timestamp = timestamp;
  }

  public int getPrincipalId()
  {
    return principalId;
  }

  public void setPrincipalId(int principalId)
  {
    this.principalId = principalId;
  }


  public static LogEventAnnonymised parseString(String logLine)
  {
    String[] tokens = logLine.split(",");
    
    //id 0, operation 1, timestamp2 , userid 3, workflowid 4, invocationid 5, versionid 6, objectid 7, objecttype 8, granteeid 9, granteetype 10, state 11

    if (tokens[1].equals("JOIN_GROUP"))
    {
      return new JoinGroupEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[7]));
    }
    else if (tokens[1].equals("LOGIN"))
    {
      return new LoginEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]));
    }
    else if (tokens[1].equals("MAKE_FRIEND"))
    {
      return new MakeFriendEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[7]));
    }
    else if (tokens[1].equals("MAKE_GROUP"))
    {
    return new MakeGroupEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[7]));
    }
    else if (tokens[1].equals("READ_BY_WORKFLOW"))
    {
      return new ReadByWorkflowEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[7]), tokens[8], Integer.parseInt(tokens[6]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]));
    }
    else if (tokens[1].equals("READ"))
    {
      return new ReadEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[7]), tokens[8], Integer.parseInt(tokens[6]));
    }
    else if (tokens[1].equals("REGISTER"))
    {
     return new RegisterEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]));
    }
    else if (tokens[1].equals("WORKFLOW_START"))
    {
      return new WorkflowStartEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]));
    }
    else if (tokens[1].equals("WORKFLOW_COMPLETE"))
    {
      return new WorkflowCompleteEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), tokens[11]);
    }
    else if (tokens[1].equals("SHARE"))
    {
      return new ShareEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[7]), tokens[8], Integer.parseInt(tokens[9]), tokens[10]);
    }
    else if (tokens[1].equals("WRITE_BY_WORKFLOW"))
    {
      return new WriteDataByWorkflowEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[7]), tokens[8], Integer.parseInt(tokens[6]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]));
    }
    else if (tokens[1].equals("WRITE"))
    {
      return new WriteEventAnnonymised(Long.parseLong(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[7]), tokens[8], Integer.parseInt(tokens[6]));
    }
    else
    {
      throw new IllegalArgumentException("Could not parse: " + logLine);
    }
  }

  @Override
  public final String toString()
  {
    //id, operation, timestamp, userid, workflowid, invocationid, versionid, objectid, objecttype, granteeid, granteetype
    if (this instanceof JoinGroupEventAnnonymised)
    {
      JoinGroupEventAnnonymised jge = (JoinGroupEventAnnonymised) this;
      return this.getId() + "," + "JOIN_GROUP" + "," + this.getTimestamp() + "," + this.getPrincipalId() + ",,,," + jge.getObjectId() + ",,,,,";
    }
    else if (this instanceof LoginEventAnnonymised)
    {
      return this.getId() + "," + "LOGIN" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + "," + "," + "," + "," + "," + "," + ","+ ",";
    }
    else if (this instanceof MakeFriendEventAnnonymised)
    {
      MakeFriendEventAnnonymised m = (MakeFriendEventAnnonymised) this;
      return this.getId() + "," + "MAKE_FRIEND" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + "," + "," + "," + m.getObjectId() + "," + "," + ","+ ",";
    }
    else if (this instanceof MakeGroupEventAnnonymised)
    {
      MakeGroupEventAnnonymised m = (MakeGroupEventAnnonymised) this;
      return this.getId() + "," + "MAKE_GROUP" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + "," + "," + "," + m.getObjectId() + "," + "," + ","+ ",";
    }
    else if (this instanceof ReadByWorkflowEventAnnonymised)
    {
      ReadByWorkflowEventAnnonymised read = (ReadByWorkflowEventAnnonymised) this;
      return this.getId() + "," + "READ_BY_WORKFLOW" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + read.getWorkflowId() + "," + read.getInvocationId() + "," + read.getVersionId() + "," + read.getObjectId() + "," + read.getObjectType() + "," + ","+ ",";
    }
    else if (this instanceof ReadEventAnnonymised)
    {
      ReadEventAnnonymised read = (ReadEventAnnonymised) this;
      return this.getId() + "," + "READ" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + "," + "," + read.getVersionId() + "," + read.getObjectId() + "," + read.getObjectType() + "," + ","+ ",";
    }
    else if (this instanceof RegisterEventAnnonymised)
    {
      return this.getId() + "," + "REGISTER" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + "," + "," + "," + "," + "," + ","+ ",";
    }
    else if (this instanceof WorkflowStartEventAnnonymised)
    {
      WorkflowStartEventAnnonymised run = (WorkflowStartEventAnnonymised) this;
      return this.getId() + "," + "WORKFLOW_START" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + run.getWorkflowId() + "," + run.getInvocationId() + "," + run.getVersionId() + "," + "," + "," + "," + ",";
    }
    else if (this instanceof WorkflowCompleteEventAnnonymised)
    {
      WorkflowCompleteEventAnnonymised run = (WorkflowCompleteEventAnnonymised) this;
      return this.getId() + "," + "WORKFLOW_COMPLETE" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + run.getWorkflowId() + "," + run.getInvocationId() + "," + "," + "," + "," + "," + "," + run.getState();
    }
    else if (this instanceof ShareEventAnnonymised)
    {
      ShareEventAnnonymised share = (ShareEventAnnonymised) this;
      return this.getId() + "," + "SHARE" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + "," + "," + ","  + share.getObjectId() + "," + share.getObjectType() + "," + share.getGranteeId() + "," + share.getGranteeType()+ ",";
    }
    else if (this instanceof WriteDataByWorkflowEventAnnonymised)
    {
      WriteDataByWorkflowEventAnnonymised write = (WriteDataByWorkflowEventAnnonymised) this;
      return this.getId() + "," + "WRITE_BY_WORKFLOW" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + write.getWorkflowId() + "," + write.getInvocationId() + "," + write.getVersionId() + "," + write.getObjectId() + "," + write.getObjectType() + "," + "," + "," + ","+ ",";
    }
    else if (this instanceof WriteEventAnnonymised)
    {
      WriteEventAnnonymised write = (WriteEventAnnonymised) this;
      return this.getId() + "," + "WRITE" + "," + this.getTimestamp() + "," + this.getPrincipalId() + "," + "," + "," + write.getVersionId() + "," + write.getObjectId() + "," + write.getObjectType() + "," + ","+ ",";
    }
    else
    {
      throw new UnsupportedOperationException("Unable to serialise:" + this.getId());
    }
  }
}