package org.greengin.senseitweb.permissions;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.users.UserProfile;

public class PermissionsManager {
	

	private static PermissionsManager pm = new PermissionsManager();

	public static PermissionsManager get() {
		return pm;
	}

	private PermissionsManager() {

	}

	
	public UserProfile canCreateProject(HttpServletRequest request) {
		return UsersManager.get().currentUser(request);
	}
}
