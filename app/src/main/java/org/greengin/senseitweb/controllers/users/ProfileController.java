package org.greengin.senseitweb.controllers.users;

import org.greengin.senseitweb.logic.users.UserServiceBean;
import org.greengin.senseitweb.logic.users.ProfileRequest;
import org.greengin.senseitweb.logic.users.ProfileUpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/security")
public class ProfileController {

    @Autowired
    UserServiceBean userServiceBean;

    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    @ResponseBody
    public ProfileUpdateResponse update(@RequestBody ProfileRequest data) {
        return userServiceBean.updateProfile(data);
    }

}
