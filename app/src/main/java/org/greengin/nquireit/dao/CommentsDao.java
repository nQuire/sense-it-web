package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.CommentThreadEntity;
import org.greengin.nquireit.entities.rating.ForumThread;
import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.log.LogManagerBean;
import org.greengin.nquireit.logic.rating.CommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class CommentsDao {
    private static final String COMMENT_FEED_QUERY = "SELECT c FROM Comment c WHERE c.target.class = %s ORDER BY c.date DESC";

    @PersistenceContext
    EntityManager em;

    @Autowired
    LogManagerBean logManager;

    @Autowired
    ForumDao forumDao;

    public Comment getComment(CommentThreadEntity thread, Long commentId) {
        if (thread != null && commentId != null) {
            Comment c = em.find(Comment.class, commentId);

            if (c != null && thread.equals(c.getTarget())) {
                return c;
            }
        }

        return null;
    }

    @Transactional
    public Comment comment(UserProfile user, CommentThreadEntity target, CommentRequest request) {
        em.persist(target);
        Comment comment = commentWithinTransaction(user, target, request);
        logManager.comment(user, target, comment, true);
        em.persist(comment);
        return comment;
    }

    public Comment commentWithinTransaction(UserProfile user, CommentThreadEntity target, CommentRequest request) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setTarget(target);
        target.getComments().add(comment);
        request.update(comment);
        return comment;
    }

    @Transactional
    public boolean deleteComment(UserProfile user, CommentThreadEntity target, Long commentId) {
        if (user != null && target != null && commentId != null) {
            Comment c = em.find(Comment.class, commentId);
            if (c != null && user.equals(c.getAuthor()) && target.equals(c.getTarget())) {
                logManager.comment(user, target, c, false);
                deleteComment(c);
                return true;
            }
        }

        return false;
    }

    @Transactional
    public void deleteComment(Comment comment) {
        em.persist(comment);
        CommentThreadEntity thread = comment.getTarget();

        boolean isForumThread = thread instanceof ForumThread;

        if (isForumThread && ((ForumThread) thread).getFirstComment().equals(comment)) {
            forumDao.deleteForumThread((ForumThread) thread);
        } else {
            if (thread != null) {
                thread.getComments().remove(comment);
            }

            if (isForumThread) {
                ((ForumThread) thread).updateLastPost();
            }

            em.remove(comment);
        }
    }

    @Transactional
    public void updateComment(UserProfile user, Comment comment, String text) {
        if (user != null && comment != null && user.equals(comment.getAuthor())) {
            em.persist(comment);
            comment.setComment(text);
        }
    }

    @Transactional
    public void updateComment(UserProfile user, CommentThreadEntity thread, Long commentId, CommentRequest data) {
        updateComment(user, this.getComment(thread, commentId), data.getComment());
    }




    public <E extends VotableEntity> List<Comment> commentsFeed(Class<E> threadClass, int commentCount) {
        TypedQuery<Comment> query = em.createQuery(String.format(COMMENT_FEED_QUERY, threadClass.getSimpleName()), Comment.class);
        query.setMaxResults(commentCount);
        return query.getResultList();
    }

}
