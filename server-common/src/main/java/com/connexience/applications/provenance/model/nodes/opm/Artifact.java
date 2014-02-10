package com.connexience.applications.provenance.model.nodes.opm;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Created by IntelliJ IDEA.
 * User: nsjw7
 * Date: 23/03/2011
 * Time: 08:19
 */
public class Artifact extends OpmObject
{
  public Artifact(Node undelyingNode)
  {
    super(undelyingNode);
  }

  public final Relationship wasGeneratedBy(Process process, String role, String account)
  {
      Relationship rel = underlyingNode.createRelationshipTo(process.getUnderlyingNode(), OpmRelationshipTypes.WAS_GENERATED_BY);
      rel.setProperty(OpmObject.ROLE, role);
      rel.setProperty(OpmObject.ACCOUNT, account);
      return rel;
  }

  public final Relationship wasDerivedFrom(Artifact artifact, String role, String account)
  {
      Relationship rel = underlyingNode.createRelationshipTo(artifact.getUnderlyingNode(), OpmRelationshipTypes.WAS_DERIVED_FROM);
      rel.setProperty(OpmObject.ROLE, role);
      rel.setProperty(OpmObject.ACCOUNT, account);
      return rel;
  }

}
