package com.connexience.server.ejb.search;

import org.w3c.dom.Document;

import java.util.List;
import java.io.Serializable;

/**
 * Author: Simon
 * Date: May 19, 2009
 */
public class MetaDataSearchResult implements Serializable
{

  /*
 * The meta data document that was found
 * */
  private Document metaDataDoc;

  private String metaDataDocId;

  private String metaDataDocName;

  /*
 * The documents that the meta data document is linked to
 * */
  private String documentId;

  private String documentName;

  public MetaDataSearchResult()
  {
  }

  public MetaDataSearchResult(Document metaDataDoc, String metaDataDocId, String metaDataDocName, String documentId, String documentName)
  {
    this.metaDataDoc = metaDataDoc;
    this.metaDataDocId = metaDataDocId;
    this.metaDataDocName = metaDataDocName;
    this.documentId = documentId;
    this.documentName = documentName;
  }

  public String getMetaDataDocName()
  {
    return metaDataDocName;
  }

  public void setMetaDataDocName(String metaDataDocName)
  {
    this.metaDataDocName = metaDataDocName;
  }

  public Document getMetaDataDoc()
  {
    return metaDataDoc;
  }

  public void setMetaDataDoc(Document metaDataDoc)
  {
    this.metaDataDoc = metaDataDoc;
  }

  public String getDocumentId()
  {
    return documentId;
  }

  public void setDocumentId(String documentId)
  {
    this.documentId = documentId;
  }

  public String getDocumentName()
  {
    return documentName;
  }

  public void setDocumentName(String documentName)
  {
    this.documentName = documentName;
  }


  public String getMetaDataDocId()
  {
    return metaDataDocId;
  }

  public void setMetaDataDocId(String metaDataDocId)
  {
    this.metaDataDocId = metaDataDocId;
  }
}
