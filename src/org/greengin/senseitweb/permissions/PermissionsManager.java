package org.greengin.senseitweb.permissions;


import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.subscriptions.SubscriptionType;
import org.greengin.senseitweb.entities.users.UserProfile;

public class PermissionsManager {
	

	private static PermissionsManager pm = new PermissionsManager();

	public static PermissionsManager get() {
		return pm;
	}

	private PermissionsManager() {

	}

	
	public boolean canCreateProject(UserProfile user) {
		return user != null;
	}
	
	public boolean canEditProject(Project project, UserProfile user) {
		return SubscriptionManager.get().is(SubscriptionType.ADMIN, project, user);
	}
}
