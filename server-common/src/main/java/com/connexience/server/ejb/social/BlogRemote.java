package com.connexience.server.ejb.social;

import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.social.blog.BlogPost;
import com.connexience.server.model.social.blog.Blog;
import com.connexience.server.model.social.blog.Comment;
import com.connexience.server.ConnexienceException;

import javax.ejb.Remote;
import java.util.Collection;
import java.util.List;

@Remote
public interface BlogRemote
{

  /**
   * Create a Blog.  Assigns the authorId to the caller's id
   */
  Blog createBlog(Ticket ticket, String title, String description, String shortName) throws ConnexienceException;
  
  /**
   * Post a new Blog Entry.  The authorId is set to the id of the user invoking it.
   */
  BlogPost createPost(Ticket ticket, String blogId, String title, String body, String category, boolean notification) throws ConnexienceException;

  /**
   *  Create a Post with some summary text
   */
  BlogPost createPost(Ticket ticket, String blogId, String title, String body, String category, String summary, boolean notification) throws ConnexienceException;
  
  /** 
   * Get a BlogPost by ID
   */
  BlogPost getPost(Ticket ticket, String postId) throws ConnexienceException;
  
  /**
   * Create a comment on a blog post
   */
  Comment createComment(Ticket ticket, String postId, String text) throws ConnexienceException;


  Blog getBlogFromUUID(Ticket ticket, String uuid) throws ConnexienceException;


  /**
   * Retrieve an individual blog
   */
  Blog getBlog(Ticket ticket, String blogId) throws ConnexienceException;

  /**
   * Get all the blog posts for the current user.
   */
  List getPosts(Ticket ticket, String blogId, int start, int maxCount) throws ConnexienceException;

  /**
   * Get the categories that a user has used to categorise blog posts
   */
  Collection getCategories(Ticket ticket, String blogId) throws ConnexienceException;

  /**
   * Get the comments attached to a blog post
   */
  List getComments(Ticket ticket, String postId) throws ConnexienceException;

  /** 
   * Update the details of a blog post - throws an exception if the post does not exist
   */
  BlogPost updatePost(Ticket ticket, String postid, String title, String body, String category, boolean notification) throws ConnexienceException;

  /** 
   * Update the details of a blog post - throws an exception if the post does not exist
   */
  BlogPost updatePost(Ticket ticket, String postid, String title, String body, String category, String summary, boolean notification) throws ConnexienceException;

  /**
   * Update the details of a comment - throws an exception if the comment does not exist
   */
  Comment updateComment(Ticket ticket, String commentId, String text) throws ConnexienceException;

  /**
   * Delete a Blog.  Cascades and deletes all the blog posts and comments.
   */
  void deleteBlog(Ticket ticket, String blogId) throws ConnexienceException;

  /**
   * Delete a blog post.  Will also delete all comments attached to the post
   */
  void deletePost(Ticket ticket, String postId) throws ConnexienceException;

  /**
   * Delete a comment
   */
  void deleteComment(Ticket ticket, String commentId) throws ConnexienceException;

  /**
   * Update the details of a blog - throws an exception if that user does not have a blog
   */
  Blog updateBlog(Ticket ticket, String blogId, String title, String description) throws ConnexienceException;

  /**
     * Retrieve an individual blog from an id
     */
  Blog getBlogFromShortName(Ticket ticket, String userId, String shortName) throws ConnexienceException;

  /** Does a blog short name exist */
  boolean shortNameExists(Ticket ticket, String shortName) throws ConnexienceException;

  /*
  * Reset the private URL for a blog to a random UUID
  * */
  Blog resetPrivateURL(Ticket ticket, String blogId) throws ConnexienceException;


  Collection getBlogsWithWriteAccess(Ticket ticket, String userId) throws ConnexienceException;

  /**
   * Get all the blog posts for the current user.
  */
  Long getNumberofPosts(Ticket ticket, String blogId) throws ConnexienceException;

  /**
   * Get a comment
   */
  Comment getComment(Ticket ticket, String commentId) throws ConnexienceException;

  /**
   * Create a comment on a blog post
   */
  Comment createComment(Ticket ticket, String postId, String text, String authorName) throws ConnexienceException;

  /**
   * Get the date of the last post to the blog
   *
   * @param ticket The ticket of the user calling the method
   * @param blogId the id of the blog that contains the posts
   * @return the number date of the last post in the blog as a Long
   * @throws com.connexience.server.ConnexienceException If the blog id is invalid or the ticket doesn't have access
   */
  Long getDateOfLastPost(Ticket ticket, String blogId) throws ConnexienceException;

  Collection<String> getFavouriteBlogIds(Ticket ticket) throws ConnexienceException;

  void addBlogToFavourites(Ticket ticket, Blog b) throws ConnexienceException;

  void removeBlogFromFavourites(Ticket ticket, Blog b) throws ConnexienceException;

  Collection<Blog> getFavouriteBlogs(Ticket ticket, int start, int maxResults) throws ConnexienceException;

  int getNumberOfFavouriteBlogs(Ticket ticket) throws ConnexienceException;
  
  public boolean isBlogPostRead(Ticket ticket, String blogId, String postId) throws ConnexienceException;

  /**
   * Retrieve the blogs that a user can see - the blog object only contains the name etc. not the posts - see BlogPosts.
   */
  Collection getVisibleBlogs(Ticket ticket, String userId, int start, int maxResults) throws ConnexienceException;

  /**
  * Retrieve the blogs that a user can see - the blog object only contains the name etc. not the posts - see BlogPosts.
  */
  Collection getOwnedBlogs(Ticket ticket, String userId, int start, int maxResults) throws ConnexienceException;

  void markPostAsReadorUnread(Ticket ticket, String blogId, String postId, boolean read) throws ConnexienceException;

  void markBlogAsRead(Ticket ticket, String blogId) throws ConnexienceException;

  /**
   * Get a comment
   */
  Long getNumberOfComments(Ticket ticket, String blogPostId);
}
