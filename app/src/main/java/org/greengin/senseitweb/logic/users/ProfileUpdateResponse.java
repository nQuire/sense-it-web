package org.greengin.senseitweb.logic.users;


import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.users.UserProfile;

public class ProfileUpdateResponse {

    @Getter
    @Setter
    UserProfile profile;

    @Getter
    @Setter
    String explanation;
}
