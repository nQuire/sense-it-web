package org.greengin.nquireit.logic.project;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.activities.challenge.ChallengeActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.spotit.SpotItActivity;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectMetadata;
import org.greengin.nquireit.entities.projects.ProjectMetadataBlock;
import org.greengin.nquireit.entities.projects.ProjectType;
import org.greengin.nquireit.logic.ContextBean;

import java.util.Vector;

public class ProjectCreationRequest {
    @Getter
    @Setter
    ProjectType type;

    @Getter
    @Setter
    String title;

    public void initProject(Project project, ContextBean context) {
        project.setType(type);
        project.setTitle(title);
        project.setMetadata(new ProjectMetadata());
        project.getMetadata().setBlocks(new Vector<ProjectMetadataBlock>());

        switch (type) {
            case CHALLENGE:
                project.setActivity(new ChallengeActivity());
                context.getChallengeDao().initProject(project);
                break;
            case SENSEIT:
                project.setActivity(new SenseItActivity());
                context.getSenseItDao().initProject(project);
                break;
            case SPOTIT:
                project.setActivity(new SpotItActivity());
                context.getSpotItDao().initProject(project);
                break;
        }
    }
}
