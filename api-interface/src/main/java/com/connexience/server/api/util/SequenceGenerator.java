/*
 * SequenceGenerator.java
 */

package com.connexience.server.api.util;
import java.io.*;

/**
 * This class provides a simple incrementing counter to provide a call id
 * sequence to avoid replay attacks
 * @author hugo
 */
public class SequenceGenerator implements Serializable {
    /** Current sequence value */
    private long currentValue = 0;

    /** Initialise with the current system time */
    public SequenceGenerator() {
        currentValue = System.nanoTime();
    }

    /** Get the next sequence value */
    public synchronized String nextStringValue(){
        currentValue = System.nanoTime();
        return Long.toString(currentValue);
    }

    /** Get the next long sequence value */
    public synchronized long nextLongValue(){
        currentValue = System.nanoTime();
        return System.nanoTime();
    }

    /** Get the current value as a string */
    public synchronized String currentStringValue(){
        return Long.toString(currentValue);
    }

    /** Get the current value as a long */
    public synchronized long currentLongValue(){
        return currentValue;
    }
}