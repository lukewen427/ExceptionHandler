/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.connexience.server.model.logging.graph;
import java.util.*;
import java.io.*;

/** Service dependency */
public class Dependency implements Serializable {
    /** ID of the library */
    private String id;

    /** Version ID of the library */
    private String versionId;

    /** Name of the document */
    private String name;

    /** Dependencies of the library */
    private ArrayList<Dependency> dependencies = new ArrayList<Dependency>();

    public ArrayList<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(ArrayList<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id + "-" + versionId + "-" + name;
    }
}
