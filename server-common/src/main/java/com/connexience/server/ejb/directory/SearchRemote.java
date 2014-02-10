package com.connexience.server.ejb.directory;

import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.social.TagCloudElement;
import com.connexience.server.ConnexienceException;

import javax.ejb.Remote;
import java.util.List;
import java.util.Vector;

/**
 * Author: Simon
 * Date: Jun 11, 2009
 */
@Remote
public interface SearchRemote
{
  List tagSearch(Ticket ticket, String searchText, int start, int maxResults) throws ConnexienceException;

  Integer countTagSearch(Ticket ticket, String searchText) throws ConnexienceException;

  /*
 * Method to get the top n recently created documents
 * */
  List getRecentlyAccessedDocuments(Ticket ticket, int start, int maxResults) throws ConnexienceException;

  List<TagCloudElement> getVisibleTags(Ticket ticket, int start, int maxResults) throws ConnexienceException;

  List freeTextSearch(Ticket ticket, String searchText, Vector<String> advancedOptions, int start, int maxResults) throws ConnexienceException;

  /* FREE TEXT SEARCHING METHODS      */
  /************************************/

  int countFreeTextSearch(Ticket ticket, String searchText, Vector<String> advancedOptions) throws ConnexienceException;
}
