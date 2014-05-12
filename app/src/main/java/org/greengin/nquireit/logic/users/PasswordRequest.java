package org.greengin.nquireit.logic.users;


import lombok.Getter;
import lombok.Setter;

public class PasswordRequest {

    @Getter
    @Setter
    String oldPassword;


    @Getter
    @Setter
    String newPassword;
}
