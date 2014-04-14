package org.greengin.senseitweb.logic.users;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.users.UserProfile;

public class StatusConnectionResponse {

    @Getter
    @Setter
    String provider;

    @Getter
    @Setter
    boolean active;

    @Getter
    @Setter
    String providerProfileUrl;

}