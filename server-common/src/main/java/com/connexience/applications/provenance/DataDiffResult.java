package com.connexience.applications.provenance;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: nsjw7
 * Date: 27/07/2012
 * Time: 15:27
 * To change this template use File | Settings | File Templates.
 */
public class DataDiffResult implements Serializable
{

  private String documentId1 = "";
  private String versionId1 = "";
  private String documentId2 = "";
  private String versionId2 = "";

  private boolean filesSame = false;

  public DataDiffResult()
  {
  }

  public DataDiffResult(String documentId1, String versionId1, String documentId2, String versionId2)
  {
    this.documentId1 = documentId1;
    this.versionId1 = versionId1;
    this.documentId2 = documentId2;
    this.versionId2 = versionId2;
  }

  public String getDocumentId1()
  {
    return documentId1;
  }

  public void setDocumentId1(String documentId1)
  {
    this.documentId1 = documentId1;
  }

  public String getVersionId1()
  {
    return versionId1;
  }

  public void setVersionId1(String versionId1)
  {
    this.versionId1 = versionId1;
  }

  public String getDocumentId2()
  {
    return documentId2;
  }

  public void setDocumentId2(String documentId2)
  {
    this.documentId2 = documentId2;
  }

  public String getVersionId2()
  {
    return versionId2;
  }

  public void setVersionId2(String versionId2)
  {
    this.versionId2 = versionId2;
  }

  public boolean isFilesSame()
  {
    return filesSame;
  }

  public void setFilesSame(boolean filesSame)
  {
    this.filesSame = filesSame;
  }

  @Override
  public String toString()
  {
    return "DataDiffResult{" +
        "documentId1='" + documentId1 + '\'' +
        ", versionId1='" + versionId1 + '\'' +
        ", documentId2='" + documentId2 + '\'' +
        ", versionId2='" + versionId2 + '\'' +
        ", filesSame=" + filesSame +
        '}';
  }
}
