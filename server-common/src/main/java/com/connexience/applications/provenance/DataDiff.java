package com.connexience.applications.provenance;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: nsjw7
 * Date: 27/07/2012
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class DataDiff implements Serializable
{
  private String documentId = "";
  private String versionId = "";
  private String name = "";
  private int versionNum = 0;


  public DataDiff()
  {

  }

  public DataDiff(String documentId, String versionId, String name, int versionNum)
  {

    this.documentId = documentId;
    this.versionId = versionId;
    this.name = name;
    this.versionNum = versionNum;
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

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public int getVersionNum()
  {
    return versionNum;
  }

  public void setVersionNum(int versionNum)
  {
    this.versionNum = versionNum;
  }

  @Override
  public String toString()
  {
    return "DataDiff{" +
        "documentId='" + documentId + '\'' +
        ", versionId='" + versionId + '\'' +
        ", name='" + name + '\'' +
        ", versionNum=" + versionNum +
        '}';
  }
}
