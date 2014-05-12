package org.greengin.nquireit.logic.project;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by evilfer on 4/23/14.
 */
public class ProjectListResponse {

    @Setter
    @Getter
    List<ProjectResponse> list;

    @Getter
    @Setter
    HashMap<String, Integer> categories;
}
