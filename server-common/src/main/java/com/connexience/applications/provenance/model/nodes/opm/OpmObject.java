package com.connexience.applications.provenance.model.nodes.opm;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Created by IntelliJ IDEA.
 * User: nsjw7
 * Date: 23/03/2011
 * Time: 08:19
 */
public class OpmObject
{
  public static final String NAME = "name";
  public static final String ROLE = "role";
  public static final String ACCOUNT = "account";

  protected Node underlyingNode;

  public OpmObject(Node undelyingNode)
  {
    this.underlyingNode = undelyingNode;
  }

  public Node getUnderlyingNode()
  {
    return underlyingNode;
  }

  public String getName()
  {
    return (String) this.underlyingNode.getProperty(NAME);
  }

  public void setName(String name)
  {
    this.underlyingNode.setProperty(NAME, name);
  }

  public void printProperties()
  {
    for(String key : this.underlyingNode.getPropertyKeys())
    {
      System.out.println(key + " = " + this.underlyingNode.getProperty(key) + "\n");
    }
  }

  public void printRelationships()
  {
    for(Relationship rel : this.underlyingNode.getRelationships())
    {
      System.out.println("relationship: " + rel.getType());
    }
  }
}
