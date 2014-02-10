/*
 * ApplicationServlet.java
 */

package com.connexience.server.api.servlet;

import com.connexience.server.api.*;
import com.connexience.server.api.util.*;
import com.connexience.server.api.impl.*;
import com.connexience.client.api.impl.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.net.*;

/**
 * This class provides a standard application servlet that is used as the
 * entry point for the inkspot server to manage application lifecycle.
 * @author nhgh
 */
public class ApplicationServlet extends HttpServlet {
    /** Register default types */
    static {
        InkspotTypeRegistration.register();
    }

    /** Application private key. This is read from the web.xml file as a deployment property */
    private String privateKey;

    /** ID of the application */
    private String applicationId;

    /** URL of the server to send requests back to */
    private String serverUrl;

    /** API Factory */
    private APIFactory factory = new APIFactory();

    /** Listener object that provides the application functionality */
    private ApplicationServletListener listener = null;

    /** Set up the API factory to provide the standard Http client */
    public ApplicationServlet() {
        factory.setApiClass(HttpClientAPI.class);
    }

    @Override
    public void init() throws ServletException {
        super.init();

        ServletContext context = getServletContext();

        // Set up the application ID */
        if(context.getInitParameter("appid")!=null){
            applicationId = context.getInitParameter("appid").trim();
            System.out.println("ApplicationID: " + applicationId);
        } else {
            applicationId = "";
            System.out.println("*** NO APPLICATION ID SET ***");
        }

        // Server URL to connect back to
        if(context.getInitParameter("serverUrl")!=null){
            serverUrl = context.getInitParameter("serverUrl").trim();
            System.out.println("ServerURL: " + serverUrl);
        } else {
            serverUrl = "";
            System.out.println("*** NO SERVER URL SET ***");
        }

        // Set up the private key */
        if(context.getInitParameter("key")!=null){
            privateKey = context.getInitParameter("key").trim();
            System.out.println("Key: " + privateKey);
        } else {
            privateKey = "";
            System.out.println("*** NO PRIVATE KEY SET ***");
        }

    }

    /** Set the listener */
    public void setListener(ApplicationServletListener listener){
        this.listener = listener;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        
        if(validateRequest(request)){
            HttpSession session = request.getSession(true);

            try {
                Hashtable<String,String> properties = extractHeaderData(request);
                String userId = properties.get("userid");

                API link = factory.authenticateApplication(new URL(serverUrl), applicationId, privateKey);
                ((APIControl)link).setUserContextId(userId);
                
                session.setAttribute("LINKOBJECT", link);

                // Get Make sure that there is an application session setup
                ApplicationSession appSession = ApplicationSessionManager.GLOBAL_INSTANCE.getSession(request);
                if(appSession.getLink()==null){
                    appSession.setLink(link);
                }

                String[] sections = APIServletUtils.splitRequestPath(request.getPathInfo());

                if(sections.length>0){
                    String action = sections[0];
                    if(action.equalsIgnoreCase("summary")){
                        if(listener!=null){
                            listener.renderSummary(request, response, session.getId(), link, userId);
                        }

                    }
                }

            } catch (Exception ie){

                try {
                    OutputStream stream = response.getOutputStream();
                    ObjectBuilder.writeObjectToStream(APIServletUtils.createErrorMessage(ie), stream);
                    stream.flush();
                } catch (Exception e){}
            }

        } else {
            // Invalid request
            try {
                OutputStream stream = response.getOutputStream();
                PrintWriter writer = new PrintWriter(stream);
                writer.println("Security Error fetching application data");
                writer.flush();
                writer.close();
                stream.flush();
            } catch (Exception e){

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /** Validate a signed request object */
    public boolean validateRequest(HttpServletRequest request){
        Hashtable<String,String> parameters = extractHeaderData(request);
        String standardProperties = APISecurity.standardiseHashtable(parameters);
        try {
            if(request.getHeader("signature")!=null){
                String signature = request.getHeader("signature");
                String calculatedSignature = APISecurity.signString(standardProperties, privateKey);
                if(calculatedSignature.equals(signature)){
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }

    /** Construct a hashtable with all the request header data */
    public Hashtable<String,String> extractHeaderData(HttpServletRequest request){
        Hashtable<String,String> results = new Hashtable<String,String>();
        ArrayList<String> keys = new ArrayList<String>();
        keys.add("sequence");
        keys.add("applicationid");
        keys.add("userid");
        Collections.sort(keys);
        Iterator e = keys.iterator();
        String headerName;
        String value;
        while(e.hasNext()) {
            headerName = e.next().toString();
            value = request.getHeader(headerName);
            results.put(headerName, value);
        }
        return results;
    }
}