package org.greengin.senseitweb.logic.project;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;

public class ProjectCreationRequest extends ProjectRequest {
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
		case CHALLENGE:
			project.setActivity(new ChallengeActivity());
			break;
		case SENSEIT:
			project.setActivity(new SenseItActivity());
			break;
		}
	}
}
