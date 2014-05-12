package org.greengin.nquireit.logic.project.spotit;

import org.greengin.nquireit.entities.activities.base.BaseAnalysis;
import org.greengin.nquireit.entities.activities.spotit.SpotItActivity;
import org.greengin.nquireit.entities.activities.spotit.SpotItObservation;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.data.DataActions;

import javax.servlet.http.HttpServletRequest;

public class SpotItActivityActions extends DataActions<SpotItObservation, BaseAnalysis, SpotItActivity> {


    public SpotItActivityActions(ContextBean context, Long projectId, UserProfile user, boolean tokenOk) {
        super(context, projectId, SpotItActivity.class, SpotItObservation.class, BaseAnalysis.class, user, tokenOk);
    }

    public SpotItActivityActions(ContextBean context, Long projectId, HttpServletRequest request) {
        super(context, projectId, SpotItActivity.class, SpotItObservation.class, BaseAnalysis.class, request);
    }

}
