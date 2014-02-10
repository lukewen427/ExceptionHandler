/*
 * Organisation.java
 */

package com.connexience.server.model.organisation;
import com.connexience.server.model.*;

/**
 * This class represents an organisation, which can contain
 * groups, users, documents, etc
 * @author hugo
 */
public class Organisation extends ServerObject {
    /** Administration group */
    private String adminGroupId;
    
    /** ID of the users folder */
    private String userFolderId;
    
    /** Default group for new users */
    private String defaultGroupId;
    
    /** ID of the groups folder */
    private String groupFolderId;
    
    /** ID of the data folder */
    private String dataFolderId;

    /** ID of the data store */
    private String dataStoreId;
    
    /** ID of the data types folder */
    private String documentTypesFolderId;
    
    /** ID of the services folder */
    private String servicesFolderId;

    /** ID of the applications folder */
    private String applicationsFolderId;

    /** ID of the default user that can be assigned if nobody is logged in */
    private String defaultUserId;

    /** ID of the public access group */
    private String publicGroupId;
    
    /** Constructor */
    public Organisation(){
    }

    /** Is a folder a special folder */
    public boolean isSpecialFolder(String folderId){
        if(folderId!=null){
            if(folderId.equals(applicationsFolderId) || folderId.equals(dataFolderId)
               || folderId.equals(documentTypesFolderId) || folderId.equals(groupFolderId)
               || folderId.equals(servicesFolderId)
               || folderId.equals(userFolderId)){
                return true;

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /** Get the id of the group that can administer this organisation */
    public String getAdminGroupId() {
        return adminGroupId;
    }

    /** Set the id of the group that can administer this organisation */
    public void setAdminGroupId(String adminGroupId) {
        this.adminGroupId = adminGroupId;
    }

    /** Get the id of the user folder object */
    public String getUserFolderId() {
        return userFolderId;
    }

    /** Set the ID of the user folder for this organisation */
    public void setUserFolderId(String userFolderId) {
        this.userFolderId = userFolderId;
    }

    /** Set the ID of the groups folder for this organisation */
    public String getGroupFolderId() {
        return groupFolderId;
    }

    /** Set the ID of the groups folder for this organisation */
    public void setGroupFolderId(String groupFolderId) {
        this.groupFolderId = groupFolderId;
    }

    /** Get the ID of the data folder */
    public String getDataFolderId() {
        return dataFolderId;
    }

    /** Set the id of the data folder */
    public void setDataFolderId(String dataFolderId) {
        this.dataFolderId = dataFolderId;
    }

    /** Get the ID of the data store for this organisation */
    public String getDataStoreId() {
        return dataStoreId;
    }

    /** Set the ID of the data store for this organisation */
    public void setDataStoreId(String dataStoreId) {
        this.dataStoreId = dataStoreId;
    }
    
    /** Get the id of the data types folder */
    public String getDocumentTypesFolderId() {
        return documentTypesFolderId;
    }

    /** Set the id of the data types folder */
    public void setDocumentTypesFolderId(String documentTypesFolderId) {
        this.documentTypesFolderId = documentTypesFolderId;
    }

    /** Set the default group ID */
    public void setDefaultGroupId(String defaultGroupId){
        this.defaultGroupId = defaultGroupId;
    }
    
    /** Get the default group ID */
    public String getDefaultGroupId(){
        return defaultGroupId;
    }
    
    /** Get the services folder ID */
    public String getServicesFolderId(){
        return servicesFolderId;
    }
    
    /** Set the ID of the services folder */
    public void setServicesFolderId(String servicesFolderId){
        this.servicesFolderId = servicesFolderId;
    }

    /**
     * @return the applicationsFolderId
     */
    public String getApplicationsFolderId() {
        return applicationsFolderId;
    }

    /**
     * @param applicationsFolderId the applicationsFolderId to set
     */
    public void setApplicationsFolderId(String applicationsFolderId) {
        this.applicationsFolderId = applicationsFolderId;
    }

    /**
     * @return the defaultUserId
     */
    public String getDefaultUserId() {
        return defaultUserId;
    }

    /**
     * @param defaultUserId the defaultUserId to set
     */
    public void setDefaultUserId(String defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    /**
     * @return the publicGroupId
     */
    public String getPublicGroupId() {
        return publicGroupId;
    }

    /**
     * @param publicGroupId the publicGroupId to set
     */
    public void setPublicGroupId(String publicGroupId) {
        this.publicGroupId = publicGroupId;
    }
}