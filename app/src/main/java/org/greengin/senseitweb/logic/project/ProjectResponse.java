package org.greengin.senseitweb.logic.project;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.permissions.AccessLevel;

public class ProjectResponse {
	Project project;
	AccessLevel access;
	
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	public AccessLevel getAccess() {
		return access;
	}
	public void setAccess(AccessLevel access) {
		this.access = access;
	}

}
