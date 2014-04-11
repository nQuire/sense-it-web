package org.greengin.senseitweb.logic.project;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.rating.Comment;
import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.RoleType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.AbstractContentManager;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.users.AccessLevel;
import org.greengin.senseitweb.logic.project.senseit.FileMapUpload;
import org.greengin.senseitweb.logic.rating.CommentRequest;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProjectActions extends AbstractContentManager {

    protected Long projectId;
    protected Project project;
    protected AccessLevel accessLevel;
    protected boolean projectExists;


    public ProjectActions(ContextBean context, Long projectId, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
        setProject(projectId);
    }

    public ProjectActions(ContextBean context, Long projectId, HttpServletRequest request) {
        super(context, request);
        setProject(projectId);
    }


    private void setProject(Long projectId) {
        EntityManager em = context.createEntityManager();
        this.projectId = projectId;
        this.project = em.find(Project.class, projectId);
        this.projectExists = this.project != null;

        this.accessLevel = context.getSubscriptionManager().getAccessLevel(project, user);
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
                case PROJECT_COMMENT:
                    return hasAccess(PermissionType.PROJECT_ADMIN) || hasAccess(PermissionType.PROJECT_MEMBER_ACTION);
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * common actions
     */


    public Project get() {
        if (accessLevel.isAdmin() || project.getOpen()) {
            project.setSelectedVoteAuthor(user);
            return project;
        } else {
            return null;
        }
    }

    /**
     * participant actions *
     */

    public AccessLevel join() {
        if (hasAccess(PermissionType.PROJECT_JOIN) && !accessLevel.isMember()) {
            EntityManager em = context.createEntityManager();
            context.getSubscriptionManager().subscribe(em, user, project, RoleType.MEMBER);
            return context.getSubscriptionManager().getAccessLevel(projectId, em, user);
        }
        return null;
    }

    public AccessLevel leave() {
        if (this.hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            EntityManager em = context.createEntityManager();
            context.getSubscriptionManager().unsubscribe(em, user, project, RoleType.MEMBER);
            return context.getSubscriptionManager().getAccessLevel(projectId, em, user);
        }

        return null;
    }

    /**
     * admin actions *
     */

    public Project setOpen(Boolean open) {
        if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            EntityManager em = context.createEntityManager();
            em.getTransaction().begin();
            project.setOpen(open);
            em.getTransaction().commit();
            return project;
        }

        return null;
    }

    public Collection<UserProfile> getUsers() {
        if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            return context.getSubscriptionManager().projectMembers(project);
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
                String fileContext = projectId.toString();
                for (Map.Entry<String, FileMapUpload.FileData> entry : files.getData().entrySet()) {
                    try {
                        context.getFileManager().uploadFile(fileContext, entry.getValue().filename, entry.getValue().data);
                        data.getDescription().put(entry.getKey(), fileContext + "/" + entry.getValue().filename);
                    } catch (Exception e) {
                        data.getDescription().put(entry.getKey(), null);
                    }
                }
            }

            EntityManager em = context.createEntityManager();
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
            EntityManager em = context.createEntityManager();
            em.getTransaction().begin();
            em.remove(project);
            em.getTransaction().commit();
            return true;
        } else {
            return null;
        }
    }


    /**
     * comment actions
     */

    public List<Comment> getComments() {
        if (hasAccess(PermissionType.PROJECT_COMMENT)) {
            return project.getComments();
        }

        return null;
    }


    public List<Comment> comment(CommentRequest request) {
        if (hasAccess(PermissionType.PROJECT_COMMENT)) {
            context.getCommentManager().comment(user, project, request);
            return project.getComments();
        }

        return null;
    }

    public List<Comment> deleteComment(Long commentId) {
        if (hasAccess(PermissionType.PROJECT_COMMENT)) {
            if (context.getCommentManager().deleteComment(user, project, commentId)) {
                return project.getComments();
            }
        }

        return null;
    }
}
