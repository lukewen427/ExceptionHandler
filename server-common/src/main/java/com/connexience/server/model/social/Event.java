package com.connexience.server.model.social;

import com.connexience.server.model.folder.*;

import java.util.Date;
import java.io.Serializable;

/**
 * Author: Simon
 * Date: 15-Jul-2008
 * <p/>
 * Class to represent a generic event such as meeting, conference etc.
 * May be subclassed in the future to provide specific events
 */
public class Event extends LinksFolder implements Serializable
{
  private Date startDate = new Date();
  private Date endDate = new Date();


  public Event()
  {
    super();
  }

  public long getStartDateTimestamp(){
      return startDate.getTime();
  }

  public void setStartDateTimestamp(long startDateTimestamp){
      startDate = new Date(startDateTimestamp);
  }

  public long getEndDateTimestamp(){
      return endDate.getTime();
  }

  public void setEndDateTimestamp(long endDateTimestamp){
      endDate = new Date(endDateTimestamp);
  }
  
  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }  
}
