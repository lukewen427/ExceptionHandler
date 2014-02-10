/*
 * InkspotAPI.java
 */

package com.connexience.server.api;

import org.w3c.dom.*;

import java.util.*;
import java.io.*;
import java.rmi.*;
import java.net.*;

/**
 * This interface provides an object level InkSpot API client that can communicate
 * with the REST API interface.
 * @author hugo
 */
public interface API extends Remote {
    /** Create a new object. This uses the XML Name defined in the interface
     * class. Most implementations will just use the ObjectBuilder to create
     * the new object, but this behaviour can be overridden if needed. */
    public IObject createObject(String objectXmlName) throws APIInstantiationException;

    /** Get the URL of the server that is providing API access */
    public URL getServerUrl();

    /** Get the ID of the user that this link was created for */
    public String getUserContextId();

    /** Get the IUser object that this link was created for */
    public IUser getUserContext();

    /** Authenticate as the public user */
    public IUser authenticateAsPublic() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Autheticate a user */
    public IUser authenticate(String username, String password) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a user by ID */
    public IUser getUser(String userId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Save changes to a user */
    public void saveUser(IUser user);

    /** Get the groups that a user is a member of */
    public List<IGroup> getUserGroups(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the list of connections that a user has */
    public List<IUser> getUserConnections(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the users home data folder */
    public IFolder getUserFolder(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the document contents of a folder */
    public List<IObject> getFolderContents(IFolder folder) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a folder object by ID */
    public IFolder getFolder(String folderId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a list of versions for a document */
    public List<IDocumentVersion> getDocumentVersions(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the ID of the latest version of a document */
    public String getLatestVersionId(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a list of signatures for a document */
    public List<ISignatureData> getDocumentSignatures(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Download the latest contents of a document to an output stream */
    public void download(IDocument document, OutputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Download the contents of a specific version of a document to an output stream */
    public void download(IDocument document, int versionNumber, OutputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Download the contents of a specific version of a document to an output stream */
    public void download(IDocument document, String versionId, OutputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get an input stream connected to the latest version of a document */
    public InputStream getDocumentInputStream(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get an input stream connected to a specific version of a document */
    public InputStream getDocumentInputStream(IDocument document, String versionId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Create a new document in a parent folder */
    public IDocument saveDocument(IFolder parentFolder, IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Save an existing document */
    public IDocument saveDocument(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a document object */
    public IDocument getDocument(String documentId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Upload data for a document */
    public IDocumentVersion upload(IDocument document, InputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** List the users metadata documents */
    public List<IXmlMetaData> listMetaData(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Upload a metadata descriptor document */
    public IXmlMetaData saveMetaDataInfo(IUser user, IXmlMetaData metaData) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a metadata descriptor document */
    public IXmlMetaData getMetaDataInfo(IUser user, String metaDataId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the XML contents for a metadata descriptor document */
    public Document downloadMetaData(IXmlMetaData metaData) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Upload some Xml data for a metadata descriptor document */
    public IXmlMetaData uploadMetaData(IUser user, String name, InputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the user workflows */
    public List<IWorkflow> listWorkflows() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** List the invocations for a workflow */
    public List<IWorkflowInvocation> listInvocations(IWorkflow workflow) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Start a workflow running on some data */
    public IWorkflowInvocation executeWorkflow(IWorkflow workflow, IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Start a workflow running with a parameter set */
    public IWorkflowInvocation executeWorkflow(IWorkflow workflow, IWorkflowParameterList parameters) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a workflow invocation folder by ID. This is typically used to check the invocation status */
    public IWorkflowInvocation getWorkflowInvocation(String invocationId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a list of the locations where an object has been updated*/
    public List<ILatLongHolder> getLocationsForObject(ISecuredObject object, int logMessageType) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Delete a folder and all its children */
    public void deleteFolder(IFolder folder) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Delete a document */
    public void deleteDocument(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a dynamic workflow library by name */
    public IDynamicWorkflowLibrary getDynamicWorkflowLibraryByName(String libraryName) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Save an external object */
    public IExternalObject saveExternalObject(IExternalObject object) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Delete an external object */
    public void deleteExternalObject(IExternalObject object) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get an external object by ID */
    public IExternalObject getExternalObject(String externalObjectId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get all the external objects for an application */
    public List<IExternalObject> listExternalObjects() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get all the properties of an object */
    public IPropertyList getObjectProperties(String objectId, String propertyGroup) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Set all the properties of an object */

    public IPropertyList setObjectProperties(String objectId, String propertyGroup, IPropertyList properties) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the permissions of an object */
    public IPermissionList getObjectPermissions(String objectId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Assign a permission to an object */
    public IPermissionList grantObjectPermission(ISecuredObject object, ISecuredObject principal, String permissionType) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Remove a permission from an object */
    public void revokeObjectPermission(ISecuredObject object, ISecuredObject principal, String permissionType) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the public user for the system */
    public IUser getPublicUser() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Link two objects together */
    public ILink linkObjects(ILink link) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get all of the objects linked to the specified object */
    public List<ISecuredObject> getLinkedObjects(String sourceObjectId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a group by ID */
    public IGroup getGroup(String groupId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Join a group */
    public void joinGroup(IGroup group) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Add a folder */
    public IFolder addChildFolder(IFolder parent, String childName) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Save a folder */
    public IFolder saveFolder(IFolder folder) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Terminate a workflow */
    public void terminateWorkflow(IWorkflowInvocation invocation) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Create a new group */
    public IGroup createGroup(String groupName, String groupDescription) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Save a group */
    public IGroup saveGroup(IGroup group) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a workflow by id */
    public IWorkflow getWorkflow(String workflowId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Zip a folder on the server and return a reference to the zipped document */
    public IDocument zipFolder(IFolder folder, IDocument targetDocument) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Register a new user.  Only administrative applications are allowed to do this.  Throws @APISecurityException if invoked by non-administrative application */
    public IUser register(INewUser user)throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get a user by ID */
    public IUser findUserByEmail(String emailAddress) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;

    /** Get the XML describing a service */
    public String getServiceXml(IDocument serviceDocument) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException;
    
}