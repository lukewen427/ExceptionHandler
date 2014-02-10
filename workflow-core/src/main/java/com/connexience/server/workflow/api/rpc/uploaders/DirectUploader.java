/*
 * DirectUploader.java
 */
package com.connexience.server.workflow.api.rpc.uploaders;

import com.connexience.server.api.APIConnectException;
import com.connexience.server.api.APIInstantiationException;
import com.connexience.server.api.IDocument;
import com.connexience.server.api.IDocumentVersion;
import com.connexience.server.api.impl.InkspotTypeRegistration;
import com.connexience.server.api.util.ObjectBuilder;
import com.connexience.server.model.document.DocumentRecord;
import com.connexience.server.model.document.DocumentVersion;
import com.connexience.server.model.storage.DataStore;
import com.connexience.server.workflow.api.rpc.CallSender;
import com.connexience.server.workflow.api.rpc.InkspotObjectTransport;
import com.connexience.server.workflow.api.rpc.Uploader;
import com.connexience.server.workflow.rpc.CallObject;

/**
 * This class uploads data directly to supported DataStore objects obtained
 * from the server.
 * @author hugo
 */
public class DirectUploader extends Uploader {
    static {
        InkspotTypeRegistration.register();
    }
    
    /** Make a call to the server to create a new document version */
    private DocumentVersion createNextVersion(DocumentRecord doc) throws Exception {
        CallObject call = new CallObject("APICreateNextVersion");
        call.getCallArguments().add("DocumentID", doc.getId());
        CallSender sender = new CallSender(client, call);
        sender.setup(parent);
        if(sender.syncCall()){
            IDocumentVersion v = (IDocumentVersion)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("DocumentVersion")).getObject();
            return createDocumentVersion(v);
        } else {
            throw new Exception("Error executing call: " + call.getStatusMessage());
        }
    }
    
    /** Save an updated document version */
    private DocumentVersion updateVersion(DocumentVersion version) throws Exception {
        CallObject call = new CallObject("APIUpdateVersion");
        call.getCallArguments().add("DocumentVersion", new InkspotObjectTransport(createDocumentVersion(version)));
        CallSender sender = new CallSender(client, call);
        sender.setup(parent);
        if(sender.syncCall()){
            IDocumentVersion result = (IDocumentVersion)((InkspotObjectTransport)call.getReturnArguments().xmlStorableValue("DocumentVersion")).getObject();
            return createDocumentVersion(result);
        } else {
            throw new Exception("Error executing call: " + call.getStatusMessage());
        }
    }
    
    public DocumentVersion createDocumentVersion(IDocumentVersion versionObj) {
        DocumentVersion version = new DocumentVersion();
        version.setComments(versionObj.getComments());
        version.setDocumentRecordId(versionObj.getDocumentId());
        version.setId(versionObj.getId());
        version.setSize(versionObj.getSize());
        version.setUserId(versionObj.getUserId());
        version.setVersionNumber(versionObj.getVersionNumber());
        return version;
    }
    
    /** Create an IDocumentVersion object */
    private IDocumentVersion createDocumentVersion(DocumentVersion version) throws APIInstantiationException {
        IDocumentVersion iversionObj = (IDocumentVersion)ObjectBuilder.instantiateObject(IDocumentVersion.XML_NAME);
        iversionObj.setDocumentId(version.getDocumentRecordId());
        iversionObj.setId(version.getId());
        iversionObj.setUserId(version.getUserId());
        iversionObj.setVersionNumber(version.getVersionNumber());
        iversionObj.setComments(version.getComments());
        iversionObj.setSize(version.getSize());
        return iversionObj;
    }    
    
    private DocumentRecord createDocumentRecord(){
        DocumentRecord record = new DocumentRecord();
        record.setId(document.getId());
        record.setContainerId(document.getContainerId());
        record.setCreatorId(document.getOwnerId());
        record.setName(document.getName());
        record.setOrganisationId(parent.getTicket().getOrganisationId());
        return record;
    }
    
    @Override
    public boolean upload() throws APIConnectException {
        try {
            // Create the next version for the document
            DocumentRecord record = createDocumentRecord();
            DocumentVersion v = createNextVersion(record);

            // Upload the data to the data store
            DataStore ds = parent.getDataStore();
            v = ds.readFromStream(record, v, stream);
            v.setUserId(parent.getTicket().getUserId());
            
            // Update the document version
            v = updateVersion(v);
            uploadedDocumentVersion = createDocumentVersion(v);
            return true;
        } catch (Exception e){
            throw new APIConnectException("Error uploading data directly to datastore: " + e.getMessage(), e);
        }
    }
}
