package org.greengin.senseitweb.tests.helpers;

import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactory;
import org.greengin.senseitweb.logic.project.ProjectCreationRequest;
import org.greengin.senseitweb.logic.project.ProjectListActions;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by evilfer on 3/31/14.
 */
public class DbHelper {

    CustomEntityManagerFactory emf;

    public DbHelper(CustomEntityManagerFactory emf) {
        this.emf = emf;
    }

    public void clear() {
        EntityManager em = emf.createEntityManager();

        String[] types = new String[] {"RoleContextEntity", "AbstractEntity"};

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
        return emf.createEntityManager().find(Project.class, id);
    }

    public UserProfile createUser(String name) {
        UserProfile user = new UserProfile();
        user.setName(name);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        return user;
    }

    public Project createProject(SubscriptionManager subscriptionManager, UserProfile user) {
        return createProject(subscriptionManager, user, "project1", ProjectType.SENSEIT);
    }

    public Project createProject(SubscriptionManager subscriptionManager, UserProfile user, String title, ProjectType type) {
        ProjectCreationRequest projectRequest = new ProjectCreationRequest();
        projectRequest.setType(type);
        projectRequest.setTitle(title);

        ProjectListActions actions = new ProjectListActions(subscriptionManager, user, true, emf.createEntityManager());
        Long id = actions.createProject(projectRequest);
        return emf.createEntityManager().find(Project.class, id);
    }


    public List<UserProfile> users() {
        return emf.createEntityManager().createQuery("SELECT u from UserProfile u", UserProfile.class).getResultList();
    }

}
