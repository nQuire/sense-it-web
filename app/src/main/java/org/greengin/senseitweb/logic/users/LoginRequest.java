package org.greengin.senseitweb.logic.users;


import lombok.Getter;
import lombok.Setter;

public class LoginRequest {

    @Getter
    @Setter
    String username;


    @Getter
    @Setter
    String password;
}
