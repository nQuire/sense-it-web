package org.greengin.nquireit.logic.users;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by evilfer on 7/1/14.
 */
public class RegisterRequest extends LoginRequest {

    @Getter
    @Setter
    String email;
}
