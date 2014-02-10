package com.connexience.server.model.logging.events;

/**
 * Author: Simon
 * Date: Jan 18, 2010
 */
public class ShareEvent extends LogEvent
{
  private String objectId;

  private String objectName;

  private String objectType;

  private String granteeId;

  private String granteeType;

  private String granteeName;

  public ShareEvent()
  {
  }

  public ShareEvent(Long timestamp,
                    String principalId, 
                    String objectId,
                    String objectType,
                    String granteeId,
                    String granteeType,
                    String principalName,
                    String objectName,
                    String granteeName)
  {
    super(timestamp, principalId, principalName);
    this.objectId = objectId;
    this.objectType = objectType;
    this.granteeId = granteeId;
    this.granteeType = granteeType;
    this.objectName = objectName;
    this.granteeName = granteeName;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId(String objectId)
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

  public String getGranteeId()
  {
    return granteeId;
  }

  public void setGranteeId(String granteeId)
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

  public String getObjectName()
  {
    return objectName;
  }

  public void setObjectName(String objectName)
  {
    this.objectName = objectName;
  }

  public String getGranteeName()
  {
    return granteeName;
  }

  public void setGranteeName(String granteeName)
  {
    this.granteeName = granteeName;
  }
}
