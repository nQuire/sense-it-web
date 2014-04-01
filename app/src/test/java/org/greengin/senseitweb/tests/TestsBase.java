package org.greengin.senseitweb.tests;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.data.FileManager;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactory;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.greengin.senseitweb.tests.helpers.DbHelper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by evilfer on 3/31/14.
 */
public abstract class TestsBase {

    @Autowired
    protected CustomEntityManagerFactory entityManagerFactory;

    @Autowired
    protected FileManager fileManager;

    @Autowired
    protected SubscriptionManager subscriptionManager;

    protected DbHelper helper;

    @Before
    public void before() {
        new DbHelper(entityManagerFactory).clear();
        helper = new DbHelper(entityManagerFactory);
    }

    public ProjectActions projectActions(Long projectId, UserProfile user) {
        return new ProjectActions(projectId, subscriptionManager, fileManager, user, true, entityManagerFactory.createEntityManager());
    }

    public AccessLevel accessLevel(Long projectId, UserProfile user) {
        return subscriptionManager.getAccessLevel(helper.getProject(projectId), user);
    }

}
