package com.connexience.server.ejb.notifications;

import com.connexience.server.model.messages.Message;
import com.connexience.server.model.notifcations.Notification;
import com.connexience.server.ConnexienceException;

import javax.ejb.Remote;
import java.net.ConnectException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: martyn
 * Date: 16-Nov-2009
 * Time: 15:48:12
 * To change this template use File | Settings | File Templates.
 */

@Remote
public interface NotificationsRemote
{
    //Notification Keys
    public static final String EMAIL_ON_MESSAGE_RECIEVE = "sendEmailOnMessageRecieve";

    public static final String EMAIL_ON_WORKFLOW_COMPLETION = "sendEmailOnMessageRecieve";

    public static final String MESSAGE_ON_WORKFLOW_COMPLETION = "sendEmailOnMessageRecieve";

    public static final String EMAIL_ON_BLOG_POST_COMMENT = "sendEmailOnBlogPostComment";

    public static final String MESSAGE_ON_BLOG_POST_COMMENT = "sendMessageOnBlogPostComment";

    public static final String EMAIL_ON_BLOG_POST_COMMENT_COMMENT = "sendEmailOnBlogPostCommentComment";

    public static final String MESSAGE_ON_BLOG_POST_COMMENT_COMMENT = "sendMessageOnBlogPostCommentComment";
    
    void sendNotification(Notification notification) throws ConnexienceException;

    boolean getNotificationValue(String notificationKey, String userId) throws ConnexienceException;

    ArrayList<String> getNotificationKeys();
}
