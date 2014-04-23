package org.greengin.senseitweb.logic.project;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.logic.project.metadata.ProjectRequest;

public class ProjectCreationRequest {
    @Getter
    @Setter
    ProjectType type;

    public void initProject(Project project) {
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
