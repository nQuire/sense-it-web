package org.greengin.nquireit.controllers.users;


import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.users.social.FacebookPostRequest;
import org.greengin.nquireit.logic.users.social.TweeterPostRequest;
import org.greengin.nquireit.logic.users.social.SocialPostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.ApiException;
import org.springframework.social.DuplicateStatusException;
import org.springframework.social.InsufficientPermissionException;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;


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
    @ResponseView(value = Views.UserProfileData.class)
    public SocialPostResponse twitterPost(@RequestBody TweeterPostRequest data) {
        SocialPostResponse response = new SocialPostResponse();
        if (twitter.test()) {
            try {
                Tweet tweet = twitter.getApi().timelineOperations().updateStatus(data.getTweet());
                response.setUrl(String.format("http://twitter.com/%s/status/%d", tweet.getUser().getScreenName(), tweet.getId()));
            } catch (DuplicateStatusException e) {
                response.setError("duplicate");
            }
        } else {
            response.setError("connection");
        }
        return response;
    }

    @RequestMapping(value = "/api/social/facebook/post", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public SocialPostResponse facebookPost(@RequestBody FacebookPostRequest data) {

        SocialPostResponse response = new SocialPostResponse();
        if (facebook.test()) {
            try {
                FacebookLink link = new FacebookLink(data.getLink(),
                        data.getLinkName(), data.getCaption(), data.getDescription());
                facebook.getApi().feedOperations().postLink(data.getCaption(), link);
                response.setUrl(facebook.getProfileUrl());
            } catch (InsufficientPermissionException e) {
                response.setError("permission");
            } catch (MissingAuthorizationException e) {
                response.setError("authorization");
            } catch (DuplicateStatusException e) {
                response.setError("duplicate");
            } catch (RateLimitExceededException e) {
                response.setError("rate");
            } catch (ApiException e) {
                response.setError("api");
            }
        } else {
            response.setError("connection");
        }
        return response;
    }

}
