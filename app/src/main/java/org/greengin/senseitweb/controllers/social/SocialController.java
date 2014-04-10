package org.greengin.senseitweb.controllers.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	private static final Logger logger = LoggerFactory.getLogger(SocialController.class);
	
	@RequestMapping(value = "/social/facebook/login", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
        model.addAttribute("signin_url", String.format("/%ssignin/%s", serverPath, "facebook"));
        model.addAttribute("provider", "Facebook");
		return "provider_login";
	}
	
	@RequestMapping(value = "/social/facebook/welcome", method = RequestMethod.GET)
	public String welcome(Locale locale, Model model) {
		Facebook fb = facebook.getApi();

		List<Page> moviePages = fb.likeOperations().getMovies();
		
		List<String> movies = new ArrayList<String>();
		for (Page p : moviePages) {
			movies.add(p.getName());
		}
		
		// Uncomment the following line to see how easy posting to your timeline is.
		//fb.feedOperations().post(userId, "I just tried out the My Movies example site!");
		
		int numFriends = fb.friendOperations().getFriends().size();
		
		model.addAttribute("facebook", true);
		model.addAttribute("numFriends", numFriends);
		model.addAttribute("name", facebook.getDisplayName());
		model.addAttribute("movies", movies);
		
		return "welcome_user";
	}
	
}
