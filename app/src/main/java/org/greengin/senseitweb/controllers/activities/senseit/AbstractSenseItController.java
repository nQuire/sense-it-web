package org.greengin.senseitweb.controllers.activities.senseit;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.persistence.EntityManagerFactory;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
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
    EntityManagerFactory entityManagerFactory;

    @Autowired
    UsersManager usersManager;

    @Autowired
    SenseItOperations senseItOperations;

    protected SenseItActivityActions createManager(Long projectId, HttpServletRequest request) {
        return new SenseItActivityActions(projectId, senseItOperations, subscriptionManager, usersManager, entityManagerFactory.createEntityManager(), request);
    }
}
