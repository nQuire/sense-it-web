package org.greengin.nquireit.logic.users;


import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.users.UserProfile;

import java.util.List;


public class LoggedInProfilesResponse {

    @Setter
    @Getter
    List<UserProfile> users;

    @Getter
    @Setter
    int count;
}
