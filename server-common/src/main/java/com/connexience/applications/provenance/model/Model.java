package com.connexience.applications.provenance.model;

/**
 * Created by IntelliJ IDEA.
 * User: nsjw7
 * Date: 09/02/2012
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
public class Model
{
  private long id;
  
  private String blockUUID;
  
  private String type = "";

  private byte[] modelBinary = null;

  private int numPoints = 0;

  public Model()
  {
  }
  
  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getBlockUUID()
  {
    return blockUUID;
  }

  public void setBlockUUID(String blockUUID)
  {
    this.blockUUID = blockUUID;
  }

  public byte[] getModelBinary()
  {
    return modelBinary;
  }

  public void setModelBinary(byte[] modelBinary)
  {
    this.modelBinary = modelBinary;
  }

  public int getNumPoints()
  {
    return numPoints;
  }

  public void setNumPoints(int numPoints)
  {
    this.numPoints = numPoints;
  }
}
