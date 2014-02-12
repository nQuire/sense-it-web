package org.greengin.senseitweb.permissions;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.subscriptions.Subscription;
import org.greengin.senseitweb.entities.subscriptions.SubscriptionType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.persistence.EMF;

public class SubscriptionManager {

	static final String SUBSCRIPTION_QUERY = String.format(
			"SELECT s FROM %s s WHERE s.project.id = :projectId AND s.user = :user", Subscription.class.getName());

	static final String USER_QUERY = String.format(
			"SELECT u FROM %s s INNER JOIN s.user u WHERE s.project = :project AND s.type = :type",
			Subscription.class.getName(), UserProfile.class.getName());

	static final String DELETE_PROJECT_QUERY = String.format("DELETE FROM %s s WHERE s.project = :project",
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

	public int projectDeleted(Project project) {
		EntityManager em = EMF.get().createEntityManager();
		Query query = em.createQuery(DELETE_PROJECT_QUERY);
		query.setParameter("project", project);
		int rowCount = query.executeUpdate();
		return rowCount;
	}

	public void addSubscriptionInTransaction(EntityManager em, Project project, UserProfile user, SubscriptionType type) {
		Subscription s = new Subscription();
		s.setProject(project);
		s.setUser(user);
		s.setType(type);
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


}
