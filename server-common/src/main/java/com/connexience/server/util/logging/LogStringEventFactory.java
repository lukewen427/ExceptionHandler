package com.connexience.server.util.logging;

import java.util.Date;

/**
 * Author: Simon
 * Date: Jan 14, 2010
 */
public class LogStringEventFactory implements ILogEventFactory
{
  public ILogEvent newRegisteration(Date timestamp, String userId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.REGISTER.toString());
  }

  public LogEventString newLogin(Date timestamp, String userId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.LOGIN.toString());
  }

  public LogEventString newRunWorkflow(Date timestamp, String userId, String workflowId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.RUN_WORKFLOW.toString(), workflowId);
  }

  public LogEventString newMakeFriends(Date timestamp, String user1, String user2)
  {
    return new LogEventString(timestamp.getTime(), user1, Operation.MAKE_FRIEND.toString(), user2);
  }

  public LogEventString newMakeGroup(Date timestamp, String userId, String groupId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.MAKE_GROUP.toString(), groupId);
  }

  public LogEventString newCreateService(Date timestamp, String userId, String serviceId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.NEW_SERVICE.toString(), serviceId);
  }

  public LogEventString newCreateWorkflow(Date timestamp, String userId, String workflowId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.NEW_WORKFLOW.toString(), workflowId);
  }

  public LogEventString newReadData(Date timestamp, String userId, String dataId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.READ_DATA.toString(), dataId);
  }

  public LogEventString newReadDataByWorkflow(Date timestamp, String userId, String dataId, String workflowId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.READ_DATA.toString(), dataId, workflowId);
  }

  public LogEventString newWriteData(Date timestamp, String userId, String dataId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.WRITE_DATA.toString(), dataId);
  }

  public LogEventString newWriteDataByWorkflow(Date timestamp, String userId, String dataId, String workflowId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.WRITE_DATA.toString(), dataId, workflowId);
  }

  public LogEventString newJoinGroup(Date timestamp, String userId, String groupId)
  {
    return new LogEventString(timestamp.getTime(), userId, Operation.JOIN_GROUP.toString(), groupId);
  }

  public LogEventString newPermission(Date timestamp, String granterId, String objectId, String granteeId)
  {
    return new LogEventString(timestamp.getTime(), granterId, Operation.SHARE_DATA.toString(), objectId, granteeId);
  }


  //todo: writing a new version - need separate event type as writebyworkflow uses same number of params?
  //todo: table with strings changed to ints 0 starting, unique for each object type
  //todo: get friendship dates and sharing from news feed?

}
