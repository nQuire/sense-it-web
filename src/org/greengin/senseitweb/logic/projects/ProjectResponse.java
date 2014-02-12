package org.greengin.senseitweb.logic.projects;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.permissions.AccessLevel;

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
