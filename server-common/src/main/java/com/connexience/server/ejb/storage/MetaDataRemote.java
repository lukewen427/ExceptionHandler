package com.connexience.server.ejb.storage;

import org.w3c.dom.Document;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.document.XMLMetaDataProxy;
import com.connexience.server.ConnexienceException;
import com.connexience.server.util.XPathExpression;
import com.connexience.server.ejb.search.MetaDataSearchResults;

import javax.ejb.Remote;
import java.util.Set;
import java.util.List;
import java.util.Collection;
import java.util.Vector;

/**
 * Author: Simon
 * Date: May 11, 2009
 */
@Remote
public interface MetaDataRemote
{

  public XMLMetaDataProxy addMetaDataToDocument(Ticket ticket, String documentId, String name, Document xmlMetaData) throws ConnexienceException;

  public XMLMetaDataProxy addMetaDataDocumentToDatabase(Ticket ticket, String name, Document xmlMetaData) throws ConnexienceException;

  Collection getDocumentMetaData(Ticket ticket, String documentId) throws ConnexienceException;

  Document getMetaDataFromId(Ticket ticket, String metaDataId) throws ConnexienceException;

  void removeDocumentMetaData(Ticket ticket, String documentId, String metaDataId, boolean deleteMetaDataFromDatabase) throws ConnexienceException;

  Collection getAllMetaDataForUser(Ticket ticket) throws ConnexienceException;

  XMLMetaDataProxy saveXMLMetaDataProxy(Ticket ticket, XMLMetaDataProxy proxy) throws ConnexienceException;

  /*
 * Method to search the metadata using XPATH - e.g. //name/text() = 'simon' will match <name>simon</name>
 * */
  MetaDataSearchResults searchVisibleMetaData(Ticket ticket, String xpath, String operator, String comparison, int start, int maxResults) throws ConnexienceException;

  /*
 * Method to search the metadata using a vector of XPATH expressions
 * */
  MetaDataSearchResults searchVisibleMetaData(Ticket ticket, Vector<XPathExpression> xpaths, int start, int maxResults) throws ConnexienceException;
}
