package org.greengin.nquireit.logic.project;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.projects.Project;

/**
 * Created by evilfer on 6/18/14.
 */
public class MyProjectResponse {
    @Getter
    @Setter
    Long id;

    @Getter
    @Setter
    String title;

    public MyProjectResponse(Project p) {
        this.id = p.getId();
        this.title = p.getTitle();
    }
}
