package org.greengin.nquireit.logic.project.spotit;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by evilfer on 5/12/14.
 */
public class JoinedProjectResponse {
    @Getter
    @Setter
    Long id;

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    String description;

    @Getter
    @Setter
    Boolean geolocated;

    @Getter
    @Setter
    String author;

}
