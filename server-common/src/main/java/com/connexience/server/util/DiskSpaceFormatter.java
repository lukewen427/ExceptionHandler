/*
 * DiskSpaceFormatter.java
 */
package com.connexience.server.util;

import java.text.NumberFormat;

/**
 * This class formats a disk space into a human readable string
 * @author hugo
 */
public class DiskSpaceFormatter {

    /** Size in bytes */
    private long sizeInBytes = 0;
    private NumberFormat format;

    public DiskSpaceFormatter(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
        format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);        
    }
    
    public String format() {
        double storageKb = (double) sizeInBytes / 1024.0;
        String msg;
        if (storageKb <= 0) {
            msg = "0 bytes";
        } else if (storageKb < 1024) {
            // Less than 1MB
            msg = format.format(Math.abs(storageKb)) + " kilobytes";

        } else if (storageKb >= 1024 && storageKb < 1048576) {
            // More than 1MB, but less than 1 GB
            msg = format.format((Math.abs(storageKb) / 1024.0)) + " MB";
        } else {
            // In the gigabytes..
            msg = format.format(Math.abs(storageKb) / 1048576.0) + " GB";
        }
        return msg;
    }
}
