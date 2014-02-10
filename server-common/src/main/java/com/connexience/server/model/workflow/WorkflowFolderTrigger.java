/*
 * WorkflowFolderTrigger.java
 */
package com.connexience.server.model.workflow;
import java.io.*;

/**
 * This class defines a trigger for running a workflow whenever a document is uploaded
 * to a folder. 
 * @author hugo
 */
public class WorkflowFolderTrigger implements Serializable {
    static final long serialVersionUID = 1701083428027625450L;
    
    /** Database id */
    private long id;
    
    /** Target folder ID */
    private String folderId;
    
    /** Is this trigger enabled */
    private String enabled;
    
    /** Workflow to execute */
    private String workflowId;
    
    /** User that owns this trigger */
    private String ownerId;
    
    /** Should the file name be passed through a RegEx to check it matches a pattern */
    private boolean nameRegExChecked = false;
    
    /** RegEx to use to check the name */
    private String nameRegEx = "";

    /** Comments for this trigger */
    private String comments = "";
    
    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameRegEx() {
        return nameRegEx;
    }

    public void setNameRegEx(String nameRegEx) {
        this.nameRegEx = nameRegEx;
    }

    public boolean isNameRegExChecked() {
        return nameRegExChecked;
    }

    public void setNameRegExChecked(boolean nameRegExChecked) {
        this.nameRegExChecked = nameRegExChecked;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    
}