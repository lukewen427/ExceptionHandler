/*
 * ApplicationHttpClient.java
 */

package com.connexience.server.workflow.api;
import com.connexience.server.workflow.api.rpc.*;

import java.net.*;


/**
 * This class provides an Http Client that can send requests to an application
 * via the main website so that requests get signed correctly.
 * @author hugo
 */
public class ApplicationHttpClient {

    /** Base URL of the redirection servlet */
    private URL servletUrl;

    /** API client to get ticket from */
    private RPCClientApi api;

    /** ID of the application being targetted */
    private String appId;

    public ApplicationHttpClient(RPCClientApi api, String appId) throws MalformedURLException {
        String host = api.getServerUrl().getHost();
        int port = api.getServerUrl().getPort();
        servletUrl = new URL("http://" + host + ":" + port + "/WorkflowServer/RedirectServlet;jsessionid=" + api.getClient().getSessionId());
        this.api = api;
        this.appId = appId;
    }

    public HttpURLConnection getConnection(URL url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection)servletUrl.openConnection();
        connection.addRequestProperty("url", url.toString());
        connection.addRequestProperty("appid", appId);
        return connection;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(api!=null){
            api.terminateClient();
            api = null;
        }
    }
}