package org.greengin.senseitweb.logic.users;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.users.UserProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class StatusResponse {

    @Getter
    @Setter
    boolean logged;

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