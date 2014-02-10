/*
 * ZipUtils.java
 */
package com.connexience.server.util;

import java.io.*;
import java.util.zip.*;
import java.util.*;

/**
 * This class contains various zip file utility methods
 * @author nhgh
 */
public class ZipUtils {

    /** Unzip a file to a specified directory */
    public static void unzip(File file, File targetDirectory) throws Exception {
        Enumeration entries;
        ZipFile zipFile = new ZipFile(file);

        entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();

            if (!entry.getName().startsWith(".")) {
                if (entry.isDirectory()) {
                    // Assume directories are stored parents first then children.
                    (new File(targetDirectory, entry.getName())).mkdirs();
                    continue;
                }

                InputStream inStream = zipFile.getInputStream(entry);
                BufferedOutputStream outStream = null;
                try {
                    outStream = new BufferedOutputStream(new FileOutputStream(new File(targetDirectory, entry.getName())));
                    copyInputStream(inStream, outStream);
                } catch (Exception e) {
                    System.out.println("Zip error: " + e.getMessage());
                } finally {
                    inStream.close();
                    if (outStream != null) {
                        outStream.close();
                    }
                }
            }
        }
        zipFile.close();
    }

    /** Copy the data from one stream to another */
    public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int len;

        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }

    /** Zip the contents of a directory */
    public static void zip(File directory, File zipFile) throws IOException {
        ZipOutputStream zipStream = null;
        try {
            zipDir(directory.getPath(), zipStream);
        } catch (IOException ioe){
            throw ioe;
        } finally {
            try {
                zipStream.flush();
            } catch (Exception e){}

            try {
                zipStream.close();
            } catch (Exception e){}
        }
    }

    /** Zip a directory to a zip output stream */
    public static void zipDir(String dir2zip, ZipOutputStream zos) throws IOException {

        //create a new File object based on the directory we have to zip
        File zipDir = new File(dir2zip);
        //get a listing of the directory content
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        //loop through dirList, and zip the files
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                //if the File object is a directory, call this
                //function again to add its content recursively
                String filePath = f.getPath();
                zipDir(filePath, zos);
                //loop again
                continue;
            }
            //if we reached here, the File object f was not a directory
            //create a FileInputStream on top of f
            FileInputStream fis = new FileInputStream(f);
            // create a    new zipentry
            ZipEntry anEntry = new ZipEntry(f.getPath());
            //place the zip entry in the ZipOutputStream object
            zos.putNextEntry(anEntry);
            //now write the content of the file to the ZipOutputStream
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            //close the Stream
            fis.close();
        }
    }
}
