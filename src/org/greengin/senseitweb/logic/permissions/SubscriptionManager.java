package org.greengin.senseitweb.logic.permissions;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.subscriptions.Subscription;
import org.greengin.senseitweb.entities.subscriptions.SubscriptionType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.persistence.EMF;

public class SubscriptionManager {

	private static final String SUBSCRIPTION_QUERY = String.format(
			"SELECT s FROM %s s WHERE s.project.id = :projectId AND s.user = :user", Subscription.class.getName());
	
	private static final String USER_IS_QUERY = String.format(
			"SELECT CASE WHEN (COUNT(s) > 0) THEN 1 ELSE 0 END FROM %s s WHERE s.project= :project AND s.user = :user AND s.type = :type", Subscription.class.getName());

	private static final String USER_QUERY = String.format(
			"SELECT u FROM %s s INNER JOIN s.user u WHERE s.project = :project AND s.type = :type",
			Subscription.class.getName(), UserProfile.class.getName());

	private static final String SEARCH_SUBSCRIPTION_QUERY = String.format("SELECT s FROM %s s WHERE s.project = :project AND s.user = :user AND s.type = :type",
			Subscription.class.getName());

	private static SubscriptionManager sm = new SubscriptionManager();

	public static SubscriptionManager get() {
		return sm;
	}

	private SubscriptionManager() {

	}

	public List<UserProfile> projectUsers(Project project, SubscriptionType type) {
		EntityManager em = EMF.get().createEntityManager();
		TypedQuery<UserProfile> query = em.createQuery(USER_QUERY, UserProfile.class);
		query.setParameter("project", project);
		query.setParameter("type", type);
		return query.getResultList();
	}

	public List<UserProfile> projectAdmins(Project project) {
		return projectUsers(project, SubscriptionType.ADMIN);
	}

	public List<UserProfile> projectMembers(Project project) {
		return projectUsers(project, SubscriptionType.MEMBER);
	}

	public void addSubscriptionInTransaction(EntityManager em, Project project, UserProfile user, SubscriptionType type) {
		Subscription s = new Subscription();
		s.setProject(project);
		s.setUser(user);
		s.setType(type);
		project.getSubscriptions().add(s);
		em.persist(s);
	}

	public void projectCreatedInTransaction(EntityManager em, Project project, UserProfile user) {
		addSubscriptionInTransaction(em, project, user, SubscriptionType.AUTHOR);
		addSubscriptionInTransaction(em, project, user, SubscriptionType.ADMIN);
	}

	public AccessLevel getAccessLevel(Project project, HttpServletRequest request) {
		AccessLevel level = new AccessLevel();

		UserProfile user = UsersManager.get().currentUser(request);
		if (project != null && user != null) {
			EntityManager em = EMF.get().createEntityManager();
			TypedQuery<Subscription> query = em.createQuery(SUBSCRIPTION_QUERY, Subscription.class);
			query.setParameter("projectId", project.getId());
			query.setParameter("user", user);

			for (Subscription s : query.getResultList()) {
				switch (s.getType()) {
				case AUTHOR:
					level.setAuthor(true);
					break;
				case ADMIN:
					level.setAdmin(true);
					break;
				case MEMBER:
					level.setMember(true);
					break;
				}
			}
		}

		return level;
	}
	
	public boolean is(SubscriptionType type, Project project, UserProfile user) {
		if (project != null && user != null) {
			EntityManager em = EMF.get().createEntityManager();
			TypedQuery<Long> query = em.createQuery(USER_IS_QUERY, Long.class);
			query.setParameter("project", project);
			query.setParameter("user", user);
			query.setParameter("type", type);
			
			long result = query.getSingleResult();
			
			return result != 0;
		}

		return false;
	}
	
	public AccessLevel getAccessLevel(EntityManager em, UserProfile user, Long projectId) {
		AccessLevel level = new AccessLevel();

		if (projectId != null && user != null) {
			TypedQuery<Subscription> query = em.createQuery(SUBSCRIPTION_QUERY, Subscription.class);
			query.setParameter("projectId", projectId);
			query.setParameter("user", user);

			for (Subscription s : query.getResultList()) {
				switch (s.getType()) {
				case AUTHOR:
					level.setAuthor(true);
					break;
				case ADMIN:
					level.setAdmin(true);
					break;
				case MEMBER:
					level.setMember(true);
					break;
				}
			}
		}

		return level;
	}

	public AccessLevel subscribe(EntityManager em, UserProfile user, Project project, SubscriptionType type) {
		em.getTransaction().begin();
		this.addSubscriptionInTransaction(em, project, user, type);
		em.getTransaction().commit();
		return this.getAccessLevel(em, user, project.getId());
	}

	public AccessLevel unsubscribe(EntityManager em, UserProfile user, Project project, SubscriptionType type) {
		em.getTransaction().begin();
		
		TypedQuery<Subscription> query = em.createQuery(SEARCH_SUBSCRIPTION_QUERY, Subscription.class);
		query.setParameter("project", project);
		query.setParameter("user", user);
		query.setParameter("type", type);
		for (Subscription s : query.getResultList()) {
			project.getSubscriptions().remove(s);
		}
		em.getTransaction().commit();
		return this.getAccessLevel(em, user, project.getId());
	}
}
