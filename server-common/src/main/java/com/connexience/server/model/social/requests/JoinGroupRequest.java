package com.connexience.server.model.social.requests;

import java.io.Serializable;

/**
 * Author: Simon
 * Date: Jun 22, 2009
 */
public class JoinGroupRequest extends Request implements Serializable
{
  /*
 * The id of the group to join
 * */
  private String groupId;

  public JoinGroupRequest()
  {
    super();
  }

  public String getGroupId()
  {
    return groupId;
  }

  public void setGroupId(String groupId)
  {
    this.groupId = groupId;
  }
}
