package org.greengin.nquireit.logic.project.metadata;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.projects.ProjectDescription;

public class ProjectRequest {
    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    ProjectDescription description;

}
