package org.greengin.senseitweb.controllers.activities.challenge;

import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.persistence.EntityManagerFactory;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by evilfer on 3/25/14.
 */
public class AbstractChallengeController {

    @Autowired
    SubscriptionManager subscriptionManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    UsersManager usersManager;

    protected ChallengeActivityActions createManager(Long projectId, HttpServletRequest request) {
        return new ChallengeActivityActions(projectId, subscriptionManager, usersManager, entityManagerFactory.createEntityManager(), request);
    }
}
