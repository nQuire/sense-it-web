package org.greengin.nquireit.controllers.users;


import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.users.ProfileRequest;
import org.greengin.nquireit.logic.users.SocialPostRequest;
import org.greengin.nquireit.logic.users.StatusResponse;
import org.greengin.nquireit.logic.users.UserProfileActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PostData;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.plus.moments.Moment;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TweetData;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by evilfer on 7/23/14.
 */
@Controller
public class SocialController {

    @Inject
    @Qualifier("facebook")
    Connection<Facebook> facebook;


    @Inject
    @Qualifier("twitter")
    Connection<Twitter> twitter;

    @Autowired
    ContextBean context;


    @RequestMapping(value = "/api/social/twitter/post", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(value = Views.UserProfileData.class)
    public Long update(@RequestBody SocialPostRequest data, HttpServletRequest request) {
        if (twitter.test()) {
            Tweet tweet = twitter.getApi().timelineOperations().updateStatus(data.getContent());
            return tweet.getId();
        }
        return null;
    }

}
