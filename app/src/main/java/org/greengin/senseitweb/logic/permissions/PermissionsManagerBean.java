package org.greengin.senseitweb.logic.permissions;


import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.RoleType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;

public class PermissionsManagerBean {
	

    @Autowired
    SubscriptionManagerBean subscriptionManager;

	
	public boolean canCreateProject(UserProfile user) {
		return user != null;
	}
	
	public boolean canEditProject(Project project, UserProfile user) {
		return subscriptionManager.is(RoleType.ADMIN, project, user);
	}
	
	public boolean canManageProject(Project project, UserProfile user) {
		return subscriptionManager.is(RoleType.ADMIN, project, user);
	}

}
