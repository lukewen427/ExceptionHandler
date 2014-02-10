
package com.connexience.server.ejb.storage;

import com.connexience.server.*;
import com.connexience.server.model.*;
import com.connexience.server.model.document.*;
import com.connexience.server.model.storage.*;
import com.connexience.server.model.security.*;
import com.connexience.server.model.folder.*;
import javax.ejb.Remote;
import java.util.HashMap;
import java.util.List;

/**
 * This is the business interface for ObjectStorage enterprise bean.
 */
@Remote
public interface StorageRemote {
    /**
     * Get a DocumentRecord object from the database
     */
    com.connexience.server.model.document.DocumentRecord getDocumentRecord(Ticket ticket, String recordId) throws ConnexienceException;

    /**
     * Get all of the metadata associated with a document record
     */
    com.connexience.server.model.document.MetaDataCollection getDocumentMetadata(Ticket ticket, String documentId) throws ConnexienceException;

    /**
     * Set all of the metadata associated with a document record
     */
    void setDocumentMetadata(Ticket ticket, String documentId, MetaDataCollection metadata) throws ConnexienceException;

    /**
     * Save a document record to the database
     */
    com.connexience.server.model.document.DocumentRecord saveDocumentRecord(Ticket ticket, DocumentRecord record) throws ConnexienceException;

    /**
     * Search for document records based on a metadata set
     */
    java.util.List searchDocuments(Ticket ticket, MetaDataCollection metadata) throws ConnexienceException;

    /**
     * Attach a piece of metadata to a DocumentRecord
     */
    void attachMetadata(Ticket ticket, String objectId, PropertyMetadata metadata) throws ConnexienceException;

    /**
     * Get the top level data folder for the ticket organisation
     */
    com.connexience.server.model.folder.DataFolder getDataFolder(Ticket ticket) throws ConnexienceException;

    /**
     * Get the document record contents of a folder
     */
    java.util.List getFolderDocumentRecords(Ticket ticket, String folderId) throws ConnexienceException;

    /**
     * Get all of the contents of a folder
     */
    java.util.List getAllFolderContents(Ticket ticket, String folderId) throws ConnexienceException;
    
    /**
     * Add a child to a folder
     */
    Folder addChildFolder(Ticket ticket, String parentId, Folder child) throws ConnexienceException;

    /**
     * Get the child folders of a folder
     */
    java.util.List getChildFolders(Ticket ticket, String folderId) throws ConnexienceException;

    /**
     * Get the top level folders for an organisation that a user can see
     */
    java.util.List getTopLevelFolders(Ticket ticket) throws ConnexienceException;

    
    /**
     * Save a datastore
     */
    com.connexience.server.model.storage.DataStore saveDataStore(Ticket ticket, DataStore store) throws ConnexienceException;

    /**
     * Get the datastore for an organisation
     */
    com.connexience.server.model.storage.DataStore getOrganisationDataStore(Ticket ticket, String organisationId) throws ConnexienceException;

    /**
     * Get a datastore
     */
    com.connexience.server.model.storage.DataStore getDataStore(Ticket ticket, String storeId) throws ConnexienceException;

    /**
     * Get the next Version object for a document record. What gets returned depends on the
     * settings in the DocumentRecord. If the document is versioned a new version is returned. If
     * there is only a single version, the existing version returned if there is one, otherwise
     * a new version is created. This is controlled by the DocumentVersionManager object
     */
    com.connexience.server.model.document.DocumentVersion createNextVersion(Ticket ticket, String documentId) throws ConnexienceException;

    /**
     * List the versions for a document
     */
    java.util.List listVersions(Ticket ticket, String documentId) throws ConnexienceException;

    /**
     * Get the latest version of a DocumentRecord
     */
    com.connexience.server.model.document.DocumentVersion getLatestVersion(Ticket ticket, String documentId) throws ConnexienceException;

    /** Get the latest versions of a list of DocumentRecord IDs */
    java.util.List getLatestVersions(Ticket ticket, java.util.List documentIds) throws ConnexienceException;
    
    /**
     * Update a version object to the database
     */
    com.connexience.server.model.document.DocumentVersion updateVersion(Ticket ticket, DocumentVersion version) throws ConnexienceException;

    /**
     * Get a specific version from the database
     */
    com.connexience.server.model.document.DocumentVersion getVersion(Ticket ticket, String documentId, String versionId) throws ConnexienceException;

    /**
     * Get a specific version by number from the database
     */
    com.connexience.server.model.document.DocumentVersion getVersion(Ticket ticket, String documentId, int version) throws ConnexienceException;

    /**
     * Remove a DocumentRecord from the database
     */
    void removeDocumentRecord(Ticket ticket, String recordId) throws ConnexienceException;

    /**
     * Remove all of the documents from a folder. This does not remove child folders
     */
    void removeFolderDocumentRecords(Ticket ticket, String folderId) throws ConnexienceException;

    /**
     * Remove all child folders from the specified folder downwards
     */
    void removeFolderTree(Ticket ticket, String folderId) throws ConnexienceException;

    /**
     * Get a named DocumentRecord from a Folder
     */
    com.connexience.server.model.document.DocumentRecord getNamedDocumentRecord(Ticket ticket, String folderId, String name) throws ConnexienceException;

    /**
     * Rename a Folder
     */
    void renameFolder(Ticket ticket, String folderId, String name) throws ConnexienceException;

    /**
     * List document types for the ticket organisation
     */
    java.util.List listDocumentTypes(Ticket ticket) throws ConnexienceException;

    /**
     * Get a specific document type
     */
    com.connexience.server.model.document.DocumentType getDocumentType(Ticket ticket, String documentTypeId) throws ConnexienceException;

    /**
     * Remove a document type
     */
    void removeDocumentType(Ticket ticket, String documentTypeId) throws ConnexienceException;

    /**
     * Save a document type
     */
    com.connexience.server.model.document.DocumentType saveDocumentType(Ticket ticket, DocumentType type) throws ConnexienceException;

    /**
     * Get a document type by extension
     */
    com.connexience.server.model.document.DocumentType getDocumentTypeByExtension(Ticket ticket, String extension) throws ConnexienceException;

    /** Get a document type by mime-type */
    com.connexience.server.model.document.DocumentType getDocumentTypeByMime(Ticket ticket, String mime) throws ConnexienceException;

    /**
     * Get a folder based upon a URI string. This is derived from the Data folder
     * of the current organisation
     */
    com.connexience.server.model.ServerObject getResourceByURI(Ticket ticket, String baseFolderId, String uri, String permissionType) throws ConnexienceException;

    /**
     * Move a Folder to a new parent
     */
    void moveFolder(Ticket ticket, String sourceFolderId, String targetFolderId) throws ConnexienceException;

    /** Move a document to a new parent */
    public void moveDocument(Ticket ticket, String documentId, String targetFolderId) throws ConnexienceException;

    /** 
     * Get a specific folder by ID 
     */
    public Folder getFolder(Ticket ticket, String folderId) throws ConnexienceException;  

    /** Get a folder using it's shortname */
    public Folder getFolderByShortName(Ticket ticket, String folderShortName) throws ConnexienceException;
    
    /**
     * Get a User's home folder
     */
    public Folder getHomeFolder(Ticket ticket, String userId) throws ConnexienceException;    
    
    /** Get the total size of a users files */
    public long getUserStorageUsage(Ticket ticket, String userId) throws ConnexienceException;

  /** Get a DocumentRecord assuming the user has the permission type passed in.  Used in workflow blocks to get scripts that have
   * an execute permission*/
  DocumentRecord getDocumentRecord(Ticket ticket, String recordId, String permissionTypeToAssert) throws ConnexienceException;

  /** Get the latest version of a DocumentRecord assuming the user has the supplied permission */
  DocumentVersion getLatestVersion(Ticket ticket, String documentId, String permissionTypeToAssert) throws ConnexienceException;

  /** Get the workflow folder for a User */
  Folder getWorkflowFolder(Ticket ticket, String userId) throws ConnexienceException;

  /** Update a folder */
  Folder updateFolder(Ticket ticket, Folder folder) throws ConnexienceException;

  /**
   * Get the list of links folders that are created in groups
   */
  List<LinksFolder> listGroupLinkFolders(Ticket ticket, String groupId) throws ConnexienceException;

    /**
     * Get the top 5 count of file types stored by the user.
     * @param ticket User ticket
     * @return HashMap of File type (String) -> Number of files (Long)
     * @throws com.connexience.server.ConnexienceException Something broke
     */
    HashMap<String, Long> getFileTypesForUser(Ticket ticket) throws ConnexienceException;

  /** Get a named Folder from a Folder */
  Folder getNamedFolder(Ticket ticket, String folderId, String name) throws ConnexienceException;

    /** Make a folder a template folder which can be copied when a user registers */
    TemplateFolder createTemplateFolder(Ticket ticket, Folder folder, String description) throws ConnexienceException;

  @SuppressWarnings("unchecked")
  List<TemplateFolder> listTemplateFolders(Ticket ticket, String domain) throws ConnexienceException;

  @SuppressWarnings("unchecked")
  TemplateFolder getTemplateFolder(Ticket ticket, String id) throws ConnexienceException;

  @SuppressWarnings("unchecked")
  void deleteTemplateFolder(Ticket ticket, String id) throws ConnexienceException;

  boolean addTemplateFolderToUsersSpace(Ticket ticket, String userId, TemplateFolder templateFolder) throws ConnexienceException;

  @SuppressWarnings("unchecked")
  List<TemplateFolder> listTemplateFolders(Ticket ticket) throws ConnexienceException;
}
