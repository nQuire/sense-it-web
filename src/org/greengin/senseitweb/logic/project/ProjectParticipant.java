package org.greengin.senseitweb.logic.project;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.subscriptions.SubscriptionType;
import org.greengin.senseitweb.logic.AbstractContentEditor;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;

public class ProjectParticipant extends AbstractContentEditor {

	protected Long projectId;
	protected Project project;
	protected AccessLevel access;

	public ProjectParticipant(Long projectId, HttpServletRequest request) {
		super(request);
		this.projectId = projectId;
		this.project = em.find(Project.class, projectId);
		this.hasAccess = true;
		this.access = SubscriptionManager.get().getAccessLevel(project, request);
	}

	public AccessLevel join() {
		if (this.hasAccess && !this.access.isMember()) {
			return SubscriptionManager.get().subscribe(em, user, project, SubscriptionType.MEMBER);
		} else {
			return null;
		}
	}

	public AccessLevel leave() {
		if (this.hasAccess && this.access.isMember()) {
			return SubscriptionManager.get().unsubscribe(em, user, project, SubscriptionType.MEMBER);
		} else {
			return null;
		}
	}

}
