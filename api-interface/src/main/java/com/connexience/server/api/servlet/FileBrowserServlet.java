/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * This servlet provides API based file browser support
 * @author hugo
 */
public class FileBrowserServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        API api = APIServletUtils.getLink(request);
        PrintWriter writer = response.getWriter();
        try {
            String id = request.getParameter("root");
            IUser user = api.getUserContext();
            List<IObject> contents;
            IFolder folder;
            IObject obj;
            IDocument doc;
            JSONObject childJson;
            JSONArray childrenJson;
            boolean homeFolder;

            // Get the correct folder
            if(id.equalsIgnoreCase("source")){
                folder = api.getUserFolder(user);
                homeFolder = true;
            } else {
                folder = api.getFolder(id);
                homeFolder = false;
            }

            String parentFolderId = folder.getId();

            if(folder!=null){
                childrenJson = new JSONArray();

                if(homeFolder){
                    // Just put in top folder
                    childJson = new JSONObject();
                    childJson.put("text", folder.getName());
                    childJson.put("id", folder.getId());
                    childJson.put("containerId", folder.getId());
                    childJson.put("hasChildren", true);
                    childJson.put("classes", "folder");
                    childJson.put("isFile", false);
                    childrenJson.put(childJson);
                    
                } else {
                    // Otherwise list contents
                    contents = api.getFolderContents(folder);
                    for(int i=0;i<contents.size();i++){
                        obj = contents.get(i);
                        childJson = new JSONObject();

                        if(obj instanceof IFolder){
                            // Child folder
                            folder = (IFolder)obj;

                            // Don't add the parent folder
                            if(!folder.getId().equals(parentFolderId)){
                                childJson.put("text", folder.getName());
                                childJson.put("id", folder.getId());
                                childJson.put("containerId", folder.getContainerId());
                                childJson.put("hasChildren", true);
                                childJson.put("classes", "folder");
                                childJson.put("isFile", false);
                                childrenJson.put(childJson);
                            }

                        } else {
                            // Document
                            doc = (IDocument)obj;
                            childJson.put("text", doc.getName());
                            childJson.put("id", doc.getId());
                            childJson.put("containerId", doc.getContainerId());
                            childrenJson.put(childJson);
                            childJson.put("classes", "file");
                            childJson.put("isFile", true);
                        }

                    }
                }

                childrenJson.write(writer);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        writer.flush();
    } 


    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "File Browser Servlet";
    }// </editor-fold>
}