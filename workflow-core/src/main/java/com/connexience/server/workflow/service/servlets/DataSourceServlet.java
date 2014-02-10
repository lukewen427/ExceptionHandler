/*
 * DataSourceServlet.java
 */

package com.connexience.server.workflow.service.servlets;

import com.connexience.server.workflow.service.*;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet acts as a workflow data source for DataProcessor services
 * that need to access data. It allows download by invocation+context+input id
 * and also uploads of completed data sets.
 * @author hugo
 */
public class DataSourceServlet extends HttpServlet {
    /** Singleton link to a global data source */
    private static DataProcessorDataSource dataSource = null;
    
    /** Set the data source */
    public static void setDataSource(DataProcessorDataSource singletonSource){
        dataSource = singletonSource;
    }
    
    /** Get the global data source */
    public static DataProcessorDataSource getDataSource(){
        return dataSource;
    }
    
    /** Get a set of data from the global data source */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String invocationId = request.getParameter("invocationid");
        String contextId = request.getParameter("contextid");
        String inputName = request.getParameter("inputname");
        
        if(invocationId!=null && contextId!=null && inputName!=null){
            try {
                InputStream dataStream = dataSource.getInputDataStream(invocationId, contextId, inputName);
                OutputStream targetStream = response.getOutputStream();
                
                byte[] buffer = new byte[4096];
                int len;
                while((len=dataStream.read(buffer))!=-1){
                    targetStream.write(buffer, 0, len);
                }
                targetStream.flush();
                targetStream.close();
            } catch(Exception e){
                response.setStatus(404);
            }
            
        } else {
            response.setStatus(404);
        }
    } 

    /** 
    * Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String invocationId = request.getParameter("invocationid");
        String contextId = request.getParameter("contextid");
        String outputName = request.getParameter("outputname");
        String dataType = request.getParameter("datatype");
        if(invocationId!=null && contextId!=null && outputName!=null && dataType!=null){
            try {
                InputStream dataStream = request.getInputStream();
                OutputStream targetStream = dataSource.getOutputDataStream(invocationId, contextId, outputName, dataType);

                byte[] buffer = new byte[4096];
                int len;
                while((len=dataStream.read(buffer))!=-1){
                    targetStream.write(buffer, 0, len);
                }
                targetStream.flush();
                targetStream.close();
            } catch (Exception e){
                response.setStatus(404);
            }
        } else {
            response.setStatus(404);
        }
    }

    /** 
    * Returns a short description of the servlet.
    */
    public String getServletInfo() {
        return "Data source servlet";
    }

}
