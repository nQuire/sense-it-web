package org.greengin.nquireit.controllers.users;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mangofactory.jsonview.ResponseView;
import org.apache.commons.lang3.text.WordUtils;
import org.greengin.nquireit.entities.users.UserProfile;
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

    private boolean hasConnection(String provider) {
        Connection<?> c = null;
        if ("google".equals(provider)) {
            c = google;
        } else if ("facebook".equals(provider)) {
            c = facebook;
        } else if ("twitter".equals(provider)) {
            c = twitter;
        }

        if (c == null) {
            return false;
        }

        try {
            return c.test();
        } catch (Exception ignored) {
            return false;
        }
    }


    @RequestMapping(value = "/api/security/profile", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public StatusResponse update(@RequestBody ProfileRequest data, HttpServletRequest request) {
        StatusResponse response = context.getUsersManager().status(getConnections(), request.getSession());
        boolean completed = new UserProfileActions(context, request).updateProfile(response, data);
        return completed ? response : null;
    }

    @RequestMapping(value = "/api/security/profile/image/files", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public StatusResponse update(HttpServletRequest request) {
        try {
            StatusResponse response = context.getUsersManager().status(getConnections(), request.getSession());
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
    @ResponseView(value = Views.UserProfileData.class)
    public StatusResponse checkLogin(HttpServletRequest request) {
        return context.getUsersManager().status(getConnections(), request.getSession());
    }


    @RequestMapping(value = "/api/security/login", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public Boolean performLogin(@RequestBody LoginRequest data, HttpServletRequest request, HttpServletResponse response) {
        resetLoginSessionAttr(request);
        return context.getUsersManager().login(data.getUsername(), data.getPassword(), request, response);
    }

    @RequestMapping(value = "/api/security/logout", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public StatusResponse performLogout(HttpServletRequest request, HttpServletResponse response) {
        resetLoginSessionAttr(request);
        UserProfile user = context.getUsersManager().currentUser();
        if (user != null) {
            return context.getUsersManager().logout(user, getConnections(), request, response);
        } else {
            return context.getUsersManager().status(getConnections(), request.getSession());
        }
    }


    @RequestMapping(value = "/api/security/register", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public StatusResponse performRegister(@RequestBody RegisterRequest data, HttpServletRequest request, HttpServletResponse response) {
        resetLoginSessionAttr(request);
        return context.getUsersManager().registerUser(data, getConnections(), request, response);
    }

    @RequestMapping(value = "/api/security/connection/{providerId}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public StatusResponse deleteConnection(@PathVariable("providerId") String providerId, HttpServletRequest request) {
        StatusResponse response = context.getUsersManager().status(getConnections(), request.getSession());
        boolean completed = new UserProfileActions(context, request).deleteConnection(response, providerId);
        return completed ? response : null;
    }


    @RequestMapping(value = "/api/security/password", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public StatusResponse setPassword(@RequestBody PasswordRequest data, HttpServletRequest request) {
        StatusResponse response = context.getUsersManager().status(getConnections(), request.getSession());
        boolean completed = new UserProfileActions(context, request).setPassword(response, data);
        return completed ? response : null;
    }

    @RequestMapping(value = "/api/profiles/feed", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public LoggedInProfilesResponse getLoggedInUsersFeed() {
        return context.getUsersManager().getLoggedUsers(3);
    }

    @RequestMapping(value = "/api/profiles/loggedin", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public List<UserProfile> getLoggedInUsers() {
        return context.getUsersManager().getLoggedUsers();
    }

    @RequestMapping(value = "/social/new")
    public String newuser(HttpServletRequest request, Model model) {
        return providerLoginRedirect(request, model);
    }


    @RequestMapping(value = "/social/welcome", method = RequestMethod.GET)
    public String welcome(HttpServletRequest request, Model model) {
        return providerLoginRedirect(request, model);
    }

    @RequestMapping(value = "/social/merge", method = RequestMethod.POST)
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String action = (String) session.getAttribute("oauth-action");
        String provider = (String) session.getAttribute("oauth-provider");
        Long[] mergeIds = (Long[]) session.getAttribute("oauth-merge");
        session.removeAttribute("destination");

        if ("link".equals(action) && provider != null && mergeIds != null
                && mergeIds.length == 2
                && !hasConnection(provider)) {

            UserProfile currentUser = context.getUsersManager().currentUser();
            UserProfile mergeUser = context.getUserProfileDao().loadUserById(mergeIds[1]);
            if (currentUser != null && mergeUser != null &&
                    currentUser.getId().equals(mergeIds[0]) && !currentUser.equals(mergeUser)) {

                if (context.getUsersManager().mergeAccount(currentUser, mergeUser, provider)) {
                    return String.format("redirect:/social/%s/link", provider);
                }
            }
        }


        return "redirect:/#/profile";
    }


    @RequestMapping(value = "/social/{provider}/{action}", method = RequestMethod.GET)
    public String login(@PathVariable("provider") String provider, @PathVariable("action") String action, @RequestParam(value = "d", required = false) String destination, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        resetLoginSessionAttr(session);

        if ("login".equals(action) || "link".equals(action)) {
            session.setAttribute("destination", destination);
            session.setAttribute("oauth-action", action);
            session.setAttribute("oauth-provider", provider);

            String providerName = providerName(provider);
            String msgTmpl = "login".equals(action) ? "Sign in with %s" : "Link %s account";
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
            model.addAttribute("provider_name", providerName);
            model.addAttribute("scopes", scopes);
            model.addAttribute("msg", String.format(msgTmpl, providerName));
            return "provider_login";
        } else {
            return "redirect:/#/profile";
        }
    }

    private void resetLoginSessionAttr(HttpServletRequest request) {
        resetLoginSessionAttr(request.getSession());
    }

    private void resetLoginSessionAttr(HttpSession session) {
        session.removeAttribute("oauth-action");
        session.removeAttribute("oauth-provider");
        session.removeAttribute("oauth-merge");
    }

    private String providerName(String providerId) {
        return WordUtils.capitalize(providerId);
    }

    private String providerLoginRedirect(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        String destination = (String) request.getSession().getAttribute("destination");
        String action = (String) session.getAttribute("oauth-action");
        String provider = (String) session.getAttribute("oauth-provider");
        Long[] mergeIds = (Long[]) session.getAttribute("oauth-merge");

        if ("link".equals(action) && provider != null && mergeIds != null
                && mergeIds.length == 2
                && !hasConnection(provider)) {

            UserProfile currentUser = context.getUsersManager().currentUser();
            UserProfile mergeUser = context.getUserProfileDao().loadUserById(mergeIds[1]);
            if (currentUser != null && mergeUser != null &&
                    currentUser.getId().equals(mergeIds[0]) && !currentUser.equals(mergeUser)) {
                model.addAttribute("current_user", currentUser);
                model.addAttribute("merge_user", mergeUser);
                model.addAttribute("provider_name", providerName(provider));
                return "provider_merge";
            }
        }

        if (!context.getUsersManager().currentUserIsNew()) {
            request.getSession().removeAttribute("destination");
        }

        if (destination == null) {
            destination = "/profile";
        }

        resetLoginSessionAttr(session);

        return String.format("redirect:/#%s", destination);
    }
}
