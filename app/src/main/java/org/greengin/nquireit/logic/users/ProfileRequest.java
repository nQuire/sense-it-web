package org.greengin.nquireit.logic.users;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class ProfileRequest {

    @Getter
    @Setter
    String username;

    @Getter
    @Setter
    HashMap<String, String> metadata;

}
