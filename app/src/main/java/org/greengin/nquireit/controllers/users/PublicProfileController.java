package org.greengin.nquireit.controllers.users;

import com.fasterxml.jackson.annotation.JsonView;
import com.mangofactory.jsonview.ResponseView;
import org.apache.commons.lang3.text.WordUtils;
import org.greengin.nquireit.json.JacksonObjectMapper;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.files.RequestsUtils;
import org.greengin.nquireit.logic.users.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.google.api.Google;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Handles requests for the application home page.
 */
@Controller
public class PublicProfileController {


    @Autowired
    ContextBean context;


    @RequestMapping(value = "/api/profiles/{userId}", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(value = Views.UserProfileData.class)
    public PublicProfileResponse getPublicProfile(@PathVariable Long userId, HttpServletRequest request) {
        return context.getUsersManager().getPublicProfile(userId);
    }

}
