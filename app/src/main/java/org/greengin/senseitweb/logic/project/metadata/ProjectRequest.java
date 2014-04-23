package org.greengin.senseitweb.logic.project.metadata;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectDescription;

import java.util.Map;

public class ProjectRequest {
    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    ProjectDescription description;

}
