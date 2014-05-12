package org.greengin.nquireit.logic.users;

import lombok.Getter;
import lombok.Setter;

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