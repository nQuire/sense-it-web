package org.greengin.senseitweb.logic.project;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.AbstractContentManager;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.permissions.AccessLevel;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Vector;

public class ProjectListActions extends AbstractContentManager {

    static final String PROJECTS_QUERY = String.format("SELECT p FROM %s p", Project.class.getName());

    public ProjectListActions(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }

    public ProjectListActions(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }


    /**
     * any user actions *
     */
    public List<Project> getProjects() {
        List<Project> filtered = new Vector<Project>();
        if (hasAccess(PermissionType.BROWSE)) {
            EntityManager em = context.createEntityManager();
            TypedQuery<Project> query = em.createQuery(PROJECTS_QUERY, Project.class);
            List<Project> all = query.getResultList();
            for (Project p : all) {
                AccessLevel access = context.getSubscriptionManager().getAccessLevel(p, user);
                if (access.isAdmin() || p.getOpen()) {
                    filtered.add(p);
                }
            }
        }

        return filtered;
    }

    /**
     * registered user actions *
     */
    public Long createProject(ProjectCreationRequest projectData) {
        if (hasAccess(PermissionType.CREATE_PROJECT)) {
            EntityManager em = context.createEntityManager();
            em.getTransaction().begin();
            Project project = new Project();
            projectData.initProject(project);
            em.persist(project);
            context.getSubscriptionManager().projectCreatedInTransaction(em, project, user);
            em.getTransaction().commit();

            return project.getId();
        } else {
            return null;
        }
    }
}
