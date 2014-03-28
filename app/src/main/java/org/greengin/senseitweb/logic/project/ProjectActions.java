package org.greengin.senseitweb.logic.project;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.subscriptions.Subscription;
import org.greengin.senseitweb.entities.subscriptions.SubscriptionType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.AbstractContentManager;
import org.greengin.senseitweb.logic.data.FileManager;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.project.senseit.FileMapUpload;

public class ProjectActions extends AbstractContentManager {

	static final String USERS_QUERY = String.format(
			"SELECT DISTINCT u FROM %s s INNER JOIN s.user u WHERE s.type = :type AND s.project = :project",
			Subscription.class.getName());

	protected Long projectId;
	protected Project project;
	protected AccessLevel accessLevel;
	protected boolean projectExists;

    SubscriptionManager subscriptionManager;
    FileManager fileManager;


    public ProjectActions(Long projectId, SubscriptionManager subscriptionManager, FileManager fileManager, UserProfile user, boolean tokenOk, EntityManager em) {
        super(user, tokenOk, em);
        setProject(projectId, subscriptionManager, fileManager);
    }
    public ProjectActions(Long projectId, SubscriptionManager subscriptionManager, FileManager fileManager, UsersManager usersManager, EntityManager em, HttpServletRequest request) {
        super(usersManager, em, request);
        setProject(projectId, subscriptionManager, fileManager);
    }

    private void setProject(Long projectId, SubscriptionManager subscriptionManager, FileManager fileManager) {
        this.subscriptionManager = subscriptionManager;
        this.fileManager = fileManager;
        this.projectId = projectId;
        this.project = em.find(Project.class, projectId);
        this.projectExists = this.project != null;

        this.accessLevel = subscriptionManager.getAccessLevel(project, user);
    }

	@Override
	public boolean hasAccess(Role role) {
		if (super.hasAccess(role)) {
			return true;
		} else if (loggedWithToken) {
			switch (role) {
			case PROJECT_MEMBER:
				return accessLevel.isMember() && project.getOpen();
			case PROJECT_ADMIN:
				return accessLevel.isAdmin();
			case PROJECT_EDITOR:
				return accessLevel.isAdmin() && !project.getOpen();
			default:
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean hasMemberAccessIgnoreToken() {
		return super.hasAccessIgnoreToken(Role.LOGGEDIN) && accessLevel.isMember() && project.getOpen();
	}


	/** participant actions **/

	public AccessLevel join() {
		if (hasAccess(Role.LOGGEDIN) && !accessLevel.isMember()) {
            subscriptionManager.subscribe(em, user, project, SubscriptionType.MEMBER);
			return subscriptionManager.getAccessLevel(em, user, projectId);
		} 
		return null;
	}

	public AccessLevel leave() {
		if (this.hasAccess(Role.PROJECT_MEMBER)) {
            subscriptionManager.unsubscribe(em, user, project, SubscriptionType.MEMBER);
			return subscriptionManager.getAccessLevel(em, user, projectId);
		} 
		
		return null;
	}
	
	/** admin actions **/
	
	public Project setOpen(Boolean open) {
		if (hasAccess(Role.PROJECT_ADMIN)) {
			em.getTransaction().begin();
			project.setOpen(open);
			em.getTransaction().commit();
			return project;
		}
		
		return null;
	}

	public Collection<UserProfile> getUsers() {
		if (hasAccess(Role.PROJECT_ADMIN)) {
			return subscriptionManager.projectMembers(project);
		} else {
			return null;
		}
	}
	

	/** editor actions **/
	
	public Project updateMetadata(ProjectRequest data, FileMapUpload files) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
            if (files != null && data.getDescription() != null) {
                String context = projectId.toString();
                for (Map.Entry<String, FileMapUpload.FileData> entry : files.getData().entrySet()) {
                    try {
                        fileManager.uploadFile(context, entry.getValue().filename, entry.getValue().data);
                        data.getDescription().put(entry.getKey(), context + "/" + entry.getValue().filename);
                    } catch (Exception e) {
                        data.getDescription().put(entry.getKey(), null);
                    }
                }
            }

			em.getTransaction().begin();
			data.updateProject(project);
			em.getTransaction().commit();

			return project;
		} else {
			return null;
		}
	}

	public Boolean deleteProject() {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			em.getTransaction().begin();
			em.remove(project);
			em.getTransaction().commit();
			return true;
		} else {
			return null;
		}
	}
}
