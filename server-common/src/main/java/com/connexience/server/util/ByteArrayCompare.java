/*
 * ByteArrayCompare.java
 */

package com.connexience.server.util;

/**
 * This class compares whether two byte arrays are identicat
 * @author hugo
 */
public abstract class ByteArrayCompare {
    
    /** Compare the contents of two byte arrays */
    public static boolean isIdentical(byte[] a, byte[] b){
        if(a.length==b.length){
            for(int i=0;i<a.length;i++){
                if(a[i]!=b[i]){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
