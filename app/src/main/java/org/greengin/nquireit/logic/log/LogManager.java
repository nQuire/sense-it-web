package org.greengin.nquireit.logic.log;

import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by evilfer on 6/26/14.
 */
public class LogManager extends AbstractContentManager {

    public LogManager(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }

    public LogManager(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }

    /* view actions */

    public Boolean pageView(String path, String session) {
        return context.getLogManager().pageView(user, path, session);
    }


}
