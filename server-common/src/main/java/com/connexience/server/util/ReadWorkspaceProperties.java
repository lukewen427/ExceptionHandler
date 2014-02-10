/*
 * ReadWorkspaceProperties.java
 */

package com.connexience.server.util;

import com.connexience.server.*;

import java.io.*;
import java.util.zip.*;
import java.util.*;

/**
 * This class tries to read a set of workspace properties from a worksapce
 * file or input stream.
 * @author hugo
 */
public class ReadWorkspaceProperties {
    /** Read Properties from an InputStream */
    public static Properties readProperties(InputStream stream) throws ConnexienceException {
        ZipInputStream zip = new ZipInputStream(stream);
        ZipEntry entry;
        try {
            while((entry = zip.getNextEntry())!=null){
                if(entry.getName().equals("workspace.properties")){
                    // Load the properties
                    Properties props = new Properties();
                    props.load(zip);
                    return props;
                }
            }
        } catch (IOException ioe){
            throw new ConnexienceException("Error reading properties: " + ioe.getMessage());
        }
        
        return null;
    }
}
