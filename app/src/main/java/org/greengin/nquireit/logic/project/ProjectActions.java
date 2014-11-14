package org.greengin.nquireit.logic.project;

import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectType;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.users.RoleType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.metadata.ProjectRequest;
import org.greengin.nquireit.logic.rating.CommentFeedResponse;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;
import org.greengin.nquireit.logic.users.AccessLevel;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.rating.CommentRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
        this.projectId = projectId;
        this.project = projectId != null ? context.getProjectDao().project(projectId) : null;
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
    public ProjectListResponse getProjects(String typeStr, String status, String filter, String keyword) {

        List<ProjectResponse> filtered = new Vector<ProjectResponse>();
        HashMap<String, Integer> categories = new HashMap<String, Integer>();

        categories.put("challenge", 0);
        categories.put("senseit", 0);
        categories.put("spotit", 0);
        categories.put("featured", 0);
        categories.put("joined", 0);
        categories.put("not-joined", 0);
        categories.put("mine", 0);

        boolean anyStatus = "".equals(status);
        boolean onlyJoined = "joined".equals(status);
        boolean onlyNotJoined = "not-joined".equals(status);
        boolean onlyMine = "mine".equals(status);
        boolean useKeyword = !"".equals(keyword);

        ProjectType type = null;
        if ("sense-it".equals(typeStr)) {
            type = ProjectType.SENSEIT;
        } else if ("spot-it".equals(typeStr)) {
            type = ProjectType.SPOTIT;
        } else if ("win-it".equals(typeStr)) {
            type = ProjectType.CHALLENGE;
        }

        int allCount = 0;

        if (hasAccess(PermissionType.BROWSE)) {

            for (Project p : context.getProjectDao().getProjects(type, !"all".equals(filter))) {

                AccessLevel access = context.getSubscriptionManager().getAccessLevel(p, user);
                if (access.isAdmin() || p.getOpen()) {

                    String projectType = p.getType().getValue();

                    boolean add = anyStatus ||
                            (onlyJoined && access.isMember()) ||
                            (onlyNotJoined && !access.isMember()) ||
                            (onlyMine && access.isAdmin());

                    if (add && useKeyword) {
                        add = p.getTitle().contains(keyword) ||
                                p.getMetadata().containsKeyword(keyword);
                    }

                    if (add) {
                        filtered.add(projectResponse(p));
                    }


                    categories.put(projectType, categories.get(projectType) + 1);

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
    public UserProjectListResponse getMyProjects() {
        if (loggedWithToken) {
            return context.getProjectDao().getMyProjects(user, true, true);
        } else {
            return null;
        }
    }

    public List<SimpleProjectResponse> getProjectsSimple(String type) {
        List<SimpleProjectResponse> response = new Vector<SimpleProjectResponse>();

        if (hasAccess(PermissionType.BROWSE)) {
            List<Project> all = context.getProjectDao().getProjectsSimple(type);

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
            Project project = context.getProjectDao().createProject(projectData, user);
            context.getLogManager().projectCreationAction(user, project, true);
            return project.getId();
        }
        return null;
    }

    @Override
    public boolean hasAccess(PermissionType permission) {
        if (super.hasAccess(permission)) {
            return true;
        }

        if (project != null) {
            if (permission == PermissionType.PROJECT_BROWSE) {
                return true;
            } else if (permission == PermissionType.PROJECT_VIEW_IMAGE) {
                return project.getOpen() || accessLevel.isAdmin();
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
            }
        }

        return false;

    }

    /**
     * common actions
     */


    public ProjectResponse get() {
        return project != null && (accessLevel.isAdmin() || project.getOpen()) ? projectResponse(project) : null;
    }

    /**
     * participant actions *
     */

    public AccessLevel join() {
        if (hasAccess(PermissionType.PROJECT_JOIN) && !accessLevel.isMember()) {
            context.getSubscriptionManager().subscribe(user, project, RoleType.MEMBER);
            context.getLogManager().projectMembershipAction(user, project, true);
            return context.getSubscriptionManager().getAccessLevel(project, user);
        }
        return null;
    }

    public AccessLevel leave() {
        if (this.hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            context.getSubscriptionManager().unsubscribe(user, project, RoleType.MEMBER);
            context.getLogManager().projectMembershipAction(user, project, false);
            return context.getSubscriptionManager().getAccessLevel(project, user);
        }

        return null;
    }

    public ProjectResponse setOpen(Boolean open) {
        if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            context.getProjectDao().setOpen(project, open);
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
            return projectResponse(context.getProjectDao().updateMetadata(project, data, files));
        }

        return null;

    }

    @Transactional
    public Boolean deleteProject() {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            context.getLogManager().projectCreationAction(user, project, false);
            return context.getProjectDao().deleteProject(project);
        }
        return null;

    }


    /**
     * comment actions
     */

    public List<CommentFeedResponse> getProjectCommentFeed() {
        List<CommentFeedResponse> list = new Vector<CommentFeedResponse>();
        for (Comment c : context.getCommentsDao().commentsFeed(Project.class, 3)) {
            list.add(new CommentFeedResponse(c));
        }
        return list;
    }

    public List<Comment> getComments() {
        if (hasAccess(PermissionType.PROJECT_BROWSE)) {
            return project.getComments();
        }

        return null;
    }


    public List<Comment> comment(CommentRequest request) {
        if (hasAccess(PermissionType.PROJECT_COMMENT)) {
            context.getCommentsDao().comment(user, project, request);
            return project.getComments();
        }

        return null;
    }

    public List<Comment> deleteComment(Long commentId) {
        if (hasAccess(PermissionType.PROJECT_COMMENT)) {
            if (context.getCommentsDao().deleteComment(user, project, commentId)) {
                return project.getComments();
            }
        }

        return null;
    }

    public VoteCount voteComment(Long commentId, VoteRequest voteData) {
        if (hasAccess(PermissionType.PROJECT_COMMENT) || (loggedWithToken && voteData.isReport())) {
            Comment comment = context.getCommentsDao().getComment(project, commentId);
            if (comment != null) {
                return context.getVoteDao().vote(user, comment, voteData);
            }
        }

        return null;
    }
}
