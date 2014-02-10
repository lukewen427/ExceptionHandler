/*
 * StorageUtils.java
 */
package com.connexience.server.util;

import com.connexience.server.model.*;
import com.connexience.server.*;
import com.connexience.server.ejb.storage.*;
import com.connexience.server.ejb.util.*;
import com.connexience.server.model.folder.Folder;
import com.connexience.server.model.security.Permission;
import com.connexience.server.model.workflow.DynamicWorkflowLibrary;
import com.connexience.server.model.workflow.DynamicWorkflowService;
import com.connexience.server.model.workflow.WorkflowDocument;
import com.connexience.server.util.*;
import com.connexience.server.model.document.*;
import com.connexience.server.model.storage.*;
import com.connexience.server.model.security.*;
import com.connexience.server.model.organisation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.security.*;
import java.security.cert.*;
import javax.servlet.http.HttpServletResponse;

/**
 * This class provides utility methods for managing storage and retrieval of data
 * using upload and download reservations.
 *
 * @author nhgh
 */
public abstract class StorageUtils {
    /**
     * Cache of document types 
     */
    private static final ConcurrentHashMap<String, DocumentType> documentTypeCache = new ConcurrentHashMap<String, DocumentType>();

    /**
     * Keystore password
     */
    private static char[] password = new String("sT0r3pa33worD").toCharArray();

    /**
     * Download a file to a specified directory
     */
    public static void downloadFileToDirectory(Ticket ticket, DocumentRecord record, DocumentVersion version, File targetDirectory) throws ConnexienceException {
        // Check to see if the document record is a link. If it is, get the actual document
        if(record instanceof DocumentRecordLink){
            record = EJBLocator.lookupStorageBean().getDocumentRecord(ticket, ((DocumentRecordLink)record).getSinkObjectId());
            version = null; // Links always point to latest
        }

        // Make sure there is a version. If not, get the latest one
        if (version == null) {
            version = EJBLocator.lookupStorageBean().getLatestVersion(ticket, record.getId());
        }

        // Check that the file represents a directory
        if (!targetDirectory.isDirectory()) {
            throw new ConnexienceException("Target is not a directory");
        }

        DataStore store = EJBLocator.lookupStorageBean().getOrganisationDataStore(ticket, ticket.getOrganisationId());

        // Get data if the store exists
        if (store != null) {
            try {
                File targetFile = new File(targetDirectory, record.getName());
                FileOutputStream stream = new FileOutputStream(targetFile);
                store.writeToStream(record, version, stream);
                stream.flush();
                stream.close();
            } catch (Exception e) {
                throw new ConnexienceException("Error writing data to target file: " + e.getMessage());
            }

        } else {
            throw new ConnexienceException("Could not locate data store for document: " + record.getName());
        }
    }

    /**
     * Get an InputStream connected to a document record
     */
    public static InputStream getInputStream(Ticket ticket, DocumentRecord record, DocumentVersion version) throws ConnexienceException {
        // Check to see if the document record is a link. If it is, get the actual document
        if(record instanceof DocumentRecordLink){
            record = EJBLocator.lookupStorageBean().getDocumentRecord(ticket, ((DocumentRecordLink)record).getSinkObjectId());
            version = null; // Links always point to latest
        }

        // Make sure there is a version. If not, get the latest one
        if (version == null) {
            version = EJBLocator.lookupStorageBean().getLatestVersion(ticket, record.getId());
        }
        
        DataStore store = EJBLocator.lookupStorageBean().getOrganisationDataStore(ticket, ticket.getOrganisationId());
 
        // Get data if the store exists
        if (store != null) {
            return store.getInputStream(record, version);
        } else {
            throw new ConnexienceException("Could not locate data store for document: " + record.getName());
        }
    }

    /**
     * Get some server data into a byte array. Send in a null document version to get the latest file version
     */
    public static byte[] download(Ticket ticket, DocumentRecord record, DocumentVersion version) throws ConnexienceException {
        // Check to see if the document record is a link. If it is, get the actual document
        if(record instanceof DocumentRecordLink){
            record = EJBLocator.lookupStorageBean().getDocumentRecord(ticket, ((DocumentRecordLink)record).getSinkObjectId());
            version = null; // Links always point to latest
        }
        
        // Make sure there is a version. If not, get the latest one
        if (version == null) {
            version = EJBLocator.lookupStorageBean().getLatestVersion(ticket, record.getId());
        }

        DataStore store = EJBLocator.lookupStorageBean().getOrganisationDataStore(ticket, ticket.getOrganisationId());

        // Get data if the store exists
        if (store != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            store.writeToStream(record, version, stream);
            return stream.toByteArray();
        } else {
            throw new ConnexienceException("Could not locate data store for document: " + record.getName());
        }
    }

    /**
     * Get some server data into a byte array. Send in a null document version to get the latest file version
     */
    public static byte[] download2(Ticket ticket, DocumentRecord record, DocumentVersion version, String permissionToAssert) throws ConnexienceException {
        // Check to see if the document record is a link. If it is, get the actual document
        if(record instanceof DocumentRecordLink){
            record = EJBLocator.lookupStorageBean().getDocumentRecord(ticket, ((DocumentRecordLink)record).getSinkObjectId());
            version = null; // Links always point to latest
        }

        // Make sure there is a version. If not, get the latest one
        if (version == null) {
            version = EJBLocator.lookupStorageBean().getLatestVersion(ticket, record.getId(), permissionToAssert);
        }

        DataStore store;
        if (record.getOrganisationId().equals(ticket.getOrganisationId())) {
            store = EJBLocator.lookupStorageBean().getOrganisationDataStore(ticket, ticket.getOrganisationId());
        } else {
            throw new ConnexienceException(ConnexienceException.UNSUPPORTED_OPERATION_MESSAGE);
        }

        // Get data if the store exists
        if (store != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            store.writeToStream(record, version, stream);
            return stream.toByteArray();
        } else {
            throw new ConnexienceException("Could not locate data store for document: " + record.getName());
        }
    }

    /**
     * Upload a File to the server
     */
    public static DocumentVersion upload(Ticket ticket, File file, DocumentRecord document, String comments) throws ConnexienceException {
        try {
            // Check to see if the document record is a link. If it is, get the actual document
            if(document instanceof DocumentRecordLink){
                document = EJBLocator.lookupStorageBean().getDocumentRecord(ticket, ((DocumentRecordLink)document).getSinkObjectId());
            }

            // Get the datastore for the organisation that the user belongs to
            DataStore store = EJBLocator.lookupStorageBean().getOrganisationDataStore(ticket, ticket.getOrganisationId());

            // Create a new version
            DocumentVersion version = EJBLocator.lookupStorageBean().createNextVersion(ticket, document.getId());

            // Update the version information back into the database
            version.setUserId(ticket.getUserId());
            version.setSize(file.length());
            version.setComments(comments);
            EJBLocator.lookupStorageBean().updateVersion(ticket, version);
            version = store.readFromStream(document, version, new FileInputStream(file));
            version = EJBLocator.lookupStorageBean().updateVersion(ticket, version);
            return version;

        } catch (ConnexienceException ce) {
            throw ce;
        } catch (Exception e) {
            throw new ConnexienceException("Error signing byte array: " + e.getMessage());
        }
    }

    /**
     * Upload a byte[] array to the server as a file
     */
    public static DocumentVersion upload(Ticket ticket, byte[] data, DocumentRecord document, String comments) throws ConnexienceException {
        try {
            // Check to see if the document record is a link. If it is, get the actual document
            if(document instanceof DocumentRecordLink){
                document = EJBLocator.lookupStorageBean().getDocumentRecord(ticket, ((DocumentRecordLink)document).getSinkObjectId());
            }

            // Get the datastore for the organisation that the user belongs to
            DataStore store = EJBLocator.lookupStorageBean().getOrganisationDataStore(ticket, ticket.getOrganisationId());

            // Create a new version
            DocumentVersion version = EJBLocator.lookupStorageBean().createNextVersion(ticket, document.getId());

            // Update the version information back into the database
            version.setUserId(ticket.getUserId());
            version.setSize(data.length);
            version.setComments(comments);
            EJBLocator.lookupStorageBean().updateVersion(ticket, version);

            version = store.readFromStream(document, version, new ByteArrayInputStream(data));
            version = EJBLocator.lookupStorageBean().updateVersion(ticket, version);            
            return version;

        } catch (ConnexienceException ce) {
            throw ce;
        } catch (Exception e) {
            throw new ConnexienceException("Error signing byte array: " + e.getMessage());
        }
    }

    /**
     * Upload data from an InputStream to the server
     */
    public static DocumentVersion upload(Ticket ticket, InputStream inStream, DocumentRecord document, String comments) throws ConnexienceException {
        try {
            // Check to see if the document record is a link. If it is, get the actual document
            if(document instanceof DocumentRecordLink){
                document = EJBLocator.lookupStorageBean().getDocumentRecord(ticket, ((DocumentRecordLink)document).getSinkObjectId());
            }

            // Get the datastore for the organisation that the user belongs to
            DataStore store = EJBLocator.lookupStorageBean().getOrganisationDataStore(ticket, ticket.getOrganisationId());

            // Create a new version
            DocumentVersion version = EJBLocator.lookupStorageBean().createNextVersion(ticket, document.getId());

            // Update the version information back into the database
            version.setUserId(ticket.getUserId());
            version.setSize(0);
            version.setComments(comments);
            version = EJBLocator.lookupStorageBean().updateVersion(ticket, version);
            version = store.readFromStream(document, version, inStream);
            version = EJBLocator.lookupStorageBean().updateVersion(ticket, version);            
            return version;

        } catch (ConnexienceException ce) {
            throw ce;
        } catch (Exception e) {
            throw new ConnexienceException("Error signing byte array: " + e.getMessage());
        }
    }

    /**
     * Create a new document record, or return the existing document record for a named file within a folder
     */
    public static DocumentRecord getOrCreateDocumentRecord(Ticket ticket, String folderId, String fileName) throws ConnexienceException {
        DocumentRecord existing = EJBLocator.lookupStorageBean().getNamedDocumentRecord(ticket, folderId, fileName);
        if (existing != null) {
            return existing;
        } else {
            DocumentRecord record = new DocumentRecord();
            record.setName(fileName);
            record.setContainerId(folderId);

            // Try and find the document type
            String extension = getExtension(fileName);
            if (extension != null) {
                DocumentType type = getDocumentTypeByExtention(ticket, extension);
                if (type != null) {
                    record.setDocumentTypeId(type.getId());
                }
            }
            record = EJBLocator.lookupStorageBean().saveDocumentRecord(ticket, record);
            return record;
        }
    }

    /** Get a document type by extension. This method uses a cache */
    public static DocumentType getDocumentTypeByExtention(Ticket ticket, String extension) throws ConnexienceException {
        if(documentTypeCache.containsKey(extension)){
            return documentTypeCache.get(extension);
        } else {
            DocumentType type = EJBLocator.lookupStorageBean().getDocumentTypeByExtension(ticket, extension);
            if(type!=null){
                documentTypeCache.put(extension, type);
            }
            return type;
        }
    }
    
    /**
     * Get the file name extension
     */
    private static String getExtension(String fileName) {
        int lastDotIdx = fileName.lastIndexOf(".");
        if (lastDotIdx > 0 && lastDotIdx < fileName.length() - 1) {
            return fileName.substring(lastDotIdx + 1).trim();
        }
        return null;
    }

    /**
     * Get the PrivateKey for the current user
     */
    public static PrivateKey getPrivateKey(Ticket ticket) throws ConnexienceException {
        try {
            byte[] keystoreData = EJBLocator.lookupCertificateBean().getKeyStoreData(ticket, ticket.getUserId());

            ByteArrayInputStream stream = new ByteArrayInputStream(keystoreData);
            KeyStore store = KeyStore.getInstance("JKS");
            store.load(stream, password);

            KeyStore.PrivateKeyEntry pke = (KeyStore.PrivateKeyEntry) store.getEntry("MyKey", new KeyStore.PasswordProtection(password));
            return pke.getPrivateKey();
        } catch (ConnexienceException ce) {
            throw ce;
        } catch (Exception e) {
            throw new ConnexienceException(e.getMessage());
        }
    }

    /**
     * Download a file to an HttpServletResponse. This method makes sure that
     * the database access worked before the output stream is committed
     */
    public static boolean downloadFileToServletResponse(Ticket ticket, DocumentRecord record, DocumentVersion version, HttpServletResponse response) throws ConnexienceException {
        InputStream inStream = getInputStream(ticket, record, version);
        byte[] buffer = new byte[4096];
        int len;
        BufferedOutputStream bufferedStream = null;
        try {
            response.setContentLength((int)version.getSize());
            bufferedStream = new BufferedOutputStream(response.getOutputStream());
            while ((len = inStream.read(buffer)) > 0) {
                bufferedStream.write(buffer, 0, len);
            }
            return true;
        } catch (Exception e) {
            throw new ConnexienceException("Error downloading file: " + e.getMessage());
        } finally {
            try {
                inStream.close();
            } catch (Exception e) {
            }
            
            try {
                bufferedStream.flush();
            } catch(Exception e){
            }
            
            try {
                bufferedStream.close();
            } catch(Exception e){
            }
        }    
    }
    
    /**
     * Download a file to an output stream
     */
    public static void downloadFileToOutputStream(Ticket ticket, DocumentRecord record, DocumentVersion version, OutputStream stream) throws ConnexienceException {
        InputStream inStream = getInputStream(ticket, record, version);
        byte[] buffer = new byte[4096];
        int len;
        try {
            while ((len = inStream.read(buffer)) > 0) {
                stream.write(buffer, 0, len);
            }
            stream.flush();
        } catch (Exception e) {
            throw new ConnexienceException("Error downloading file: " + e.getMessage());
        } finally {
            try {
                inStream.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Download data from a stream to a file
     */
    public static long copyStreamToFile(InputStream stream, File file) throws ConnexienceException {
        try {
            byte[] buffer = new byte[4096];
            FileOutputStream outStream = new FileOutputStream(file);
            long fileLen = 0;

            int len;
            while ((len = stream.read(buffer)) > 0) {
                fileLen = fileLen + len;
                outStream.write(buffer, 0, len);
            }

            outStream.flush();
            outStream.close();
            return fileLen;
        } catch (Exception e) {
            throw new ConnexienceException("Error copying stream data to temporary file: " + e.getMessage());
        }
    }

    /** Copy data from one documentrecord to another where they are owned by the same user */
    public static DocumentVersion copyDocumentData(Ticket ticket, DocumentRecord sourceDocument, DocumentVersion sourceVersion, DocumentRecord targetDocument) throws ConnexienceException {

        //Call the method which deals with two users but passing the same user.
        User user = EJBLocator.lookupUserDirectoryBean().getUser(ticket, ticket.getUserId());
        return copyDocumentData(ticket, user, sourceDocument, sourceVersion, targetDocument);
    }

    /**
     * Copy data from one document record to another where the records are owned by different users
     */
    public static DocumentVersion copyDocumentData(Ticket ownerTicket, User newUser, DocumentRecord sourceDocument, DocumentVersion sourceVersion, DocumentRecord targetDocument) throws ConnexienceException {
        try {
            Ticket newUserTicket = EJBLocator.lookupTicketBean().createWebTicketForDatabaseId(newUser.getId());

            // Check to see if the document record is a link. If it is, get the actual document
            if(sourceDocument instanceof DocumentRecordLink){
                sourceDocument = EJBLocator.lookupStorageBean().getDocumentRecord(ownerTicket, ((DocumentRecordLink)sourceDocument).getSinkObjectId());
            }

            // Check to see if the document record is a link. If it is, get the actual document
            if(targetDocument instanceof DocumentRecordLink){
                targetDocument = EJBLocator.lookupStorageBean().getDocumentRecord(ownerTicket, ((DocumentRecordLink)targetDocument).getSinkObjectId());
            }

            if (sourceVersion == null) {
                sourceVersion = EJBLocator.lookupStorageBean().getLatestVersion(ownerTicket, sourceDocument.getId());
            }

            //Get the source document
            InputStream inStream = getInputStream(ownerTicket, sourceDocument, sourceVersion);

            //Set up the new DocumentVersion
            DocumentVersion targetVersion = EJBLocator.lookupStorageBean().createNextVersion(newUserTicket, targetDocument.getId());
            targetVersion.setComments(sourceVersion.getComments());
            targetVersion.setUserId(newUserTicket.getUserId());
            targetVersion = EJBLocator.lookupStorageBean().updateVersion(newUserTicket, targetVersion);

            //Copy the actual data and update the version
            DataStore store = EJBLocator.lookupStorageBean().getOrganisationDataStore(ownerTicket, ownerTicket.getOrganisationId());
            targetVersion = store.readFromStream(targetDocument, targetVersion, inStream);
            targetVersion = EJBLocator.lookupStorageBean().updateVersion(newUserTicket, targetVersion);
            
            return targetVersion;
        } catch (ConnexienceException ce) {
            throw ce;
        } catch (Exception e) {
            throw new ConnexienceException("Error copying document data: " + e.getMessage());
        }
    }


    /** Copy the contents of a folder to another folder owned by the same user */
    public static Folder copyFolderContents(Ticket ticket, Folder sourceFolder, Folder parentFolder, String targetFolderName) throws ConnexienceException, ConnexienceSecurityException {

        //Call the method which deals with different users.  Pass in the same user asd ignore the docIdMap and workflowList
        //which map the old IDs to new IDs
        User user = EJBLocator.lookupUserDirectoryBean().getUser(ticket, ticket.getUserId());
        return copyFolderContents(ticket, sourceFolder, user, parentFolder, targetFolderName, new HashMap<String, String>(), new ArrayList<WorkflowDocument>());
    }


    /** Copy the contents of a folder from one owner to another.  Replace the IDs of copied documents where possible.
     * Will replace the following:
     *
     * 1) Service IDs in workflow documents if the services are copied too
     * 2) DocumentWrapper and FolderWrapper service parameters when the source document is copied.
     *
     * */
    public static Folder copyFolderContentsAndReplaceWorkflowIds(Ticket ticket, Folder sourceFolder, User parentFolderOwner, Folder parentFolder, String targetFolderName) throws ConnexienceException, ConnexienceSecurityException {

        //Get a ticket for the owner of the source folder
        Ticket currentUserTicket = EJBLocator.lookupTicketBean().createWebTicketForDatabaseId(parentFolderOwner.getId());

        //HM to hold the mapping of old ID -> new ID
        HashMap<String, String> docIdMap = new HashMap<String, String>();

        //List of workflow documents that have been copied
        List<WorkflowDocument> workflowList = new ArrayList<WorkflowDocument>();

        //Copy the contents
        Folder newFolder = copyFolderContents(ticket, sourceFolder, parentFolderOwner, parentFolder, targetFolderName, docIdMap, workflowList);

        //Go through the workflows and replace the IDs
        for (WorkflowDocument workflow : workflowList) {
            WorkflowEJBLocator.lookupWorkflowManagementBean().replaceWorkflowServiceIds(currentUserTicket, workflow, docIdMap);
        }

        return newFolder;
    }


    /** Copy the contents of a folder to another.  Will copy subfolders too and handle folders which are owned by different people.
     * Will return a list of the workflow documents copied and a Map of document IDs which have been copied, old ID -> new ID
     * */
    private static Folder copyFolderContents(Ticket folderOwnerTicket, Folder sourceFolder, User parentFolderOwner, Folder parentFolder, String targetFolderName, HashMap<String, String> docIdMap, List<WorkflowDocument> workflowList) throws ConnexienceException, ConnexienceSecurityException {

        //Get a ticket for the owner of the new folder
        Ticket currentUserTicket = EJBLocator.lookupTicketBean().createWebTicketForDatabaseId(parentFolderOwner.getId());

        //We must be able to read the source folder
        StorageRemote storageRemote = EJBLocator.lookupStorageBean();
        if (EJBLocator.lookupAccessControlBean().canTicketAccessResource(folderOwnerTicket, sourceFolder, Permission.WRITE_PERMISSION)) {

            //Create the new folder
            Folder targetFolder = new Folder();
            targetFolder.setName(targetFolderName);
            targetFolder.setContainerId(parentFolder.getId());
            targetFolder.setPublicVisible(sourceFolder.isPublicVisible());
            targetFolder.setShortName(sourceFolder.getShortName());
            targetFolder.setCreationTime(new Date());
            targetFolder.setCreatorId(parentFolderOwner.getId());
            targetFolder.setDescription(sourceFolder.getDescription());
            targetFolder.setObjectType(sourceFolder.getObjectType());
            targetFolder.setOrganisationId(sourceFolder.getOrganisationId());
            targetFolder.setTimeInMillis(new Date().getTime());
            targetFolder = EJBLocator.lookupStorageBean().addChildFolder(currentUserTicket, parentFolder.getId(), targetFolder);


            //Create the documents
            @SuppressWarnings("unchecked")
            List<DocumentRecord> contents = storageRemote.getFolderDocumentRecords(folderOwnerTicket, sourceFolder.getId());
            for (DocumentRecord doc : contents) {
                DocumentVersion version = EJBLocator.lookupStorageBean().getLatestVersion(folderOwnerTicket, doc.getId());
                if (version != null) {

                    //Set workflow and service specific items
                    DocumentRecord duplicate;
                    if (doc instanceof WorkflowDocument) {
                        WorkflowDocument oldWfDoc = (WorkflowDocument) doc;
                        WorkflowDocument newWfDoc = new WorkflowDocument();
                        newWfDoc.setEngineType(oldWfDoc.getEngineType());
                        newWfDoc.setExternalDataBlockName(oldWfDoc.getExternalDataBlockName());
                        newWfDoc.setExternalDataSupported(oldWfDoc.isExternalDataSupported());
                        newWfDoc.setIntermedateDataStored(oldWfDoc.isIntermedateDataStored());
                        duplicate = newWfDoc;
                    } else if (doc instanceof DynamicWorkflowService) {
                        DynamicWorkflowService wfService = (DynamicWorkflowService) doc;
                        DynamicWorkflowService newWfService = new DynamicWorkflowService();
                        newWfService.setCategory(wfService.getCategory());
                        duplicate = newWfService;
                    } else {
                        //Just a DocumentRecord
                        duplicate = new DocumentRecord();
                    }

                    //Set standard properties
                    duplicate.setName(doc.getName());
                    duplicate.setContainerId(targetFolder.getId());
                    duplicate.setCreationTime(new Date());
                    duplicate.setCreatorId(parentFolderOwner.getId());
                    duplicate.setDescription(doc.getDescription());
                    duplicate.setDocumentTypeId(doc.getDocumentTypeId());
                    duplicate.setLimitVersions(doc.isLimitVersions());
                    duplicate.setMaxVersions(doc.getMaxVersions());
                    duplicate.setObjectType(doc.getObjectType());
                    duplicate.setOrganisationId(doc.getOrganisationId());
                    duplicate.setVersioned(doc.isVersioned());

                    //Save the document in the correct way for workflows and services
                    if (duplicate instanceof WorkflowDocument) {
                        duplicate = WorkflowEJBLocator.lookupWorkflowManagementBean().saveWorkflowDocument(currentUserTicket, (WorkflowDocument) duplicate);
                        workflowList.add((WorkflowDocument) duplicate);
                    } else if (duplicate instanceof DynamicWorkflowService) {
                        duplicate = WorkflowEJBLocator.lookupWorkflowManagementBean().saveDynamicWorkflowService(currentUserTicket, (DynamicWorkflowService) duplicate);
                    } else {
                        duplicate = EJBLocator.lookupStorageBean().saveDocumentRecord(currentUserTicket, duplicate);
                    }

                    //Add the new ID to the Map so that IDs can be replaced in workflows
                    docIdMap.put(doc.getId(), duplicate.getId());

                    //Copy the actual data
                    DocumentVersion newVersion = StorageUtils.copyDocumentData(currentUserTicket, doc, version, duplicate);

                    // Now do any final config
                    if (duplicate instanceof WorkflowDocument) {
                        // Get a preview picture
                        WorkflowEJBLocator.lookupWorkflowManagementBean().updateWorkflowImage(currentUserTicket, duplicate.getId());
                    } else if (duplicate instanceof DynamicWorkflowService) {
                        String newServiceCategory = ((DynamicWorkflowService) duplicate).getCategory();
                        // Parse the service data
                        WorkflowEJBLocator.lookupWorkflowManagementBean().updateServiceXml(currentUserTicket, duplicate.getId(), newVersion.getId(), newServiceCategory);
                    }

                }
            }

            //Recurse through the subfolders
            @SuppressWarnings("unchecked")
            List<Folder> subFolders = storageRemote.getChildFolders(folderOwnerTicket, sourceFolder.getId());
            for (Folder subFolder : subFolders) {
                copyFolderContents(folderOwnerTicket, subFolder, parentFolderOwner, targetFolder, subFolder.getName(), docIdMap, workflowList);
            }
        } else {
            throw new ConnexienceSecurityException("Not allowed to copy folder");
        }
        return new Folder();
    }
}
