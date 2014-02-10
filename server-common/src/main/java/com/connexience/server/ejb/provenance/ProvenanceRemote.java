package com.connexience.server.ejb.provenance;


import com.connexience.applications.provenance.DiffResult;
import com.connexience.server.ConnexienceException;

import javax.ejb.Remote;
import java.util.HashMap;

/**
 * User: nsjw7
 * Date: Mar 15, 2011
 * Time: 2:18:55 PM
 * This interface represents how clients can query provenance data
 */
@Remote
public interface ProvenanceRemote
{

  /**
   * Get a JSON version of a workflow which represnets the provenance of an objcet
   *
   * @param versionId The eSC versionId of the object, note not the documentId
   * @param positions positions on the canvas where the blocks should be placed.  The key should be blockUUID-invocationId
   * @return JSON String of the workflow
   */
  public String createVirtualWorkflow(String versionId, HashMap<String, HashMap<String, Integer>> positions) throws ConnexienceException;

  /**
   * Get the provenance (history) or effects of an object
   *
   * @param versionId DocumentVersionId of the object
   * @param direction forwards (effects) or backwards (provenance)
   * @return JSON String that is formatted for the JIT force directed layout
   */
  public String getProvenance(String versionId, String direction) throws ConnexienceException;

  public DiffResult pDiff(String documentId1, String versionId1, String documentId2, String versionId2, String workflowId) throws ConnexienceException;

}
