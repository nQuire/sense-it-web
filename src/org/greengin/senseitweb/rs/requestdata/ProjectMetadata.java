package org.greengin.senseitweb.rs.requestdata;

import org.greengin.senseitweb.entities.Project;

public class ProjectMetadata {
	String title;
	String description;
	
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public void updateProject(Project project) {
		project.setTitle(title);
		project.setDescription(description);
	}
}
