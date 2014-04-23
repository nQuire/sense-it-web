package org.greengin.senseitweb.tests.helpers;

import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.greengin.senseitweb.logic.project.ProjectCreationRequest;

import javax.persistence.EntityManager;
import java.util.List;

public class DbHelper {

    private final String userConnectionTableDrop = "DROP TABLE IF EXISTS UserConnection;";
    private final String userConnectionTableCreate = "create table UserConnection (userId varchar(255) not null,\n" +
            "\tproviderId varchar(255) not null,\n" +
            "\tproviderUserId varchar(255),\n" +
            "\trank int not null,\n" +
            "\tdisplayName varchar(255),\n" +
            "\tprofileUrl varchar(512),\n" +
            "\timageUrl varchar(512),\n" +
            "\taccessToken varchar(255) not null,\n" +
            "\tsecret varchar(255),\n" +
            "\trefreshToken varchar(255),\n" +
            "\texpireTime bigint,\n" +
            "\tprimary key (userId, providerId, providerUserId));";
    private final String userConnectionTableIndex = "create unique index UserConnectionRank on UserConnection(userId, providerId, rank);";

    ContextBean context;

    public DbHelper(ContextBean context) {
        this.context = context;
    }

    public void clear() {
        EntityManager em = context.createEntityManager();

        em.getTransaction().begin();
        em.createNativeQuery(userConnectionTableDrop).executeUpdate();
        em.createNativeQuery(userConnectionTableCreate).executeUpdate();
        em.createNativeQuery(userConnectionTableIndex).executeUpdate();
        em.getTransaction().commit();

        String[] types = new String[]{"Role", "Project", "AbstractEntity"};

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
        user.setUsername(name);
        em.persist(user);
        em.getTransaction().commit();

        return user;
    }

    public Project createProject(UserProfile user) {
        return createProject(user, ProjectType.SENSEIT);
    }

    public Project createProject(UserProfile user, ProjectType type) {
        ProjectCreationRequest projectRequest = new ProjectCreationRequest();
        projectRequest.setType(type);

        ProjectActions actions = new ProjectActions(context, null, user, true);
        Long id = actions.createProject(projectRequest);
        return context.createEntityManager().find(Project.class, id);
    }


    public List<UserProfile> users() {
        return context.createEntityManager().createQuery("SELECT u from UserProfile u", UserProfile.class).getResultList();
    }

}
