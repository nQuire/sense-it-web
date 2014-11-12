package org.greengin.nquireit.logic.log;

import org.greengin.nquireit.dao.LogsDao;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.CommentThreadEntity;
import org.greengin.nquireit.entities.rating.ForumThread;
import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogManagerBean {

    @Autowired
    LogsDao logsDao;

    @Autowired
    ContextBean context;

    public Boolean pageView(UserProfile user, String path, String session) {
        return logsDao.log(user, "page-view", path, null, null, session);
    }

    public void projectMembershipAction(UserProfile user, Project project, boolean join) {
        String type = "project-membership";
        String path = project.getReportedPath(context);
        logsDao.log(user, type, path, project, project.getAuthor(), String.valueOf(join));
    }

    public void projectCreationAction(UserProfile user, Project project, boolean create) {
        String type = create ? "project-create" : "project-delete";
        String path = project.getReportedPath(context);
        logsDao.log(user, type, path, project, null, project.getTitle());
    }

    public void comment(UserProfile user, CommentThreadEntity target, Comment comment, boolean create) {
        if (target != null) {
            String type = create ? "comment" : "comment-delete";
            String path = String.format("%s : %s", target.getReportedPath(context),
                    target.getReportedType(context));
            logsDao.log(user, type, path, target, target.getAuthor(), comment != null ? comment.getComment() : null);
        }
    }

    public void threadComment(UserProfile user, ForumThread thread, Comment comment) {
        if (thread != null && comment != null) {
            String type = "thread-comment";
            String path = thread.getReportedPath(context);
            logsDao.log(user, type, path, thread, thread.getAuthor(), comment.getComment());
        }
    }

    public void newThread(UserProfile user, ForumThread thread) {
        if (thread != null) {
            String type = "new-thread";
            String path = thread.getReportedPath(context);
            logsDao.log(user, type, path, thread, null, thread.getTitle());
        }
    }

    public void vote(UserProfile user, VotableEntity target, Long value) {
        String type = "vote";
        String path = String.format("%s : %s", target.getReportedPath(context),
                target.getReportedType(context));

        logsDao.log(user, type, path, target, target.getAuthor(), String.valueOf(value));
    }

    public void data(UserProfile user, Project project, Long dataId, boolean create) {
        String type = create ? "data-create" : "data-delete";
        String path = project.getReportedPath(context);
        logsDao.log(user, type, path, project, project.getAuthor(), String.valueOf(dataId));
    }

    public void reportedContentRemoved(UserProfile admin, VotableEntity entity) {
        reportedContentAction(admin, entity, false);
    }

    public void reportedContentApproved(UserProfile admin, VotableEntity entity) {
        reportedContentAction(admin, entity, true);
    }

    public void reportedContentAction(UserProfile admin, VotableEntity entity, boolean approved) {
        String type = approved ? "reported-content-approved" : "reported-content-removed";
        String path = entity.getReportedPath(context);
        logsDao.log(admin, type, path, entity, entity.getAuthor(), null);
    }

    public void usersMerged(UserProfile user, UserProfile mergedUser) {
        logsDao.log(user, "users-merged", null, null, mergedUser, mergedUser.getUsername());
    }

    public void socialPost(UserProfile user, String path, String network, String url) {
        logsDao.log(user, String.format("social-post-%s", network), path, null, null, url);
    }

    public void loggedIn(UserProfile user) {
        logsDao.log(user, "logged-in", null, null, null, null);
    }

    public void loggedOut(UserProfile user) {
        logsDao.log(user, "logged-out", null, null, null, null);
    }

    public boolean userRecentAction(UserProfile user, int sessionTimeout) {
        return logsDao.userRecentAction(user, sessionTimeout);
    }

    public List<UserProfile> getRecentUsers(long sessionTimeout) {
        return logsDao.getRecentUsers(sessionTimeout);
    }

}
