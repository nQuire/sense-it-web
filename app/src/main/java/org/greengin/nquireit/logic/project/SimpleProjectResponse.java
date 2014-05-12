package org.greengin.nquireit.logic.project;

import lombok.Getter;
import lombok.Setter;

public class SimpleProjectResponse {
    @Getter
    @Setter
    Long id;

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    String author;

    @Getter
    @Setter
    boolean joined;

}
