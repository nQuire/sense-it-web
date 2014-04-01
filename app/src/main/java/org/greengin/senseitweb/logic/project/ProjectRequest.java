package org.greengin.senseitweb.logic.project;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.projects.Project;

import java.util.HashMap;
import java.util.Map;

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
            if (project.getDescription() == null) {
                project.setDescription(description);
            } else {
                for (Map.Entry<String, String> entry : description.entrySet()) {
                    if (entry.getValue() == null) {
                        project.getDescription().remove(entry.getKey());
                    } else {
                        project.getDescription().put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
    }
}
