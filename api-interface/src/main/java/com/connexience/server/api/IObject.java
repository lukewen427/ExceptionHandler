/*
 * IObject.java
 */

package com.connexience.server.api;
import java.util.*;
/**
 * This interface defines the basic functionality of an Object within the system.
 * @author hugo
 */
public interface IObject {
/** Get the object properties as a Hashtable */
    public Hashtable<String,Object> getProperties();
}