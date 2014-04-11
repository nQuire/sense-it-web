package org.greengin.senseitweb.logic.users;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.users.UserProfile;

public class StatusResponse {

    @Getter
    @Setter
    boolean logged;

    @Getter
    @Setter
    UserProfile profile;

    @Getter
    @Setter
    String token;

}