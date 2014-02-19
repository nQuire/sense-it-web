package org.greengin.senseitweb.logic;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.persistence.EMF;

public class AbstractContentManager {
	protected UserProfile user;
	protected EntityManager em;
	protected boolean isLoggedIn;
	
	public AbstractContentManager(HttpServletRequest request) {
		this.em = EMF.get().createEntityManager();
		this.user = UsersManager.get().currentUser(request);
		this.isLoggedIn = user != null;
	}

	public UserProfile getCurrentUser() {
		return user;
	}
	
	public boolean hasAccess(Role role) {
		return role == Role.NONE || (isLoggedIn && role == Role.LOGGEDIN);
	}
}
