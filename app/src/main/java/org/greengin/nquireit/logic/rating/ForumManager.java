package org.greengin.nquireit.logic.rating;

import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by evilfer on 6/26/14.
 */
public class ForumManager extends AbstractContentManager {
    public ForumManager(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }

    public ForumManager(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }


}
