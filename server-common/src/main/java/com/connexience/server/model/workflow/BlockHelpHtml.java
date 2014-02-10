/*
 * BlockHelpHtml.java
 */

package com.connexience.server.model.workflow;
import java.io.*;
/**
 * This class contains the extracted html.html file from a workflow service
 * that has been uploaded.
 * @author hugo
 */
public class BlockHelpHtml implements Serializable {
    /** Database ID */
    private String id;

    /** ID of the service document */
    public String serviceId;

    /** ID of the workflow document version */
    public String versionId;

    /** XML Data as a string */
    public String htmlData;

    public String getHtmlData() {
        return htmlData;
    }

    public void setHtmlData(String htmlData) {
        this.htmlData = htmlData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
}