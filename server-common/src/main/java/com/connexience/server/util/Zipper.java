/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.connexience.server.util;
import com.connexience.server.model.document.*;
import com.connexience.server.model.security.*;
import com.connexience.server.ejb.util.*;
import com.connexience.server.*;
import java.util.zip.*;
import java.io.*;
/**
 *
 * @author hugo
 */
public class Zipper {
    ZipOutputStream zipStream;
    DocumentRecord target;
    Ticket ticket;
    boolean error = false;
    String errorMessage = "";
    String comments;
    boolean running = false;
    File tempFile;

    public Zipper(Ticket ticket, DocumentRecord target, String comments) {
        this.ticket = ticket;
        this.target = target;
        this.comments = comments;
    }

    public void setupStreams() throws IOException {
        try {
            tempFile = File.createTempFile("data", "zip");
            zipStream = new ZipOutputStream(new FileOutputStream(tempFile));
        } catch (IOException ioe){
            error = true;
            errorMessage = ioe.getMessage();
            throw ioe;
        }
    }

    public boolean hasError(){
        return error;
    }

    public boolean isRunning(){
        return running;
    }

    public DocumentVersion closeStreams() throws IOException, ConnexienceException {
        DocumentVersion version = null;
        try {
            zipStream.flush();
            zipStream.finish();
            zipStream.close();

            if(!error){
                version = StorageUtils.upload(ticket, tempFile, target, comments);
            }
            
            if(!tempFile.delete()){
                tempFile.deleteOnExit();
                System.out.println("Could not delete: " + tempFile.getName() + " deleting on exit!");
            }
        } catch (IOException ioe){
            error = true;
            errorMessage = ioe.getMessage();
            running = false;
        }
        return version;
    }

    public void appendDocumentRecord(DocumentRecord doc) throws Exception {
        try {
            if(zipStream!=null){
                DocumentVersion version = EJBLocator.lookupStorageBean().getLatestVersion(ticket, doc.getId());
                ZipEntry entry = new ZipEntry(doc.getName());
                zipStream.putNextEntry(entry);
                StorageUtils.downloadFileToOutputStream(ticket, doc, version, zipStream);
                zipStream.closeEntry();
            } else {
                throw new Exception("Not running");
            }
        } catch (Exception e){
            error = true;
            errorMessage = e.getMessage();
            throw e;
        }

    }
    
    public void appendDocumentRecord(String path, DocumentRecord doc) throws Exception {
        try {
            if(zipStream!=null){
                DocumentVersion version = EJBLocator.lookupStorageBean().getLatestVersion(ticket, doc.getId());
                ZipEntry entry = new ZipEntry(path + doc.getName());
                zipStream.putNextEntry(entry);
                StorageUtils.downloadFileToOutputStream(ticket, doc, version, zipStream);
                zipStream.closeEntry();
            } else {
                throw new Exception("Not running");
            }
        } catch (Exception e){
            error = true;
            errorMessage = e.getMessage();
            throw e;
        }

    }    

    public void appendEntry(String name) throws Exception {
        try {
            if(zipStream!=null){
                ZipEntry entry = new ZipEntry(name);
                zipStream.putNextEntry(entry);
            } else {
                throw new Exception("Not running");
            }
        } catch (Exception e){
            error = true;
            errorMessage = e.getMessage();
            throw e;
        }
    }

    public void closeEntry() throws Exception {
        try {
            if(zipStream!=null){
                zipStream.closeEntry();
            } else {
                throw new Exception("Not running");
            }
        } catch (Exception e){
            error = true;
            errorMessage = e.getMessage();
            throw e;
        }
    }
}
