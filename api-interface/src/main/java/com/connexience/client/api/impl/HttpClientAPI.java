/*
 * HttpClientAPI.java
 */

package com.connexience.client.api.impl;

import com.connexience.server.api.*;
import com.connexience.server.api.impl.*;
import com.connexience.server.api.util.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.net.*;
import java.util.*;
import java.io.*;

/**
 * This class provides an Http client side implementation of the REST API
 * @author nhgh
 */
public class HttpClientAPI implements API, APIControl, Serializable {
    static {
        InkspotTypeRegistration.register();
    }

    /** Server URL */
    private URL serverUrl;

    /** API id for authentication */
    private String apiId;

    /** API key for message signing */
    private String apiKey;

    /** API Sequence generator */
    private SequenceGenerator sequence = new SequenceGenerator();

    /** ID of the user that this link was created for */
    private String userContextId;

    /** User object that this link is currently running for */
    private IUser userContext;

    /** Get a URL connection to a method */
    private HttpURLConnection getConnection(String methodCall, long sequenceValue) throws APIConnectException {
        try {
            URL methodUrl = new URL(serverUrl + methodCall);
            HttpURLConnection connection = (HttpURLConnection)methodUrl.openConnection();
            connection.setRequestProperty("sequenceid", Long.toString(sequenceValue));
            connection.setRequestProperty("apiid", apiId);
            connection.setRequestProperty("methodcall", methodCall);
            return connection;

        } catch (MalformedURLException me){
            throw new APIConnectException("Invalid server URL: " + me.getMessage());
        } catch (IOException ioe){
            throw new APIConnectException("Connection error: " + ioe.getMessage());
        }
    }

    /** Set the signature data in a URL connection before it is sent */
    private void signURLConnectionData(URLConnection connection) throws APISecurityException {
        // Get the properties already in the connection. This must also include the signature of the
        // objects being uploaded in the request body
        String standardisedProperties = APISecurity.standardiseMap(connection.getRequestProperties());
        String signature = APISecurity.signString(standardisedProperties, apiKey);
        connection.addRequestProperty("requestsignature", signature);
    }

    /** Add a content signature to a url connetion */
    private void addContentSignature(URLConnection connection, List<IObject> objects, long sequenceValue) throws APISecurityException {
        if(objects!=null && objects.size()>0){
            String signature = APISecurity.signList(objects, apiId, apiKey, sequenceValue);
            connection.addRequestProperty("contentsignature", signature);
        } else {
            connection.addRequestProperty("contentsignature", "");
        }
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

    // =========================================================================
    // APIControl methods
    // =========================================================================

    public void initialise(URL serverUrl) {
        this.serverUrl = serverUrl;
    }

    public URL getServerUrl() {
        return serverUrl;
    }


    public void setKeyDetails(String apiId, String apiKey) {
        this.apiId = apiId;
        this.apiKey = apiKey;
    }

    /** Set the ID of the user */
    public void setUserContextId(String userContextId) {
        this.userContextId = userContextId;
    }

    // =========================================================================
    // API Redirect methods
    // =========================================================================

    /** Get a string of data from the server */
    public String doRedirectedGet(String url, String appId) throws Exception {
        HttpURLConnection connection = getRedirectConnection(url, appId);
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setRequestMethod("GET");
        InputStream inStream = connection.getInputStream();
        String data = extractString(inStream);
        inStream.close();
        connection.disconnect();
        return data;
    }

    /** Post a string of data to the server */
    public String doPost(String url, String data, String appId){
        return "";
    }

    /** Get a redirected connection to another app */
    private HttpURLConnection getRedirectConnection(String url, String appId) throws Exception {
        long sequenceValue = sequence.nextLongValue();
        HttpURLConnection connection = getConnection("/redirect", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);
        connection.setRequestProperty("applicationid", appId);
        connection.addRequestProperty("url", url);
        connection.addRequestProperty("userid", getUserContextId());
        return connection;
    }

    /**
     * Extract a String from an InputStream
     */
    private String extractString(InputStream stream) throws Exception {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        boolean finished = false;
        int value;

        while (finished == false) {
            value = stream.read();
            if (value == -1) {
                finished = true;
            } else {
                array.write(value);
            }
        }
        return new String(array.toByteArray());
    }

    // =========================================================================
    // API Methods
    // =========================================================================

    /** Get the ID of the user that this link was created to communicate with */
    public String getUserContextId() {
        return userContextId;
    }

    /** Get the user object that this link was created for */
    public IUser getUserContext() {
        if(userContext!=null){
            return userContext;
        } else {
            if(userContextId!=null){
                try {
                    userContext = getUser(userContextId);
                    return userContext;
                } catch (Exception e){
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public List<IDocumentVersion> getDocumentVersions(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/documents/" + document.getId() + "/versions", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            List<IDocumentVersion> results = new ArrayList<IDocumentVersion>();

            for(int i=0;i<items.size();i++){
                results.add((IDocumentVersion)items.get(i));
            }
            return results;
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    public String getLatestVersionId(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        List<IDocumentVersion> versions = getDocumentVersions(document);
        if(versions.size()>0){
            IDocumentVersion latestVersion = versions.get(0);

            // Check for newer if more than one version
            if(versions.size()>1){
                for(int i=1;i<versions.size();i++){
                    if(versions.get(i).getVersionNumber() > latestVersion.getVersionNumber()){
                        latestVersion = versions.get(i);
                    }
                }
            }
            return latestVersion.getId();
        } else {
            return null;
        }
    }


    public List<IObject> getFolderContents(IFolder folder) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException{
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/folders/" + folder.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> results = ObjectBuilder.parseInputStream(stream);
            checkForError(results);
            return results;
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    public IFolder getFolder(String folderId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/folders/" + folderId + "/folder", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> results = ObjectBuilder.parseInputStream(stream);
            if(results.get(0) instanceof IFolder){
                return (IFolder)results.get(0);
            } else {
                return null;
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** Get the public user */
    public IUser getPublicUser() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/users/public", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream stream = connection.getInputStream();
            List<IObject> results = ObjectBuilder.parseInputStream(stream);
            checkForError(results);
            if(results.get(0) instanceof IUser){
                return (IUser)results.get(0);
            } else {
                return null;
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    public IUser getUser(String userId)  throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/users/" + userId , sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream stream = connection.getInputStream();
            List<IObject> results = ObjectBuilder.parseInputStream(stream);
            checkForError(results);
            if(results.get(0) instanceof IUser){
                return (IUser)results.get(0);
            } else {
                return null;
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** List friends */
    public List<IUser> getUserConnections(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + user.getId() + "/connections", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            ArrayList<IUser> results = new ArrayList<IUser>();
            for(int i=0;i<items.size();i++){
                results.add((IUser)items.get(i));
            }
            return results;
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** Get the home folder for a User */
    public IFolder getUserFolder(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + user.getId() + "/folders/home", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream stream = connection.getInputStream();
            List<IObject> results = ObjectBuilder.parseInputStream(stream);
            checkForError(results);
            if(results.get(0) instanceof IFolder){
                return (IFolder)results.get(0);
            } else {
                return null;
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** Get a document record */
    public IDocument getDocument(String documentId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
    	
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/documents/" + documentId, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream stream = connection.getInputStream();
            List<IObject> results = ObjectBuilder.parseInputStream(stream);
            checkForError(results);
            if(results.get(0) instanceof IDocument){
                return (IDocument)results.get(0);
            } else {
                return null;
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** Zip a folder on the server and return a reference to the zipped document */
    public IDocument zipFolder(IFolder folder, IDocument targetDocument) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/compressedfolders/" + folder.getId() + "/" + targetDocument.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream stream = connection.getInputStream();
            List<IObject> results = ObjectBuilder.parseInputStream(stream);
            checkForError(results);
            if(results.get(0) instanceof IDocument){
                return (IDocument)results.get(0);
            } else {
                return null;
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }


    /** Download the latest contents of a document to an output stream */
    public void download(IDocument document, OutputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/data/" + userContextId + "/" + document.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream documentStream = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int len;
            while((len = documentStream.read(buffer))>0){
                stream.write(buffer, 0, len);
            }
            stream.flush();
            documentStream.close();

        } catch (IOException ioe){
            throw new APIConnectException("Error downloading data: " + ioe.getMessage());
        }
    }

    /** Open an input stream connected to the latest version of a document */
    public InputStream getDocumentInputStream(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/data/" + userContextId + "/" + document.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream documentStream = connection.getInputStream();
            return documentStream;

        } catch (IOException ioe){
            throw new APIConnectException("Error downloading data: " + ioe.getMessage());
        }
    }

    /** Download the contents of a specific version of a document to an output stream */
    public void download(IDocument document, int versionNumber, OutputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/data/" + userContextId + "/" + document.getId() + "/" + versionNumber, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream documentStream = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int len;
            while((len = documentStream.read(buffer))>0){
                stream.write(buffer, 0, len);
            }
            stream.flush();
            documentStream.close();

        } catch (IOException ioe){
            throw new APIConnectException("Error downloading data: " + ioe.getMessage());
        }
    }

    public void download(IDocument document, String versionId, OutputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/data/" + userContextId + "/" + document.getId() + "/vid/" + versionId, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream documentStream = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int len;
            while((len = documentStream.read(buffer))>0){
                stream.write(buffer, 0, len);
            }
            stream.flush();
            documentStream.close();

        } catch (IOException ioe){
            throw new APIConnectException("Error downloading data: " + ioe.getMessage());
        }
    }

    /** Get an input stream connected to a specific version of a document */
    public InputStream getDocumentInputStream(IDocument document, String versionId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/data/" + userContextId + "/" + document.getId() + "/vid/" + versionId, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream documentStream = connection.getInputStream();
            return documentStream;

        } catch (IOException ioe){
            throw new APIConnectException("Error downloading data: " + ioe.getMessage());
        }
    }

    /** Upload data for a file */
    public IDocumentVersion upload(IDocument document, InputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException{
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/data/" + userContextId + "/" + document.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        connection.setDoInput(true);
        connection.setDoOutput(true);
        ((HttpURLConnection)connection).setChunkedStreamingMode(32768);
        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        try {
            connection.connect();

            OutputStream outStream = connection.getOutputStream();
            byte[] buffer = new byte[4096];
            int len;
            while((len = stream.read(buffer))>0){
                outStream.write(buffer, 0, len);
            }
            outStream.flush();
            stream.close();


            List<IObject>items = ObjectBuilder.parseInputStream(connection.getInputStream());
            checkForError(items);
            if(items.get(0) instanceof IDocumentVersion){
                return (IDocumentVersion)items.get(0);
            } else {
                throw new APIInstantiationException("Wrong object returned by server. Expecting DocumentVersion");
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** List the groups that a user is a member of */
    public List<IGroup> getUserGroups(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException{
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + user.getId() + "/groups", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            ArrayList<IGroup> results = new ArrayList<IGroup>();
            for(int i=0;i<items.size();i++){
                results.add((IGroup)items.get(i));
            }
            return results;
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    public IDocument saveDocument(IFolder parentFolder, IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/folders/" + parentFolder.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        try {
            connection.connect();
            ObjectBuilder.writeObjectToStream(document, connection.getOutputStream());
            List<IObject>items = ObjectBuilder.parseInputStream(connection.getInputStream());
            checkForError(items);
            if(items.get(0) instanceof IDocument){
                return (IDocument)items.get(0);
            } else {
                throw new APIInstantiationException("Wrong object returned by server. Expecting Document");
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public IDocument saveDocument(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        if(document.getId()!=null && !(document.getId().trim().equals(""))){
            URLConnection connection = getConnection("/users/" + userContextId + "/documents/" + document.getId(), sequenceValue);
            addContentSignature(connection, null, sequenceValue);
            signURLConnectionData(connection);

            connection.setDoInput(true);
            connection.setDoOutput(true);
            try {
                ((HttpURLConnection)connection).setRequestMethod("POST");
            } catch (Exception e){
                throw new APIConnectException("HTTP Error: " + e.getMessage());
            }

            try {
                connection.connect();
                ObjectBuilder.writeObjectToStream(document, connection.getOutputStream());
                List<IObject>items = ObjectBuilder.parseInputStream(connection.getInputStream());
                checkForError(items);
                if(items.get(0) instanceof IDocument){
                    return (IDocument)items.get(0);
                } else {
                    throw new APIInstantiationException("Wrong object returned by server. Expecting Document");
                }
            } catch (IOException ioe){
                throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
            } catch (APIGenerateException apie){
                throw new APIConnectException("API XML Generation error: " + apie.getMessage());
            }
        } else {
            throw new APIConnectException("Document must be saved to a folder before it can be updated");
        }

    }

    public void saveUser(IUser user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** List the users metadata documents */
    public List<IXmlMetaData> listMetaData(IUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + user.getId() + "/metadata", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            ArrayList<IXmlMetaData> results = new ArrayList<IXmlMetaData>();
            for(int i=0;i<items.size();i++){
                results.add((IXmlMetaData)items.get(i));
            }
            return results;
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** List the users workflows */
    public List<IWorkflow> listWorkflows() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/workflows", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            ArrayList<IWorkflow> results = new ArrayList<IWorkflow>();
            for(int i=0;i<items.size();i++){
                results.add((IWorkflow)items.get(i));
            }
            return results;
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** List the invocations of a workflow */
    public List<IWorkflowInvocation> listInvocations(IWorkflow workflow) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/workflows/" + workflow.getId() + "/invocations", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            ArrayList<IWorkflowInvocation> results = new ArrayList<IWorkflowInvocation>();
            for(int i=0;i<items.size();i++){
                results.add((IWorkflowInvocation)items.get(i));
            }
            return results;
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** Upload a metadata descriptor document */
    public IXmlMetaData saveMetaDataInfo(IUser user, IXmlMetaData metaData) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + user.getId() + "/metadata", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        try {
            connection.connect();
            ObjectBuilder.writeObjectToStream(metaData, connection.getOutputStream());
            List<IObject>items = ObjectBuilder.parseInputStream(connection.getInputStream());
            checkForError(items);
            if(items.get(0) instanceof IXmlMetaData){
                return (IXmlMetaData)items.get(0);
            } else {
                throw new APIInstantiationException("Wrong object returned by server. Expecting metadata descriptor");
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    /** Get a metadata descriptor document */
    public IXmlMetaData getMetaDataInfo(IUser user, String metaDataId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + user.getId() + "/metadata/" + metaDataId, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);

            if(items.get(0) instanceof IXmlMetaData){
                return (IXmlMetaData)items.get(0);
            } else {
                throw new APIInstantiationException("No metadata description returned");
            }

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** Get the XML contents for a metadata descriptor document */
    public Document downloadMetaData(IXmlMetaData metaData) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/metadata/" + metaData.getId() + "/contents", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream documentStream = connection.getInputStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setCoalescing(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(documentStream);
            return doc;

        } catch (Exception ioe){
            throw new APIConnectException("Error downloading data: " + ioe.getMessage());
        }
    }

    /** Upload some Xml data for a metadata descriptor document */
    public IXmlMetaData uploadMetaData(IUser user, String name, InputStream stream) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + user.getId() + "/metadata/new/" + name, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        try {
            connection.connect();

            OutputStream outStream = connection.getOutputStream();
            byte[] buffer = new byte[4096];
            int len;
            while((len = stream.read(buffer))>0){
                outStream.write(buffer, 0, len);
            }
            outStream.flush();
            stream.close();


            List<IObject>items = ObjectBuilder.parseInputStream(connection.getInputStream());
            checkForError(items);
            if(items.get(0) instanceof IXmlMetaData){
                return (IXmlMetaData)items.get(0);
            } else {
                throw new APIInstantiationException("Wrong object returned by server. Expecting Xml metadata");
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** Authenticate as the public user */
    public IUser authenticateAsPublic() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        String username = "_PUBLIC_";
        String password = "_PUBLIC_";
        return authenticate(username, password);
    }

    /** Authenticate a user and use this as the current user context */
    public IUser authenticate(String username, String password) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/auth/logon", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        // Execute the method
        try {
            connection.connect();
            IAuthenticationRequest authRequest = new InkspotAuthenticationRequest();
            authRequest.setLogonName(username);
            authRequest.setPassword(password);

            ObjectBuilder.writeObjectToStream(authRequest, connection.getOutputStream());
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1){
                if(items.get(0) instanceof IUser){
                    userContext = (IUser)items.get(0);
                    userContextId = userContext.getId();
                    return userContext;

                } else {
                    throw new APIParseException("Wrong object returned. Expecting user");
                }
            } else {
                throw new APIParseException("Too much data returned by server");
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("XML Generation Exception: " + apie.getMessage());
        }
    }

    public IWorkflowInvocation executeWorkflow(IWorkflow workflow, IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/workflows/" + workflow.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        try {
            connection.connect();
            ObjectBuilder.writeObjectToStream(document, connection.getOutputStream());
            List<IObject>items = ObjectBuilder.parseInputStream(connection.getInputStream());
            checkForError(items);
            if(items.get(0) instanceof IWorkflowInvocation){
                return (IWorkflowInvocation)items.get(0);
            } else {
                throw new APIInstantiationException("Wrong object returned by server. Expecting workflow invocation data");
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public IWorkflowInvocation executeWorkflow(IWorkflow workflow, IWorkflowParameterList parameters) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/workflows/" + workflow.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        try {
            connection.connect();
            ObjectBuilder.writeObjectToStream(parameters, connection.getOutputStream());
            List<IObject>items = ObjectBuilder.parseInputStream(connection.getInputStream());
            checkForError(items);
            if(items.get(0) instanceof IWorkflowInvocation){
                return (IWorkflowInvocation)items.get(0);
            } else {
                throw new APIInstantiationException("Wrong object returned by server. Expecting workflow invocation data");
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public IWorkflowInvocation getWorkflowInvocation(String invocationId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/workflowinvocations/" + invocationId, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);
        connection.setDoInput(true);

        try {
            connection.connect();
            List<IObject>items = ObjectBuilder.parseInputStream(connection.getInputStream());
            checkForError(items);
            if(items.get(0) instanceof IWorkflowInvocation){
                return (IWorkflowInvocation)items.get(0);
            } else {
                throw new APIInstantiationException("Wrong object returned by server. Expecting workflow invocation data");
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

  /** Get a list of the locations where an object has been updated*/
  public List<ILatLongHolder> getLocationsForObject(ISecuredObject object, int logMessageType) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException
  {
      long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/locations/" + object.getId() + "/" + logMessageType, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            ArrayList<ILatLongHolder> results = new ArrayList<ILatLongHolder>();
            for(int i=0;i<items.size();i++){
                results.add((ILatLongHolder)items.get(i));
            }
            return results;
        } catch (Exception ioe){
            throw new APIConnectException("Error downloading data: " + ioe.getMessage());
        }
  }

    /** Delete a folder and all its children */
    public void deleteFolder(IFolder folder) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        HttpURLConnection connection = getConnection("/users/" + userContextId + "/folders/" + folder.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);
        try {
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);

        } catch (Exception ioe){
            throw new APIConnectException("Error deleting document: " + ioe.getMessage());
        }
    }

    /** Delete a document */
    public void deleteDocument(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        HttpURLConnection connection = getConnection("/data/" + userContextId + "/" + document.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);
        try {
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);

        } catch (Exception ioe){
            throw new APIConnectException("Error deleting document: " + ioe.getMessage());
        }
    }

    /** Get the latest version of a workflow library */
    public IDynamicWorkflowLibrary getDynamicWorkflowLibraryByName(String libraryName) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        HttpURLConnection connection = getConnection("/libraries/" + userContextId + "/names/" + libraryName  , sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1){
                if(items.get(0) instanceof IDynamicWorkflowLibrary){
                    return (IDynamicWorkflowLibrary)items.get(0);
                } else {
                    throw new APIConnectException("Wrong number of libraries returned");
                }
            } else {
                throw new APIConnectException("Wrong number of libraries returned");
            }
        } catch (IOException e){
            throw new APIConnectException("Error getting workflow service: " + e.getMessage());
        }
    }

    /** Create a new object */
    public IObject createObject(String objectXmlName) throws APIInstantiationException {
        return ObjectBuilder.instantiateObject(objectXmlName);
    }

    /** Get a list of signatures for a document */
    public List<ISignatureData> getDocumentSignatures(IDocument document) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        return new ArrayList<ISignatureData>();
    }

    public IExternalObject saveExternalObject(IExternalObject object) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/externalobjects", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        try {
            connection.connect();
            ObjectBuilder.writeObjectToStream(object, connection.getOutputStream());
            List<IObject>items = ObjectBuilder.parseInputStream(connection.getInputStream());
            checkForError(items);
            if(items.get(0) instanceof IExternalObject){
                return (IExternalObject)items.get(0);
            } else {
                throw new APIInstantiationException("Wrong object returned by server. Expecting ExternalObject");
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public IExternalObject getExternalObject(String externalObjectId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/externalobjects/" + externalObjectId, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream stream = connection.getInputStream();
            List<IObject> results = ObjectBuilder.parseInputStream(stream);
            checkForError(results);
            if(results.get(0) instanceof IExternalObject){
                return (IExternalObject)results.get(0);
            } else {
                return null;
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    public void deleteExternalObject(IExternalObject object) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        HttpURLConnection connection = getConnection("/users/" + userContextId + "/externalobjects/" + object.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);
        try {
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);

        } catch (Exception ioe){
            throw new APIConnectException("Error deleting document: " + ioe.getMessage());
        }
    }

    public List<IExternalObject> listExternalObjects() throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/externalobjects", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            ArrayList<IExternalObject> results = new ArrayList<IExternalObject>();
            for(int i=0;i<items.size();i++){
                results.add((IExternalObject)items.get(i));
            }
            return results;
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    public IPropertyList getObjectProperties(String objectId, String propertyGroup) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/properties/" + objectId + "/" + propertyGroup, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1){
                if(items.get(0) instanceof IPropertyList){
                    return (IPropertyList)items.get(0);
                } else {
                    throw new APIConnectException("Wrong object returned");
                }
            } else {
                throw new APIConnectException("Wrong number of objects returned");
            }

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    public IPropertyList setObjectProperties(String objectId, String propertyGroup, IPropertyList properties) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/properties/" + objectId + "/" + propertyGroup, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        // Execute the method
        try {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            ObjectBuilder.writeObjectToStream(properties, connection.getOutputStream());

            // Parse the results
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1){
                if(items.get(0) instanceof IPropertyList){
                    return (IPropertyList)items.get(0);
                } else {
                    throw new APIConnectException("Wrong object returned");
                }
            } else {
                throw new APIConnectException("Wrong number of objects returned");
            }

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public IPermissionList getObjectPermissions(String objectId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/permissions/" + objectId, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1){
                if(items.get(0) instanceof IPermissionList){
                    return (IPermissionList)items.get(0);
                } else {
                    throw new APIConnectException("Wrong object returned");
                }
            } else {
                throw new APIConnectException("Wrong number of objects returned");
            }

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    /** Assign a permission to an object */
    public IPermissionList grantObjectPermission(ISecuredObject object, ISecuredObject principal, String permissionType) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();

        URLConnection connection = getConnection("/users/" + userContextId + "/permissions/" + object.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        try {
            connection.connect();
            InkspotPermission perm = new InkspotPermission();
            perm.setPermissionType(permissionType);
            perm.setPrincipalId(principal.getId());
            ObjectBuilder.writeObjectToStream(perm, connection.getOutputStream());
            List<IObject>items = ObjectBuilder.parseInputStream(connection.getInputStream());
            checkForError(items);

            if(items.get(0) instanceof IPermissionList){
                return (IPermissionList)items.get(0);
            } else {
                throw new APIConnectException("Wrong object returned");
            }

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }

    }

    /** Revoke a permission from an object */
    public void revokeObjectPermission(ISecuredObject object, ISecuredObject principal, String permissionType) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        HttpURLConnection connection = getConnection("/users/" + userContextId + "/permissions/" + object.getId() + "/" + principal.getId() + "/" + permissionType, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);
        try {
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);

        } catch (Exception ioe){
            throw new APIConnectException("Error revoking permission: " + ioe.getMessage());
        }
    }

    /** Create a link between two server objects */
    public ILink linkObjects(ILink link) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/links/" + link.getSourceObjectId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        // Execute the method
        try {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            ObjectBuilder.writeObjectToStream(link, connection.getOutputStream());

            // Parse the results
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1){
                if(items.get(0) instanceof ILink){
                    return (ILink)items.get(0);
                } else {
                    throw new APIConnectException("Wrong object returned");
                }
            } else {
                throw new APIConnectException("Wrong number of objects returned");
            }

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public List<ISecuredObject> getLinkedObjects(String sourceObjectId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/links/" + sourceObjectId, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            List<ISecuredObject> results = new ArrayList<ISecuredObject>();
            for(int i=0;i<items.size();i++){
                results.add((ISecuredObject)items.get(i));
            }
            return results;

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    public IGroup getGroup(String groupId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/allgroups/" + groupId, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1 && items.get(0) instanceof IGroup){
                return (IGroup)items.get(0);
            } else {
                throw new APIConnectException("Wrong number of objects returned. Expecting 1, got " + items.size());
            }

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    public void joinGroup(IGroup group) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/groups/membership", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        // Execute the method
        try {
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            ObjectBuilder.writeObjectToStream(group, connection.getOutputStream());
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public IFolder addChildFolder(IFolder parent, String childName) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {

        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/folders/" + parent.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        // Execute the method
        try {
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            IFolder childFolder = (IFolder)createObject(IFolder.XML_NAME);
            childFolder.setContainerId(parent.getId());
            childFolder.setName(childName);
            childFolder.setDescription("Created by API");

            ObjectBuilder.writeObjectToStream(childFolder, connection.getOutputStream());
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1 && items.get(0) instanceof IFolder){
                return (IFolder)items.get(0);
            } else {
                throw new APIConnectException("Wrong number of items returned. Expecting 1, got " + items.size());
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public IFolder saveFolder(IFolder folder) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {

        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/folders/" + folder.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        // Execute the method
        try {
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            ObjectBuilder.writeObjectToStream(folder, connection.getOutputStream());
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1 && items.get(0) instanceof IFolder){
                return (IFolder)items.get(0);
            } else {
                throw new APIConnectException("Wrong number of items returned. Expecting 1, got " + items.size());
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public void terminateWorkflow(IWorkflowInvocation invocation) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        HttpURLConnection connection = getConnection("/users/" + userContextId + "/invocations/" + invocation.getId(), sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);
        try {
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);

        } catch (Exception ioe){
            throw new APIConnectException("Error terminating invocation: " + ioe.getMessage());
        }
    }

    public IGroup createGroup(String groupName, String groupDescription) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/allgroups/", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        // Execute the method
        try {
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            IGroup group = (IGroup)createObject(IGroup.XML_NAME);
            group.setName(groupName);
            group.setDescription(groupDescription);
            ObjectBuilder.writeObjectToStream(group, connection.getOutputStream());

            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1 && items.get(0) instanceof IGroup){
                return (IGroup)items.get(0);
            } else {
                throw new APIConnectException("Wrong number of objects returned. Expecting 1, got " + items.size());
            }

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public IGroup saveGroup(IGroup group) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/allgroups/", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        try {
            ((HttpURLConnection)connection).setRequestMethod("POST");
        } catch (Exception e){
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        // Execute the method
        try {
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            ObjectBuilder.writeObjectToStream(group, connection.getOutputStream());
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1 && items.get(0) instanceof IGroup){
                return (IGroup)items.get(0);
            } else {
                throw new APIConnectException("Wrong number of objects returned. Expecting 1, got " + items.size());
            }

        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie){
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public IWorkflow getWorkflow(String workflowId) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
       
    	
    	long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/workflows/" + workflowId, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);
            if(items.size()==1 && items.get(0) instanceof IWorkflow){
                return (IWorkflow)items.get(0);
            } else {
                throw new APIConnectException("Wrong data returned from server");
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    public IUser register(INewUser user) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/users/new", sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        try {
            ((HttpURLConnection) connection).setRequestMethod("POST");
        } catch (Exception e) {
            throw new APIConnectException("HTTP Error: " + e.getMessage());
        }

        // Execute the method
        try {
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            ObjectBuilder.writeObjectToStream(user, connection.getOutputStream());
            InputStream stream = connection.getInputStream();
            List<IObject> items = ObjectBuilder.parseInputStream(stream);
            checkForError(items);

            if (items.size() == 1 && items.get(0) instanceof IUser) {
                return (IUser) items.get(0);
            } else {
                throw new APIConnectException("Wrong data returned from server");
            }

        } catch (IOException ioe) {
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        } catch (APIGenerateException apie) {
            apie.printStackTrace();
            throw new APIConnectException("API XML Generation error: " + apie.getMessage());
        }
    }

    public IUser findUserByEmail(String email)  throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        URLConnection connection = getConnection("/users/" + userContextId + "/users/email/" + email, sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);

        // Execute the method
        try {
            connection.connect();

            InputStream stream = connection.getInputStream();
            List<IObject> results = ObjectBuilder.parseInputStream(stream);
            checkForError(results);
            if(results.get(0) instanceof IUser){
                return (IUser)results.get(0);
            } else {
                return null;
            }
        } catch (IOException ioe){
            throw new APIConnectException("Error connecting to server: " + ioe.getMessage());
        }
    }

    @Override
    public String getServiceXml(IDocument serviceDocument) throws APIConnectException, APISecurityException, APIParseException, APIInstantiationException {
        long sequenceValue = sequence.nextLongValue();
        HttpURLConnection connection = getConnection("/users/" + userContextId + "/services/" + serviceDocument.getId()  , sequenceValue);
        addContentSignature(connection, null, sequenceValue);
        signURLConnectionData(connection);
        try {
            connection.connect();
            InputStream stream = connection.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int value;
            while((value=stream.read())!=-1){
                outStream.write(value);
            }
            stream.close();
            outStream.flush();
            return new String(outStream.toByteArray());
        } catch (IOException e){
            throw new APIConnectException("Error getting workflow service: " + e.getMessage());
        }
    }
    
}