/*
 * DynamicWorkflowLibrary.java
 */

package com.connexience.server.model.workflow;

import com.connexience.server.model.document.*;

/**
 * This class extends a document record to provide a storage for a library
 * component that gets downloaded to support workflow service objects.
 * @author nhgh
 */
public class DynamicWorkflowLibrary extends DocumentRecord {
    /** Short name for this library. This is equivalent to the artifactId
     * property in a pom. It enables dependencies to be expressed in terms
     * of this rather than a long ID string */
    private String libraryName = "";

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryName() {
        return libraryName;
    }
}
