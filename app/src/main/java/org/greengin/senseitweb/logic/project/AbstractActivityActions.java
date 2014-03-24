package org.greengin.senseitweb.logic.project;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.AbstractActivity;
import org.greengin.senseitweb.logic.permissions.Role;

public class AbstractActivityActions<T extends AbstractActivity> extends ProjectActions {
	
	protected T activity;
	boolean validActivity;
	Class<T> type;
	
	public AbstractActivityActions(Long projectId, HttpServletRequest request, Class<T> type) {
		super(projectId, request);
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
