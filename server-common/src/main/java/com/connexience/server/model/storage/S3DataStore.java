/*
 * S3DataStore.java
 */

package com.connexience.server.model.storage;

import com.connexience.server.*;
import com.connexience.server.model.*;
import com.connexience.server.model.document.*;
import com.connexience.server.util.StorageUtils;

import org.jets3t.service.*;
import org.jets3t.service.impl.rest.httpclient.*;
import org.jets3t.service.model.*;
import org.jets3t.service.security.*;
        
import java.io.*;
import java.util.*;

/**
 * This class provides a connexience data store that uses Amazon S3 for its back end storage
 * @author hugo
 */
public class S3DataStore extends DataStore {
    /** Amazon Access Key ID */
    private String accessKeyId = "";
    
    /** Amazon Secret Key ID */
    private String accessKey = "";
    
    /** Organisation Bucket Name */
    private String organisationBucket = "";
    
    /** Location in Europe */
    public static final String EUROPE = S3Bucket.LOCATION_EUROPE;
    
    /** Location in USA */
    public static final String USA = S3Bucket.LOCATION_US;
    
    /** Get an InputStream that can be used to read the contents of the document */
    public InputStream getInputStream(DocumentRecord document, DocumentVersion version) throws ConnexienceException {
        S3Service service = getS3Service();
        
        // Find the relevant object
        try {
            S3Object fileObject = service.getObject(organisationBucket, version.getId(), null, null, null, null, null, null);
            InputStream inStream = fileObject.getDataInputStream();
            return inStream;
            
        } catch (Exception e){
            throw new ConnexienceException("Error getting S3 data stream: " + e.getMessage());
        }

    }
    
    /** Read a record from a File */
    public DocumentVersion readFromFile(DocumentRecord document, DocumentVersion record, File file) throws ConnexienceException {
        // Get the S3 Service
        S3Service service = getS3Service();
        
        // Create an object to store the version
        S3Object fileObject = new S3Object(record.getId());
        fileObject.setContentLength(file.length());
        fileObject.setDataInputFile(file);
        record.setSize(file.length());
        try {
            service.putObject(organisationBucket, fileObject);
        } catch (Exception e){
            throw new ConnexienceException("Error uploading data to S3: " + e.getMessage());
        }
        return record;
    }
    
    /** Read a record from an InputStream */
    public DocumentVersion readFromStream(DocumentRecord document, DocumentVersion record, InputStream stream) throws ConnexienceException {
        // Get the S3 Service
        S3Service service = getS3Service();

        // Copy the data from the stream to a temporary file
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("esc", "dat");
            StorageUtils.copyStreamToFile(stream, tmpFile);
        } catch (Exception e){
            throw new ConnexienceException("Error creating temporary file: " + e.getMessage(), e);
        }

        // Create an object to store the version
        S3Object fileObject = new S3Object(record.getId());
        fileObject.setContentLength(tmpFile.length());
        fileObject.setDataInputFile(tmpFile);
        
        try {
            service.putObject(organisationBucket, fileObject);
            record.setSize(fileObject.getContentLength());
            return record;
        } catch (Exception e){
            throw new ConnexienceException("Error uploading data to S3: " + e.getMessage());
        } finally {
            if (tmpFile != null) {
                if (!tmpFile.delete()) {
                    tmpFile.deleteOnExit();
                }
            }
        }
    }
    
    /** Write a record to an OutputStream */
    public void writeToStream(DocumentRecord document, DocumentVersion record, OutputStream stream) throws ConnexienceException {
        // Get the S3 Service
        S3Service service = getS3Service();

        // Find the relevant object
        try {
            S3Object fileObject = service.getObject(organisationBucket, record.getId(), null, null, null, null, null, null);
            
            InputStream inStream = fileObject.getDataInputStream();
            byte[] buffer = new byte[4096];
            int len = inStream.read(buffer);
            while(len!=-1){
                stream.write(buffer, 0, len);
                len = inStream.read(buffer);
            }
            stream.flush();
            inStream.close();
            
        } catch (Exception e){
            throw new ConnexienceException("Error getting S3 data: " + e.getMessage());
        }
        
    }
    
    /** Remove a record */
    public void removeRecord(DocumentRecord document, DocumentVersion record) throws ConnexienceException {
        // Get the S3 Service
        S3Service service = getS3Service();
        try {
            service.deleteObject(organisationBucket, record.getId());
        } catch (Exception e){
            throw new ConnexienceException("Error deleting S3 data: " + e.getMessage());
        }
    }

    @Override
    public void bulkDelete(String organisationId, ArrayList<String> documentIds) throws ConnexienceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }   
    

    /** Create an S3 service object configured with the access keys */
    private S3Service getS3Service() throws ConnexienceException {
        try {
            AWSCredentials myCredentials =  new AWSCredentials(accessKeyId, accessKey);
            return new RestS3Service(myCredentials);            
        } catch (Exception e){
            throw new ConnexienceException("Error connecting to Amazon S3: " + e.getMessage());
        }
    }
    
    /** Try and create a new S3 Bucket. A ConnexienceException is thrown if this
     * cannot be done, whilst the bucket name will be returned as a string if it
     * can be done.*/
    public String createBucket(String bucketName, String location) throws ConnexienceException {
        S3Service service = getS3Service();
        try {
            S3Bucket bucket = service.createBucket(bucketName, location);
            if(bucket!=null){
                return bucket.getName();
            } else {
                throw new ConnexienceException("Cannot create bucket: " + bucketName);
            }
        } catch (Exception e){
            throw new ConnexienceException("Cannot create bucket: " + e.getMessage());
        }
    } 
    
    /** Set the Amazon ID of the access Key */
    public String getAccessKeyId() {
        return accessKeyId;
    }
    
    /** Get the Amazon ID of the access Key */
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    /** Get the private access Key */
    public String getAccessKey() {
        return accessKey;
    }

    /** Set the private access Key */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /** Get the name of the Amazon bucket that stores the organisation data */
    public String getOrganisationBucket() {
        return organisationBucket;
    }

    /** Set the name of the Amazon bucket that stores the organisation data */
    public void setOrganisationBucket(String organisationBucket) {
        this.organisationBucket = organisationBucket;
    }
}
