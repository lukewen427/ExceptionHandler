package com.connexience.applications.provenance.model.relationships;

import org.neo4j.graphdb.RelationshipType;

/**
 * Author: Simon
 * Date: Feb 23, 2011
 */
public enum ProvenanceRelationshipTypes implements RelationshipType
{
  REF,
  WAS_CONTROLLED_BY,
  RUN_OF,
  USED,
  WAS_GENERATED_BY,
  VERSION_OF,
  CONTAINED,
  INSTANCE_OF,
  REQUIRED
}
