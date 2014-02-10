/*
 * InkspotObjectList.java
 */

package com.connexience.server.api.impl;
import com.connexience.server.api.*;
import java.util.*;
/**
 * This class provides an implementation of the object list that can hold
 * an embedded list of objects
 * @author nhgh
 */
public class InkspotObjectList extends InkspotObject implements IObjectList {
    /** Object store */
    protected ArrayList<IObject> objects = new ArrayList<IObject>();

    public InkspotObjectList() {
        super();
    }

    public void add(IObject item) {
        objects.add(item);
    }

    public Iterator<IObject> iterator() {
        return objects.iterator();
    }

    public void remove(IObject item) {
        objects.remove(item);
    }

    public int size() {
        return objects.size();
    }

    /** Get the objects */
    public ArrayList<IObject> getObjects() {
        return objects;
    }

    /** Remove an item by index */
    public void remove(int index) {
        objects.remove(index);
    }

    /** Get an object by index */
    public Object getObject(int index){
        return objects.get(index);
    }
}