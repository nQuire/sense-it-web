package org.greengin.senseitweb.controllers.users;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.greengin.senseitweb.entities.users.UserGrantedAuthority;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.users.StatusResponse;
import org.greengin.senseitweb.logic.users.UserServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SocialController {

    @Inject
    @Qualifier("facebook")
    Connection<Facebook> facebook;

    @Inject
    @Qualifier("google")
    Connection<?> google;

    @Value("${server.appUrl}")
    String serverPath;

    @Autowired
    UserServiceBean userServiceBean;

    @Autowired
    UsersConnectionRepository usersConnectionRepository;

    private static final Logger logger = LoggerFactory.getLogger(SocialController.class);

    @RequestMapping(value = "/social/{provider}/login", method = RequestMethod.GET)
    public String login(@PathVariable("provider") String provider, Locale locale, Model model) {

        String scopes = "";
        if ("facebook".equals(provider)) {
            scopes = "email,user_likes,friends_likes,publish_stream";
        } else if ("google".equals(provider)) {
            scopes = "email";
        }

        model.addAttribute("signin_url", String.format("%s/signin/%s", serverPath, provider));
        model.addAttribute("provider", provider);
        model.addAttribute("scopes", scopes);
        return "provider_login";
    }


    @RequestMapping(value = "/social/new")
    public String newuser(Locale locale, Model model) {
        String message = "at new!";
        model.addAttribute("message", message);
        return "welcome_user";
    }


    @RequestMapping(value = "/social/welcome", method = RequestMethod.GET)
    public String welcome(Locale locale, Model model, HttpServletRequest request) {
        ObjectMapper mapper = new ObjectMapper();

        StatusResponse status = userServiceBean.status(request.getSession());

        try {
            model.addAttribute("user", mapper.writeValueAsString(status));
        }catch (Exception e) {
            e.printStackTrace();
        }

        try {
            model.addAttribute("google", google.test());
        } catch (Exception e) {
            model.addAttribute("google", false);
        }
        try {
            model.addAttribute("facebook", facebook.test());
        } catch (Exception e) {
            model.addAttribute("facebook", false);
        }

        return "welcome_user";
    }

    @RequestMapping(value = "/social/test/google", method = RequestMethod.GET)
    @ResponseBody
    public String testGoogle(HttpServletRequest request) {
        StatusResponse status = userServiceBean.status(request.getSession());
        return "!";
    }

}
