package com.connexience.applications.provenance;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: nsjw7
 * Date: 27/07/2012
 * Time: 13:42
 * A service that has been run and pulled out of the provenance
 */
public class ServiceDiff implements Serializable
{
  private String serviceId = "";
  private String versionId = "";
  private String name = "";
  private int versionNum = 0;

  public ServiceDiff()
  {
  }

  public ServiceDiff(int versionNum, String serviceId, String versionId, String name)
  {
    this.versionNum = versionNum;
    this.serviceId = serviceId;
    this.versionId = versionId;
    this.name = name;
  }

  public String getServiceId()
  {
    return serviceId;
  }

  public void setServiceId(String serviceId)
  {
    this.serviceId = serviceId;
  }

  public String getVersionId()
  {
    return versionId;
  }

  public void setVersionId(String versionId)
  {
    this.versionId = versionId;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public int getVersionNum()
  {
    return versionNum;
  }

  public void setVersionNum(int versionNum)
  {
    this.versionNum = versionNum;
  }

  @Override
  public String toString()
  {
    return "ServiceDiff{" +
        "serviceId='" + serviceId + '\'' +
        ", versionId='" + versionId + '\'' +
        ", name='" + name + '\'' +
        ", versionNum=" + versionNum +
        '}';
  }
}
