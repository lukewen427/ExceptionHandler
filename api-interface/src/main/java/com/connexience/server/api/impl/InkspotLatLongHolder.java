package com.connexience.server.api.impl;

import com.connexience.server.api.ILatLongHolder;

/**
 * Author: Simon
 * Date: Jul 7, 2009
 */
public class InkspotLatLongHolder extends InkspotObject implements ILatLongHolder
{
  public InkspotLatLongHolder()
  {
    super();
    putProperty("latitude", "");
    putProperty("longitude", "");
    putProperty("numPoints", "");
    putProperty("message", "");
  }

   public double getLatitude()
  {
    return Double.parseDouble(getPropertyString("latitude"));
  }

  public void setLatitude(double latitude)
  {
    putProperty("latitude", Double.toString(latitude));
  }

  public double getLongitude()
  {
    return Double.parseDouble(getPropertyString("longitude"));
  }

  public void setLongitude(double longitude)
  {
    putProperty("longitude",  Double.toString(longitude));
  }

  public Long getNumPoints()
  {
    return (Long) getPropertyObject("numPoints");
  }

  public void setNumPoints(Long numPoints)
  {
    putProperty("numPoints", numPoints);
  }

  public String getMessage()
  {
    return getPropertyString("message");
  }

  public void setMessage(String message)
  {
    putProperty("message", message);
  }
}
