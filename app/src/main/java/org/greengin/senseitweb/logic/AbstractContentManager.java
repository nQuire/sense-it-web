package org.greengin.senseitweb.logic;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.persistence.EMF;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractContentManager {
	protected UserProfile user;
	protected EntityManager em;
	protected boolean isLoggedIn;
	protected HttpServletRequest request;

    @Autowired
    UsersManager usersManager;


	public AbstractContentManager(HttpServletRequest request) {
		this.request = request;
		this.em = EMF.get().createEntityManager();
		
	}
	
	private void setUser(UserProfile user, String token) {
		this.user = usersManager.currentUser(request);
		this.isLoggedIn = user != null && usersManager.checkToken(request);
	}

	public UserProfile getCurrentUser() {
		return user;
	}
	
	public boolean hasAccess(Role role) {
		return role == Role.NONE || (isLoggedIn && role == Role.LOGGEDIN);
	}
	public boolean hasAccessIgnoreToken(Role role) {
		return role == Role.NONE || (user != null && role == Role.LOGGEDIN);
	}
}
