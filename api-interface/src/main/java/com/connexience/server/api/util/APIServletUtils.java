/*
 * APIServletUtils.java
 */

package com.connexience.server.api.util;

import com.connexience.server.api.impl.*;
import com.connexience.server.api.*;

import javax.servlet.http.*;
import java.util.*;
import java.text.*;
import javax.servlet.*;
import java.net.*;
import java.security.cert.*;
import java.io.*;

/**
 * This class contains utility methods for handling API servlet security
 * and parameter parsing
 * @author nhgh
 */
public class APIServletUtils {
    /** Date formatter */
    private static DateFormat dateFormatter = DateFormat.getDateTimeInstance();

    /** Register default types */
    static {
        InkspotTypeRegistration.register();
    }

    /** Default factory */
    private static APIFactory factory = new APIFactory();
    static{
        factory.setApiClass(com.connexience.client.api.impl.HttpClientAPI.class);
    }

    /** Parse the URL into blocks */
    public static String[] splitRequestPath(String path){
        if(path.startsWith("/")){
            // Split leading /
            path = path.substring(1);
        }
        return path.split("/");
    }

    /** Get the User ID from the divided path info */
    public static String getUserId(String[] pathInfo){
        return pathInfo[0];
    }

    /** Get the Action subsection information from the divided path info */
    public static String getUserSection(String[] pathInfo){
        return pathInfo[1];
    }

    /** Get the application ID from the request object */
    public static String getApplicationId(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        if(session.getAttribute("APPLICATION_ID")!=null){
            return session.getAttribute("APPLICATION_ID").toString();
        } else {
            return null;
        }
    }

    /** Set up the link object in the session */
    public static API setupLink(HttpServletRequest request, ServletContext context) throws APISecurityException {
        Hashtable<String,String> params = extractURLParameters(request);
        HttpSession session = request.getSession(true);
        if(session.getAttribute("APILINK") instanceof API){
            return (API)session.getAttribute("APILINK");
        } else {
            if(validateURLRequest(request, context)){
            // Set up the application ID */
                String applicationId = context.getInitParameter("appid").trim();
                String serverUrl = context.getInitParameter("serverUrl").trim();
                String privateKey = context.getInitParameter("key").trim();
                String userId = request.getParameter("userid");
                try {
                    API link = factory.authenticateApplication(new URL(serverUrl), applicationId, privateKey);
                    ((APIControl)link).setUserContextId(userId);
                    session.setAttribute("APILINK", link);
                    return link;
                } catch (Exception e){
                    System.out.println("Error setting up link: " + e.getMessage());
                    return null;
                }

            } else {
                throw new APISecurityException("Signature error creating link");
            }
        }
    }

    /** Get the link object from the session if there is one */
    public static API getLink(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        if(session.getAttribute("APILINK")!=null){
            return (API)session.getAttribute("APILINK");
        } else {
            return null;
        }
    }

    /** Extract the application side parameters from a request URL */
    public static Hashtable<String,String> extractURLParameters(HttpServletRequest request){
        Hashtable<String,String> results = new Hashtable<String,String>();

        if(request.getParameter("contextid")!=null){
            results.put("contextid", request.getParameter("contextid"));
        } else {
            results.put("contextid", "");
        }

        if(request.getParameter("userid")!=null){
            results.put("userid", request.getParameter("userid"));
        } else {
            results.put("userid", "");
        }

        if(request.getParameter("applicationid")!=null){
            results.put("applicationid", request.getParameter("applicationid"));
        } else {
            results.put("applicationid", "");
        }

        if(request.getParameter("sequence")!=null){
            results.put("sequence", request.getParameter("sequence"));
        } else {
            results.put("sequence", "");
        }

        if(request.getParameter("contextroot")!=null){
            results.put("contextroot", request.getParameter("contextroot"));
        } else {
            results.put("contextroot", "");
        }
        return results;
    }

    /** Validate a request signature */
    public static boolean validateURLRequest(HttpServletRequest request, ServletContext context){
        String configAppId = context.getInitParameter("appid");
        String configKey = context.getInitParameter("key");
        
        // Get and standardise the parameters
        Hashtable<String,String> parameters = extractURLParameters(request);
        String standardProperties = APISecurity.standardiseHashtable(parameters);
        System.out.println("Standard properties: " + standardProperties);
        
        // Check signature
        if(parameters.get("applicationid").equals(configAppId)){
            if(request.getParameter("signature")!=null){

                // Replace spaces with +'s. TODO: Needs to be fixed properly
                String signature = request.getParameter("signature").replace(' ', '+');
                
                try {
                    String calculatedSignature = APISecurity.signString(standardProperties, configKey);
                    if(calculatedSignature.equals(signature)){
                        return true;
                    } else {
                        System.out.println("validateURLRequest: Signature present but incorrect: URLsig: " + signature + " expected: " + calculatedSignature);
                        return false;
                    }
                } catch (Exception e){
                    System.out.println("validateURLRequest: Signature Exception: " + e.getMessage());
                    e.printStackTrace();
                    return false; // Signature error
                }
            } else {
                System.out.println("validateURLRequest: No signature found");
                return false; // No signature provided
            }
        } else {
            System.out.println("validateURLRequest: Application ID does not match");
            return false; // Application ID does not match
        }
    }

    /** Construct a hashtable with all the request header data */
    public static Hashtable<String,String> extractHeaderData(HttpServletRequest request){
        Hashtable<String,String> results = new Hashtable<String,String>();
        ArrayList<String> keys = new ArrayList<String>();
        keys.add("sequenceid");
        keys.add("apiid");
        keys.add("methodcall");
        keys.add("contentsignature");
        Collections.sort(keys);
        Iterator e = keys.iterator();
        String headerName;
        String value;
        while(e.hasNext()) {
            headerName = e.next().toString();
            value = request.getHeader(headerName);
            results.put(headerName.toLowerCase(), value);
        }        
        return results;
    }

    /**
     * Extract a String from an InputStream
     */
    public static String extractString(InputStream stream) throws Exception {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        boolean finished = false;
        int value;

        while (finished == false) {
            value = stream.read();
            if (value == -1) {
                finished = true;
            } else {
                array.write(value);
            }
        }
        return new String(array.toByteArray());
    }

    /** Create an error message */
    public static IAPIErrorMessage createErrorMessage(Exception e) throws APIInstantiationException {
        IAPIErrorMessage imessageObj = (IAPIErrorMessage)ObjectBuilder.instantiateObject(IAPIErrorMessage.XML_NAME);
        imessageObj.setErrorMessage(e.getMessage());
        return imessageObj;
    }

    /** Create an error message */
    public static IAPIErrorMessage createErrorMessage(String message) throws APIInstantiationException {
        IAPIErrorMessage imessageObj = (IAPIErrorMessage)ObjectBuilder.instantiateObject(IAPIErrorMessage.XML_NAME);
        imessageObj.setErrorMessage(message);
        return imessageObj;
    }
    
    /** Create some javascript that sets up the static script loader from an API link */
    public static String generateLoaderScript(API link) {
        String host = link.getServerUrl().getHost();
        int port = link.getServerUrl().getPort();
        String protocol = link.getServerUrl().getProtocol();
        String staticUrl = protocol + "://" + host + ":" + port + "/static";
        StringBuilder builder = new StringBuilder();
        builder.append("<script type=\"text/javascript\"> src=\"" + staticUrl + "/scripts/util/jquery.rloader1_1.min.js\"></script>");
        
       
        return builder.toString();
    }
}
