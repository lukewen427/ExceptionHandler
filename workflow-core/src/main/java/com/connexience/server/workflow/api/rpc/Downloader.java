/*
 * Downloader.java
 */
package com.connexience.server.workflow.api.rpc;

import com.connexience.server.workflow.rpc.*;
import com.connexience.server.workflow.util.*;
import com.connexience.server.api.*;

import java.io.*;
import java.net.*;
import org.apache.log4j.*;

/**
 * This class attempts multiple times to download a file if there are any IO / 
 * Socket errors
 * @author hugo
 */
public abstract class Downloader {
    protected RPCClientApi parent;
    protected RPCClient client;
    protected IDocument document;
    protected String versionId = null;
    protected int versionNumber = -1;
    protected OutputStream stream;
    
   
    public Downloader(){
        
    }

    public void setDocument(IDocument document) {
        this.document = document;
    }

    public void setParent(RPCClientApi parent) {
        this.parent = parent;
        this.client = parent.getClient();
    }

    public void setStream(OutputStream stream) {
        this.stream = stream;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
       
    public abstract boolean download() throws APIConnectException;
    
    public abstract InputStream getInputStream() throws APIConnectException;
}
