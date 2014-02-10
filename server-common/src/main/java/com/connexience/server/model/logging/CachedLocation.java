package com.connexience.server.model.logging;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Simon
 * Date: Jul 1, 2009
 */
public class CachedLocation implements Serializable
{

  /*
  * the id of this entry in the cache
  * */
  private long id;

  /*
  * the IP address;
  * */
  private String ipAddress;

  /*
   * Latitude
   * */
   private double latitude;

   /*
   * Longitude
   * */
   private double longitude;

   /*
   * Country
   * */
   private String country;

   /*
   * City or area
   * */
   private String city;

  /*
  * the time that this entry was created
  * */
  private Date timestamp;

  public CachedLocation()
  {
    this.timestamp = new Date();
  }

  public CachedLocation(String ipAddress, double latitude, double longitude, String country, String city)
  {
    this.ipAddress = ipAddress;
    this.latitude = latitude;
    this.longitude = longitude;
    this.country = country;
    this.city = city;
    this.timestamp = new Date();
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public String getIpAddress()
  {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress)
  {
    this.ipAddress = ipAddress;
  }

  public double getLatitude()
  {
    return latitude;
  }

  public void setLatitude(double latitude)
  {
    this.latitude = latitude;
  }

  public double getLongitude()
  {
    return longitude;
  }

  public void setLongitude(double longitude)
  {
    this.longitude = longitude;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public Date getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }
}
