package com.connexience.server.model.document;

import org.w3c.dom.Document;
import com.connexience.server.model.ServerObject;

import java.io.Serializable;

/**
 * Author: Simon
 * Date: May 11, 2009
 */
public class XMLMetaDataProxy extends ServerObject implements Serializable
{
  private String metaDataId;

  public String getMetaDataId()
  {
    return metaDataId;
  }

  public void setMetaDataId(String metaDataId)
  {
    this.metaDataId = metaDataId;
  }
}
