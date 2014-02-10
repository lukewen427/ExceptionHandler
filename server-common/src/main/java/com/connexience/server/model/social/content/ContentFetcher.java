/*
 * ContentFetcher.java
 */

package com.connexience.server.model.social.content;

import com.connexience.server.*;

import javax.ejb.Remote;
import java.util.List;

/**
 * This interface defines an EJB that can fetch a certain type of content from
 * a data source. The actual configuration is done at the EJB level, this interface
 * just defines high level access to content.
 * @author hugo
 */
@Remote
public interface ContentFetcher {
    /** Is a specific content type supported */
    public boolean typeSupported(ContentType type);
    
    /** Perform a query */
    public List executeQuery(ContentQuery query, int maxResults, int offset) throws ConnexienceException;
}