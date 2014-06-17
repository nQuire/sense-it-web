package org.greengin.nquireit.logic.project;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.activities.challenge.ChallengeActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.spotit.SpotItActivity;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectType;

public class ProjectCreationRequest {
    @Getter
    @Setter
    ProjectType type;

    @Getter
    @Setter
    String title;

    public void initProject(Project project) {
        project.setType(type);
        project.setTitle(title);

        switch (type) {
            case CHALLENGE:
                project.setActivity(new ChallengeActivity());
                break;
            case SENSEIT:
                project.setActivity(new SenseItActivity());
                break;
            case SPOTIT:
                project.setActivity(new SpotItActivity());
                break;
        }
    }
}
