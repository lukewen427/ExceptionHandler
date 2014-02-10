package com.connexience.server.model.logging.events.annonymised;

import com.connexience.server.model.logging.events.LogEvent;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class ShareEventAnnonymised extends LogEventAnnonymised
{
  private int objectId;

  private String objectType;

  private int granteeId;

  private String granteeType;

  public ShareEventAnnonymised()
  {
  }

  public ShareEventAnnonymised(Long timestamp, int principalId, int objectId, String objectType, int granteeId, String granteeType)
  {
    super(timestamp, principalId);
    this.objectId = objectId;
    this.objectType = objectType;
    this.granteeId = granteeId;
    this.granteeType = granteeType;
  }

  public int getObjectId()
  {
    return objectId;
  }

  public void setObjectId(int objectId)
  {
    this.objectId = objectId;
  }

  public String getObjectType()
  {
    return objectType;
  }

  public void setObjectType(String objectType)
  {
    this.objectType = objectType;
  }

  public int getGranteeId()
  {
    return granteeId;
  }

  public void setGranteeId(int granteeId)
  {
    this.granteeId = granteeId;
  }

  public String getGranteeType()
  {
    return granteeType;
  }

  public void setGranteeType(String granteeType)
  {
    this.granteeType = granteeType;
  }
}