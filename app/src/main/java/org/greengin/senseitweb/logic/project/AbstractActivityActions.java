package org.greengin.senseitweb.logic.project;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.AbstractActivity;
import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.ContextBean;

public class AbstractActivityActions<T extends AbstractActivity> extends ProjectActions {
	
	protected T activity;
	boolean validActivity;
	Class<T> type;

    public AbstractActivityActions(ContextBean context, Long projectId, Class<T> type, UserProfile user, boolean tokenOk) {
        super(context, projectId, user, tokenOk);
        setType(type);
    }

    public AbstractActivityActions(ContextBean context, Long projectId, Class<T> type, HttpServletRequest request) {
        super(context, projectId, request);
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
	public boolean hasAccess(PermissionType permission) {
		return validActivity && super.hasAccess(permission);
	}

}
