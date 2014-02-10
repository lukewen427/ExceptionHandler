/*
 * FileFetcher.java
 */

package com.connexience.server.service.util;

import com.connexience.server.api.*;
import com.connexience.server.model.service.*;
import com.connexience.server.workflow.rpc.*;
import com.connexience.server.workflow.api.rpc.*;
import com.connexience.server.workflow.xmlstorage.*;
import com.connexience.server.util.SignatureUtils;
import com.connexience.server.util.Base64;

import org.pipeline.core.xmlstorage.prefs.*;

import java.security.cert.*;
import java.net.*;
import java.io.*;

/**
 * This class fetches files from the workflow data servlet using X509 certificates
 * to sign the request.
 * @author hugo
 */
public class FileFetcher {
    /** Client object */
    private RPCClient client;

    /** Service name */
    private String documentId;

    /** Download location */
    private String downloadUrl;

    /** Document record that has been fetched from the server */
    private DocumentRecordWrapper document;

    /** Latest version at the time the document was fetched */
    private DocumentVersionWrapper version;

    public FileFetcher(URL serverUrl, String documentId) {
        String url = "http://" + serverUrl.getHost() + ":" + serverUrl.getPort() + "/WorkflowServer/WorkflowServlet";
        downloadUrl = "http://" + serverUrl.getHost() + ":" + serverUrl.getPort() + "/WorkflowServer/WorkflowAPIDataServlet";
        this.client = new RPCClient(url);
        client.setSecurityMethod(RPCClient.PRIVATE_KEY_SECURITY);
        client.setSessionIdRequired(false);
        client.setPrivateKey(PreferenceManager.getPrivateKey());
        client.setSigningUserId(PreferenceManager.getCertificateOwnerId());
        client.setSerializationMethod(RPCClient.XML_SERIALIZATION);
        this.documentId = documentId;
    }

    public File download(File tmpDir) throws Exception {
        // Get the document
        CallObject call = new CallObject("SVCGetDocument");
        call.getCallArguments().add("DocumentID", documentId);
        client.syncCall(call);
        if(call.getStatus()==CallObject.CALL_EXECUTED_OK){
            document = (DocumentRecordWrapper)call.getReturnArguments().xmlStorableValue("Document");
            version = (DocumentVersionWrapper)call.getReturnArguments().xmlStorableValue("LatestVersion");
            URLConnection connection = new URL(downloadUrl).openConnection();
            connection.addRequestProperty("documentid", document.getId());
            connection.addRequestProperty("type", "document");
            connection.addRequestProperty("userid", PreferenceManager.getCertificateOwnerId());
            connection.addRequestProperty("versionid", version.getId());
            String timestamp = Long.toString(System.currentTimeMillis());
            connection.addRequestProperty("timestamp", timestamp);

            StringBuffer sigData = new StringBuffer();
            sigData.append(document.getId());
            sigData.append(PreferenceManager.getCertificateOwnerId());
            sigData.append(timestamp);
            byte[] signature = SignatureUtils.signString(PreferenceManager.getPrivateKey(), sigData.toString());
            connection.addRequestProperty("signature", Base64.encodeBytes(signature));
            
            byte[] buffer = new byte[4096];
            int len;
            BufferedInputStream inStream = new BufferedInputStream(connection.getInputStream());
            File outFile = File.createTempFile("doc", ".dat", tmpDir);
            FileOutputStream stream = new FileOutputStream(outFile);
            while((len=inStream.read(buffer))>0){
                stream.write(buffer, 0, len);
            }
            inStream.close();
            stream.flush();
            stream.close();
            return outFile;
        } else {
            throw new Exception("RPC Error: " + call.getStatusMessage());
        }
    }

    /** Get the downloaded document record */
    public DocumentRecordWrapper getDocument(){
        return document;
    }

    /** Get the downloaded version */
    public DocumentVersionWrapper getVersion(){
        return version;
    }
}