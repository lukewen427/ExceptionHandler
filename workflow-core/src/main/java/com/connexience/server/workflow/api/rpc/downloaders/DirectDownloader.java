/*
 * DirectDownloader.java
 */
package com.connexience.server.workflow.api.rpc.downloaders;

import com.connexience.server.api.APIConnectException;
import com.connexience.server.api.IDocument;
import com.connexience.server.api.IDocumentVersion;
import com.connexience.server.api.impl.InkspotTypeRegistration;
import com.connexience.server.model.document.*;
import com.connexience.server.model.storage.DataStore;
import com.connexience.server.workflow.util.ZipUtils;
import com.connexience.server.workflow.api.rpc.Downloader;
import java.io.InputStream;
import java.util.*;
import org.apache.log4j.*;

/**
 * This downloader makes use of the DataStore object supplied by the server to make
 * a direct connection with the storage infrastructure.
 * @author hugo
 */
public class DirectDownloader extends Downloader {
    private static Logger logger = Logger.getLogger(DirectDownloader.class);
    static {
        InkspotTypeRegistration.register();
    }
    @Override
    public boolean download() throws APIConnectException {
        InputStream inStream = null;
        try {
            DataStore ds = parent.getDataStore();
            inStream = ds.getInputStream(createDocumentRecord(), createDocumentVersion());
            ZipUtils.copyInputStream(inStream, stream);
            return true;
        } catch (Exception e){
            throw new APIConnectException("Error downloading data using DirectDownloader: " + e.getMessage(), e);
        } finally {
            if(inStream!=null){
                try {inStream.close();}catch(Exception e){}
            }
        }
    }

    @Override
    public InputStream getInputStream() throws APIConnectException {
        try {
            DataStore ds = parent.getDataStore();
            return ds.getInputStream(createDocumentRecord(), createDocumentVersion());
        } catch (Exception e){
            throw new APIConnectException("Error downloading data using DirectDownloader: " + e.getMessage(), e);
        }
    }
    
    private DocumentVersion createDocumentVersion() throws Exception {
        DocumentVersion docVersion = new DocumentVersion();
        docVersion.setDocumentRecordId(document.getId());
        
        if(versionId==null && versionNumber==-1){
            // No version or number
            logger.info("Fetching latest version from server");
            IDocument doc = (IDocument)parent.createObject(IDocument.XML_NAME);
            doc.setId(document.getId());
            docVersion.setId(parent.getLatestVersionId(doc));
            

        } else if(versionId!=null){
            // Actual version
            docVersion.setId(versionId);

        } else if(versionId==null && versionNumber!=-1){
            // Version number
            logger.info("Fetching all versions from server");
            IDocument doc = (IDocument)parent.createObject(IDocument.XML_NAME);
            doc.setId(document.getId());
            List<IDocumentVersion> versions = parent.getDocumentVersions(doc);

        }        
        return docVersion;
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
}