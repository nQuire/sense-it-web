package org.greengin.senseitweb.logic.project.senseit;

import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.subscriptions.Subscription;
import org.greengin.senseitweb.entities.subscriptions.SubscriptionType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.AbstractContentManager;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.utils.NamedObject;

public class JoinedProfilesActions extends AbstractContentManager {

	static final String PROJECTS_QUERY = String.format(
			"SELECT DISTINCT p FROM %s s INNER JOIN s.project p WHERE s.user=:user AND s.type=:access AND p.type=:type",
			Subscription.class.getName());

    public JoinedProfilesActions(UserProfile user, boolean tokenOk, EntityManager em) {
        super(user, tokenOk, em);
    }

    public JoinedProfilesActions(UsersManager usersManager, EntityManager em, HttpServletRequest request) {
        super(usersManager, em, request);
    }

    /** logged in user actions **/

	public JoinedProfilesResponse joinedProfiles() {
		if (hasAccess(Role.LOGGEDIN)) {
			JoinedProfilesResponse response = new JoinedProfilesResponse();
			TypedQuery<Project> query = em.createQuery(PROJECTS_QUERY, Project.class);
			query.setParameter("access", SubscriptionType.MEMBER);
			query.setParameter("type", ProjectType.SENSEIT);
			query.setParameter("user", user);
			List<Project> projects = query.getResultList();
			for (Project p : projects) {
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
