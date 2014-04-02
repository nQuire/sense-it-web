package org.greengin.senseitweb.controllers.activities.challenge;

import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class AbstractChallengeController {

    @Autowired
    ContextBean context;

    protected ChallengeActivityActions createManager(Long projectId, HttpServletRequest request) {
        return new ChallengeActivityActions(context, projectId, request);
    }
}
