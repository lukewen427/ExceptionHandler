package com.connexience.applications.provenance.model.nodes;

import com.connexience.applications.provenance.model.nodes.opm.Artifact;
import com.connexience.applications.provenance.model.relationships.ProvenanceRelationshipTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Created by IntelliJ IDEA.
 * User: nsjw7
 * Date: Mar 9, 2011
 * Time: 9:41:29 AM
 */
public class Library extends Artifact
{
  public static final String ESC_ID = "escId";
  public static final String VERSION_ID = "versionId";
  public static final String VERSION_NUM = "versionNumber";

  public Library(Node underlyingNode)
  {
    super(underlyingNode);
    this.underlyingNode.setProperty("TYPE", "Library");
  }

  public void setEscId(String escId)
  {
    underlyingNode.setProperty(ESC_ID, escId);
  }

  public String getEscId()
  {
    return (String) underlyingNode.getProperty(ESC_ID);
  }

 public void setVersionId(String versionId)
  {
    underlyingNode.setProperty(VERSION_ID, versionId);
  }

  public String getVersionId()
  {
    return (String) underlyingNode.getProperty(VERSION_ID);
  }

  public void setVersionNum(int versionNum)
  {
    underlyingNode.setProperty(VERSION_NUM, versionNum);
  }

  public int getVersionNum()
  {
    return (Integer) underlyingNode.getProperty(VERSION_NUM);
  }



  public Relationship versionOf(Library previousVersion)
  {
    return underlyingNode.createRelationshipTo(previousVersion.getUnderlyingNode(), ProvenanceRelationshipTypes.VERSION_OF);
  }

}