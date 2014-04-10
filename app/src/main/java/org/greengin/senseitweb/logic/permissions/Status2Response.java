package org.greengin.senseitweb.logic.permissions;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.users.UserProfile2;

public class Status2Response {

    @Getter
    @Setter
    boolean logged;

    @Getter
    @Setter
    UserProfile2 profile;

    @Getter
    @Setter
    String token;

}