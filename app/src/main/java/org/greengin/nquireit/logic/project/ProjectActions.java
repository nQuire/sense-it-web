package org.greengin.nquireit.logic.project;

import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectDescription;
import org.greengin.nquireit.entities.projects.ProjectType;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.users.Role;
import org.greengin.nquireit.entities.users.RoleType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.metadata.ProjectRequest;
import org.greengin.nquireit.logic.users.AccessLevel;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.rating.CommentRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

public class ProjectActions extends AbstractContentManager {

    static final String PROJECTS_QUERY = "SELECT p FROM Project p";
    static final String TYPED_PROJECTS_QUERY = "SELECT p FROM Project p WHERE p.type = :type";
    private static final String MY_PROJECTS_QUERY = "SELECT r, e FROM Role r INNER JOIN r.context e WHERE r.user = :user";

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
        this.project = projectId != null ? em.find(Project.class, projectId) : null;
        this.projectExists = this.project != null;

        this.accessLevel = context.getSubscriptionManager().getAccessLevel(project, user);
    }


    protected ProjectResponse projectResponse(Project project) {
        ProjectResponse response = new ProjectResponse();

        HashMap<String, Long> data = new HashMap<String, Long>();
        data.put("members", context.getSubscriptionManager().projectUserCount(project, RoleType.MEMBER));
        project.getActivity().loadProjectData(context, project, user, data);
        response.setProject(project);
        response.setAccess(context.getSubscriptionManager().getAccessLevel(project, user));
        response.setData(data);

        return response;
    }

    /**
     * any user actions *
     */
    public ProjectListResponse getProjects() {
        List<ProjectResponse> filtered = new Vector<ProjectResponse>();
        HashMap<String, Integer> categories = new HashMap<String, Integer>();

        categories.put("challenge", 0);
        categories.put("senseit", 0);
        categories.put("spotit", 0);
        int allCount = 0;

        if (hasAccess(PermissionType.BROWSE)) {
            EntityManager em = context.createEntityManager();
            TypedQuery<Project> query = em.createQuery(PROJECTS_QUERY, Project.class);
            List<Project> all = query.getResultList();

            for (Project p : all) {
                AccessLevel access = context.getSubscriptionManager().getAccessLevel(p, user);
                if (access.isAdmin() || p.getOpen()) {
                    filtered.add(projectResponse(p));
                    String type = p.getType().getValue();
                    categories.put(type, categories.get(type) + 1);
                    allCount++;
                }
            }
        }

        categories.put("all", allCount);


        ProjectListResponse response = new ProjectListResponse();
        response.setList(filtered);
        response.setCategories(categories);

        return response;
    }


    /**
     * any user actions *
     */
    public MyProjectListResponse getMyProjects() {
        MyProjectListResponse response = new MyProjectListResponse();

        if (loggedWithToken) {
            EntityManager em = context.createEntityManager();
            TypedQuery<Object[]> query = em.createQuery(MY_PROJECTS_QUERY, Object[].class);
            query.setParameter("user", user);
            List<Object[]> all = query.getResultList();

            for (Object[] entry : all) {
                if (entry.length == 2 && entry[0] instanceof Role && entry[1] instanceof Project) {
                    Role r = (Role) entry[0];
                    Project p = (Project) entry[1];

                    if (r.getType() == RoleType.ADMIN) {
                        response.getAdmin().add(new MyProjectResponse(p));
                    } else if (r.getType() == RoleType.MEMBER) {
                        response.getMember().add(new MyProjectResponse(p));
                    }
                }
            }
        }

        return response;
    }

    public List<SimpleProjectResponse> getProjectsSimple(String type) {
        List<SimpleProjectResponse> response = new Vector<SimpleProjectResponse>();

        if (hasAccess(PermissionType.BROWSE)) {
            EntityManager em = context.createEntityManager();
            TypedQuery<Project> query = em.createQuery(TYPED_PROJECTS_QUERY, Project.class);
            query.setParameter("type", ProjectType.create(type));

            List<Project> all = query.getResultList();
            for (Project p : all) {
                SimpleProjectResponse spr = new SimpleProjectResponse();
                spr.setId(p.getId());
                spr.setTitle(p.getTitle());
                spr.setJoined(context.getSubscriptionManager().is(RoleType.MEMBER, p, user));
                spr.setAuthor(p.getAuthor().getUsername());
                response.add(spr);
            }
        }

        return response;
    }

    /**
     * registered user actions *
     */
    public Long createProject(ProjectCreationRequest projectData) {
        if (hasAccess(PermissionType.CREATE_PROJECT)) {
            EntityManager em = context.createEntityManager();
            em.getTransaction().begin();
            Project project = new Project();
            project.setTitle("New project");
            project.setOpen(false);
            project.setAuthor(user);
            projectData.initProject(project);
            em.persist(project);
            context.getSubscriptionManager().projectCreatedInTransaction(em, project, user);
            em.getTransaction().commit();

            return project.getId();
        } else {
            return null;
        }
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


    public ProjectResponse get() {
        if (accessLevel.isAdmin() || project.getOpen()) {
            project.setSelectedVoteAuthor(user);
            return projectResponse(project);
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

    public ProjectResponse setOpen(Boolean open) {
        if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            EntityManager em = context.createEntityManager();
            em.getTransaction().begin();
            project.setOpen(open);
            em.getTransaction().commit();
            return projectResponse(project);
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

    public ProjectResponse updateMetadata(ProjectRequest data, FileMapUpload files) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
            em.getTransaction().begin();

            project.setTitle(data.getTitle());
            if (project.getDescription() == null) {
                project.setDescription(new ProjectDescription());
            }
            project.getDescription().setTeaser(data.getDescription().getTeaser());

            if (files != null && files.getData().containsKey("image")) {
                FileMapUpload.FileData file = files.getData().get("image");
                String fileContext = projectId.toString();
                if (file == null) {
                    project.getDescription().setImage(null);
                } else {
                    try {
                        String filename = context.getFileManager().uploadFile(fileContext, file.filename, file.data);
                        project.getDescription().setImage(filename);
                    } catch (IOException exception) {
                        project.getDescription().setImage(null);
                    }
                }
            } else {
                project.getDescription().setImage(data.getDescription().getImage());
            }

            project.getDescription().setBlocks(data.getDescription().getBlocks());

            em.getTransaction().commit();

            return projectResponse(project);
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