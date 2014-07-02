package org.greengin.nquireit.logic.admin;

import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AdminActions extends AbstractContentManager {

    boolean isAdmin;

    public AdminActions(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
        init();
    }

    public AdminActions(ContextBean context, HttpServletRequest request) {
        super(context, request);
        init();
    }

    private void init() {
        this.isAdmin = loggedWithToken && user.isAdmin();
    }


    public List<UserProfile> getUsers() {
        return isAdmin ? context.getUserProfileDao().listUsers() : null;
    }

    public void setAdmin(Long userId, UserAdminRequest data) {
        if (isAdmin) {
            context.getUserProfileDao().setAdmin(userId, data.isAdmin());
        }
    }
}
