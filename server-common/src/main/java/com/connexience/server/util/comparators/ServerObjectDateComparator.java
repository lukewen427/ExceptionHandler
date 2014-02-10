package com.connexience.server.util.comparators;

import com.connexience.server.model.ServerObject;

import java.util.Comparator;

/**
 * Author: Simon
 * Date: Sep 17, 2009
 */
public class ServerObjectDateComparator implements Comparator<ServerObject>
{
  public int compare(ServerObject o1, ServerObject o2)
  {
    return o2.getCreationDate().compareTo(o1.getCreationDate()); 
  }
}