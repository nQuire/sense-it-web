package org.greengin.nquireit.logic.project.spotit;

import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;
import org.greengin.nquireit.entities.activities.spotit.SpotItActivity;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectType;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.users.RoleType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.utils.NamedObject;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

public class JoinedProjectsActions extends AbstractContentManager {

	static final String PROJECTS_QUERY = "SELECT DISTINCT e FROM Role r INNER JOIN r.context e WHERE r.user=:user AND r.type=:access";

    public JoinedProjectsActions(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }

    public JoinedProjectsActions(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }

    /** logged in user actions **/

	public Collection<JoinedProjectResponse> joinedProjects() {
		if (hasAccess(PermissionType.BROWSE)) {
            List<JoinedProjectResponse> projects = new Vector<JoinedProjectResponse>();

            EntityManager em = context.createEntityManager();

			TypedQuery<AbstractEntity> query = em.createQuery(PROJECTS_QUERY, AbstractEntity.class);
			query.setParameter("access", RoleType.MEMBER);
			query.setParameter("user", user);
			for (AbstractEntity context : query.getResultList()) {
                if (context instanceof Project) {
                    Project p = (Project) context;
                    if (p.getType() == ProjectType.SPOTIT) {
                        JoinedProjectResponse projectResponse = new JoinedProjectResponse();
                        projectResponse.setId(p.getId());
                        projectResponse.setTitle(p.getTitle());
                        projectResponse.setAuthor(p.getAuthor().getUsername());
                        projectResponse.setGeolocated(true);
                        projectResponse.setDescription(p.getDescription().getTeaser());

                        projects.add(projectResponse);
                    }
                }
			}

			return projects;
		}

		return null;
	}

}
