/*
 * HttpDownloader.java
 */
package com.connexience.server.workflow.api.rpc.downloaders;

import com.connexience.server.api.APIConnectException;
import com.connexience.server.api.IDocument;
import com.connexience.server.workflow.api.rpc.Downloader;
import com.connexience.server.workflow.api.rpc.RPCClientApi;
import com.connexience.server.workflow.rpc.RPCClient;
import com.connexience.server.workflow.util.ZipUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 * This class contacts the main server via http to download files.
 * @author hugo
 */
public class HttpDownloader extends Downloader {
    static Logger logger = Logger.getLogger(HttpDownloader.class);
    int retryCount = 20;
    int initialRetryInterval = 1000;
    int maxWaitInterval = 10000;
    double retryMultiplier = 2;
    String downloadLocation = "";
    File tempFile;
    
    public HttpDownloader() {
    }
     
    private void setProperties(){
        retryCount = parent.getRetryCount();
        initialRetryInterval = parent.getInitialRetryInterval();
        maxWaitInterval = parent.getMaxWaitInterval();
        retryMultiplier = parent.getRetryMultiplier();
        downloadLocation = parent.getDownloadLocation();
    }
    
    private URL createUrl() throws APIConnectException {
        try {
            URL serverUrl = new URL(client.getServerUrl());
            URL downloadUrl;
            if(client.getSessionId()!=null && !client.getSessionId().trim().equals("")){
                downloadUrl = new URL("http://" + serverUrl.getHost() + ":" + serverUrl.getPort() + downloadLocation + ";jsessionid=" + client.getSessionId());
            } else {
                downloadUrl = new URL("http://" + serverUrl.getHost() + ":" + serverUrl.getPort() + downloadLocation);
            }        
            return downloadUrl;
        } catch (Exception e){
            throw new APIConnectException("Cannot create download URL: " + e.getMessage(), e);
        }
    }
    
    public boolean download() throws APIConnectException {
        setProperties();
        logger.info("Attempting to download: " + document.getName());
        try {
            URL url = createUrl();
            
            int count = 0;
            double waitTime = initialRetryInterval;
            for(count = 1;count<=retryCount;count++){
                if(!attemptDownload(url)){
                    logger.info("Recoverable download error. Waiting for: " + waitTime);
                    try {
                        Thread.sleep((int)waitTime);
                    }  catch (Exception e){}
                    waitTime = waitTime * retryMultiplier;
                    if(waitTime>=maxWaitInterval){
                        waitTime = maxWaitInterval;
                    }
                } else {
                    logger.info("Download succeeded after: " + count + " attempts");
                    if(tempFile!=null && tempFile.exists()){
                        try {
                            ZipUtils.copyFileToOutputStream(tempFile, stream);
                            return true;
                        } catch (Exception e){
                            throw new APIConnectException("Error copying data from temporary file: " + e.getMessage());
                        }
                    } else {
                        throw new APIConnectException("No temporary file");
                    }
                }
            }
            return false;        
        } catch (APIConnectException e){
            logger.error("Download exception: " + e.getMessage(), e);
            throw e;
        } finally {
            removeTempFile();
        }        
    }
    
    public InputStream getInputStream() throws APIConnectException {
        setProperties();
        try {
            HttpURLConnection connection = createConnection(createUrl());

            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream inStream = connection.getInputStream();
                return new DownloaderInputStream(this, inStream);
            } else {
                throw new APIConnectException("Server returned error code: " + connection.getResponseCode() + " downloading file: " + connection.getResponseMessage());
            }        
            
        } catch (SocketTimeoutException stoe){
            throw new APIConnectException("Socket timeout");
            
        } catch (SocketException se){
            throw new APIConnectException("Socket exception");
            
        } catch (IOException ioe){
            throw new APIConnectException("IOException");
            
        }       
    }
    
    private boolean attemptDownload(URL url) throws APIConnectException {
        FileOutputStream fileStream = null;
        try {
            // Create a temp file
            if(tempFile!=null && tempFile.exists()){
                tempFile.delete();
            }
            tempFile = File.createTempFile("download", "dat");
            fileStream = new FileOutputStream(tempFile);
            
            HttpURLConnection connection = createConnection(url);
            
            byte[] buffer = new byte[4096];
            int len;

            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                BufferedInputStream inStream = new BufferedInputStream(connection.getInputStream());
                while((len=inStream.read(buffer))>0){
                    fileStream.write(buffer, 0, len);
                }
                inStream.close();
                fileStream.flush();
                fileStream.close();
                
                // Check the content length matches the file size
                if(connection.getContentLength()==tempFile.length()){
                    return true;
                } else {
                    logger.info("Expected file size did not match actual file size. CL=" + connection.getContentLength() + " FL=" + tempFile.length() + ". Recoverable");
                    return false;
                }
            } else {
                throw new APIConnectException("Server returned error code: " + connection.getResponseCode() + " downloading file: " + connection.getResponseMessage());
            }        
            
        } catch (SocketTimeoutException stoe){
            logger.info("Socket timeout exception. Recoverable");
            return false;
            
        } catch (SocketException se){
            logger.info("Socket exception downloading. Revoverable");
            return false;
            
        } catch (IOException ioe){
            logger.info("IOError downloading. Recoverable");
            
            return false;
        } finally {
            if(fileStream!=null){
                try {fileStream.flush();}catch (Exception e){}
                try {fileStream.close();}catch (Exception e){}
            }
        }
    }
    
    private HttpURLConnection createConnection(URL url) throws SocketTimeoutException, SocketException, IOException{
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setReadTimeout(client.getDefaultTimeout());  // Use the timeout from the RPC client

        if(versionId==null && versionNumber==-1){
            // No version or number
            connection.addRequestProperty("documentid", document.getId());
            connection.addRequestProperty("type", "document");                

        } else if(versionId!=null){
            // Actual version
            connection.addRequestProperty("documentid", document.getId());
            connection.addRequestProperty("type", "document");
            connection.addRequestProperty("versionid", versionId);

        } else if(versionId==null && versionNumber!=-1){
            // Version number
            connection.addRequestProperty("documentid", document.getId());
            connection.addRequestProperty("type", "document");
            connection.addRequestProperty("versionnumber", Integer.toString(versionNumber));                

        }

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
        return connection;
    }
    
    private void removeTempFile(){
        if(tempFile!=null && tempFile.exists()){
            if(!tempFile.delete()){
                tempFile.deleteOnExit();
            }
        }
    }        
}
