/*
 * Uploader.java
 */
package com.connexience.server.workflow.api.rpc;
import com.connexience.server.workflow.rpc.*;
import com.connexience.server.api.*;
import com.connexience.server.workflow.util.*;
import com.connexience.server.api.util.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.log4j.*;
/**
 * This class uploads data and retries if it fails.
 * @author hugo
 */
public abstract class Uploader {
    private static Logger logger = Logger.getLogger(Uploader.class);
    protected InputStream stream;
    protected IDocument document;
    protected IDocumentVersion uploadedDocumentVersion = null;
    protected RPCClient client;
    protected RPCClientApi parent;    
    
    public Uploader() {

    }
    
    public void setParent(RPCClientApi parent){
        this.parent = parent;
        this.client = parent.getClient();
    }
    
    public void setDocument(IDocument document){
        this.document = document;
    }

    public void setStream(InputStream stream){
        this.stream = stream;
    }
    
    public IDocumentVersion getUploadedDocumentVersion(){
        return uploadedDocumentVersion;
    }
            
    
    public abstract boolean upload() throws APIConnectException;

    
    

}
