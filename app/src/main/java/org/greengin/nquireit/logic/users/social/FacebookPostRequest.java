package org.greengin.nquireit.logic.users.social;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by evilfer on 7/23/14.
 */
public class FacebookPostRequest {

    @Getter
    @Setter
    String link;

    @Getter
    @Setter
    String linkName;

    @Getter
    @Setter
    String caption;

    @Getter
    @Setter
    String description;
}
