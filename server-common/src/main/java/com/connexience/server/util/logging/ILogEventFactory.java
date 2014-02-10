package com.connexience.server.util.logging;

import java.util.Date;

/**
 * Author: Simon
 * Date: Jan 14, 2010
 */
public interface ILogEventFactory
{
  ILogEvent newRegisteration(Date timestamp, String userId);

  ILogEvent newLogin(Date timestamp, String userId);

  ILogEvent newRunWorkflow(Date timestamp, String userId, String workflowId);

  ILogEvent newMakeFriends(Date timestamp, String user1, String user2);

  ILogEvent newMakeGroup(Date timestamp, String userId, String groupId);

  ILogEvent newCreateService(Date timestamp, String userId, String serviceId);

  ILogEvent newCreateWorkflow(Date timestamp, String userId, String workflowId);

  ILogEvent newReadData(Date timestamp, String userId, String dataId);

  ILogEvent newReadDataByWorkflow(Date timestamp, String userId, String dataId, String workflowId);

  ILogEvent newWriteData(Date timestamp, String userId, String dataId);

  ILogEvent newWriteDataByWorkflow(Date timestamp, String userId, String dataId, String workflowId);

  ILogEvent newJoinGroup(Date timestamp, String userId, String groupId);

  ILogEvent newPermission(Date timestamp, String granterId, String objectId, String granteeId);
}
