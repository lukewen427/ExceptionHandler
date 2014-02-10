/*
 * WorkflowDataWriteOperation.java
 */

package com.connexience.server.model.logging.graph;

import java.util.Date;

/**
 * This operation is triggered whenever a user writes a piece of
 * data.
 *
 * @author hugo
 */
public class UserWriteOperation  extends GraphOperation
{

  /**
   * ID of the document that was read
   */
  String documentId;

  /**
   * Version ID of the document that was written
   */
  String versionId;

  /**
   * Name of the document being written
   */
  String documentName;

  /**
   * Version number of the document being written
   */
  String versionNumber;

  public UserWriteOperation()
  {
  }

  public UserWriteOperation(String documentId, String versionId, String documentName, String versionNumber, String userId, Date timestamp)
  {
    super();
    this.documentId = documentId;
    this.versionId = versionId;
    this.documentName = documentName;
    this.versionNumber = versionNumber;
    setUserId(userId);
    setTimestamp(timestamp);
  }

  public String getDocumentId()
  {
    return documentId;
  }

  public void setDocumentId(String documentId)
  {
    this.documentId = documentId;
  }

  public String getVersionId()
  {
    return versionId;
  }

  public void setVersionId(String versionId)
  {
    this.versionId = versionId;
  }

  public String getDocumentName()
  {
    return documentName;
  }

  public void setDocumentName(String documentName)
  {
    this.documentName = documentName;
  }

  public String getVersionNumber()
  {
    return versionNumber;
  }

  public void setVersionNumber(String versionNumber)
  {
    this.versionNumber = versionNumber;
  }


}