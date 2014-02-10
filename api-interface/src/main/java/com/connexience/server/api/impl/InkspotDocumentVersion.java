/*
 * InkspotDocumentVersion.java
 */

package com.connexience.server.api.impl;
import com.connexience.server.api.*;

/**
 * This class provides a inkspot document version implementation
 * @author hugo
 */
public class InkspotDocumentVersion extends InkspotObject implements IDocumentVersion {

    public InkspotDocumentVersion() {
        super();
        putProperty("id", "");
        putProperty("number", "");
        putProperty("documentid", "");
        putProperty("userid", "");
        putProperty("modified", "");
        putProperty("size", "");
        putProperty("comments", "");
    }

    /** Version ID */
    public String getId(){
        return getPropertyString("id");
    }

    public void setId(String id){
        putProperty("id", id);
    }

    public String getDocumentId() {
        return getPropertyString("documentid");
    }

    public String getModificationTime() {
        return getPropertyString("modified");
    }

    public String getUserId() {
        return getPropertyString("userid");
    }

    public int getVersionNumber() {
        return Integer.parseInt(getPropertyString("number"));
    }

    public void setDocumentId(String documentId) {
        putProperty("documentid", documentId);
    }

    public void setModificationTime(String modificationTime) {
        putProperty("modified", modificationTime);
    }

    public void setUserId(String userId) {
        putProperty("userid", userId);
    }

    public void setVersionNumber(int versionNumber) {
        putProperty("number", Integer.toString(versionNumber));
    }

    @Override
    public String getComments() {
        return getPropertyString("comments");
    }

    @Override
    public void setComments(String comments) {
        putProperty("comments", comments);
    }

    @Override
    public long getSize() {
        return Long.parseLong(getPropertyString("size"));
    }

    @Override
    public void setSize(long size) {
        putProperty("size", Long.toString(size));
    }
}
