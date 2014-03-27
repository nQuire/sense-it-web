package org.greengin.senseitweb.logic.project;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.projects.Project;

import java.util.HashMap;

public class ProjectRequest {
    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    HashMap<String, String> description;


    public void updateProject(Project project) {
        if (title != null) {
            project.setTitle(title);
        }

        if (description != null) {
            project.setDescription(description);
        }
    }
}
