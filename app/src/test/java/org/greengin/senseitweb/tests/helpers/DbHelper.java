package org.greengin.senseitweb.tests.helpers;

import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.users.OpenIdEntity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.project.ProjectCreationRequest;
import org.greengin.senseitweb.logic.project.ProjectListActions;

import javax.persistence.EntityManager;
import java.util.List;

public class DbHelper {

    ContextBean context;

    public DbHelper(ContextBean context) {
        this.context = context;
    }

    public void clear() {
        EntityManager em = context.createEntityManager();

        String[] types = new String[] {"Role", "Project", "AbstractEntity"};

        for (String type : types) {
            List<Long> ids = em.createQuery(String.format("SELECT e.id from %s e", type), Long.class).getResultList();

            for (Long id : ids) {
                try {
                    AbstractEntity entity = em.find(AbstractEntity.class, id);
                    if (entity != null) {
                        try {
                            em.getTransaction().begin();
                            em.remove(entity);
                            em.getTransaction().commit();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            em.getTransaction().rollback();
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public Project getProject(Long id) {
        return context.createEntityManager().find(Project.class, id);
    }

    public UserProfile createUser(String name) {

        EntityManager em = context.createEntityManager();
        em.getTransaction().begin();
        UserProfile user = new UserProfile();
        user.setName(name);
        OpenIdEntity openId = new OpenIdEntity();
        openId.setOpenId(name);
        user.getOpenIds().add(openId);
        em.persist(user);
        em.getTransaction().commit();

        return user;
    }

    public Project createProject(UserProfile user) {
        return createProject(user, "project1", ProjectType.SENSEIT);
    }

    public Project createProject( UserProfile user, String title, ProjectType type) {
        ProjectCreationRequest projectRequest = new ProjectCreationRequest();
        projectRequest.setType(type);
        projectRequest.setTitle(title);

        ProjectListActions actions = new ProjectListActions(context, user, true);
        Long id = actions.createProject(projectRequest);
        return context.createEntityManager().find(Project.class, id);
    }


    public List<UserProfile> users() {
        return context.createEntityManager().createQuery("SELECT u from UserProfile u", UserProfile.class).getResultList();
    }

}
