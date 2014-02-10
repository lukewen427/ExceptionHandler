package com.connexience.server.api;

/**
 * This interface represents the location of a point in the world.  It can optionally have a
 * message associated with it and the number of points that exist in this location
 * Author: Simon
 * Date: Jul 7, 2009
 */
public interface ILatLongHolder extends IObject
{
  /**
   * XML Name for object
   */
  public static final String XML_NAME = "LatLongHolder";

  public double getLatitude();

  public void setLatitude(double latitude);

  public double getLongitude();

  public void setLongitude(double longitude);

  public Long getNumPoints();

  public void setNumPoints(Long numPoints);

  public String getMessage();

  public void setMessage(String message);

}
