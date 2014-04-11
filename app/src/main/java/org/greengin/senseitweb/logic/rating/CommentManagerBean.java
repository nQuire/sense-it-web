package org.greengin.senseitweb.logic.rating;

import org.greengin.senseitweb.entities.rating.Comment;
import org.greengin.senseitweb.entities.rating.CommentThreadEntity;
import org.greengin.senseitweb.entities.rating.Vote;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class CommentManagerBean {
	private static final String VOTE_QUERY = String.format(
			"SELECT v FROM %s v WHERE v.target = :target AND v.user = :user", Vote.class.getName());

    @Autowired
    CustomEntityManagerFactoryBean entityManagerFactory;

    public Comment comment(UserProfile user, CommentThreadEntity target, CommentRequest request) {
        EntityManager em = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setTarget(target);
        target.getComments().add(comment);
        request.update(comment);
        em.persist(comment);

        em.getTransaction().commit();
        return comment;
    }

    public boolean deleteComment(UserProfile user, CommentThreadEntity target, Long commentId) {
        if (user != null && target != null && commentId != null) {
            EntityManager em = entityManagerFactory.createEntityManager();
            Comment c = em.find(Comment.class, commentId);

            if (c != null && user.equals(c.getUser()) && target.equals(c.getTarget())) {
                em.getTransaction().begin();
                target.getComments().remove(c);
                em.remove(c);
                em.getTransaction().commit();

                return true;
            }
        }

        return false;
    }
}
