/*
 * DownloaderInputStream.java
 */
package com.connexience.server.workflow.api.rpc.downloaders;

import com.connexience.server.api.APIConnectException;
import java.io.*;

/**
 * This class extends an input stream and contains the downloader object itself
 * @author hugo
 */
public class DownloaderInputStream extends BufferedInputStream {
    private HttpDownloader downloader;
    public DownloaderInputStream(HttpDownloader downloader, InputStream stream) throws APIConnectException {
        super(stream);
        this.downloader = downloader;
    }
}
