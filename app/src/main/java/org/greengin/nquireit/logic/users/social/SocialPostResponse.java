package org.greengin.nquireit.logic.users.social;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by evilfer on 7/23/14.
 */
public class SocialPostResponse {

    @Setter
    @Getter
    String url;

    @Getter
    @Setter
    String error;

    public SocialPostResponse() {
        this.url = null;
        this.error = null;
    }

}
