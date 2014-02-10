package com.connexience.applications.provenance.model.nodes.opm;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Created by IntelliJ IDEA.
 * User: nsjw7
 * Date: 23/03/2011
 * Time: 08:19
 */
public class Process extends OpmObject
{
  public Process(Node undelyingNode)
  {
    super(undelyingNode);
  }

  public Relationship used(Artifact artifact, String role, String account)
  {
      Relationship rel = underlyingNode.createRelationshipTo(artifact.getUnderlyingNode(), OpmRelationshipTypes.USED);
      rel.setProperty(OpmObject.ROLE, role);
      rel.setProperty(OpmObject.ACCOUNT, account);
      return rel;
  }

  public void wasControlledBy(Agent agent, String role, String account)
  {
      Relationship rel = underlyingNode.createRelationshipTo(agent.getUnderlyingNode(), OpmRelationshipTypes.WAS_CONTROLLED_BY);
      rel.setProperty(OpmObject.ROLE, role);
      rel.setProperty(OpmObject.ACCOUNT, account);
  }

  public void wasTriggeredBy(Process process, String role, String account)
  {
      Relationship rel = underlyingNode.createRelationshipTo(process.getUnderlyingNode(), OpmRelationshipTypes.WAS_TRIGGERED_BY);
      rel.setProperty(OpmObject.ROLE, role);
      rel.setProperty(OpmObject.ACCOUNT, account);
  }
}
