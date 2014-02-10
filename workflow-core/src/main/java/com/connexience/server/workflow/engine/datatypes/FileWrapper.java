/*
 * FileWrapper.java
 */

package com.connexience.server.workflow.engine.datatypes;

import com.connexience.server.util.DigestBuilder;
import com.connexience.server.workflow.engine.HashableTransferObject;
import java.io.*;
import java.util.*;
import org.pipeline.core.drawing.*;
import org.apache.log4j.*;

/**
 * This class provides a wrapper for the FileWrapperDataType which allows data
 * to be passed between blocks as raw non-parsed files. File references are stored
 * as a list of names. It is the responsibility of the storage client to
 * translate these names into actual locations.
 * @author nhgh
 */
public class FileWrapper implements TransferData, StorableTransferData, HashableTransferObject {
    private static Logger logger = Logger.getLogger(FileWrapper.class);
    
    /** File reference payload */
    private ArrayList<String> payload = new ArrayList<String>();

    /** Working directory. Used to calculate hash of files */
    private File workingDir;
    
    public FileWrapper() {
    }

    /** Instatiate with a single file */
    public FileWrapper(File file){
        payload.add(file.getName());
    }
    
    /** Set the working directory */
    public void setWorkingDir(File workingDir){
        this.workingDir = workingDir;
    }
    
    /** Construct with a payload */
    public FileWrapper(ArrayList<String> payload) {
        this.payload = payload;
    }

    /** Get a copy of this file wrapper */
    public TransferData getCopy() throws DrawingException {
        try {
            FileWrapper copy = new FileWrapper();
            Iterator<String>files = files();
            while(files.hasNext()){
                copy.addFile(files.next());
            }
            return copy;
        } catch (Exception e){
            throw new DrawingException("Error copying file wrapper: " + e.getMessage());
        }
    }

    /** Get the object contained in this wrapper */
    public Object getPayload() {
        return payload;
    }

    /** Add a file name to this wrapper */
    public void addFile(String name){
        payload.add(name);
    }

    /** Add a file to this wrapper */
    public void addFile(File file){
        payload.add(file.getName());
    }

    /** Get the number of files */
    public int getFileCount(){
        return payload.size();
    }

    /** Get a specific file */
    public String getFile(int index){
        return payload.get(index);
    }

    /** Get an Iterator of all the files */
    public Iterator<String> files(){
        return payload.iterator();
    }

    /** Load the file reference from storage */
    public void loadFromInputStream(InputStream stream) throws DrawingException {
        try {
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(stream));
            payload.clear();
            String line;
            while((line=reader.readLine())!=null){
                payload.add(line);
            }
            reader.close();
        } catch (Exception e){
            throw new DrawingException("Error loading file list in FileWrapper: " + e.getMessage());
        }
    }

    /** Save the list of files to an output stream */
    public void saveToOutputStream(OutputStream stream) throws DrawingException {
        try {
            PrintWriter writer = new PrintWriter(stream);
            Iterator<String> files = payload.iterator();
            while(files.hasNext()){
                writer.println(files.next());
            }
            writer.flush();
            writer.close();
        } catch (Exception e){
            throw new DrawingException("Error saving file list in FileWrapper: " + e.getMessage());
        }
    }

    @Override
    public String getHash() throws DrawingException {
        if(workingDir!=null){
            try {

                DigestBuilder builder = new DigestBuilder();
                Collections.sort(payload);               
                for(String fileName : payload){
                    builder.update(new File(workingDir, fileName));
                }
                return builder.getHash();
            } catch (Exception e){
                logger.error("Error calculating hash: " + e.getMessage(), e);
                throw new DrawingException(e.getMessage(), e);
            }
        } else {
            logger.error("File wrapper needs a working directory in order to calculate file hashes");
            throw new DrawingException("No working directory set for file wrapper");
        }
    }
    
    
    /** Get the size of all of the files referenced in this wrapper */
    public long getTotalFileSize(File workingDirectory){
        long size = 0;
        File f;
        for(int i=0;i<payload.size();i++){
            f = new File(workingDirectory, payload.get(i));
            if(f.exists() && f.isFile()){
                size = size + f.length();
            }
        }
        return size;
    }

    /** Recursively add a directory to the FileWrapper */
    public void addDir(File dir)
    {
        addDir(dir, "");
    }

    private void addDir(File dir, String base)
     {
       FilenameFilter filter = new FilenameFilter()
       {
         public boolean accept(File dir, String name)
         {
           return !name.startsWith(".");
         }
       };


       File[] children = dir.listFiles(filter);
       if (children == null)
       {
         // Either dir does not exist or is not a directory
       }
       else
       {
         for (File file : children)
         {
           if (!file.isDirectory())
           {
             String thisBase;
             if (base.equals(""))
             {
               thisBase = dir.getName();
             }
             else
             {
               thisBase = base + File.separator + dir.getName();
             }

             String fileToAdd = thisBase + File.separator + file.getName();
             System.out.println("Adding: " + fileToAdd);

             payload.add(fileToAdd);
           }
           else
           {
             String thisBase = dir.getName();
             addDir(file, thisBase);
           }
         }
       }
     }

}