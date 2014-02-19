package org.greengin.senseitweb.logic.project;

import java.util.List;
import java.util.Vector;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.subscriptions.Subscription;
import org.greengin.senseitweb.logic.AbstractContentManager;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;

public class ProjectListManager extends AbstractContentManager {

	static final String PROJECTS_QUERY = String.format("SELECT p FROM %s p", Project.class.getName());

	static final String USERS_QUERY = String.format(
			"SELECT DISTINCT u FROM %s s INNER JOIN s.user u WHERE s.type = :type AND s.project = :project",
			Subscription.class.getName());

	public ProjectListManager(HttpServletRequest request) {
		super(request);
	}

	/** any user actions **/
	public List<Project> getProjects() {
		if (hasAccess(Role.NONE)) {
			TypedQuery<Project> query = em.createQuery(PROJECTS_QUERY, Project.class);
			return (List<Project>) query.getResultList();
		} else {
			return new Vector<Project>();
		}
	}

	/** registered user actions **/
	public Long createProject(ProjectCreationRequest projectData) {
		if (hasAccess(Role.LOGGEDIN)) {
			em.getTransaction().begin();
			Project project = new Project();
			projectData.initProject(project);
			em.persist(project);
			SubscriptionManager.get().projectCreatedInTransaction(em, project, user);
			em.getTransaction().commit();
		
			return project.getId();
		} else {
			return null;
		}
	}
}
