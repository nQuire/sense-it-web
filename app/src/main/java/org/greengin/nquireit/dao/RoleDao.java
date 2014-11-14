package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.users.Role;
import org.greengin.nquireit.entities.users.RoleType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.users.UserServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Vector;

public class RoleDao {

    private static final String ROLE_QUERY = "SELECT r FROM Role r WHERE r.context.id = :contextId AND r.user = :user";

    private static final String USER_HAS_QUERY = "SELECT CASE WHEN (COUNT(r) > 0) THEN 1 ELSE 0 END FROM Role r WHERE r.context= :context AND r.user = :user AND r.type = :type";

    private static final String USER_TYPE_QUERY = "SELECT u FROM Role r INNER JOIN r.user u WHERE r.context = :context AND r.type = :type";

    private static final String USER_QUERY = "SELECT r FROM Role r WHERE r.user = :user";

    private static final String USER_QUERY_COUNT = "SELECT COUNT(u) FROM Role r INNER JOIN r.user u WHERE r.context = :context AND r.type = :type";

    private static final String SEARCH_ROLE_QUERY = "SELECT r FROM Role r WHERE r.context = :context AND r.user = :user AND r.type = :type";

    private static final String DELETE_CONTEXT_ROLES_QUERY = "DELETE FROM Role r WHERE r.context = :context";


    @Autowired
    UserServiceBean usersManager;

    @PersistenceContext
    EntityManager em;

    public long contextUserCount(AbstractEntity context, RoleType type) {
        TypedQuery<Long> query = em.createQuery(USER_QUERY_COUNT, Long.class);
        query.setParameter("context", context);
        query.setParameter("type", type);
        return query.getSingleResult();
    }

    public List<UserProfile> contextUsers(AbstractEntity context, RoleType type) {
        TypedQuery<UserProfile> query = em.createQuery(USER_TYPE_QUERY, UserProfile.class);
        query.setParameter("context", context);
        query.setParameter("type", type);
        return query.getResultList();
    }

    public List<RoleType> userRoles(UserProfile user, AbstractEntity context) {
        Vector<RoleType> roles = new Vector<RoleType>();

        TypedQuery<Role> query = em.createQuery(ROLE_QUERY, Role.class);
        query.setParameter("contextId", context.getId());
        query.setParameter("user", user);
        for (Role role : query.getResultList()) {
            roles.add(role.getType());
        }

        return roles;
    }


    public void addRoleInTransaction(EntityManager em, AbstractEntity context, UserProfile user, RoleType type) {
        Role role = new Role();
        role.setContext(context);
        role.setUser(user);
        role.setType(type);
        em.persist(role);
    }

    public boolean is(RoleType type, AbstractEntity context, UserProfile user) {
        if (context != null && user != null) {
            TypedQuery<Integer> query = em.createQuery(USER_HAS_QUERY, Integer.class);
            query.setParameter("context", context);
            query.setParameter("user", user);
            query.setParameter("type", type);

            long result = query.getSingleResult();

            return result != 0;
        }

        return false;
    }


    @Transactional
    public void addRole(UserProfile user, AbstractEntity context, RoleType type) {
        this.addRoleInTransaction(em, context, user, type);
    }

    @Transactional
    public void removeRole(UserProfile user, AbstractEntity context, RoleType type) {
        TypedQuery<Role> query = em.createQuery(SEARCH_ROLE_QUERY, Role.class);
        query.setParameter("context", context);
        query.setParameter("user", user);
        query.setParameter("type", type);
        for (Role r : query.getResultList()) {
            em.remove(r);
        }
    }

    @Transactional
    public void removeContextRoles(AbstractEntity context) {
        Query query = em.createQuery(DELETE_CONTEXT_ROLES_QUERY);
        query.setParameter("context", context);
        query.executeUpdate();
    }

    @Transactional
    public void transferRoles(UserProfile fromUser, UserProfile toUser) {
        TypedQuery<Role> query = em.createQuery(USER_QUERY, Role.class);
        query.setParameter("user", fromUser);

        for (Role r : query.getResultList()) {
            AbstractEntity context = r.getContext();
            RoleType type = r.getType();

            em.remove(r);

            if (!is(type, context, toUser)) {
                addRoleInTransaction(em, context, toUser, type);
            }
        }

    }
}
