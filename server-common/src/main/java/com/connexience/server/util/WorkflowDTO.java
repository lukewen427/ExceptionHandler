package com.connexience.server.util;

import java.io.Serializable;

/**
 * Author: Simon
 * Date: Feb 9, 2010
 */
public class WorkflowDTO implements Serializable
{
  private String id;

  private String name;

  private Long numInvoations;

  private Long numSuccess;

  private Long numFailures;

  public WorkflowDTO()
  {
  }

  public WorkflowDTO(String id, String name, Long numInvoations, Long numSuccess, Long numFailures)
  {
    this.id = id;
    this.name = name;
    this.numInvoations = numInvoations;
    this.numSuccess = numSuccess;
    this.numFailures = numFailures;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public Long getNumInvoations()
  {
    return numInvoations;
  }

  public void setNumInvoations(Long numInvoations)
  {
    this.numInvoations = numInvoations;
  }

  public Long getNumSuccess()
  {
    return numSuccess;
  }

  public void setNumSuccess(Long numSuccess)
  {
    this.numSuccess = numSuccess;
  }

  public Long getNumFailures()
  {
    return numFailures;
  }

  public void setNumFailures(Long numFailures)
  {
    this.numFailures = numFailures;
  }
}
