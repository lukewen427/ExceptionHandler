/*
 * RPCClientApi.java
 */

package com.connexience.server.workflow.api.rpc;

import com.connexience.server.workflow.rpc.*;
import com.connexience.server.api.*;
import com.connexience.server.api.impl.InkspotTypeRegistration;
import com.connexience.server.api.util.*;
import com.connexience.server.util.SerializationUtils;
import com.connexience.server.model.security.*;
import com.connexience.server.model.storage.DataStore;
import com.connexience.server.workflow.api.rpc.downloaders.DirectDownloader;
import com.connexience.server.workflow.api.rpc.downloaders.HttpDownloader;
import com.connexience.server.workflow.api.rpc.uploaders.DirectUploader;
import com.connexience.server.workflow.api.rpc.uploaders.HttpUploader;
import org.pipeline.core.xmlstorage.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.Document;
import java.net.*;
import java.util.ArrayList;
import org.apache.log4j.*;

/**
 * This class provides an RPC backed API connection to the server
 * @author nhgh
 */
public class RPCClientApi implements API, Serializable, XmlStorable {
    /** Logger */
    private static Logger logger = Logger.getLogger(RPCClientApi.class);
        
    /** RPCClient used for communication */
    private RPCClient client;

    /** Security ticket */
    private Ticket ticket;

    /** URL for the download servlet */
    private String downloadLocation = "/WorkflowServer/WorkflowAPIDataServlet";

    /** Number of times to try resending a call */
    int retryCount = 20;
    
    /** Starting waiting interval at first failure */
    int initialRetryInterval = 1000;
    
    /** Maximum waiting interval after ramping up */
    int maxWaitInterval = 10000;
    
    /** Interval multiplication factor for each failed attempt */
    double retryMultiplier = 2;
    
    /** Datastore from the organisation if there is one */
    private DataStore dataStore = null;
    
    /** Force http upload and download */
    private boolean forceHttp = false;
    
    /** Empty constructor */
    public RPCClientApi(){

    }

    public void setForceHttp(boolean forceHttp) {
        this.forceHttp = forceHttp;
    }

    public boolean isForceHttp() {
        return forceHttp;
    }
    
    
    /** Construct by copying an existing client, but specifying a new ticket */
    public RPCClientApi(RPCClient existingClient, Ticket ticket) throws RPCException {
        client = new RPCClient(existingClient.getServerUrl());
        client.setSecurityMethod(RPCClient.TICKET_SECURITY);
        client.setTicket(ticket);
        this.ticket = ticket;
    }

    @Override
    public URL getServerUrl() {
        try {
            return new URL(client.getServerUrl());
        } catch (Exception e){
            return null;
        }
    }

    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public void setInitialRetryInterval(int initialRetryInterval) {
        this.initialRetryInterval = initialRetryInterval;
    }

    public int getInitialRetryInterval() {
        return initialRetryInterval;
    }

    public void setMaxWaitInterval(int maxWaitInterval) {
        this.maxWaitInterval = maxWaitInterval;
    }

    public int getMaxWaitInterval() {
        return maxWaitInterval;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryMultiplier(double retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
    }

    public double getRetryMultiplier() {
        return retryMultiplier;
    }

    public RPCClient getClient(){
        return client;
    }

    public Ticket getTicket(){
        return ticket;
    }

    public RPCClientWorkflowAPI createWorkflowApi() throws RPCException {
        RPCClientWorkflowAPI wfApi = new RPCClientWorkflowAPI(client, ticket);
        return wfApi;
    }
    
    public void setDownloadLocation(String downloadLocation){
        this.downloadLocation = downloadLocation;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }

    /** Terminate the RPC client object */
    public void terminateClient(){
        client.close();
    }

    /** Send ticket details */
    private void sendTicket(boolean force) throws RPCException {
        // Do an authentication
        if(force || client.getSessionId()==null ||client.getSessionId().trim().equalsIgnoreCase("")){
            if(ticket!=null){
                CallObject call = new CallObject("APISetTicket");
                call.getCallArguments().add("UserID", ticket.getUserId());
                call.setAuthenticationRequired(false);
                client.syncCall(call);
                client.setSessionId(call.getReturnArguments().stringValue("SessionID", null));
            }
        }
    }

    public RPCClientApi(RPCClient client) {
        this.client = client;
    }

    /** Check for an error object and return an Exception that can be thrown */
    private void checkForError(List<IObject> results) throws APISecurityException {
        if(results.size()>0){
            if(results.get(0) instanceof IAPIErrorMessage){
                IAPIErrorMessage message = (IAPIErrorMessage)results.get(0);
                throw new APISecurityException(message.getErrorMessage());
            }
        }
    }

    /** Create a downloader object */
    private Downloader createDownloader() {
        if(forceHttp){
            logger.info("Forcing HTTP download");
            return new HttpDownloader();
        } else {
            if(dataStore!=null){
                if(dataStore.isDirectAccessSupported()){
                    logger.info("Direct access supported for data store: " + dataStore.getClass().getSimpleName());
                    return new DirectDownloader();
                } else {
                    logger.info("Direct access not supported for data store: " + dataStore.getClass().getSimpleName() + " using HttpDownloader");
                    return new HttpDownloader();
                }
            } else {
                logger.info("No datastore present - using HttpDownloader");
                return new HttpDownloader();
            }
            
        }
    }
    
    /** Create an uploader object */
    private Uploader createUploader(){
        if(forceHttp){
            logger.info("Forcing HTTP uploader");
            return new HttpUploader();
        } else {
            if(dataStore!=null){
                if(dataStore.isDirectAccessSupported()){
                    logger.info("Direct access supported for data store: " + dataStore.getClass().getSimpleName());
                    return new DirectUploader();
                } else {
                    logger.info("Direct access not supported for data store: " + dataStore.getClass().getSimpleName() + " using HttpUploader");
                    return new HttpUploader();
                }
            } else {
                logger.info("No datastore present - using HttpUploader");
                return new HttpUploader();
            }        
        }
    }
    
    /** Authenticate a user. This method just returns the current user because the API is already authenticated */
    public IUser authenticate(String username, String password) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        return null;
    }

    @Override
    public IUser authenticateAsPublic() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        return null;
    }

    /** Download document data to an output stream */
    public void download(IDocument document, OutputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        try {
            Downloader downloader = createDownloader();
            downloader.setParent(this);
            downloader.setDocument(document);
            downloader.setStream(stream);
            downloader.download();

        } catch (Exception e){
            throw new APIConnectException("Error connecting to server: " + e.getMessage());
        }
    }

    public InputStream getDocumentInputStream(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        try {
            Downloader downloader = createDownloader();
            downloader.setParent(this);
            downloader.setDocument(document);
            return downloader.getInputStream();       
        } catch (Exception e){
            throw new APIConnectException("Error connecting to server: " + e.getMessage());
        }
    }


    public void download(IDocument document, int versionNumber, OutputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        try {
            Downloader downloader = createDownloader();
            downloader.setParent(this);
            downloader.setDocument(document);
            downloader.setVersionNumber(versionNumber);
            downloader.setStream(stream);
            downloader.download();
            
        } catch (Exception e){
            throw new APIConnectException("Error connecting to server: " + e.getMessage());
        }
    }

    /** Download the contents of a specific version of a document to an output stream */
    public void download(IDocument document, String versionId, OutputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        try {
            Downloader downloader = createDownloader();
            downloader.setParent(this);
            downloader.setDocument(document);
            downloader.setVersionId(versionId);
            downloader.setStream(stream);
            downloader.download();
        } catch (Exception e){
            throw new APIConnectException("Error connecting to server: " + e.getMessage());
        }
    }

    public InputStream getDocumentInputStream(IDocument document, String versionId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        try {
            Downloader downloader = createDownloader();
            downloader.setParent(this);
            downloader.setDocument(document);
            downloader.setVersionId(versionId);
            return downloader.getInputStream();       
        } catch (Exception e){
            throw new APIConnectException("Error connecting to server: " + e.getMessage());
        }
    }


    public Document downloadMetaData(IXmlMetaData metaData) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** Run a workflow on the server */
    public IWorkflowInvocation executeWorkflow(IWorkflow workflow, IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIExecuteWorkflow");
        call.getCallArguments().add("WorkflowID", workflow.getId());
        call.getCallArguments().add("DocumentID", document.getId());
        
        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }
        
        if(success){
            try {
                InkspotObjectTransport transport = (InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("WorkflowInvocation");
                return (IWorkflowInvocation)transport.getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned workflow: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public IWorkflowInvocation executeWorkflow(IWorkflow workflow, IWorkflowParameterList parameters) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIExecuteWorkflowWithParameters");
        call.getCallArguments().add("WorkflowID", workflow.getId());

        try {
            call.getCallArguments().add("Parameters", new InkspotObjectTransport(parameters));
        } catch (Exception e){
            throw new APIConnectException("Error serializing parameters: " + e.getMessage(), e);
        }
        
        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                InkspotObjectTransport transport = (InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("WorkflowInvocation");
                return (IWorkflowInvocation)transport.getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned workflow: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }
    
    /** Get a document record by ID */
    public IDocument getDocument(String documentId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetDocument");
        call.getCallArguments().add("DocumentID", documentId);
        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                InkspotObjectTransport transport = (InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Document");
                return (IDocument)transport.getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned document: " + e.getMessage());
            }         
        } else {
            throw new APIConnectException("Could not send call after retrying");
        }
      
    }

    /** Get the id of the latest version of a document */
    @Override
    public String getLatestVersionId(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetLatestVersionId");
        call.getCallArguments().add("DocumentID", document.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return call.getReturnArguments().stringValue("VersionID", null);
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned version list: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }        
    }
    
    /** List all of the versions of a document */
    public List<IDocumentVersion> getDocumentVersions(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetDocumentVersions");
        call.getCallArguments().add("DocumentID", document.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                ArrayList<IDocumentVersion> results = new ArrayList<IDocumentVersion>();

                int size = call.getReturnArguments().intValue("VersionCount", 0);
                for(int i=0;i<size;i++){
                    results.add((IDocumentVersion)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Version" + i)).getObject());
                }

                return results;

            } catch (Exception e){
                throw new APIConnectException("Error accessing returned version list: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public IFolder getFolder(String folderId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetFolder");
        call.getCallArguments().add("FolderID", folderId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IFolder)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Folder")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing folder: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }


    /** List the contents of a folder */
    public List<IObject> getFolderContents(IFolder folder) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetFolderContents");
        call.getCallArguments().add("FolderID", folder.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                ArrayList<IObject> results = new ArrayList<IObject>();

                int size = call.getReturnArguments().intValue("ObjectCount", 0);
                for(int i=0;i<size;i++){
                    results.add((IObject)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Object" + i)).getObject());
                }
                return results;

            } catch (Exception e){
                throw new APIConnectException("Error accessing returned version list: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** List all of the known locations for an object */
    public List<ILatLongHolder> getLocationsForObject(ISecuredObject object, int logMessageType) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetLocationsForObject");
        call.getCallArguments().add("ObjectID", object.getId());
        call.getCallArguments().add("LogMessageType", logMessageType);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                ArrayList<ILatLongHolder> results = new ArrayList<ILatLongHolder>();
                int size = call.getReturnArguments().intValue("LocationCount", 0);
                for(int i=0;i<size;i++){
                    results.add((ILatLongHolder)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Location" + i)).getObject());
                }
                return results;
                
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned version list: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** Get a piece of meta data information */
    public IXmlMetaData getMetaDataInfo(IUser user, String metaDataId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetMetaDataInfo");
        call.getCallArguments().add("UserID", user.getId());
        call.getCallArguments().add("MetaDataID", metaDataId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IXmlMetaData)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("MetaDataInfo")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned metadata: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** Get a specific user */
    public IUser getUser(String userId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetUser");
        call.getCallArguments().add("UserID", userId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IUser)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("User")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned user: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** Get a list of people connected to a user */
    public List<IUser> getUserConnections(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        return new ArrayList<IUser>();
    }

    /** Get the currently logged in user */
    public IUser getUserContext() {

        CallObject call = new CallObject("APIGetCurrentUser");

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (Exception e){
            return null;
        }

        if(success){
            try {
                return (IUser)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("User")).getObject();
            } catch (Exception e){
                return null;
            }

        } else {
            return null;
        }

    }

    /** Get the ID of the current user */
    public String getUserContextId() {
        IUser user = getUserContext();
        if(user!=null){
            return user.getId();
        } else {
            return null;
        }
    }

    /** Get the home folder for a user */
    public IFolder getUserFolder(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetUserFolder");
        call.getCallArguments().add("UserID", user.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IFolder)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Folder")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing user folder: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** Get a list of user groups */
    public List<IGroup> getUserGroups(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetUserGroups");
        call.getCallArguments().add("UserID", user.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                ArrayList<IGroup> results = new ArrayList<IGroup>();
                int size = call.getReturnArguments().intValue("GroupCount", 0);
                for(int i=0;i<size;i++){
                    results.add((IGroup)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Group" + i)).getObject());
                }
                return results;

            } catch (Exception e){
                throw new APIConnectException("Error accessing returned group list: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** Get an existing workflow invocation directory */
    public IWorkflowInvocation getWorkflowInvocation(String invocationId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetWorkflowInvocation");
        call.getCallArguments().add("InvocationID", invocationId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IWorkflowInvocation)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Invocation")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing workflow invocation: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** List all of the invocations of a workflow */
    public List<IWorkflowInvocation> listInvocations(IWorkflow workflow) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIListWorkflowInvocations");
        call.getCallArguments().add("WorkflowID", workflow.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                int size = call.getReturnArguments().intValue("InvocationCount", 0);
                ArrayList<IWorkflowInvocation>invocations = new ArrayList<IWorkflowInvocation>();
                for(int i=0;i<size;i++){
                    invocations.add((IWorkflowInvocation)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Invocation" + i)).getObject());
                }
                return invocations;
            } catch (Exception e){
                throw new APIConnectException("Error accessing workflow invocation: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }



    /** List all of the meta data items for a user */
    public List<IXmlMetaData> listMetaData(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIListMetaData");
        call.getCallArguments().add("UserID", user.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                ArrayList<IXmlMetaData> results = new ArrayList<IXmlMetaData>();
                int size = call.getReturnArguments().intValue("MetaDataCount", 0);
                for(int i=0;i<size;i++){
                    results.add((IXmlMetaData)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("MetaData" + i)).getObject());
                }
                return results;

            } catch (Exception e){
                throw new APIConnectException("Error accessing returned metadata list: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** List all of a users workflows */
    public List<IWorkflow> listWorkflows() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIListWorkflows");

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                ArrayList<IWorkflow> results = new ArrayList<IWorkflow>();
                int size = call.getReturnArguments().intValue("WorkflowCount", 0);
                for(int i=0;i<size;i++){
                    results.add((IWorkflow)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Workflow" + i)).getObject());
                }
                return results;

            } catch (Exception e){
                throw new APIConnectException("Error accessing returned workflow list: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** Save a document record back to the server in a specific folder */
    public IDocument saveDocument(IFolder parentFolder, IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISaveDocumentToFolder");
        call.getCallArguments().add("FolderID", parentFolder.getId());

        try {
            call.getCallArguments().add("Document", new InkspotObjectTransport(document));
        } catch (Exception e){
            throw new APIConnectException("Error saving document to call object: " + e.getMessage());
        }

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IDocument)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Document")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error saving document: " + e.getMessage());
            }            
        } else {
            throw new APIConnectException("Could not send call after retrying");
        }
       
    }

    /** Save a modified existing document to the server */
    public IDocument saveDocument(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISaveDocument");

        try {
            call.getCallArguments().add("Document", new InkspotObjectTransport(document));
        } catch (Exception e){
            throw new APIConnectException("Error saving document to call object: " + e.getMessage());
        }

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IDocument)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Document")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error saving document: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** Save a piece of metadata locator information */
    public IXmlMetaData saveMetaDataInfo(IUser user, IXmlMetaData metaData) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISaveMetaDataInfo");

        try {
            call.getCallArguments().add("MetaDataInfo", new InkspotObjectTransport(metaData));
        } catch (Exception e){
            throw new APIConnectException("Error saving metadata info to call object: " + e.getMessage());
        }

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IXmlMetaData)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("MetaDataInfo")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error saving metadata information: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public void saveUser(IUser user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IDocumentVersion upload(IDocument document, InputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        try {
            Uploader uploader = createUploader();
            uploader.setParent(this);
            uploader.setDocument(document);
            uploader.setStream(stream);

            boolean success = uploader.upload();
            
            if(success){
                try {
                    return uploader.getUploadedDocumentVersion();
                } catch (Exception e){
                    throw new APIConnectException("Error saving document: " + e.getMessage());
                }            
            } else {
                throw new APIConnectException("Could not send call after retrying");
            }

        } catch (Exception e){
            e.printStackTrace();
            throw new APIConnectException("Error connecting to server: " + e.getMessage());
        }
    }

    public IXmlMetaData uploadMetaData(IUser user, String name, InputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteDocument(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIDeleteDocument");
        call.getCallArguments().add("DocumentID", document.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }


    public void deleteFolder(IFolder folder) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIDeleteFolder");
        call.getCallArguments().add("FolderID", folder.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public IDynamicWorkflowLibrary getDynamicWorkflowLibraryByName(String libraryName) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetLibraryByName");
        call.getCallArguments().add("LibraryName", libraryName);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                IDynamicWorkflowLibrary library = (IDynamicWorkflowLibrary)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Library")).getObject();
                return library;
            } catch (Exception e){
                throw new APIConnectException("Cannot access library object");
            }
        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** Create a new object */
    public IObject createObject(String objectXmlName) throws APIInstantiationException {
        return ObjectBuilder.instantiateObject(objectXmlName);
    }

//    @Override
    public List<ISignatureData> getDocumentSignatures(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetDocumentSignatures");
        call.getCallArguments().add("DocumentID", document.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                ArrayList<ISignatureData> results = new ArrayList<ISignatureData>();
                int size = call.getReturnArguments().intValue("SignatureCount", 0);
                for(int i=0;i<size;i++){
                    results.add((ISignatureData)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Signature" + i)).getObject());
                }
                return results;

            } catch (Exception e){
                throw new APIConnectException("Error accessing returned signature list: " + e.getMessage());
            }
        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }

    }

    @Override
    public void deleteExternalObject(IExternalObject object) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIDeleteExternalObject");
        call.getCallArguments().add("ObjectID", object.getId());
        
        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public IExternalObject getExternalObject(String externalObjectId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetExternalObject");
        call.getCallArguments().add("ObjectID", externalObjectId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                IExternalObject object = (IExternalObject)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("ExternalObject")).getObject();
                return object;
            } catch (Exception e){
                throw new APIConnectException("Cannot access external object");
            }
        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public List<IExternalObject> listExternalObjects() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIListExternalObjects");

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }        

        if(success){
            try {
                ArrayList<IExternalObject> results = new ArrayList<IExternalObject>();
                int size = call.getReturnArguments().intValue("ObjectCount", 0);
                for(int i=0;i<size;i++){
                    results.add((IExternalObject)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("ExternalObject" + i)).getObject());
                }
                return results;

            } catch (Exception e){
                throw new APIConnectException("Error accessing returned external object list: " + e.getMessage());
            }
        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public IExternalObject saveExternalObject(IExternalObject object) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISaveExternalObject");
        try {
            call.getCallArguments().add("ExternalObject", new InkspotObjectTransport(object));
        } catch (Exception e){
            throw new APIConnectException("Error saving external object to call object: " + e.getMessage());
        }

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

       if(success){
            try {
                IExternalObject returnedObject = (IExternalObject)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("ExternalObject")).getObject();
                return returnedObject;
            } catch (Exception e){
                throw new APIConnectException("Cannot access external object");
            }
        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }

    }

    @Override
    public IPropertyList getObjectProperties(String objectId, String propertyGroup) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetObjectProperties");
        call.getCallArguments().add("ObjectID", objectId);
        call.getCallArguments().add("GroupName", propertyGroup);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IPropertyList)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("PropertyGroup")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Cannot access object properties: " + e.getMessage());
            }
        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public IPropertyList setObjectProperties(String objectId, String propertyGroup, IPropertyList properties) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISetObjectProperties");
        call.getCallArguments().add("ObjectID", objectId);
        call.getCallArguments().add("GroupName", propertyGroup);

        try {
            call.getCallArguments().add("PropertyGroup", new InkspotObjectTransport(properties));
        } catch (Exception e){
            throw new APIConnectException("Error encoding object properties: " + e.getMessage());
        }

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IPropertyList)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("PropertyGroup")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Cannot save object properties: " + e.getMessage());
            }
        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public IPermissionList getObjectPermissions(String objectId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetObjectPermissions");
        call.getCallArguments().add("ObjectID", objectId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IPermissionList)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Permissions")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Cannot retrieve object permissions: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public List<ISecuredObject> getLinkedObjects(String sourceObjectId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetLinkedObjects");
        call.getCallArguments().add("SourceObjectID", sourceObjectId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                List<ISecuredObject> results = new ArrayList<ISecuredObject>();
                int size = call.getReturnArguments().intValue("ObjectCount", 0);
                ISecuredObject obj;
                for(int i=0;i<size;i++){
                    results.add((ISecuredObject)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Object" + i)).getObject());

                }
                return results;
            } catch (Exception e){
                throw new APIConnectException("Cannot save link data: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public ILink linkObjects(ILink link) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APILinkObjects");
        call.getCallArguments().add("SourceObjectID", link.getSourceObjectId());
        call.getCallArguments().add("TargetObjectID", link.getTargetObjectId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (ILink)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Link")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Cannot save link data: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    @Override
    public IUser getPublicUser() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetPublicUser");

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IUser)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("User")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned user: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public IPermissionList grantObjectPermission(ISecuredObject object, ISecuredObject principal, String permissionType) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGrantObjectPermission");
        call.getCallArguments().add("ObjectID", object.getId());
        call.getCallArguments().add("PrincipalID", principal.getId());
        call.getCallArguments().add("AccessType", permissionType);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IPermissionList)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Permissions")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned permissions: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    /** Revoke a permission from an object */
    public void revokeObjectPermission(ISecuredObject object, ISecuredObject principal, String permissionType) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIRevokeObjectPermission");
        call.getCallArguments().add("ObjectID", object.getId());
        call.getCallArguments().add("PrincipalID", principal.getId());
        call.getCallArguments().add("AccessType", permissionType);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public XmlDataStore storeObject() throws XmlStorageException {
        XmlDataStore store = new XmlDataStore("RPCClientAPI");
        store.add("Client", this.client);
        store.add("DownloadLocation", this.downloadLocation);
        
        if(dataStore!=null){
            try {
                store.add("DataStoreData", SerializationUtils.serialize(dataStore));
            } catch (Exception e){
                throw new XmlStorageException("IOError serializing data store: " + e.getMessage());
            }
        } 
        // Save the ticket as a byte array
        if(ticket!=null){
            try {
                store.add("TicketData", SerializationUtils.serialize(ticket));
            } catch (IOException ioe){
                throw new XmlStorageException("IOError serializing ticket: " + ioe.getMessage());
            }
        }
        return store;
    }

    public void recreateObject(XmlDataStore store) throws XmlStorageException {
        client = (RPCClient)store.xmlStorableValue("Client");
        downloadLocation = store.stringValue("DownloadLocation", "/WorkflowServer/WorkflowAPIDataServlet");
        if(store.containsName("TicketData")){
            try {
                ticket = (Ticket)SerializationUtils.deserialize(store.byteArrayValue("TicketData"));
            } catch (Exception e){
                ticket = null;
                throw new XmlStorageException("Error deserializing ticket: " + e.getMessage());
            }
        } else {
            ticket = null;
        }
        
        if(store.containsName("DataStoreData")){
            try {
                dataStore = (DataStore)SerializationUtils.deserialize(store.byteArrayValue("DataStoreData"));
            } catch (Exception e){
                dataStore = null;
                throw new XmlStorageException("Error deserializing data store: " + e.getMessage());
            }
        }
    }

    public IGroup getGroup(String groupId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetGroup");
        call.getCallArguments().add("GroupID", groupId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IGroup)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Group")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned group: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public IFolder saveFolder(IFolder folder) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISaveFolder");

        try {
            call.getCallArguments().add("Folder", new InkspotObjectTransport(folder));
        } catch (Exception e){
            throw new APIConnectException("Error encoding folder data: " + e.getMessage(), e);
        }

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IFolder)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Folder")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned folder: " + e.getMessage());
            }        
        } else {
            throw new APIConnectException("Could not send call after retrying");
        }

    }

    public IFolder addChildFolder(IFolder parent, String childName) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIAddChildFolder");
        call.getCallArguments().add("ParentID", parent.getId());
        call.getCallArguments().add("ChildName", childName);
        
        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IFolder)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Folder")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned folder: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public void terminateWorkflow(IWorkflowInvocation invocation) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APITerminateWorkflow");
        call.getCallArguments().add("InvocationID", invocation.getInvocationId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public void joinGroup(IGroup group) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIJoinGroup");
        call.getCallArguments().add("GroupID", group.getId());

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }        

        if(!success){
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public IGroup createGroup(String groupName, String groupDescription) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APICreateGroup");
        call.getCallArguments().add("GroupName", groupName);
        call.getCallArguments().add("GroupDescription", groupDescription);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            try {
                return (IGroup)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Group")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned group: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public IGroup saveGroup(IGroup group) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APISaveGroup");

        try {
            call.getCallArguments().add("Group", new InkspotObjectTransport(group));
        } catch (Exception e){
            throw new APIConnectException("Error encoding group data: " + e.getMessage(), e);
        }

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(!success){
            try {
                return (IGroup)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Group")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned group: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }
    }

    public IWorkflow getWorkflow(String workflowId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        CallObject call = new CallObject("APIGetWorkflow");
        call.getCallArguments().add("WorkflowID", workflowId);

        CallSender sender = new CallSender(client, call);
        sender.setResendProperties(retryCount, initialRetryInterval, maxWaitInterval, retryMultiplier);
        boolean success;
        try {
            success = sender.syncCall();
        } catch (RPCException rpce){
            throw new APIConnectException("RPC Error sending call: " + rpce.getMessage());
        }

        if(success){
            try {
                return (IWorkflow)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("Workflow")).getObject();
            } catch (Exception e){
                throw new APIConnectException("Error accessing returned workflow: " + e.getMessage());
            }

        } else {
            throw new APIConnectException("Error executing RPC call: " + call.getStatusMessage());
        }        
    }

    public IDocument zipFolder(IFolder folder, IDocument targetDoc) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IUser register(INewUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
       throw new UnsupportedOperationException("Not supported yet.");
    }

  /** Get a specific user */
     public IUser findUserByEmail(String email) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
       //todo: implement
       throw new UnsupportedOperationException("Not supported yet.");
     }

    @Override
    public String getServiceXml(IDocument serviceDocument) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
     
     
     

}