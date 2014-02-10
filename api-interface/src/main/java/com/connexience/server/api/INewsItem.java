/*
 * INewsItem.java
 */

package com.connexience.server.api;

/**
 * This interface defines a news item that can be posted to a users news feed
 * @author nhgh
 */
public interface INewsItem extends ISecuredObject {
    /** XML Name for object */
    public static final String XML_NAME = "NewsItem";

    /** Get the news item label */
    public String getLabel();

    /** Set the news item label */
    public void setLabel(String label);

    /** Get the timestamp text */
    public String getTimestamp();

    /** Set the timestamp text. NB this is not parsed by the server when posting
     *  new news items, as a date is assigned automatically */
    public void setTimestamp(String timestamp);

    /**
    * Get the id of the main item in this newsItem
    */
    public String getMainItemId();

    /**
    * Set the id of the main item in this news item.  For instance, blogPostId, fileId
    */
    public void setMainItemId(String mainItemId);

}