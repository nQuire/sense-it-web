package org.greengin.senseitweb.logic.users;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.logic.users.StatusResponse;

public class RegistrationResponse {

    @Getter
    @Setter
    StatusResponse response;

    @Getter
    @Setter
    String explanation;

}