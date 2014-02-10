/*
 * XMLDataExtractor.java
 */

package com.connexience.server.model.workflow;

import com.connexience.server.*;

import java.io.*;
import java.util.zip.*;
import java.util.*;

/**
 * This class extracts a set of XML data files from a zip stream containing a
 * workflow library item
 * @author nhgh
 */
public class XMLDataExtractor {
    /** List of files to extract */
    private ArrayList<String> fileList;

    /** Extracted data */
    private Hashtable<String,String> extractedData = new Hashtable<String,String>();

    /** Construct with a list */
    public XMLDataExtractor(String[] files) {
        fileList = new ArrayList<String>();
        for(int i=0;i<files.length;i++){
            fileList.add(files[i]);
        }
    }

    /** Extract the XML data from stream */
    public void extractXmlData(InputStream serviceArchiveStream) throws ConnexienceException {
        try {
            ZipInputStream zipStream = new ZipInputStream(serviceArchiveStream);
            ZipEntry entry;
            while((entry = zipStream.getNextEntry())!=null){
                if(fileList.contains(entry.getName())){
                    // This is the XML file
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    byte[] data = new byte[4095];
                    int len;
                    while((len = zipStream.read(data))>0){
                        buffer.write(data, 0, len);
                    }
                    extractedData.put(entry.getName(), buffer.toString());
                }
            }

        } catch (Exception e){
            throw new ConnexienceException("Cannot extract service xml: + " + e.getMessage());
        }
    }
    
    /** Does the extracted data contain all of the specified files */
    public boolean allDataPresent(){
        for(int i=0;i<fileList.size();i++){
            if(!extractedData.containsKey(fileList.get(i))){
                return false;
            }
        }
        return true;
    }

    /** Is a specific entry present */
    public boolean entryPresent(String name){
        return extractedData.containsKey(name);
    }

    /** Get an extracted xml string */
    public String getEntry(String name){
        return extractedData.get(name);
    }
}
