package org.greengin.senseitweb.logic.permissions;

import org.greengin.senseitweb.entities.users.Role;
import org.greengin.senseitweb.entities.users.RoleContextEntity;
import org.greengin.senseitweb.entities.users.RoleType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Vector;

public class RoleManager {

	private static final String ROLE_QUERY = "SELECT r FROM Role r WHERE r.context.id = :contextId AND r.user = :user";
	
	private static final String USER_HAS_QUERY = "SELECT CASE WHEN (COUNT(r) > 0) THEN 1 ELSE 0 END FROM Role r WHERE r.context= :context AND r.user = :user AND r.type = :type";

	private static final String USER_QUERY = "SELECT u FROM Role r INNER JOIN r.user u WHERE r.context = :context AND r.type = :type";

	private static final String SEARCH_ROLE_QUERY = "SELECT r FROM Role r WHERE r.context = :context AND r.user = :user AND r.type = :type";


    @Autowired
    UsersManager usersManager;


    @Autowired
    CustomEntityManagerFactory entityManagerFactory;

	public List<UserProfile> contextUsers(RoleContextEntity context, RoleType type) {
		EntityManager em = entityManagerFactory.createEntityManager();
		TypedQuery<UserProfile> query = em.createQuery(USER_QUERY, UserProfile.class);
		query.setParameter("context", context);
		query.setParameter("type", type);
		return query.getResultList();
	}

    public List<RoleType> userRoles(UserProfile user, RoleContextEntity context) {
        Vector<RoleType> roles = new Vector<RoleType>();

        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<Role> query = em.createQuery(ROLE_QUERY, Role.class);
        query.setParameter("contextId", context.getId());
        query.setParameter("user", user);
        for (Role role : query.getResultList()) {
            roles.add(role.getType());
        }

        return roles;
    }


    public void addRoleInTransaction(EntityManager em, RoleContextEntity context, UserProfile user, RoleType type) {
		Role role = new Role();
		role.setContext(context);
        role.setUser(user);
        role.setType(type);
        context.getRoles().add(role);
        user.getRoles().add(role);
		em.persist(role);
	}

	public boolean is(RoleType type, RoleContextEntity context, UserProfile user) {
		if (context != null && user != null) {
			EntityManager em = entityManagerFactory.createEntityManager();
			TypedQuery<Long> query = em.createQuery(USER_HAS_QUERY, Long.class);
			query.setParameter("context", context);
			query.setParameter("user", user);
			query.setParameter("type", type);
			
			long result = query.getSingleResult();
			
			return result != 0;
		}

		return false;
	}


	public void addRole(EntityManager em, UserProfile user, RoleContextEntity context, RoleType type) {
		em.getTransaction().begin();
		this.addRoleInTransaction(em, context, user, type);
		em.getTransaction().commit();
	}

	public void removeRole(EntityManager em, UserProfile user, RoleContextEntity context, RoleType type) {
		em.getTransaction().begin();
		
		TypedQuery<Role> query = em.createQuery(SEARCH_ROLE_QUERY, Role.class);
		query.setParameter("context", context);
		query.setParameter("user", user);
		query.setParameter("type", type);
		for (Role r : query.getResultList()) {
            context.getRoles().remove(r);
            user.getRoles().remove(r);
            em.remove(r);
		}
		em.getTransaction().commit();
	}
}
