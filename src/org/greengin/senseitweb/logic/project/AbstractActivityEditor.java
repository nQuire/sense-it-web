package org.greengin.senseitweb.logic.project;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.AbstractActivity;

public class AbstractActivityEditor<T extends AbstractActivity> extends ProjectEditor {
	
	protected T activity;
	Class<T> type;
	
	public AbstractActivityEditor(Long projectId, HttpServletRequest request, Class<T> type) {
		super(projectId, request);
		this.type = type;
		
		if (type.isInstance(project.getActivity())) {
			activity = type.cast(project.getActivity());
		} else {
			hasAccess = false;			
		}
	}

}
