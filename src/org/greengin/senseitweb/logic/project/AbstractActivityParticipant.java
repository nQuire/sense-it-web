package org.greengin.senseitweb.logic.project;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.AbstractActivity;

public class AbstractActivityParticipant<T extends AbstractActivity> extends ProjectParticipant {

	protected T activity;

	public AbstractActivityParticipant(Long projectId, HttpServletRequest request, Class<T> type) {
		super(projectId, request);
		if (type.isInstance(project.getActivity())) {
			this.hasAccess = true;
			this.activity = type.cast(project.getActivity());
		} else {
			this.hasAccess = false;
		}
	}
	
}
