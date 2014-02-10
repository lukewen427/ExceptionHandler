package com.connexience.server.model.folder;

import java.io.Serializable;

/**
 * User: nsjw7
 * Date: 15/06/2012
 * Time: 12:06
 * A template folder that can be copied into a user's home directory when they register
 */
public class TemplateFolder implements Serializable {

    private String id;

    private String folderId;

    private String name;

    private String domain;

    private String description;

    public TemplateFolder() {
    }

    public TemplateFolder(String id, String name, String domain) {
        this.id = id;
        this.name = name;
        this.domain = domain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
