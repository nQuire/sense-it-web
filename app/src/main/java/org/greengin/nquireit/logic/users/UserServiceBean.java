package org.greengin.nquireit.logic.users;

import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.social.connect.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class UserServiceBean implements UserDetailsService, InitializingBean {


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @Autowired
    RememberMeServices rememberMeServices;

    @Autowired
    ContextBean context;

    @Autowired
    @Qualifier("sessionRegistry")
    SessionRegistry sessionRegistry;

    SecureRandom random;

    Vector<String> newUsers = new Vector<String>();

    public void newUser(String id) {
        if (!newUsers.contains(id)) {
            newUsers.add(id);
        }
    }

    public boolean currentUserIsNew() {
        UserProfile user = currentUser();
        if (user != null && newUsers.contains(user.getUsername())) {
            newUsers.remove(user.getUsername());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserProfile loadUserByUsername(String s) throws UsernameNotFoundException {
        return context.getUserProfileDao().loadUserByUsername(s);
    }

    private StatusResponse createResponse(Authentication auth, HashMap<String, Connection<?>> connections, HttpSession session) {
        StatusResponse result = new StatusResponse();
        result.getConnections().clear();

        if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserProfile) {
            UserProfile user = currentUser();
            result.setLogged(true);
            result.setProfile(user);
            result.setToken((String) session.getAttribute("nquire-it-token"));

            for (Map.Entry<String, Connection<?>> entry : connections.entrySet()) {
                if (entry.getValue() != null) {
                    StatusConnectionResponse scr = new StatusConnectionResponse();
                    scr.setProvider(entry.getKey());
                    scr.setProviderProfileUrl(entry.getValue().getProfileUrl());
                    result.getConnections().put(entry.getKey(), scr);
                }
            }
        } else {
            result.setLogged(false);
            result.setProfile(null);
        }

        return result;
    }

    public boolean usernameIsAvailable(String username) {
        try {
            loadUserByUsername(username);
            return false;
        } catch (UsernameNotFoundException e) {
            return true;
        }
    }

    public StatusResponse status(HashMap<String, Connection<?>> connections, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return createResponse(auth, connections, session);
    }

    public UserProfile currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserProfile ?
                context.getUserProfileDao().user(((UserProfile) auth.getPrincipal())) : null;
    }

    public boolean checkToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object attr = session.getAttribute("nquire-it-token");
        return attr != null && attr.toString().equals(request.getHeader("nquire-it-token"));
    }

    private boolean initSession(UserProfile user, String password, boolean requirePassword, HttpServletRequest request, HttpServletResponse response) {

        Authentication auth;

        try {
            if (requirePassword) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), password);
                auth = authenticationManager.authenticate(token);
            } else {
                auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            }

            SecurityContextHolder.getContext().setAuthentication(auth);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
            rememberMeServices.loginSuccess(request, response, auth);
            sessionRegistry.registerNewSession(request.getRequestedSessionId(), user);
            request.getSession().setAttribute("nquire-it-token", new BigInteger(260, random).toString(32));
        } catch (Exception ex) {
            auth = null;
        }

        return auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserProfile;
    }


    public Boolean login(UserProfile user, HttpServletRequest request, HttpServletResponse response) {
        return initSession(user, null, false, request, response);
    }

    public Boolean testLogin(UserProfile user, HttpSession session, String sessionToken) {

        Authentication auth;

        try {
            auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            sessionRegistry.registerNewSession(session.getId(), user);
            session.setAttribute("nquire-it-token", sessionToken);
        } catch (Exception ex) {
            auth = null;
        }

        return auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserProfile;
    }

    public Boolean login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        UserProfile user = context.getUserProfileDao().loadUserByUsername(username);
        return user != null && initSession(user, password, true, request, response);
    }

    public StatusResponse logout(HashMap<String, Connection<?>> connections, HttpServletRequest request, HttpServletResponse response) {
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);
        sessionRegistry.removeSessionInformation(request.getRequestedSessionId());
        return status(connections, request.getSession());
    }

    public UserProfile providerSignIn(String username, String providerId, String providerUserId) {
        UserProfile existingUser = context.getUserProfileDao().loadUserByProviderUserId(providerId, providerUserId);
        if (existingUser != null) {
            return existingUser;
        } else {
            String email = null;

            if (username.matches("^\\S+@\\S+\\.\\S+$")) {
                email = username;
                username = username.substring(0, username.indexOf('@'));
            }

            String initialUsername = username;

            for (int i = 1; !usernameIsAvailable(initialUsername); i++) {
                initialUsername = String.format("%s_%d", username, i);
            }

            UserProfile user = context.getUserProfileDao().createUser(initialUsername, null, email, email != null);
            newUser(user.getUsername());
            return user;
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        random = new SecureRandom();
    }


    public boolean matchPassword(UserProfile user, String password) {
        return context.getUserProfileDao().matchPassword(user, password);
    }

    public void setPassword(UserProfile user, String password) {
        context.getUserProfileDao().setPassword(user, password);
    }

    public boolean updateProfileImage(StatusResponse currentStatus, FileMapUpload files) {
        return context.getUserProfileDao().updateProfileImage(currentStatus.getProfile(), files);
    }


    public boolean updateProfile(StatusResponse currentStatus, ProfileRequest data) {
        if (data.getUsername() != null) {
            if (!data.getUsername().equals(currentStatus.getProfile().getUsername())) {

                if (data.getUsername().length() == 0) {
                    currentStatus.getResponses().put("username", "username_empty");
                } else if (!usernameIsAvailable(data.getUsername())) {
                    currentStatus.getResponses().put("username", "username_not_available");
                } else {
                    context.getUserProfileDao().updateUsername(currentStatus.getProfile(), data.getUsername());
                }
            }
        }

        context.getUserProfileDao().updateUserInformation(currentStatus.getProfile(), data.getMetadata(), data.getVisibility());

        return true;
    }

    public boolean deleteConnection(StatusResponse currentStatus, String providerId) {
        if (((currentStatus.getProfile().getPassword() != null && currentStatus.getProfile().getPassword().length() > 0) ||
                currentStatus.getConnections().size() > 1) &&
                context.getUserProfileDao().deleteConnection(currentStatus.getProfile(), providerId)) {
            currentStatus.getConnections().remove(providerId);
            return true;
        }

        return false;
    }


    public StatusResponse registerUser(RegisterRequest data, HashMap<String, Connection<?>> connections, HttpServletRequest request, HttpServletResponse response) {
        try {
            loadUserByUsername(data.getUsername());
            StatusResponse result = new StatusResponse();
            result.setLogged(false);
            result.setProfile(null);
            result.getResponses().put("registration", "username_exists");
            return result;
        } catch (UsernameNotFoundException e) {

            try {
                context.getUserProfileDao().loadUserByUsername(data.getEmail());
                StatusResponse result = new StatusResponse();
                result.setLogged(false);
                result.setProfile(null);
                result.getResponses().put("registration", "email_exists");
                return result;
            } catch (UsernameNotFoundException e2) {
                UserProfile user = context.getUserProfileDao().createUser(data.getUsername(), data.getPassword(), data.getEmail(), false);
                login(user, request, response);
                return status(connections, request.getSession());
            }
        }
    }

    public PublicProfileResponse getPublicProfile(Long userId) {
        PublicProfileResponse response = new PublicProfileResponse();
        UserProfile profile = context.getUserProfileDao().loadUserById(userId);

        if (profile != null) {
            response.setUsername(profile.getUsername());

            if (profile.getVisibility() != null) {
                if (profile.getVisibility().get("metadata") && profile.getMetadata() != null) {
                    response.getMetadata().putAll(profile.getMetadata());
                }

                boolean joined = profile.getVisibility().get("projectsJoined");
                boolean created = profile.getVisibility().get("projectsCreated");
                response.setProjects(context.getProjectDao().getMyProjects(profile, joined, created));
            }
        }

        return response;
    }

    public boolean mergeAccount(UserProfile user, UserProfile mergedUser, String provider) {
        if (context.getUserProfileDao().deleteConnection(mergedUser, provider)) {
            context.getLogManager().usersMerged(user, mergedUser);
            context.getVotableDao().transferContent(mergedUser, user);
            context.getVoteDao().transferVotes(mergedUser, user);
            context.getRoleManager().transferRoles(mergedUser, user);
            context.getUserProfileDao().deleteUser(mergedUser);
            return true;
        } else {
            return false;
        }
    }

    public boolean isLoggedIn(UserProfile user) {
        return sessionRegistry.getAllPrincipals().contains(user);
    }

    public LoggedInProfilesResponse getLoggedUsers(int max) {
        LoggedInProfilesResponse response = new LoggedInProfilesResponse();
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object p : principals) {
            if (p instanceof UserProfile && !response.getUsers().contains(p)) {
                response.getUsers().add((UserProfile) p);
                if (response.getUsers().size() == max) {
                    break;
                }
            }
        }

        response.setCount(principals.size());
        return response;
    }

    public List<UserProfile> getLoggedUsers() {
        Vector<UserProfile> list = new Vector<UserProfile>();
        for (Object p : sessionRegistry.getAllPrincipals()) {
            if (p instanceof UserProfile && !list.contains(p)) {
                list.add((UserProfile) p);
            }
        }

        return list;
    }
}
