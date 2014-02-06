package org.greengin.senseitweb.rs.requestdata;

import org.greengin.senseitweb.entities.Project;
import org.greengin.senseitweb.entities.ProjectType;

public class ProjectMetadata {
	ProjectType type;
	String title;
	String description;
	
	
	public ProjectType getType() {
		return type;
	}


	public void setType(ProjectType type) {
		this.type = type;
	}


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
		project.setType(type);
		project.setTitle(title);
		project.setDescription(description);
	}
}
