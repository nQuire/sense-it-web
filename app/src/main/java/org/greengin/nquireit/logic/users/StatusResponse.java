package org.greengin.nquireit.logic.users;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.users.UserProfile;

import java.util.HashMap;
import java.util.Map;

public class StatusResponse {

    @Getter
    @Setter
    boolean logged;

    @Getter
    @Setter
    boolean admin;

    @Getter
    @Setter
    UserProfile profile;

    @Getter
    @Setter
    Map<String, StatusConnectionResponse> connections = new HashMap<String, StatusConnectionResponse>();

    @Getter
    @Setter
    String token;

    @Getter
    @Setter
    Map<String, String> responses = new HashMap<String, String>();


}