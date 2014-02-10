/*
 * JavaScriptAPIServlet.java
 */
package com.connexience.server.api.servlet;

import com.connexience.server.api.util.*;
import com.connexience.server.api.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

import org.json.*;

/**
 * This class provides a servlet that supports the JavaScript API client
 * @author hugo
 */
public class JavaScriptAPIServlet extends HttpServlet {
    /** Largest quantity of data that the servlet will transfer */
    private long maxBytes = 1000000;

    @Override
    public void init() throws ServletException {
        super.init();
        String maxBytesParam = getInitParameter("maxFileSize");
        if(maxBytesParam!=null){
            maxBytes = Long.parseLong(maxBytesParam);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String maxBytesParam = getInitParameter("maxFileSize");
        if(maxBytesParam!=null){
            maxBytes = Long.parseLong(maxBytesParam);
        }        
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter writer = response.getWriter();
        try {
            API api = APIServletUtils.getLink(request);
            if (api == null) {
                api = APIServletUtils.setupLink(request, getServletContext());
            }
            IUser user = api.getUserContext();
            String method = request.getParameter("method");
            if (method.equals("getFolderById")) {
                // Get a folder
                String id = request.getParameter("id");
                if (id != null ){
                    // Get a specific folder
                    IFolder f = api.getFolder(id);  
                    if(f!=null){
                        JSONObject folderJson = createFolderJson(f);
                        folderJson.write(writer);
                    } else {
                        throw new Exception("No such folder id: " + id);
                    }
                } else {
                    throw new Exception("Missing information");
                }
                
            } else if(method.equals("getDocumentById")){
                // Get a document by ID
                String id = request.getParameter("id");
                IDocument doc = api.getDocument(id);
                if(doc!=null){
                    JSONObject docJson = createDocumentJson(doc);
                    docJson.write(writer);
                } else {
                    throw new Exception("No such document id: " + id);
                }
                
            } else if(method.equals("getDocumentAndContentsById")){
                // Get a document an its contents
                String id = request.getParameter("id");
                String versionId = request.getParameter("versionid");
                IDocument doc = api.getDocument(id);
                if(doc!=null){
                    JSONObject docJson = createDocumentJson(doc);
                    InputStream stream;
                    if (versionId == null) {
                        stream = api.getDocumentInputStream(doc);
                    } else {
                        stream = api.getDocumentInputStream(doc, versionId);
                    }

                    if (stream != null) {
                        long totalBytesRead = 0;
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        while (totalBytesRead < maxBytes && (bytesRead = stream.read(buffer)) != -1) {
                            outStream.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                        }
                        stream.close();
                        outStream.flush();
                        outStream.close();
                        docJson.put("content", new String(outStream.toByteArray()));        
                    }
                    docJson.write(writer);

                                
                } else {
                    throw new Exception("No such document id: " + id);
                }                
            }
            
        } catch (Exception e) {
            try {
                JSONObject exJson = createExceptionJson(e);
                exJson.write(writer);
            } catch (Exception ex2) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        try {
            API api = APIServletUtils.getLink(request);
            if (api == null) {
                api = APIServletUtils.setupLink(request, getServletContext());
            }
            IUser user = api.getUserContext();
            String method = request.getParameter("method");
            
            // Get the post data and put it into a JSON object if there
            // is any
            String data = extractString(request.getInputStream());

            JSONObject dataObject;
            if(data!=null && !data.isEmpty()){
                dataObject = new JSONObject(data);
            } else {
                dataObject = new JSONObject();
            }
            
            if (method.equals("saveDocument")) {
                // Save a document
                String id = null;
                if(dataObject.has("id")){
                    id = dataObject.getString("id");
                }
                IDocument doc = null;
                if(id!=null && !id.equals("")){
                    // Existing document
                    doc = api.getDocument(id);
                    doc.setName(dataObject.getString("name"));
                    doc.setDescription(dataObject.getString("description"));
                    doc.setContainerId(dataObject.getString("containerId"));
                    doc = api.saveDocument(doc);
                    
                } else {
                    // New document
                    doc = (IDocument)api.createObject(IDocument.XML_NAME);
                    doc.setName(dataObject.getString("name"));
                    doc.setDescription(dataObject.getString("description"));
                    
                    IFolder parent;
                    if(dataObject.has("containerId")){
                        parent = api.getFolder(dataObject.getString("containerId"));
                    } else {
                        parent = api.getUserFolder(api.getUserContext());
                    }
                    doc = api.saveDocument(parent, doc);
                    
                }
                
                // Set content if there is any
                if(dataObject.has("content")){
                    String content = dataObject.getString("content");
                    ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes());
                    api.upload(doc, stream);
                    stream.close();                    
                }
                
                JSONObject docJson = createDocumentJson(doc);
                docJson.write(writer);
                
            }
            
        } catch (Exception e) {
            try {
                JSONObject exJson = createExceptionJson(e);
                exJson.write(writer);
            } catch (Exception ex2) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        writer.flush();
    }
    
    
    /** Create a JSON Object to represent an Exception */
    private JSONObject createExceptionJson(Exception e) throws Exception {
        JSONObject exceptionJson = new JSONObject();
        exceptionJson.put("class", e.getClass().getSimpleName());
        exceptionJson.put("message", e.getMessage());
        exceptionJson.put("type", "error");
        return exceptionJson;
    }

    /** Create an error JSON object */
    private JSONObject createErrorJson(String message, String errorCategory) throws Exception {
        JSONObject errorJson = new JSONObject();
        errorJson.put("message", message);
        errorJson.put("class", "GeneratedError");
        errorJson.put("type", "error");
        errorJson.put("errorCategory", errorCategory);
        return errorJson;
    }

    /** Create a folder json */
    private JSONObject createFolderJson(IFolder folder) throws Exception {
        JSONObject folderJson = new JSONObject();
        if (folder != null) {
            folderJson.put("id", folder.getId());
            folderJson.put("name", folder.getName());
            folderJson.put("description", folder.getDescription());
            folderJson.put("type", "folder");
        } else {
            throw new Exception("No such folder");
        }
        return folderJson;
    }

    /** Create a document json */
    private JSONObject createDocumentJson(IDocument doc) throws Exception {
        if (doc != null) {
            JSONObject documentJson = new JSONObject();
            documentJson.put("id", doc.getId());
            documentJson.put("name", doc.getName());
            documentJson.put("description", doc.getDescription());
            documentJson.put("type", "document");
            documentJson.put("containerId", doc.getContainerId());
            return documentJson;
        } else {
            throw new Exception("No such document");
        }
    }

    /**
     * Extract a String from an InputStream
     */
    public static String extractString(InputStream stream) throws Exception {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = stream.read(buffer)) != -1) {
            array.write(buffer, 0, len);
        }
        array.flush();
        array.close();
        return new String(array.toByteArray());
    }    
}


