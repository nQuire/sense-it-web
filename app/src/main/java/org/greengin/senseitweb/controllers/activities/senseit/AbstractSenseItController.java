package org.greengin.senseitweb.controllers.activities.senseit;

import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactory;
import org.greengin.senseitweb.logic.project.senseit.SenseItActivityActions;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItOperations;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by evilfer on 3/25/14.
 */
public class AbstractSenseItController {

    @Autowired
    SubscriptionManager subscriptionManager;

    @Autowired
    CustomEntityManagerFactory entityManagerFactory;

    @Autowired
    UsersManager usersManager;

    @Autowired
    SenseItOperations senseItOperations;

    protected SenseItActivityActions createManager(Long projectId, HttpServletRequest request) {
        return new SenseItActivityActions(projectId, subscriptionManager, usersManager, entityManagerFactory.createEntityManager(), request);
    }
}
