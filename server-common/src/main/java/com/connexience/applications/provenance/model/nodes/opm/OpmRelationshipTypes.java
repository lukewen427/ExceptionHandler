package com.connexience.applications.provenance.model.nodes.opm;

import org.neo4j.graphdb.RelationshipType;

/**
 * Created by IntelliJ IDEA.
 * User: nsjw7
 * Date: 23/03/2011
 * Time: 08:21
 * To change this template use File | Settings | File Templates.
 */
public enum OpmRelationshipTypes implements RelationshipType
{
  USED,
  WAS_GENERATED_BY,
  WAS_CONTROLLED_BY,
  WAS_TRIGGERED_BY,
  WAS_DERIVED_FROM
}
