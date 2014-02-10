package com.connexience.applications.provenance;

import java.io.Serializable;
import java.util.*;

/**
 * User: nsjw7
 * Date: 10/07/2012
 * Time: 11:11
 * The result of running a diff on a file
 */
public class DiffResult implements Serializable
{
  private String documentId1 = "";
  private String versionId1 = "";
  private String documentId2 = "";
  private String versionId2 = "";

  private boolean outputFilesSame = false;

  private int numInputs1 = 0;
  private int numInputs2 = 0;
  private int numMatchingInputs = 0;

  private HashMap<DataDiff, DataDiff> inputFilesMatch = new HashMap<DataDiff, DataDiff>();
  private boolean workflowsSame = true;
  private String workflowMismatch1 = "";
  private String workflowMismatch2 = "";

  private Set<String> invocationIds1 = new HashSet<String>();
  private Set<String> invocationIds2 = new HashSet<String>();

  private List<WorkflowDiff> workflowsForDocument1 = new ArrayList<WorkflowDiff>();
  private List<ServiceDiff> servicesForDocument1 = new ArrayList<ServiceDiff>();

  private List<WorkflowDiff> workflowsForDocument2 = new ArrayList<WorkflowDiff>();
  private List<ServiceDiff> servicesForDocument2 = new ArrayList<ServiceDiff>();

  private List<DataDiff> intermediateDataForDocument1 = new ArrayList<DataDiff>();
  private List<DataDiff> intermediateDataForDocument2 = new ArrayList<DataDiff>();

  private List<DataDiffResult> intermediateDiffs = new ArrayList<DataDiffResult>();

  public DiffResult()
  {

  }

  public DiffResult(String documentId1, String versionId1, String documentId2, String versionId2)
  {
    this.documentId1 = documentId1;
    this.versionId1 = versionId1;
    this.documentId2 = documentId2;
    this.versionId2 = versionId2;
  }

  public String getDocumentId1()
  {
    return documentId1;
  }

  public void setDocumentId1(String documentId1)
  {
    this.documentId1 = documentId1;
  }

  public String getVersionId1()
  {
    return versionId1;
  }

  public void setVersionId1(String versionId1)
  {
    this.versionId1 = versionId1;
  }

  public String getDocumentId2()
  {
    return documentId2;
  }

  public void setDocumentId2(String documentId2)
  {
    this.documentId2 = documentId2;
  }

  public String getVersionId2()
  {
    return versionId2;
  }

  public void setVersionId2(String versionId2)
  {
    this.versionId2 = versionId2;
  }

  public boolean isOutputFilesSame()
  {
    return outputFilesSame;
  }

  public void setOutputFilesSame(boolean outputFilesSame)
  {
    this.outputFilesSame = outputFilesSame;
  }

  public List<WorkflowDiff> getWorkflowsForDocument1()
  {
    return workflowsForDocument1;
  }

  public void setWorkflowsForDocument1(List<WorkflowDiff> workflowsForDocument1)
  {
    this.workflowsForDocument1 = workflowsForDocument1;
  }

  public List<ServiceDiff> getServicesForDocument1()
  {
    return servicesForDocument1;
  }

  public void setServicesForDocument1(List<ServiceDiff> servicesForDocument1)
  {
    this.servicesForDocument1 = servicesForDocument1;
  }

  public List<WorkflowDiff> getWorkflowsForDocument2()
  {
    return workflowsForDocument2;
  }

  public void setWorkflowsForDocument2(List<WorkflowDiff> workflowsForDocument2)
  {
    this.workflowsForDocument2 = workflowsForDocument2;
  }

  public List<ServiceDiff> getServicesForDocument2()
  {
    return servicesForDocument2;
  }

  public void setServicesForDocument2(List<ServiceDiff> servicesForDocument2)
  {
    this.servicesForDocument2 = servicesForDocument2;
  }

  public List<DataDiff> getIntermediateDataForDocument1()
  {
    return intermediateDataForDocument1;
  }

  public void setIntermediateDataForDocument1(List<DataDiff> intermediateDataForDocument1)
  {
    this.intermediateDataForDocument1 = intermediateDataForDocument1;
  }

  public List<DataDiff> getIntermediateDataForDocument2()
  {
    return intermediateDataForDocument2;
  }

  public void setIntermediateDataForDocument2(List<DataDiff> intermediateDataForDocument2)
  {
    this.intermediateDataForDocument2 = intermediateDataForDocument2;
  }

  public List<DataDiffResult> getIntermediateDiffs()
  {
    return intermediateDiffs;
  }

  public void setIntermediateDiffs(List<DataDiffResult> intermediateDiffs)
  {
    this.intermediateDiffs = intermediateDiffs;
  }

  public void addInputFileMatch(DataDiff file1, DataDiff file2)
  {
    inputFilesMatch.put(file1, file2);
  }

  public HashMap<DataDiff, DataDiff> getInputFilesMatch()
  {
    return inputFilesMatch;
  }

  public void setInputFilesMatch(HashMap<DataDiff, DataDiff> inputFilesMatch)
  {
    this.inputFilesMatch = inputFilesMatch;
  }

  public int getNumInputs1()
  {
    return numInputs1;
  }

  public void setNumInputs1(int numInputs1)
  {
    this.numInputs1 = numInputs1;
  }

  public int getNumInputs2()
  {
    return numInputs2;
  }

  public void setNumInputs2(int numInputs2)
  {
    this.numInputs2 = numInputs2;
  }

  public int getNumMatchingInputs()
  {
    return numMatchingInputs;
  }

  public void setNumMatchingInputs(int numMatchingInputs)
  {
    this.numMatchingInputs = numMatchingInputs;
  }

  public boolean addInnvocationId1(String s) {return invocationIds1.add(s);}

  public boolean addInnvocationId2(String s) {return invocationIds2.add(s);}

  public boolean isWorkflowsSame()
  {
    return workflowsSame;
  }

  public void setWorkflowsSame(boolean workflowsSame)
  {
    this.workflowsSame = workflowsSame;
  }

  public String getWorkflowMismatch1()
  {
    return workflowMismatch1;
  }

  public void setWorkflowMismatch1(String workflowMismatch1)
  {
    this.workflowMismatch1 = workflowMismatch1;
  }

  public String getWorkflowMismatch2()
  {
    return workflowMismatch2;
  }

  public void setWorkflowMismatch2(String workflowMismatch2)
  {
    this.workflowMismatch2 = workflowMismatch2;
  }

  @Override
  public String toString()
  {
    return "DiffResult{" +
        "documentId1='" + documentId1 + '\'' +
        ", versionId1='" + versionId1 + '\'' +
        ", documentId2='" + documentId2 + '\'' +
        ", versionId2='" + versionId2 + '\'' +
        ", outputFilesSame=" + outputFilesSame +
        ", numInputs1=" + numInputs1 +
        ", numInputs2=" + numInputs2 +
        ", numMatchingInputs=" + numMatchingInputs +
        ", workflowsSame=" + workflowsSame +
        ", workflowsMismatch1=" + workflowMismatch1 +
        ", workflowsMismatch2=" + workflowMismatch2+
        '}';
  }
}
