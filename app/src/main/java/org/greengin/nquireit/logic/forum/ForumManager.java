package org.greengin.nquireit.logic.forum;

import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.entities.rating.ForumThread;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.rating.CommentRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by evilfer on 6/26/14.
 */
public class ForumManager extends AbstractContentManager {

    static final String ROOT_NODE = "SELECT f FROM ForumNode f WHERE f.parent IS NULL";

    public ForumManager(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }

    public ForumManager(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }



    /* view actions */

    public ForumThread getThread(Long id) {
        return context.getForumDao().findThread(id);
    }

    public ForumNode getRoot() {
        return context.getForumDao().findRoot();
    }

    public ForumNode getNode(Long forumId) {
        return context.getForumDao().findForum(forumId);
    }

    /* admin actions */
    public ForumNode createForum(Long containerId, ForumRequest forumData) {

        if (loggedWithToken && user.isAdmin()) {
            context.getForumDao().createForum(containerId, forumData);
        }

        return getRoot();
    }

    public ForumNode updateForum(Long forumId, ForumRequest forumData) {
        if (loggedWithToken && user.isAdmin()) {
            context.getForumDao().updateForum(forumId, forumData);
        }

        return getRoot();
    }


    public Long createThread(Long forumId, ForumRequest forumData) {
        if (loggedWithToken) {
            ForumThread thread = context.getForumDao().createThread(user, forumId, forumData);
            if (thread != null) {
                return thread.getId();
            }
        }

        return null;
    }

    public ForumThread comment(Long threadId, CommentRequest data) {
        if (loggedWithToken) {
            ForumThread thread = context.getForumDao().findThread(threadId);
            context.getCommentsDao().comment(user, thread, data);
            return thread;
        }

        return null;
    }
}
