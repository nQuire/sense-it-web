package org.greengin.nquireit.logic.users;


import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.users.UserProfile;

import java.util.List;
import java.util.Vector;

public class LoggedInProfilesResponse {

    @Getter
    List<UserProfile> users = new Vector<UserProfile>();

    @Getter
    @Setter
    int count;
}
