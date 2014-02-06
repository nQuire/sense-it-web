package org.greengin.senseitweb.rs.requestdata;

import org.greengin.senseitweb.entities.Project;
import org.greengin.senseitweb.entities.ProjectType;
import org.greengin.senseitweb.entities.senseit.SenseItActivity;

public class ProjectCreation extends ProjectMetadata {
	ProjectType type;

	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
	}

	public void initProject(Project project) {
		super.updateProject(project);
		project.setType(type);

		switch (type) {
		case SENSEIT:
			project.setActivity(new SenseItActivity());
		}
	}
}
