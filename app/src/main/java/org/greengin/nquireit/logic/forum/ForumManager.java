package org.greengin.nquireit.logic.forum;

import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.entities.rating.ForumThread;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.rating.CommentFeedResponse;
import org.greengin.nquireit.logic.rating.CommentRequest;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Vector;

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
            context.getForumDao().comment(user, thread, data);
            return thread;
        }

        return null;
    }

    public VoteCount voteComment(Long threadId, Long commentId, VoteRequest voteData) {
        if (loggedWithToken) {
            ForumThread thread = context.getForumDao().findThread(threadId);
            if (thread != null) {
                Comment comment = context.getCommentsDao().getComment(thread, commentId);
                if (comment != null) {
                    return context.getVoteDao().vote(user, comment, voteData);
                }
            }
        }

        return null;
    }

    public List<CommentFeedResponse> getForumCommentFeed() {
        List<CommentFeedResponse> list = new Vector<CommentFeedResponse>();
        for (Comment c : context.getCommentsDao().commentsFeed(ForumThread.class, 3)) {
            list.add(new CommentFeedResponse(c));
        }
        return list;
    }
}
