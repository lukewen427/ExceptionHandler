/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.connexience.server.workflow;

import org.pipeline.core.data.*;
import org.pipeline.core.data.cxd.*;
import org.pipeline.core.data.columns.*;

import java.awt.Color;
import java.io.*;
import java.util.zip.*;
import java.util.*;

/**
 *
 * @author hugo
 */
public class DataTest {
    static {
        ColumnFactory.registerColumnType(new ColumnTypeInfo("date-column", "Date", Date.class, DateColumn.class));
        ColumnFactory.registerColumnType(new ColumnTypeInfo("double-column", "Double", Double.class, DoubleColumn.class));
        ColumnFactory.registerColumnType(new ColumnTypeInfo("integer-column", "Integer", Long.class, IntegerColumn.class));
        ColumnFactory.registerColumnType(new ColumnTypeInfo("string-column", "Text", String.class, StringColumn.class, Color.WHITE, Color.BLUE));
        
              
    }
    public static void main(String[] args){
        try {
            File file = new File("/Users/hugo/data.cxd");
            FileInputStream stream = new FileInputStream(file);
            CxdFile cxd = new CxdFile(file);
            Data data = cxd.load(stream);
            System.out.println("Data columns: " + data.getColumns());
            
            /*
            ByteArrayOutputStream bufferStream;
            
            ZipInputStream zipStream = new ZipInputStream(stream);
            
            ZipEntry entry;
            while((entry=zipStream.getNextEntry())!=null){
                bufferStream = new ByteArrayOutputStream(4096);
                
                while(zipStream.available()==1){
                    // Read data
                    bufferStream.write(zipStream.read());
                }

                zipStream.closeEntry();
                
            }
             */
        } catch (Exception e){
            e.printStackTrace();
        }
        
        
    }
}
