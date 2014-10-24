package org.greengin.nquireit.controllers.users;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.json.JacksonObjectMapper;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.files.RequestsUtils;
import org.greengin.nquireit.logic.project.ProjectResponse;
import org.greengin.nquireit.logic.project.metadata.ProjectRequest;
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
    Connection<Google> google;

    @Inject
    @Qualifier("twitter")
    Connection<Twitter> twitter;

    @Value("${server.appUrl}")
    String serverPath;


    @Autowired
    ContextBean context;

    @Autowired
    JacksonObjectMapper objectMapper;


    private HashMap<String, Connection<?>> getConnections() {
        HashMap<String, Connection<?>> connections = new HashMap<String, Connection<?>>();
        for (Connection<?> c : new Connection<?>[]{google, facebook, twitter}) {
            try {
                if (c.test()) {
                    ConnectionData data = c.createData();
                    connections.put(data.getProviderId(), c);
                }
            } catch (Exception ignored) {
            }
        }

        return connections;
    }


    @RequestMapping(value = "/api/security/profile", method = RequestMethod.PUT)
    @ResponseBody
    @JsonView(value = Views.UserProfileData.class)
    public StatusResponse update(@RequestBody ProfileRequest data, HttpServletRequest request) {
        StatusResponse response = context.getUsersManager().status(getConnections(), request.getSession(), true);
        boolean completed = new UserProfileActions(context, request).updateProfile(response, data);
        return completed ? response : null;
    }

    @RequestMapping(value = "/api/security/profile/image/files", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public StatusResponse update(HttpServletRequest request) {
        try {
            StatusResponse response = context.getUsersManager().status(getConnections(), request.getSession(), true);
            FileMapUpload files = RequestsUtils.getFiles(request);
            boolean completed = new UserProfileActions(context, request).updateProfileImage(response, files);
            return completed ? response : null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @RequestMapping(value = "/api/security/status", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(value = Views.UserProfileData.class)
    public StatusResponse checkLogin(HttpServletRequest request) {
        return context.getUsersManager().status(getConnections(), request.getSession(), true);
    }


    @RequestMapping(value = "/api/security/login", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(value = Views.UserProfileData.class)
    public Boolean performLogin(@RequestBody LoginRequest data, HttpServletRequest request, HttpServletResponse response) {
        return context.getUsersManager().login(data.getUsername(), data.getPassword(), request, response);
    }

    @RequestMapping(value = "/api/security/logout", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(value = Views.UserProfileData.class)
    public StatusResponse performLogout(HttpServletRequest request, HttpServletResponse response) {
        return context.getUsersManager().logout(getConnections(), request, response);
    }


    @RequestMapping(value = "/api/security/register", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(value = Views.UserProfileData.class)
    public StatusResponse performRegister(@RequestBody RegisterRequest data, HttpServletRequest request) {
        return context.getUsersManager().registerUser(data, getConnections(), request);
    }

    @RequestMapping(value = "/api/security/connection/{providerId}", method = RequestMethod.DELETE)
    @ResponseBody
    @JsonView(value = Views.UserProfileData.class)
    public StatusResponse deleteConnection(@PathVariable("providerId") String providerId, HttpServletRequest request) {
        StatusResponse response = context.getUsersManager().status(getConnections(), request.getSession(), true);
        boolean completed = new UserProfileActions(context, request).deleteConnection(response, providerId);
        return completed ? response : null;
    }


    @RequestMapping(value = "/api/security/password", method = RequestMethod.PUT)
    @ResponseBody
    @JsonView(value = Views.UserProfileData.class)
    public StatusResponse setPassword(@RequestBody PasswordRequest data, HttpServletRequest request) {
        StatusResponse response = context.getUsersManager().status(getConnections(), request.getSession(), true);
        boolean completed = new UserProfileActions(context, request).setPassword(response, data);
        return completed ? response : null;
    }


    @RequestMapping(value = "/social/{provider}/login", method = RequestMethod.GET)
    public String login(@PathVariable("provider") String provider, Locale locale, Model model) {

        String scopes = "";
        if ("facebook".equals(provider)) {
            scopes = "email,user_likes,friends_likes,publish_stream";
        } else if ("google".equals(provider)) {
            scopes = "email";
        } else if ("twitter".equals(provider)) {
            scopes = "email";
        }

        model.addAttribute("signin_url", String.format("%s/signin/%s", serverPath, provider));
        model.addAttribute("provider", provider);
        model.addAttribute("scopes", scopes);
        return "provider_login";
    }


    @RequestMapping(value = "/social/new")
    public String newuser(Locale locale, Model model) {
        return "welcome_user";
    }


    @RequestMapping(value = "/social/welcome", method = RequestMethod.GET)
    public String welcome(Locale locale, Model model, HttpServletRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("origin", serverPath);

        StatusResponse status = context.getUsersManager().status(getConnections(), request.getSession(), false);

        try {
            model.addAttribute("user", mapper.writeValueAsString(status));
        } catch (Exception e) {
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
