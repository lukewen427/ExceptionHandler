package com.connexience.server.rmi;


import com.connexience.server.model.logging.graph.GraphOperation;

/**
 * User: nsjw7
 * Date: 21/06/2011
 * Time: 16:02
 * This interface represents how clients can log provenance data
 */
public interface IProvenanceLogger
{
  /**
   * Log a graph opertaion.  It will be logged in either the SQLDB and/or the Neo4J graph database
   * @param operation operation to be logged
   */
  public void log(GraphOperation operation);
}
