package org.greengin.senseitweb.logic.project.senseit;

import java.util.Vector;

import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.RoleType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.AbstractContentManager;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.permissions.UsersManagerBean;
import org.greengin.senseitweb.utils.NamedObject;

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
