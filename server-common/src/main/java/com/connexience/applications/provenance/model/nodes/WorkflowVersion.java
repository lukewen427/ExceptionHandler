package com.connexience.applications.provenance.model.nodes;

import com.connexience.applications.provenance.model.nodes.opm.Artifact;
import com.connexience.applications.provenance.model.relationships.ProvenanceRelationshipTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Created by IntelliJ IDEA.
 * User: nsjw7
 * Date: 07/04/2011
 * Time: 10:48
 */
public class WorkflowVersion extends Artifact
{
  public static final String ESC_ID = "escId";
  public static final String NAME = "name";
  public static final String VERSION_ID = "versionId";
  public static final String VERSION_NUM = "versionNumber";

  public WorkflowVersion(Node undelyingNode)
  {
    super(undelyingNode);
    underlyingNode.setProperty("TYPE", "Workflow Version");
  }

  public Node getUnderlyingNode()
  {
    return underlyingNode;
  }

  public void setEscId(String escId)
  {
    underlyingNode.setProperty(ESC_ID, escId);
  }

  public String getEscId()
  {
    return (String) underlyingNode.getProperty(ESC_ID);
  }

  public void setName(String firstName)
  {
    underlyingNode.setProperty(NAME, firstName);
  }

  public String getName()
  {
    return (String) underlyingNode.getProperty(NAME);
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

  public Relationship versionOf(WorkflowVersion previousVersion)
  {
      return underlyingNode.createRelationshipTo(previousVersion.getUnderlyingNode(), ProvenanceRelationshipTypes.VERSION_OF);
  }

}
