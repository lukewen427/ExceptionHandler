/*
 * HDFSStore.java
 */
package com.connexience.server.model.storage;


import com.connexience.server.ConnexienceException;
import com.connexience.server.model.document.DocumentRecord;
import com.connexience.server.model.document.DocumentVersion;
import com.connexience.server.util.ZipUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;

/**
 * This class provides a driver for the HDFS distributed file system
 * @author hugo
 */
public class HDFSStore extends DataStore {
    /** HDFS name node address */
    private String nameNodeAddress = "localhost";
    
    /** HDFS name node port */
    private int nameNodePort = 9000;
        
    /** Replication value */
    private int replication = 1;

    public HDFSStore() {
        bulkDeleteSupported = true;
        directAccessSupported = true;
        spaceReportingSupported = false;
        sizeLimited = false;
    }

    public String getNameNodeAddress() {
        return nameNodeAddress;
    }

    public void setNameNodeAddress(String nameNodeAddress) {
        this.nameNodeAddress = nameNodeAddress;
    }

    public int getNameNodePort() {
        return nameNodePort;
    }

    public void setNameNodePort(int nameNodePort) {
        this.nameNodePort = nameNodePort;
    }

    public void setReplication(int replication) {
        this.replication = replication;
    }

    public int getReplication() {
        return replication;
    }
    
    /** Create a path for a document */
    private Path getPathForDocument(DocumentRecord record, FileSystem fs) throws Exception {
        String subdir = record.getId().substring(record.getId().length() - 2);
        String dir = "/" + record.getOrganisationId() + "/" + subdir + "/" + record.getId();
        Path p = new Path(dir);
        if(!fs.exists(p)){
           fs.mkdirs(p);
        }
        return p;
    }
    
    /** Get a filesystem connection */
    private FileSystem getFileSystem() throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://" + nameNodeAddress + ":" + nameNodePort);
        conf.set("dfs.replication", Integer.toString(replication));
        return FileSystem.get(conf);        
    }
    
    @Override
    public void bulkDelete(String organisationId, ArrayList<String> documentIds) throws ConnexienceException {
        try {
            FileSystem fs = getFileSystem();
            DocumentRecord doc = new DocumentRecord();
            doc.setOrganisationId(organisationId);
            Path p;
            for(int i=0;i<documentIds.size();i++){
                doc.setId(documentIds.get(i));
                p = getPathForDocument(doc, fs);
                fs.delete(p, true);
            }
        } catch (Exception e){
            throw new ConnexienceException("Error doing bulk delete: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream getInputStream(DocumentRecord document, DocumentVersion version) throws ConnexienceException {
        try {
            FileSystem fs = getFileSystem();
            Path dir = getPathForDocument(document, fs);
            Path docPath = new Path(dir, version.getId());
            System.out.println(docPath);
            if(fs.exists(docPath)){
                return fs.open(docPath);
            } else {
                throw new Exception("No storage file");
            }
        } catch (Exception e){
            throw new ConnexienceException("Error getting input stream for document: " + e.getMessage(), e);
        }
    }

    @Override
    public DocumentVersion readFromFile(DocumentRecord document, DocumentVersion version, File file) throws ConnexienceException {
        InputStream inStream = null;
        try {
            FileSystem fs = getFileSystem();
            Path dir = getPathForDocument(document, fs);
            Path docPath = new Path(dir, version.getId());
            if(fs.exists(dir)){
                OutputStream outStream = fs.create(docPath);
                inStream = new FileInputStream(file);
                ZipUtils.copyInputStream(inStream, outStream);
                outStream.flush();
                outStream.close();
                version.setSize(file.length());
                return version;
            } else {
                throw new Exception("No storage file");
            }
        } catch (Exception e){
            throw new ConnexienceException("Error getting input stream for document: " + e.getMessage(), e);
        } finally {
            if(inStream!=null){
                try {
                    inStream.close();
                } catch (Exception ex){}
            }
        }
    }

    @Override
    public DocumentVersion readFromStream(DocumentRecord document, DocumentVersion version, InputStream stream) throws ConnexienceException {
        try {
            FileSystem fs = getFileSystem();
            Path dir = getPathForDocument(document, fs);
            Path docPath = new Path(dir, version.getId());
            if(fs.exists(dir)){
                OutputStream outStream = fs.create(docPath);
                byte[] buffer = new byte[4096];
                int len;
                long fileLen = 0;
                while((len=stream.read(buffer))>0){
                    fileLen = fileLen + len;
                    outStream.write(buffer, 0, len);
                }
                outStream.flush();
                outStream.close();
                version.setSize(fileLen);
                return version;
            } else {
                throw new Exception("No storage file");
            }
        } catch (Exception e){
            throw new ConnexienceException("Error getting input stream for document: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeRecord(DocumentRecord document, DocumentVersion version) throws ConnexienceException {
        InputStream inStream = null;
        try {
            FileSystem fs = getFileSystem();
            Path dir = getPathForDocument(document, fs);
            Path docPath = new Path(dir, version.getId());
            if(fs.exists(docPath)){
                fs.delete(docPath, true);
            } else {
                throw new Exception("No storage file");
            }
        } catch (Exception e){
            throw new ConnexienceException("Error getting input stream for document: " + e.getMessage(), e);
        }
    }

    @Override
    public void writeToStream(DocumentRecord document, DocumentVersion version, OutputStream stream) throws ConnexienceException {
        try {
            FileSystem fs = getFileSystem();
            Path dir = getPathForDocument(document, fs);
            Path docPath = new Path(dir, version.getId());
            if(fs.exists(docPath)){
                InputStream inStream = fs.open(docPath);
                ZipUtils.copyInputStream(inStream, stream);
                stream.flush();
                inStream.close();
            } else {
                throw new Exception("No storage file");
            }
        } catch (Exception e){
            throw new ConnexienceException("Error getting input stream for document: " + e.getMessage(), e);
        }
    }
}