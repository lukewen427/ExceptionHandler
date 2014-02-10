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
public class LibrarySaveOperation extends GraphOperation
{

  /**
   * ID of the lib
   */
  String documentId;

  /**
   * versionId
   */
  String versionId;

  /**
   * Version number of the library
   */
  int versionNum;

  /**
   * Name of the lib
   */
  String name;


  public LibrarySaveOperation()
  {
  }

  public LibrarySaveOperation(String documentId, String versionId, String libraryName, int versionNum, String userId, Date timestamp )
  {
    super();
    this.documentId = documentId;
    this.versionId = versionId;
    this.versionNum = versionNum;
    this.name = libraryName;
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

  public int getVersionNum()
  {
    return versionNum;
  }

  public void setVersionNum(int versionNum)
  {
    this.versionNum = versionNum;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}