package org.greengin.senseitweb.tests;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.data.FileManagerBean;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManagerBean;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactoryBean;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.greengin.senseitweb.tests.helpers.DbHelper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by evilfer on 3/31/14.
 */
public abstract class TestsBase {

    @Autowired
    protected ContextBean context;


    protected DbHelper helper;

    @Before
    public void before() {

        helper = new DbHelper(context);
        helper.clear();
    }

    public ProjectActions projectActions(Long projectId, UserProfile user) {
        return new ProjectActions(projectId, subscriptionManager, fileManager, user, true, entityManagerFactory.createEntityManager());
    }

    public AccessLevel accessLevel(Long projectId, UserProfile user) {
        return subscriptionManager.getAccessLevel(helper.getProject(projectId), user);
    }

}
