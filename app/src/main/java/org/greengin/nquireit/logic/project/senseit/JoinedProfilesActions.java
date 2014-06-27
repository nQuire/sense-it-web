package org.greengin.nquireit.logic.project.senseit;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.utils.NamedObject;

public class JoinedProfilesActions extends AbstractContentManager {

    public JoinedProfilesActions(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }

    public JoinedProfilesActions(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }

    /**
     * logged in user actions *
     */

    public JoinedProfilesResponse joinedProfiles() {
        if (hasAccess(PermissionType.BROWSE)) {
            JoinedProfilesResponse response = new JoinedProfilesResponse();
            for (Project p : context.getProjectDao().joinedProjects(user)) {
                if (p.getActivity() instanceof SenseItActivity) {
                    SenseItActivity activity = (SenseItActivity) p.getActivity();
                    Vector<NamedObject<SenseItProfile>> profiles = new Vector<NamedObject<SenseItProfile>>();
                    profiles.add(new NamedObject<SenseItProfile>(p.getTitle(), activity.getProfile()));
                    response.put(p.getId(), profiles);
                }

            }
            return response;
        }

        return null;
    }

}
