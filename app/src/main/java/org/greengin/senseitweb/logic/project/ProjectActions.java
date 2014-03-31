package org.greengin.senseitweb.logic.project;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.RoleType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.AbstractContentManager;
import org.greengin.senseitweb.logic.data.FileManager;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.project.senseit.FileMapUpload;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

public class ProjectActions extends AbstractContentManager {

    static final String USERS_QUERY = "SELECT DISTINCT u FROM Role r INNER JOIN r.user u WHERE r.type = :type AND r.project = :project";

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
    public boolean hasAccess(PermissionType permission) {
        if (super.hasAccess(permission)) {
            return true;
        } else if (permission == PermissionType.PROJECT_VIEW_IMAGE) {
            return accessLevel.isMember() && project.getOpen();
        } else if (loggedWithToken) {
            switch (permission) {
                case PROJECT_JOIN:
                    return project.getOpen();
                case PROJECT_MEMBER_ACTION:
                    return accessLevel.isMember() && project.getOpen();
                case PROJECT_ADMIN:
                    return accessLevel.isAdmin();
                case PROJECT_EDITION:
                    return accessLevel.isAdmin() && !project.getOpen();
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * participant actions *
     */

    public AccessLevel join() {
        if (hasAccess(PermissionType.PROJECT_JOIN) && !accessLevel.isMember()) {
            subscriptionManager.subscribe(em, user, project, RoleType.MEMBER);
            return subscriptionManager.getAccessLevel(projectId, em, user);
        }
        return null;
    }

    public AccessLevel leave() {
        if (this.hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            subscriptionManager.unsubscribe(em, user, project, RoleType.MEMBER);
            return subscriptionManager.getAccessLevel(projectId, em, user);
        }

        return null;
    }

    /**
     * admin actions *
     */

    public Project setOpen(Boolean open) {
        if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            em.getTransaction().begin();
            project.setOpen(open);
            em.getTransaction().commit();
            return project;
        }

        return null;
    }

    public Collection<UserProfile> getUsers() {
        if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            return subscriptionManager.projectMembers(project);
        } else {
            return null;
        }
    }


    /**
     * editor actions *
     */

    public Project updateMetadata(ProjectRequest data, FileMapUpload files) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
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
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            em.getTransaction().begin();
            em.remove(project);
            em.getTransaction().commit();
            return true;
        } else {
            return null;
        }
    }
}
