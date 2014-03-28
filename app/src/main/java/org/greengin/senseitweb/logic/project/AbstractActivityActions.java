package org.greengin.senseitweb.logic.project;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.AbstractActivity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.data.FileManager;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;

public class AbstractActivityActions<T extends AbstractActivity> extends ProjectActions {
	
	protected T activity;
	boolean validActivity;
	Class<T> type;

    public AbstractActivityActions(Long projectId, Class<T> type, SubscriptionManager subscriptionManager, FileManager fileManager, UserProfile user, boolean tokenOk, EntityManager em) {
        super(projectId, subscriptionManager, fileManager, user, tokenOk, em);
        setType(type);
    }

    public AbstractActivityActions(Long projectId, Class<T> type, SubscriptionManager subscriptionManager, FileManager fileManager, UsersManager usersManager, EntityManager em, HttpServletRequest request) {
        super(projectId, subscriptionManager, fileManager, usersManager, em, request);
        setType(type);
    }

    private void setType(Class<T> type) {
        this.type = type;

        if (type.isInstance(project.getActivity())) {
            activity = type.cast(project.getActivity());
            validActivity = true;
        } else {
            validActivity = false;
        }
    }

	@Override
	public boolean hasAccess(Role role) {
		return validActivity && super.hasAccess(role);
	}

}
