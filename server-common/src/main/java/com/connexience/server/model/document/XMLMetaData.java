package com.connexience.server.model.document;

import org.w3c.dom.Document;

import java.util.UUID;

/**
 * Author: Simon
 * Date: May 11, 2009
 */
public class XMLMetaData
{
  /* Database id of the metadata */
  private String id;

  /* XML Metadata document */
  private Document xml;

  public XMLMetaData()
  {
    this.id = UUID.randomUUID().toString();
  }

  public XMLMetaData(Document xml)
  {
    this.id = UUID.randomUUID().toString();
    this.xml = xml;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public Document getXml()
  {
    return xml;
  }

  public void setXml(Document xml)
  {
    this.xml = xml;
  }
}
