/*
 * FilesServlet.java
 */
package com.connexience.server.api.servlet;

import com.connexience.server.api.*;
import com.connexience.server.api.util.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

/**
 * This servlet provides access to files using the API
 * @author hugo
 */
public class DownloadServlet extends HttpServlet {
    private Properties mimeTypes;

    public DownloadServlet() {
        mimeTypes = new Properties();
        try {
            mimeTypes.load(getClass().getResourceAsStream("/mimetypes.properties"));
        } catch (Exception e){
            log("Error loading mime types: " + e.getMessage());
        }
    }
    
    /**
     * Get the file name extension
     */
    private String getExtension(String fileName) {
        int lastDotIdx = fileName.lastIndexOf(".");
        if (lastDotIdx > 0 && lastDotIdx < fileName.length() - 1) {
            return fileName.substring(lastDotIdx + 1).trim();
        }
        return null;
    }
    
    /** Get the mime type for a document */
    private String getDocumentMime(IDocument doc){
        String extension = getExtension(doc.getName());
        if(extension!=null){
            extension = extension.toLowerCase();
            String mimeType = mimeTypes.getProperty(extension);
            if(mimeType!=null){
                return mimeType;
            } else {
                return "text/plain";
            }
        } else {
            return "text/plain";
        }
    }
    
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("documentid");
        if(id!=null){
            try {
                API link = APIServletUtils.setupLink(request, getServletContext());
                IDocument doc = link.getDocument(id);
                if(doc!=null){
                    response.setContentType(getDocumentMime(doc));
                    link.download(doc, response.getOutputStream());
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (Exception e){
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating link");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Document access servlet";
    }
}
