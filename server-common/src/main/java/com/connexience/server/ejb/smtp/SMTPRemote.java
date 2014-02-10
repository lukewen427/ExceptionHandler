package com.connexience.server.ejb.smtp;

import com.connexience.server.ConnexienceException;
import com.connexience.server.model.social.blog.Comment;
import com.connexience.server.model.security.Ticket;
import com.connexience.server.model.messages.TextMessage;

import javax.mail.internet.InternetAddress;
import javax.ejb.Remote;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: martyn
 * Date: 18-Nov-2009
 * Time: 15:03:39
 * To change this template use File | Settings | File Templates.
 */
@Remote
public interface SMTPRemote
{
    void sendMail(List<String> userIds, String subject, String content, String contentType) throws ConnexienceException;

    void sendMail(InternetAddress[] recipients, String subject, String content, String contentType) throws ConnexienceException;

    void sendCommentOnBlogPostEmail(Ticket ticket, Comment comment) throws ConnexienceException;

    void sendCommentOnSameBlogPostEmail(Ticket ticket, String userId, Comment comment) throws ConnexienceException;

    void sendTextMessageReceivedEmail(Ticket ticket, String userId, TextMessage textMessage) throws ConnexienceException;
}
