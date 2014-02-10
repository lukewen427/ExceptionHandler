/*
 * HttpUploader.java
 */
package com.connexience.server.workflow.api.rpc.uploaders;

import com.connexience.server.api.*;
import com.connexience.server.api.util.ObjectBuilder;
import com.connexience.server.workflow.api.rpc.RPCClientApi;
import com.connexience.server.workflow.api.rpc.Uploader;
import com.connexience.server.workflow.rpc.RPCClient;
import com.connexience.server.workflow.util.ZipUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import org.apache.log4j.Logger;


/**
 * This class uploads data via an http post to the main server.
 * @author hugo
 */
public class HttpUploader extends Uploader {
    Logger logger = Logger.getLogger(HttpUploader.class);
    
    File tempFile;
    int retryCount = 20;
    int initialRetryInterval = 1000;
    int maxWaitInterval = 10000;
    double retryMultiplier = 2;
    private String uploadLocation = "";
    
    private void copyStreamToTempFile() throws IOException {
        tempFile = File.createTempFile("upload", "dat");
        FileOutputStream outStream = new FileOutputStream(tempFile);
        ZipUtils.copyInputStream(stream, outStream);
        outStream.flush();
        outStream.close();
    }
    
    private void setProperties(){
        retryCount = parent.getRetryCount();
        initialRetryInterval = parent.getInitialRetryInterval();
        maxWaitInterval = parent.getMaxWaitInterval();
        retryMultiplier = parent.getRetryMultiplier();
        uploadLocation = parent.getDownloadLocation();
    }    
    
    private URL createUrl() throws APIConnectException {
        try {
            URL serverUrl = new URL(client.getServerUrl());
            URL uploadUrl;
            if(client.getSessionId()!=null && !client.getSessionId().trim().equals("")){
                uploadUrl = new URL("http://" + serverUrl.getHost() + ":" + serverUrl.getPort() + uploadLocation + ";jsessionid=" + client.getSessionId());
            } else {
                uploadUrl = new URL("http://" + serverUrl.getHost() + ":" + serverUrl.getPort() + uploadLocation);
            }
            return uploadUrl;
        } catch (Exception e){
            throw new APIConnectException("Error creating upload URL: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean upload() throws APIConnectException {
        logger.info("Attempting to upload: " + document.getName());
        setProperties();
        URL uploadUrl = createUrl();
        try {
            copyStreamToTempFile();
        } catch (IOException e){
            throw new APIConnectException("Error copying data to temporary file: " + e.getMessage(), e);
        } finally {
            try {
                stream.close();
            } catch (Exception e){}
        }
        
        try {
            int count = 0;
            double waitTime = initialRetryInterval;
            for(count = 1;count<=retryCount;count++){
                if(!attemptUpload(uploadUrl)){
                    logger.info("Recoverable upload error. Waiting for: " + waitTime);
                    try {
                        Thread.sleep((int)waitTime);
                    }  catch (Exception e){}
                    waitTime = waitTime * retryMultiplier;
                    if(waitTime>=maxWaitInterval){
                        waitTime = maxWaitInterval;
                    }
                } else {
                    logger.info("Upload succeeded after: " + count + " attempts");
                    return true;
                }
            }
            return false;        
        } catch (APIConnectException e){
            logger.error("Upload exception: " + e.getMessage(), e);
            throw e;
        } finally {
            removeTempFile();
        }        
    }
    
    
    private boolean attemptUpload(URL uploadUrl) throws APIConnectException {
        InputStream sourceStream = null;
        
        try {
            sourceStream = new FileInputStream(tempFile);
            HttpURLConnection connection = (HttpURLConnection)uploadUrl.openConnection();
            connection.setDoOutput(true);
            connection.addRequestProperty("documentid", document.getId());
            connection.setChunkedStreamingMode(32768);
            connection.addRequestProperty("type", "document");

            // Send in a serialized ticket
            if(client.getSecurityMethod()==RPCClient.TICKET_SECURITY && client.getTicket()!=null){
                connection.addRequestProperty("ticket", client.getEncodedTicket());
            }

            // If the client is not using session security, send in some provenance
            // data if it exists
            if(client.getSecurityMethod()!=RPCClient.SESSION_SECURITY && client.getProvenanceProperties()!=null){
                connection.addRequestProperty("invocationid", client.getProvenanceProperties().stringValue("InvocationID", ""));
                connection.addRequestProperty("blockuuid", client.getProvenanceProperties().stringValue("BlockUUID", ""));
            }
            
            byte[] buffer = new byte[4096];
            int len;
            
            BufferedOutputStream outStream = new BufferedOutputStream(connection.getOutputStream());
            while((len=sourceStream.read(buffer))>0){
                outStream.write(buffer, 0, len);
            }
            outStream.flush();
            outStream.close();
            sourceStream.close();

            if(connection.getResponseCode()==500){
                // Upload problem
                return false;
            }
            
            InputStream inStream = connection.getInputStream();            
            List<IObject> results = ObjectBuilder.parseInputStream(inStream);
            IAPIErrorMessage errMsg = checkForError(results);
            if(errMsg==null){
                if(results.get(0) instanceof IDocumentVersion){
                    uploadedDocumentVersion = (IDocumentVersion)results.get(0);
                    return true;
                } else {
                    throw new APIConnectException("Error uploading document: No version returned");
                }            
                
            } else {
                // Is this an IO Error message
                return false;
            }
            
        } catch (SocketTimeoutException stoe){
            logger.info("Socket timeout exception in uploader. Recoverable");
            return false;
            
        } catch (SocketException e){
            // Plain socket exception
            logger.info("Socket exception in uploader. Recoverable");
            return false;
            
        } catch (IOException ioe){
            // Plain IO Error
            logger.info("IO exception in uploader. Recoverable");
            return false;

        } catch (APIParseException apie){
            // Error parsing response
            throw new APIConnectException("Error parsing response: " + apie, apie);
            
        } catch (APIInstantiationException apie){
            throw new APIConnectException("Error instantiating response data: " + apie, apie);
            
        } finally {
            if(sourceStream!=null){
                try {
                    sourceStream.close();
                } catch (Exception e){}
            }
        }
    }
    
    /** Check for an error object and return an Exception that can be thrown */
    private IAPIErrorMessage checkForError(List<IObject> results) {
        if(results.size()>0){
            if(results.get(0) instanceof IAPIErrorMessage){
                IAPIErrorMessage message = (IAPIErrorMessage)results.get(0);
                return message;
            } else {
                return null;
            }
        } else {
            return null;
        }
        
    }
    
    private void removeTempFile(){
        if(!tempFile.delete()){
            tempFile.deleteOnExit();
        }
    }    
}
