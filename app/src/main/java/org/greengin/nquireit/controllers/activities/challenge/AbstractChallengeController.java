package org.greengin.nquireit.controllers.activities.challenge;

import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.challenge.ChallengeActivityActions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class AbstractChallengeController {

    @Autowired
    ContextBean context;

    protected ChallengeActivityActions createManager(Long projectId, HttpServletRequest request) {
        return new ChallengeActivityActions(context, projectId, request);
    }
}
