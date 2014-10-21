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
            logsDao.log(user, type, path, target, target.getOwner(context), comment != null ? comment.getComment() : null);
        }
    }

    public void threadComment(UserProfile user, ForumThread thread, Comment comment) {
        if (thread != null && comment != null) {
            String type = "thread-comment";
            String path = thread.getReportedPath(context);
            logsDao.log(user, type, path, thread, thread.getOwner(context), comment.getComment());
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

        logsDao.log(user, type, path, target, target.getOwner(context), String.valueOf(value));
    }

    public void data(UserProfile user, Project project, Long dataId, boolean create) {
        String type = create ? "data-create" : "data-delete";
        String path = project.getReportedPath(context);
        logsDao.log(user, type, path, project, project.getOwner(context), String.valueOf(dataId));
    }
}
