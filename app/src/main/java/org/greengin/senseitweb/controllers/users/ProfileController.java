package org.greengin.senseitweb.controllers.users;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.users.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ProfileController {




    @Inject
    @Qualifier("facebook")
    Connection<Facebook> facebook;

    @Inject
    @Qualifier("google")
    Connection<?> google;


    @Value("${server.appUrl}")
    String serverPath;


    @Autowired
    ContextBean context;


    private HashMap<String, Connection<?>> getConnections() {
        HashMap<String, Connection<?>> connections = new HashMap<String, Connection<?>>();
        for (Connection<?> c : new Connection<?>[] {google, facebook}) {
            try {
                if (c.test()) {
                    ConnectionData data = c.createData();
                    connections.put(data.getProviderId(), c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return connections;
    }



    @RequestMapping(value = "/api/security/profile", method = RequestMethod.PUT)
    @ResponseBody
    public StatusResponse update(@RequestBody ProfileRequest data, HttpServletRequest request) {
        StatusResponse response = context.getUsersManager().status(getConnections(), request.getSession());
        boolean completed = new UserProfileActions(context, request).updateProfile(response, data);
        return completed ? response : null;
    }



    @RequestMapping(value = "/api/security/status", method = RequestMethod.GET)
    @ResponseBody
    public StatusResponse checkLogin(HttpServletRequest request) {
        return context.getUsersManager().status(getConnections(), request.getSession());
    }


    @RequestMapping(value = "/api/security/login", method = RequestMethod.POST)
    @ResponseBody
    public StatusResponse performLogin(@RequestParam("j_username") String username, @RequestParam("j_password") String password, HttpServletRequest request, HttpServletResponse response) {
        return context.getUsersManager().login(username, password, getConnections(), request, response);
    }

    @RequestMapping(value = "/api/security/logout", method = RequestMethod.POST)
    @ResponseBody
    public StatusResponse performLogout(HttpServletRequest request, HttpServletResponse response) {
        return context.getUsersManager().logout(getConnections(), request, response);
    }

    @RequestMapping(value = "/api/security/register", method = RequestMethod.POST)
    @ResponseBody
    public RegistrationResponse performRegister(@RequestParam("j_username") String username, @RequestParam("j_password") String password, HttpServletRequest request, HttpServletResponse response) {
        return context.getUsersManager().registerUser(username, password, getConnections(), request, response);
    }




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

        StatusResponse status = context.getUsersManager().status(getConnections(), request.getSession());

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
}
