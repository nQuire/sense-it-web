package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.entities.rating.ForumThread;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.forum.ForumRequest;
import org.greengin.nquireit.logic.rating.CommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


@Component
public class ForumDao {
    static final String ROOT_NODE = "SELECT f FROM ForumNode f WHERE f.parent IS NULL";

    @PersistenceContext
    EntityManager em;

    @Autowired
    ContextBean context;


    @Transactional
    public ForumNode findRoot() {
        TypedQuery<ForumNode> query = em.createQuery(ROOT_NODE, ForumNode.class);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            ForumNode root = new ForumNode();
            root.setParent(null);
            em.persist(root);
            return root;
        }
    }

    public ForumNode findForum(Long id) {
        return em.find(ForumNode.class, id);
    }

    public ForumThread findThread(Long id) {
        return em.find(ForumThread.class, id);
    }


    @Transactional
    public ForumNode createForum(Long containerId, ForumRequest forumData) {
        ForumNode container = findForum(containerId);
        if (container != null) {
            ForumNode node = new ForumNode();
            forumData.update(node);

            em.persist(node);

            node.setParent(container);
            container.getChildren().add(node);

            return node;
        }

        return null;
    }

    @Transactional
    public void updateForum(Long forumId, ForumRequest forumData) {
        ForumNode forum = findForum(forumId);
        if (forum != null) {
            forumData.update(forum);
        }
    }

    @Transactional
    public ForumThread createThread(UserProfile user, Long forumId, ForumRequest forumData) {
        ForumNode forum = findForum(forumId);

        if (forum != null) {
            ForumThread thread = new ForumThread();
            thread.setAuthor(user);
            forumData.update(thread);

            thread.setForum(forum);
            forum.getThreads().add(thread);

            em.persist(thread);

            CommentRequest commentRequest = new CommentRequest();
            commentRequest.setComment(forumData.getText());
            Comment comment = context.getCommentsDao().commentWithinTransaction(user, thread, commentRequest);
            context.getLogManager().newThread(user, thread);

            em.persist(comment);

            thread.setFirstComment(comment);
            thread.updateLastPost();

            return thread;
        }

        return null;
    }

    @Transactional
    public void comment(UserProfile user, ForumThread thread, CommentRequest data) {
        em.persist(thread);
        em.persist(thread.getForum());
        Comment comment = context.getCommentsDao().commentWithinTransaction(user, thread, data);
        context.getLogManager().threadComment(user, thread, comment);
        thread.updateLastPost();
        em.persist(comment);
    }

    @Transactional
    public void deleteComment(UserProfile user, ForumThread thread, Long commentId) {
        em.persist(thread);
        em.persist(thread.getForum());
        if (context.getCommentsDao().deleteComment(user, thread, commentId)) {
            thread.updateLastPost();
        }
    }

    @Transactional
    public void deleteForumThread(ForumThread thread) {
        em.persist(thread);
        ForumNode node = thread.getForum();
        if (node != null) {
            node.getThreads().remove(thread);
            node.updateLastPost();
        }
        em.remove(thread);
    }
}
