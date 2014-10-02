package org.greengin.nquireit.logic.admin;

import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActions extends AbstractContentManager {

    boolean isAdmin;

    public AdminActions(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
        init();
    }

    public AdminActions(ContextBean context, HttpServletRequest request) {
        super(context, request);
        init();
    }

    private void init() {
        this.isAdmin = loggedWithToken && user.isAdmin();
    }


    public List<UserProfile> getUsers() {
        return isAdmin ? context.getUserProfileDao().listUsers() : null;
    }

    public void setAdmin(Long userId, UserAdminRequest data) {
        if (isAdmin) {
            context.getUserProfileDao().setAdmin(userId, data.isAdmin());
        }
    }

    public List<Project> getProjects() {
        return isAdmin ? context.getProjectDao().getProjects() : null;
    }

    public void setFeatured(Long projectId, ProjectFeaturedRequest data) {
        if (isAdmin) {
            context.getProjectDao().setFeatured(projectId, data.isFeatured());
        }
    }

    public Boolean updateModel() {
        if (isAdmin) {
            context.getProjectDao().updateDataModel();
            return true;
        }
        return false;
    }

    public Boolean setText(String textId, String content) {
        if (isAdmin) {
            return context.getTextDao().setText(textId, content);
        }

        return false;

    }


    public HashMap<String, List<ReportedContent>> getReportedContent() {

        return context.getVotableDao().getReportedContent(context);
    }
}
