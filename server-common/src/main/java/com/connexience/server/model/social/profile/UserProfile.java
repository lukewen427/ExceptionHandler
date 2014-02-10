/*
 * UserProfile.java
 */

package com.connexience.server.model.social.profile;

import com.connexience.server.model.security.User;
import com.connexience.server.model.ServerObject;

import java.io.Serializable;

/**
 * This class represents the profile of a single user within the social networking
 * system.
 * <p/>
 * It is linked from the User object and provides mroe details
 *
 * @author Simon
 */
public class UserProfile implements Serializable
{
  private String id;

  private String website;

  private String emailAddress;

  private String text = "";

  private String defaultDomain = "";

  public UserProfile()
  {
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }


  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress)
  {
    this.emailAddress = emailAddress;
  }

  public String getWebsite()
  {
    return website;
  }

  public void setWebsite(String website)
  {
    this.website = website;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public String getDefaultDomain()
  {
    return defaultDomain;
  }

  public void setDefaultDomain(String defaultDomain)
  {
    this.defaultDomain = defaultDomain;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof UserProfile)) return false;

    UserProfile that = (UserProfile) o;

    if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) return false;
    if (website != null ? !website.equals(that.website) : that.website != null) return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = website != null ? website.hashCode() : 0;
    result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
    return result;
  }
}
