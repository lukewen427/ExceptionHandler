package com.connexience.server.model.social;

import com.connexience.server.model.ServerObject;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

/**
 * Author: Simon
 * Date: 15-Jul-2008
 * <p/>
 * Publication that can be linked to a profile
 */

//todo: make publication generic to deal with any bibtex format

public class Publication extends ServerObject implements Serializable
{
// Key for the publication a-la bibtex keys.  Should be unique amongst a user's publications but this isn't enforced
  private String key;

  //The type of the publication.  This should be an ENUM of the bibtex publication types
  private String type;

  //The fields that the publication has
  private Map fields = new HashMap();

  public Publication()
  {
    super();
  }

  public Publication(String key, String type, Map fields)
  {
    this.key = key;
    this.type = type;
    this.fields = fields;
  }

  public String getObjectType()
  {
    return "Publication";
  }

  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public Map getFields()
  {
    return fields;
  }

  public void setFields(Map fields)
  {
    this.fields = fields;
  }
}
