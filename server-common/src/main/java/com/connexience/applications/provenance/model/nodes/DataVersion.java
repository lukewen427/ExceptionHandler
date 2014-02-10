package com.connexience.applications.provenance.model.nodes;

import com.connexience.applications.provenance.model.nodes.opm.Artifact;
import com.connexience.applications.provenance.model.nodes.opm.Process;
import com.connexience.applications.provenance.model.relationships.ProvenanceRelationshipTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;

public class DataVersion extends Artifact
{
  public static final String ESC_ID = "escId";
  public static final String VERSION_ID = "versionId";
  public static final String DOCUMENT_ID = "documentId";
  public static final String VERSION_NUM = "versionNumber";

  public DataVersion(Node underlyingNode)
  {
    super(underlyingNode);
    this.underlyingNode.setProperty("TYPE", "DataVersion");
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

  public void setVersionId(String versionId)
  {
    underlyingNode.setProperty(VERSION_ID, versionId);
  }

  public String getVersionId()
  {
    return (String) underlyingNode.getProperty(VERSION_ID);
  }

  public void setVersionNum(String versionNum)
  {
    underlyingNode.setProperty(VERSION_NUM, versionNum);
  }

  public String getVersionNum()
  {
    return (String) underlyingNode.getProperty(VERSION_NUM);
  }


  public void setDocumentId(String documentId)
  {
    underlyingNode.setProperty(DOCUMENT_ID, documentId);
  }

  public String getDocumentId()
  {
    return (String) underlyingNode.getProperty(DOCUMENT_ID);
  }

  public Relationship versionOf(Process process, String role, String account, HashMap<String, Object> properties)
  {
    Relationship rel = super.wasGeneratedBy(process, role, account);
    for (String key : properties.keySet())
    {
      rel.setProperty(key, properties.get(key));
    }
    return rel;
  }

  public Relationship wasDerivedFrom(Artifact artifact, String role, String account, HashMap<String, Object> properties)
  {
    Relationship rel = super.wasDerivedFrom(artifact, role, account);
    for (String key : properties.keySet())
    {
      rel.setProperty(key, properties.get(key));
    }
    return rel;
  }

  public Relationship versionOf(DataVersion previousVersion)
  {
    return underlyingNode.createRelationshipTo(previousVersion.getUnderlyingNode(), ProvenanceRelationshipTypes.VERSION_OF);
  }


  //todo: needs to kick off workflow
  @Override
  public boolean equals(Object o)
  {
    if(!(o instanceof DataVersion))
    {
      return false;
    }
    DataVersion that = (DataVersion) o;
    if(this.getEscId().equals(that.getEscId()))
    {
      if(this.getDocumentId().equals(that.getDocumentId()))
      {
        if(this.getVersionId().equals(that.getVersionId()))
        {
          return  true;
        }
      }
    }
    return false;
  }

  @Override
  public String toString()
  {
    String out =  "";
    for(String key : underlyingNode.getPropertyKeys())
    {
      out+= key + ": " + underlyingNode.getProperty(key) + "\n";
    }
    return out;
  }
}
