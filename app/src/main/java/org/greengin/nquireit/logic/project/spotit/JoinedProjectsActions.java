package org.greengin.nquireit.logic.project.spotit;

import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectType;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class JoinedProjectsActions extends AbstractContentManager {


    public JoinedProjectsActions(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }

    public JoinedProjectsActions(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }

    /**
     * logged in user actions *
     */

    public HashMap<Long, JoinedProjectResponse> joinedProjects() {
        if (hasAccess(PermissionType.BROWSE)) {
            HashMap<Long, JoinedProjectResponse> projects = new HashMap<Long, JoinedProjectResponse>();

            for (Project p : context.getProjectDao().joinedProjects(user)) {
                if (p.getType() == ProjectType.SPOTIT) {
                    JoinedProjectResponse projectResponse = new JoinedProjectResponse();
                    projectResponse.setId(p.getId());
                    projectResponse.setTitle(p.getTitle());
                    projectResponse.setAuthor(p.getAuthor().getUsername());
                    projectResponse.setGeolocated(true);
                    projectResponse.setDescription(p.getMetadata().getTeaser());

                    projects.put(p.getId(), projectResponse);
                }
            }

            return projects;
        }

        return null;
    }

}
