package org.greengin.senseitweb.logic.project;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.users.AccessLevel;

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
