package org.greengin.nquireit.controllers.activities.senseit;

import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.senseit.SenseItActivityActions;
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
