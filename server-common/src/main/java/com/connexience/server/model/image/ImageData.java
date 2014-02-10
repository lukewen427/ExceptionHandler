package com.connexience.server.model.image;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class is a holder for image blobs that can be associated with a ServerObject
 * Date: Jul 31, 2009
 * Time: 1:05:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageData implements Serializable
{
  public static final String LARGE_PROFILE = "large profile";
  public static final String SMALL_PROFILE = "small profile";
  public static final String WORKFLOW_PREVIEW = "workflow preview";
  

  /**
   * Id of the image
   * */
  private String id;

  /**
   * The Id of the server object that this image is associated with
   * */
  private String serverObjectId;

  /**
   * The image data
   * */
  private byte[] data;

  /**
   * The type of the image - large or small profile picture etc.
   * */
  private String type;

  public ImageData()
  {
  }
  
  public ImageData getCopy(){
      ImageData copy = new ImageData();
      copy.setData(Arrays.copyOf(data, data.length));
      copy.setServerObjectId(serverObjectId);
      copy.setType(type);
      return copy;
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

  public byte[] getData()
  {
    return data;
  }

  public void setData(byte[] data)
  {
    this.data = data;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }
}
