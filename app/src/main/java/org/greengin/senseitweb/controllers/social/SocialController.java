package org.greengin.senseitweb.controllers.social;

import java.util.Locale;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.greengin.senseitweb.logic.permissions.Status2Response;
import org.greengin.senseitweb.logic.social.UserServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SocialController {

    @Inject
    @Qualifier("facebook")
    Connection<Facebook> facebook;

    @Value("${server.path}")
    String serverPath;

    @Autowired
    UserServiceBean userServiceBean;
	
	private static final Logger logger = LoggerFactory.getLogger(SocialController.class);
	
	@RequestMapping(value = "/social/{provider}/login", method = RequestMethod.GET)
	public String login(@PathVariable("provider") String provider, Locale locale, Model model) {

        String scopes = "";
        if ("facebook".equals(provider)) {
            scopes = "email,user_likes,friends_likes,publish_stream";
        } else if ("google".equals(provider)) {
            scopes = "email";
        }

        model.addAttribute("signin_url", String.format("/%ssignin/%s", serverPath, provider));
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
	public String welcome(Locale locale, Model model) {
        Status2Response status = userServiceBean.status();

        ObjectMapper mapper = new ObjectMapper();


        String message = "at welcome!";
        model.addAttribute("message", message);
        try {
            model.addAttribute("status", mapper.writeValueAsString(status));
        } catch (Exception e) {
            e.printStackTrace();
        }

		return "welcome_user";
	}
	
}
