/*
 * SharedDataFolder.java
 */

package com.connexience.server.model.folder;

/**
 * This is a meta-folder representing data that a user has shared
 * @author nhgh
 */
public class SharedDataFolder extends Folder {
    /** ID of the sharing user */
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}