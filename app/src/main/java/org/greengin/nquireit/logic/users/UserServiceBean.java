package org.greengin.nquireit.logic.users;

import org.greengin.nquireit.dao.UserProfileDao;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    UserProfileDao userProfileDao;

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
        return userProfileDao.loadUserByUsername(s);
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
                userProfileDao.user(((UserProfile) auth.getPrincipal())) : null;
    }

    public boolean checkToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object attr = session.getAttribute("nquire-it-token");
        return attr != null && attr.toString().equals(request.getHeader("nquire-it-token"));
    }

    public Authentication login(UserProfile user, HttpSession session, String token) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute("nquire-it-token", token);
        return auth;
    }

    public Authentication login(UserProfile user, HttpSession session) {
        return login(user, session, new BigInteger(260, random).toString(32));
    }

    public Authentication login(UserProfile user, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = login(user, request.getSession());
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
        rememberMeServices.loginSuccess(request, response, auth);
        return auth;
    }

    public Boolean login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth;
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
            rememberMeServices.loginSuccess(request, response, auth);
            request.getSession().setAttribute("nquire-it-token", new BigInteger(260, random).toString(32));
        } catch (Exception ex) {
            auth = null;
        }


        return auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserProfile;
    }

    public StatusResponse logout(HashMap<String, Connection<?>> connections, HttpServletRequest request, HttpServletResponse response) {
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);

        return status(connections, request.getSession());
    }

    public UserProfile providerSignIn(String username, String providerId, String providerUserId) {
        UserProfile existingUser = userProfileDao.loadUserByProviderUserId(providerId, providerUserId);
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

            UserProfile user = userProfileDao.createUser(initialUsername, null, email, email != null);
            newUser(user.getUsername());
            return user;
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        random = new SecureRandom();
    }


    public boolean matchPassword(UserProfile user, String password) {
        return userProfileDao.matchPassword(user, password);
    }

    public void setPassword(UserProfile user, String password) {
        userProfileDao.setPassword(user, password);
    }

    public boolean updateProfileImage(StatusResponse currentStatus, FileMapUpload files) {
        return userProfileDao.updateProfileImage(currentStatus.getProfile(), files);
    }


    public boolean updateProfile(StatusResponse currentStatus, ProfileRequest data) {
        if (data.getUsername() != null) {
            if (!data.getUsername().equals(currentStatus.getProfile().getUsername())) {

                if (data.getUsername().length() == 0) {
                    currentStatus.getResponses().put("username", "username_empty");
                } else if (!usernameIsAvailable(data.getUsername())) {
                    currentStatus.getResponses().put("username", "username_not_available");
                } else {
                    userProfileDao.updateUsername(currentStatus.getProfile(), data.getUsername());
                }
            }
        }

        if (data.getMetadata() != null) {
            userProfileDao.updateUserMetadata(currentStatus.getProfile(), data.getMetadata());
        }

        //update(currentStatus);
        return true;
    }

    public boolean deleteConnection(StatusResponse currentStatus, String providerId) {
        if (((currentStatus.getProfile().getPassword() != null && currentStatus.getProfile().getPassword().length() > 0) || currentStatus.getConnections().size() > 1) && userProfileDao.deleteConnection(currentStatus.getProfile(), providerId)) {
            currentStatus.getConnections().remove(providerId);
            return true;
        }

        return false;
    }


    public StatusResponse registerUser(RegisterRequest data, HashMap<String, Connection<?>> connections, HttpServletRequest request) {
        try {
            loadUserByUsername(data.getUsername());
            StatusResponse result = new StatusResponse();
            result.setLogged(false);
            result.setProfile(null);
            result.getResponses().put("registration", "username_exists");
            return result;
        } catch (UsernameNotFoundException e) {

            try {
                userProfileDao.loadUserByUsername(data.getEmail());
                StatusResponse result = new StatusResponse();
                result.setLogged(false);
                result.setProfile(null);
                result.getResponses().put("registration", "email_exists");
                return result;
            } catch (UsernameNotFoundException e2) {
                UserProfile user = userProfileDao.createUser(data.getUsername(), data.getPassword(), data.getEmail(), false);
                login(user, request.getSession());

                return status(connections, request.getSession());
            }
        }
    }

}
