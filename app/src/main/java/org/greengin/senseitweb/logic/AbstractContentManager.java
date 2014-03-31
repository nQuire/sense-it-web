package org.greengin.senseitweb.logic;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.Role;
import org.greengin.senseitweb.entities.users.RoleType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.UsersManager;

public class AbstractContentManager {
	protected UserProfile user;
	protected EntityManager em;
    protected boolean tokenOk;
    protected boolean loggedIn;
    protected boolean loggedWithToken;

	protected HttpServletRequest request;

    public AbstractContentManager(UserProfile user, boolean tokenOk, EntityManager em) {
        set(user, tokenOk, em);
    }

	public AbstractContentManager(UsersManager usersManager, EntityManager em, HttpServletRequest request) {
        UserProfile user = usersManager.currentUser(request);
        boolean tokenOk = usersManager.checkToken(request);

        set(user, tokenOk, em);
	}

    private void set(UserProfile user, boolean tokenOk, EntityManager em) {
        this.user = user;
        this.loggedIn = user != null;
        this.tokenOk = tokenOk;
        this.loggedWithToken = loggedIn && tokenOk;
        this.em = em;
    }

	public UserProfile getCurrentUser() {
		return user;
	}
	
	public boolean hasAccess(PermissionType permission) {
		return permission == PermissionType.BROWSE ||
                (permission == PermissionType.CREATE_PROJECT && loggedWithToken);
	}
}
