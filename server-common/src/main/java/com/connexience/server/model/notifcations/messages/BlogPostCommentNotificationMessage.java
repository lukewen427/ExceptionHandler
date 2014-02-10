package com.connexience.server.model.notifcations.messages;

import com.connexience.server.ejb.util.EJBLocator;
import com.connexience.server.model.security.User;
import com.connexience.server.model.social.blog.Comment;
import com.connexience.server.model.social.blog.BlogPost;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.ConnexienceException;

/**
 * Created by IntelliJ IDEA.
 * User: martyn
 * Date: 18-Nov-2009
 * Time: 14:50:01
 * To change this template use File | Settings | File Templates.
 */
public class BlogPostCommentNotificationMessage extends NotificationMessage
{
    private Comment comment;

    public BlogPostCommentNotificationMessage()
    {
    }
    
    public BlogPostCommentNotificationMessage(Comment comment, Ticket ticket)
    {
        this.comment = comment;
        setDescription("Post Comment");
        setIsRead(false);
        setMessage(generateMessage(ticket, false));
    }

    public BlogPostCommentNotificationMessage(Comment comment, Ticket ticket, boolean isComment)
    {
        this.comment = comment;
        setDescription("Post Comment");
        setIsRead(false);
        setMessage(generateMessage(ticket, isComment));
    }

    public Comment getComment()
    {
        return comment;
    }

    public void setComment(Comment comment)
    {
        this.comment = comment;
    }

    public String generateMessage(Ticket ticket, boolean isCommentedOn)
    {
        try
        {
            BlogPost blogPost = EJBLocator.lookupBlogBean().getPost(ticket, comment.getPostId());
          User author = EJBLocator.lookupUserDirectoryBean().getUser(ticket, blogPost.getCreatorId());
          User commenter = EJBLocator.lookupUserDirectoryBean().getUser(ticket, comment.getCreatorId());
          String defaultUserId = EJBLocator.lookupOrganisationDirectoryBean().getDefaultOrganisation(ticket).getDefaultUserId();
          String authorUrl = "";
          if(commenter.getId().equals(defaultUserId))
          {
            authorUrl = "A Public User";
          }
                       else
          {
            authorUrl = "<a href=\"../secure/profile.jsp?id=" + commenter.getId() + "\">" + commenter.getDisplayName() + "</a>";
          }
            String blogUrl = "<a href=\"../secure/viewpost.jsp?id=" + blogPost.getId() + "\">" + blogPost.getTitle() + "</a>";

            //is the notification a comment on a comment?
            if(isCommentedOn)
            {
              return authorUrl + " also commented on " + author.getDisplayName() + "'s Blog Post: " + blogUrl;
            }
            else
            {
              return authorUrl + " commented on your Blog Post: " + blogUrl;
            }
        }
        catch(ConnexienceException e)
        {
            e.toString();
            return "";
        }
    }
}