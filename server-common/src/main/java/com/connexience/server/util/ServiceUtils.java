/*
 * ServiceUtils.java
 */

package com.connexience.server.util;

import com.connexience.server.model.workflow.*;
import com.connexience.server.*;
import com.connexience.server.model.document.*;
import com.connexience.server.ejb.util.*;
import com.connexience.server.model.security.*;

/**
 * This class contains some utilities for dealing with dynamic workflow services
 * @author nhgh
 */
public class ServiceUtils {
    /** Create a new service library or get an existing one */
    public static DynamicWorkflowLibrary getOrCreateDynamicWorkflowLibrary(Ticket ticket, String folderId, String fileName) throws ConnexienceException {
        DocumentRecord existingDoc = EJBLocator.lookupStorageBean().getNamedDocumentRecord(ticket, folderId, fileName);
        if (existingDoc instanceof DynamicWorkflowLibrary) {
            return (DynamicWorkflowLibrary)existingDoc;

        } else {
            DynamicWorkflowLibrary library = new DynamicWorkflowLibrary();
            library.setName(fileName);
            library.setContainerId(folderId);

            // Try and find the document type
            String extension = getExtension(fileName);
            if (extension != null) {
                DocumentType type = EJBLocator.lookupStorageBean().getDocumentTypeByExtension(ticket, extension);
                if (type != null) {
                    library.setDocumentTypeId(type.getId());
                }
            }
            library = WorkflowEJBLocator.lookupWorkflowManagementBean().saveDynamicWorkflowLibrary(ticket, library);
            return library;
        }
    }


    /** Create a new service or get an existing one */
    public static DynamicWorkflowService getOrCreateDynamicWorkflowService(Ticket ticket, String folderId, String fileName) throws ConnexienceException {
        DocumentRecord existingDoc = EJBLocator.lookupStorageBean().getNamedDocumentRecord(ticket, folderId, fileName);
        if (existingDoc instanceof DynamicWorkflowService) {
            return (DynamicWorkflowService)existingDoc;

        } else {
            DynamicWorkflowService service = new DynamicWorkflowService();
            service.setName(fileName);
            service.setContainerId(folderId);

            // Try and find the document type
            String extension = getExtension(fileName);
            if (extension != null) {
                DocumentType type = EJBLocator.lookupStorageBean().getDocumentTypeByExtension(ticket, extension);
                if (type != null) {
                    service.setDocumentTypeId(type.getId());
                }
            }
            service = WorkflowEJBLocator.lookupWorkflowManagementBean().saveDynamicWorkflowService(ticket, service);
            return service;
        }
    }

    /** Create a new workflow document or get an existing one */
    public static WorkflowDocument getOrCreateWorkflowDocument(Ticket ticket, String folderId, String fileName) throws ConnexienceException {
        DocumentRecord existingDoc = EJBLocator.lookupStorageBean().getNamedDocumentRecord(ticket, folderId, fileName);
        if(existingDoc instanceof WorkflowDocument){
            return (WorkflowDocument)existingDoc;

        } else {
            WorkflowDocument doc = new WorkflowDocument();
            doc.setName(fileName);
            doc.setContainerId(folderId);
            doc = WorkflowEJBLocator.lookupWorkflowManagementBean().saveWorkflowDocument(ticket, doc);
            return doc;
        }
    }

    /** Process uploaded service data to extract the service.xml file */
    public static void processServiceXml(Ticket ticket, DynamicWorkflowService service) throws ConnexienceException {

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
}
