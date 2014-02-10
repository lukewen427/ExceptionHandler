package com.connexience.server.model.folder;

/**
 * This folder holds a set of links for a group.  It has the group id so that all group subfolders can be found
 */
public class LinksFolder extends Folder
{
  private String groupId;

  public LinksFolder()
  {
    super();
  }

  public LinksFolder(String groupId)
  {
    super();
    this.groupId = groupId;
  }

  public String getGroupId()
  {
    return groupId;
  }

  public void setGroupId(String groupId)
  {
    this.groupId = groupId;
  }
}
