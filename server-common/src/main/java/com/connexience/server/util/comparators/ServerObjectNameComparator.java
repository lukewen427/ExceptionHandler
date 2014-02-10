package com.connexience.server.util.comparators;

import com.connexience.server.model.ServerObject;

import java.util.Comparator;

/**
 * Author: Simon
 * Date: Sep 17, 2009
 */
public class ServerObjectNameComparator implements Comparator<ServerObject>
{

  public int compare(ServerObject o1, ServerObject o2)
  {
    return o1.getName().compareTo(o2.getName());
  }
}
