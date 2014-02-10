package com.connexience.server.model.social;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Simon
 * Date: Jun 15, 2009
 * <p/>
 * This class represents a mapping from a server object to a tag.
 */
public class TagToObject implements Serializable
{

  public static final int MACHINE_GENERATED = 1;
  public static final int USER_GENERATED = 2;

  /*
  * The id of this object - required as Hibernate doesn't like having composite primary keys
  * */
  private String id;

  /*
  * id of the object
  * */
  private String serverObjectId;

  /*
  * id of the com.connexience.server.social.tag
  * */
  private String tagId;

  /**
   * The weight of the tag so that we can have different weights for user and machine generated tags
   * */
  private int weight;

  /**
   * User id of the person who created this tag
   * */
  private String creatorId;

  /**
   * The date and time that this tag was created*/
  private Date createDate;


  public TagToObject()
  {
  }

  public TagToObject(String serverObjectId, String tagId, String creatorId, int weight)
  {
    this.serverObjectId = serverObjectId;
    this.tagId = tagId;
    this.creatorId = creatorId;
    this.weight = weight;
    this.createDate = new Date();
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getServerObjectId()
  {
    return serverObjectId;
  }

  public void setServerObjectId(String serverObjectId)
  {
    this.serverObjectId = serverObjectId;
  }

  public String getTagId()
  {
    return tagId;
  }

  public void setTagId(String tagId)
  {
    this.tagId = tagId;
  }

  public int getWeight()
  {
    return weight;
  }

  public void setWeight(int weight)
  {
    this.weight = weight;
  }

  public String getCreatorId()
  {
    return creatorId;
  }

  public void setCreatorId(String creatorId)
  {
    this.creatorId = creatorId;
  }

  public Date getCreateDate()
  {
    return createDate;
  }

  public void setCreateDate(Date createDate)
  {
    this.createDate = createDate;
  }
}

