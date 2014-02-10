package com.connexience.applications.provenance;

import com.connexience.applications.provenance.model.nodes.opm.OpmObject;

import java.util.ArrayList;
import java.util.List;

public class TraverserResult
{

  private OpmObject object1 = null;
  private OpmObject object2 = null;
  private boolean objectsEquivalent = false;

  private List<OpmObject> nextObject1s = new ArrayList<OpmObject>();
  private List<OpmObject> nextObject2s = new ArrayList<OpmObject>();

  public TraverserResult()
  {
  }

  public TraverserResult(OpmObject object1, OpmObject object2, boolean objectsEquivalent, List<OpmObject> nextObject1s, List<OpmObject> nextObject2s)
  {
    this.object1 = object1;
    this.object2 = object2;
    this.objectsEquivalent = objectsEquivalent;
    this.nextObject1s = nextObject1s;
    this.nextObject2s = nextObject2s;
  }

  public OpmObject getObject1()
  {
    return object1;
  }

  public void setObject1(OpmObject object1)
  {
    this.object1 = object1;
  }

  public OpmObject getObject2()
  {
    return object2;
  }

  public void setObject2(OpmObject object2)
  {
    this.object2 = object2;
  }

  public boolean isObjectsEquivalent()
  {
    return objectsEquivalent;
  }

  public void setObjectsEquivalent(boolean objectsEquivalent)
  {
    this.objectsEquivalent = objectsEquivalent;
  }

  public boolean addNextObject1(OpmObject opmObject) {return nextObject1s.add(opmObject);}

  public List<OpmObject> getNextObject1s()
  {
    return nextObject1s;
  }

  public void setNextObject1s(List<OpmObject> nextObject1s)
  {
    this.nextObject1s = nextObject1s;
  }

  public List<OpmObject> getNextObject2s()
  {
    return nextObject2s;
  }

  public void setNextObject2s(List<OpmObject> nextObject2s)
  {
    this.nextObject2s = nextObject2s;
  }

  public boolean addNextObject2(OpmObject opmObject) {return nextObject2s.add(opmObject);}

  @Override
  public String toString()
  {
    return "TraverserResult{" +
        "object1=" + object1 +
        ", object2=" + object2 +
        ", objectsEquivalent=" + objectsEquivalent +
        ", nextObject1s=" + nextObject1s +
        ", nextObject2s=" + nextObject2s +
        '}';
  }
}
