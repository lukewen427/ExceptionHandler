/*
 * ApplicationDataFetcher.java
 */

package com.connexience.server.api.servlet;

import com.connexience.server.api.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class acts as a proxy for the web server to fetch data from an external
 * application and render it into an output stream.
 * @author nhgh
 */
public class ApplicationDataFetcher {
    /** Buffer size for fetching data */
    private int bufferSize = 4096;

    /** Application URL */
    private String applicationUrl;

    /** Application ID */
    private String applicationId;

    /** Http client used for fetching data */
    HttpClient client = new HttpClient();

    public ApplicationDataFetcher(String applicationUrl, String applicationId) {
        this.applicationUrl = applicationUrl;
        this.applicationId = applicationId;
    }

    /** Fetch the summary html using the http client object */
    public void fetchSummary(OutputStream stream, String userId, String sequence, String signature) throws ApplicationException {
        InputStream appStream = null;
        GetMethod method = null;
        try {
            method = getMethod(applicationUrl, "/summary", userId, applicationId, sequence, signature);
            int status = client.executeMethod(method);

            if(status!=HttpStatus.SC_OK){
                throw new Exception("HTTP Error");
            }

            appStream = method.getResponseBodyAsStream();
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while((len=appStream.read(buffer))>0){
                stream.write(buffer, 0, len);
            }
            
        } catch (Exception e){
            throw new ApplicationException("Error fetching summary: "+ e.getMessage());
        } finally {
            try {stream.flush();}catch(Exception e){}
            try {appStream.close();}catch(Exception e){}
            try {method.releaseConnection();}catch(Exception e){}
        }
    }

    /** Build a property list that will form request parameters */
    public Map<String, List<String>> createSignableMap(String userId, String sequence) {
        Map<String, List<String>> data = new HashMap<String, List<String>>();

        data.put("userid", new ArrayList<String>());
        data.get("userid").add(userId);

        data.put("applicationid", new ArrayList<String>());
        data.get("applicationid").add(applicationId);

        data.put("sequence", new ArrayList<String>());
        data.get("sequence").add(sequence);
        return data;
    }

    /** Set up a connection object with properties and a signature */
    private HttpURLConnection getConnection(String applicationUrl, String method, String userId, String applicationId, String sequence, String signature) throws Exception {
        URL methodUrl = new URL(applicationUrl + method);
        HttpURLConnection connection = (HttpURLConnection)methodUrl.openConnection();
        connection.setRequestProperty("userid", userId);
        connection.setRequestProperty("sequence", sequence);
        connection.setRequestProperty("applicationid", applicationId);
        connection.setRequestProperty("signature", signature);
        return connection;
    }

    /** Create a signed GET method */
    private GetMethod getMethod(String applicationUrl, String method, String userId, String applicationId, String sequence, String signature) throws Exception {
        GetMethod methodObject = new GetMethod(applicationUrl + method);
        methodObject.setRequestHeader("userid", userId);
        methodObject.setRequestHeader("sequence", sequence);
        methodObject.setRequestHeader("applicationid", applicationId);
        methodObject.setRequestHeader("signature", signature);
        return methodObject;
    }
}