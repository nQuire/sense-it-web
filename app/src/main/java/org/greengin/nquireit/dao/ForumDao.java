package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.rating.ForumNode;
import org.greengin.nquireit.entities.rating.ForumThread;
import org.greengin.nquireit.json.Views;
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

    public ForumThread findThread(Long id) {
        return em.find(ForumThread.class, id);
    }
}
