/*
 * IObjectList.java
 */

package com.connexience.server.api;
import java.util.*;
/**
 * This interface defines a list of IObject objects
 * @author nhgh
 */
public interface IObjectList extends IObject {  
    /** Get the size of the list */
    public int size();

    /** Get an Iterator of items */
    public Iterator<IObject> iterator();

    /** Remove an item from the list */
    public void remove(IObject item);

    /** Remove an item by index */
    public void remove(int index);

    /** Get an item by index */
    public Object getObject(int index);

    /** Add an item to the list */
    public void add(IObject item);

    /** Get the objects */
    public ArrayList<IObject> getObjects();
}