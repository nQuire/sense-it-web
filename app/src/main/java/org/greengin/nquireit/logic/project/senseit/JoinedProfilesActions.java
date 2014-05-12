package org.greengin.nquireit.logic.project.senseit;

import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.users.RoleType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.utils.NamedObject;

public class JoinedProfilesActions extends AbstractContentManager {

	static final String PROJECTS_QUERY = "SELECT DISTINCT e FROM Role r INNER JOIN r.context e WHERE r.user=:user AND r.type=:access";

    public JoinedProfilesActions(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }

    public JoinedProfilesActions(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }

    /** logged in user actions **/

	public JoinedProfilesResponse joinedProfiles() {
		if (hasAccess(PermissionType.BROWSE)) {
            EntityManager em = context.createEntityManager();
			JoinedProfilesResponse response = new JoinedProfilesResponse();
			TypedQuery<AbstractEntity> query = em.createQuery(PROJECTS_QUERY, AbstractEntity.class);
			query.setParameter("access", RoleType.MEMBER);
			query.setParameter("user", user);
			for (AbstractEntity context : query.getResultList()) {
                if (context instanceof Project) {
                    Project p = (Project) context;
                    if (p.getActivity() instanceof SenseItActivity) {
                        SenseItActivity activity = (SenseItActivity) p.getActivity();
                        Vector<NamedObject<SenseItProfile>> profiles = new Vector<NamedObject<SenseItProfile>>();
                        profiles.add(new NamedObject<SenseItProfile>(p.getTitle(), activity.getProfile()));
                        response.put(p.getId(), profiles);
                    }

                }
			}
			return response;
		}

		return null;
	}

}
