package org.greengin.nquireit.logic.project.activity;

import javax.servlet.http.HttpServletRequest;

import org.greengin.nquireit.entities.activities.base.AbstractActivity;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.ProjectActions;

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
