package org.greengin.nquireit.controllers.users;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.users.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles requests for the application home page.
 */
@Controller
public class PublicProfileController {


    @Autowired
    ContextBean context;


    @RequestMapping(value = "/api/profiles/{userId}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public PublicProfileResponse getPublicProfile(@PathVariable Long userId, HttpServletRequest request) {
        return context.getUsersManager().getPublicProfile(userId);
    }

}
