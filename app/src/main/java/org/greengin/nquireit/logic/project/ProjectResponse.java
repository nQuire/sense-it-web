package org.greengin.nquireit.logic.project;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.logic.users.AccessLevel;

import java.util.HashMap;

public class ProjectResponse {
    @Getter
    @Setter
    Project project;

    @Getter
    @Setter
    AccessLevel access;

    @Getter
    @Setter
    HashMap<String, Long> data;

}
