package com.connexience.server.model.social.requests;

import com.connexience.server.model.ServerObject;
import com.connexience.server.model.messages.Message;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents the superclass for all requests, e.g. join group, make friends.
 *
 * Author: Simon
 * Date: Jun 22, 2009
 */
public class Request extends Message implements Serializable
{
  /**
   * Request is waiting for acknowledgement
   */
  public static final int REQUEST_PENDING = 0;
  /**
   * Request has been accepted
   */
  public static final int REQUEST_ACCEPTED = 1;
  /**
   * Request has been rejected
   */
  public static final int REQUEST_REJECTED = 2;

  /**
   * Request status
   */
  protected int status = REQUEST_PENDING;

  public Request()
  {
    super();
  }

  /**
   * Get the current status of this request
   */
  public int getStatus()
  {
    return status;
  }

  /**
   * Set the current status of this request
   */
  public void setStatus(int status)
  {
    this.status = status;
  }


  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof Request)) return false;

    FriendRequest request = (FriendRequest) o;

    if (getStatus() != request.getStatus()) return false;
    if (getSenderId() != null ? !getSenderId().equals(request.getSenderId()) : request.getSenderId() != null)
      return false;
    if (getRecipientId() != null ? !getRecipientId().equals(request.getRecipientId()) : request.getRecipientId() != null)
      return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = getSenderId() != null ? getSenderId().hashCode() : 0;
    result = 31 * result + (getRecipientId() != null ? getRecipientId().hashCode() : 0);
    result = 31 * result + getStatus();
    return result;
  }

}
