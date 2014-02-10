package com.connexience.server.model.social.blog;

/**
 * Created by IntelliJ IDEA.
 * User: nsjw7
 * Date: Dec 21, 2009
 * Time: 3:56:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BlogPostRead
{
  private String id;

  private String blogId;

  private String postId;

  private String userId;

  public BlogPostRead()
  {
  }

  public BlogPostRead(String blogId, String postId, String userId)
  {
    this.blogId = blogId;
    this.postId = postId;
    this.userId = userId;
  }

  public String getBlogId()
  {
    return blogId;
  }

  public void setBlogId(String blogId)
  {
    this.blogId = blogId;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getPostId()
  {
    return postId;
  }

  public void setPostId(String postId)
  {
    this.postId = postId;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof BlogPostRead)) return false;

    BlogPostRead that = (BlogPostRead) o;

    if (blogId != null ? !blogId.equals(that.blogId) : that.blogId != null) return false;
    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
    if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (blogId != null ? blogId.hashCode() : 0);
    result = 31 * result + (postId != null ? postId.hashCode() : 0);
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    return result;
  }
}
