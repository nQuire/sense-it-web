package org.greengin.senseitweb.logic;


import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.UserProfile;

public class AbstractContentManager {
	protected UserProfile user;
    protected boolean tokenOk;
    protected boolean loggedIn;
    protected boolean loggedWithToken;

	protected HttpServletRequest request;

    protected ContextBean context;

    public AbstractContentManager(ContextBean context, HttpServletRequest request) {
        this(context, context.getUsersManager().currentUser(request), context.getUsersManager().checkToken(request));
    }

    public AbstractContentManager(ContextBean context, UserProfile user, boolean tokenOk) {
        this.context = context;
        this.user = user;
        this.loggedIn = user != null;
        this.tokenOk = tokenOk;
        this.loggedWithToken = loggedIn && tokenOk;
    }


	public UserProfile getCurrentUser() {
		return user;
	}
	
	public boolean hasAccess(PermissionType permission) {
		return permission == PermissionType.BROWSE ||
                (permission == PermissionType.CREATE_PROJECT && loggedWithToken);
	}
}
