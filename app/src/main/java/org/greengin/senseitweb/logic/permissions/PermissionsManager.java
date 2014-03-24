package org.greengin.senseitweb.logic.permissions;


import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.subscriptions.SubscriptionType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;

public class PermissionsManager {
	

    @Autowired
    SubscriptionManager subscriptionManager;

	
	public boolean canCreateProject(UserProfile user) {
		return user != null;
	}
	
	public boolean canEditProject(Project project, UserProfile user) {
		return subscriptionManager.is(SubscriptionType.ADMIN, project, user);
	}
	
	public boolean canManageProject(Project project, UserProfile user) {
		return subscriptionManager.is(SubscriptionType.ADMIN, project, user);
	}

}
