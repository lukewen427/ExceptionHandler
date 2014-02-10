package com.connexience.applications.provenance.model.nodes;

import com.connexience.applications.provenance.model.nodes.opm.Process;
import com.connexience.applications.provenance.model.relationships.ProvenanceRelationshipTypes;
import org.neo4j.graphdb.Node;

/**
 * Created by IntelliJ IDEA.
 * User: nsjw7
 * Date: 07/04/2011
 * Time: 10:48
 */
public class WorkflowRun extends Process
{
  public static final String ESC_ID = "escId";
  public static final String INVOCATION_ID = "invocationId";

  public WorkflowRun(Node undelyingNode)
  {
    super(undelyingNode);
    underlyingNode.setProperty("TYPE", "Workflow Run");
  }

  public void setEscId(String escId)
  {
    underlyingNode.setProperty(ESC_ID, escId);
  }

  public String getEscId()
  {
    return (String) underlyingNode.getProperty(ESC_ID);
  }

  public void setInvocationId(String invocationId)
  {
    underlyingNode.setProperty(INVOCATION_ID,  invocationId);
  }

  public String getInvocationId()
  {
    return (String) underlyingNode.getProperty(INVOCATION_ID);
  }

  public void contained(ServiceRun serviceRun)
  {
      underlyingNode.createRelationshipTo(serviceRun.getUnderlyingNode(), ProvenanceRelationshipTypes.CONTAINED);
  }

   public void runOf(WorkflowVersion workflowVersion)
  {
      underlyingNode.createRelationshipTo(workflowVersion.getUnderlyingNode(), ProvenanceRelationshipTypes.RUN_OF);
  }
}
