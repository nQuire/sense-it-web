package org.greengin.senseitweb.controllers.activities.senseit;

import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.data.FileManagerBean;
import org.greengin.senseitweb.logic.permissions.SubscriptionManagerBean;
import org.greengin.senseitweb.logic.permissions.UsersManagerBean;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactoryBean;
import org.greengin.senseitweb.logic.project.senseit.SenseItActivityActions;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItOperations;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by evilfer on 3/25/14.
 */
public class AbstractSenseItController {

    @Autowired
    ContextBean context;

    protected SenseItActivityActions createManager(Long projectId, HttpServletRequest request) {
        return new SenseItActivityActions(context, projectId, request);
    }
}
