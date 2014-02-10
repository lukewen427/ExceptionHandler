/*
 * FileDataStore.java
 */

package com.connexience.server.model.storage;

import com.connexience.server.model.*;
import com.connexience.server.model.document.*;
import com.connexience.server.*;

import java.io.*;
import java.util.ArrayList;

/**
 * This class acts as a data store that stores everything on disk. The file store is organised
 * as follows:
 *
 *  Each organisation has its own folder
 *  DocumentRecords are in sub-folders determinied by the last two digits of the id
 *  Each DocumentRecord is a folder, which conains all of the versions
 *
 * @author nhgh
 */
public class FileDataStore extends DataStore {
    /** Directory for storage */
    private String directory;
    
    /** Creates a new instance of FileDataStore */
    public FileDataStore() {
        bulkDeleteSupported = true;
        sizeLimited = true;
        spaceReportingSupported = true;
    }

    @Override
    public long getAvailableStoreSize() throws ConnexienceException {
        File baseDir = new File(directory);
        return baseDir.getFreeSpace();
    }

    @Override
    public long getTotalStoreSize() throws ConnexienceException {
        File baseDir = new File(directory);
        return baseDir.getTotalSpace();
    }
    
    /** Read a record from an InputStream */
    public DocumentVersion readFromStream(DocumentRecord document, DocumentVersion record, InputStream stream) throws ConnexienceException {
        try {
            BufferedInputStream inStream = new BufferedInputStream(stream);
            File documentDir = getDocumentRecordDirectory(document);
            
            File outFile = new File(documentDir.getPath() + File.separator + record.getId());
            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(outFile));

            byte[] buffer = new byte[4096];
            int len;
            long fileLen = 0;
            while((len=inStream.read(buffer))>0){
                fileLen = fileLen + len;
                outStream.write(buffer, 0, len);
            }
            outStream.flush();
            outStream.close();
            inStream.close();
            record.setSize(fileLen);
            return record;
            
        } catch (Exception e){
            throw new ConnexienceException(e.getMessage(), e);
        }
    }
    
    /**
     * Remove a record. This only deletes the actual DocumentVersion unless there are
     * no versions left, in which case the document directory is also deleted
     */
    public void removeRecord(DocumentRecord document, DocumentVersion record) throws ConnexienceException {
        try {
            File documentDir = getDocumentRecordDirectory(document);
            File versionFile = new File(documentDir + File.separator + record.getId());
            if(versionFile.exists()){
                versionFile.delete();
            }
            
            // Any more versions left
            File[] children = documentDir.listFiles();
            if(children.length==0){
                documentDir.delete();
            }
            
        } catch (Exception e){
            throw new ConnexienceException("Error removing file: " + e.getMessage(), e);
        }
    }


    public void bulkDelete(String organisationId, ArrayList<String> documentIds) throws ConnexienceException {
        for(String documentId : documentIds){
            try {
                File documentDir = getDocumentRecordDirectory(organisationId, documentId);
                if(documentDir.exists()){
                    File[] children = documentDir.listFiles();
                    for(int i=0;i<children.length;i++){
                        if(!children[i].delete()){
                            children[i].deleteOnExit();
                        }
                    }
                    documentDir.delete();
                }
            } catch (Exception e){
                throw new ConnexienceException("Error removing document record: " + e.getMessage(), e);
            }
        }
    }
    
    /** Get an InputStream that can be used to read the contents of the document */
    public InputStream getInputStream(DocumentRecord document, DocumentVersion version) throws ConnexienceException {
        try {
            File documentDir = getDocumentRecordDirectory(document);
            File versionFile = new File(documentDir + File.separator + version.getId());            
            BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(versionFile));
            return inStream;
            
        } catch (Exception e){
            throw new ConnexienceException(e.getMessage(), e);
        }        
    }   
    
    /**
     * Write a record to an OutputStream
     */
    public void writeToStream(DocumentRecord document, DocumentVersion record, OutputStream stream) throws ConnexienceException {
        try {
            File documentDir = getDocumentRecordDirectory(document);
            File versionFile = new File(documentDir + File.separator + record.getId());            
            BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(versionFile));
            
            byte[] buffer = new byte[4096];
            int len = inStream.read(buffer);
            while(len!=-1){
                stream.write(buffer, 0, len);
                len = inStream.read(buffer);
            }
            stream.flush();
            inStream.close();
            
        } catch (Exception e){
            throw new ConnexienceException(e.getMessage(), e);
        }
    }

    /**
     * Read a record from a File
     */
    public DocumentVersion readFromFile(DocumentRecord document, DocumentVersion record, File file) throws ConnexienceException {
        try {
            BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
            File documentDir = getDocumentRecordDirectory(document);
            
            File outFile = new File(documentDir.getPath() + File.separator + record.getId());
            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(outFile));
            
            byte[] buffer = new byte[4096];
            int len;
            long fileLen = 0;
            while((len=inStream.read(buffer))>0){
                outStream.write(buffer, 0, len);
                fileLen = fileLen + len;
            }
            outStream.flush();
            outStream.close();
            inStream.close();
            record.setSize(fileLen);
            return record;
        } catch (Exception e){
            throw new ConnexienceException(e.getMessage(), e);
        }
    }
    
    /** Set the directory */
    public void setDirectory(String directory){
        this.directory = directory;
    }
    
    /** Get the directory */
    public String getDirectory(){
        return directory;
    }
    
    // =========================================================================
    // Code to manage the allocation of directories to organisations and id
    // subdirectories
    // =========================================================================
    
    /** Get a directory for a DocumentRecord. This uses the following semantics
     * for building a directory path:
     * 
     * root_directory/ORGANISAION_ID/LAST_2_IDDIGITS/DOCUMENTID/
     *
     * This directory is then the repository for all versions of that file.
     */
    
    private File getDocumentRecordDirectory(String organisationId, String documentId) throws IOException {
        File orgDir = getOrganisationDirectory(organisationId);
        String id = documentId;
        String subdir = id.substring(id.length() - 2);
        File recordDirectory = new File(orgDir.getPath() + File.separator + subdir + File.separator + id);
        if(recordDirectory.exists() && recordDirectory.isDirectory()){
            return recordDirectory;
        } else {
            if(!recordDirectory.exists()){
                recordDirectory.mkdirs();
                return recordDirectory;
            } else {
                throw new IOException("Cannot create document record directory");
            }
        }        
    }
    
    private File getDocumentRecordDirectory(DocumentRecord record) throws IOException {
        if(record.getId()!=null && record.getOrganisationId()!=null){
            return getDocumentRecordDirectory(record.getOrganisationId(), record.getId());
        } else {
            if(record.getId()==null){
                throw new IOException("Document does not have an ID");
            } else {
                throw new IOException("Document has not been assigned to an organisation");
            }
        }
    }
    
    /** Get the directory for an organisation. This will create the directory if
     * it doesn't already exist */
    private File getOrganisationDirectory(String organisationID) throws IOException {
        File dir = new File(directory + File.separator + organisationID);
        if(dir.exists() && dir.isDirectory()){
            return dir;
        } else {
            if(!dir.exists()){
                if(dir.mkdirs()){
                    return dir;
                } else {
                    throw new IOException("Cannot create organisation storage directory");
                }
            } else {
                throw new IOException("A file with the same ID as the organisation is preventing the creation of the organisation storage directory");
            }
        }
    }
    
 
}
