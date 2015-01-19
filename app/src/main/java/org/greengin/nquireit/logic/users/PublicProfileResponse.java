package org.greengin.nquireit.logic.users;


import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.logic.project.UserProjectListResponse;

import java.util.HashMap;

public class PublicProfileResponse {

    @Getter
    @Setter
    Long id;

    @Getter
    @Setter
    String username;

    @Getter
    @Setter
    String image;

    @Getter
    @Setter
    HashMap<String, String> metadata = new HashMap<String, String>();

    @Getter
    @Setter
    UserProjectListResponse projects = new UserProjectListResponse();
}
