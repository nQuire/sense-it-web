package org.greengin.nquireit.logic.users;

import org.greengin.nquireit.entities.users.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SocialAdapter implements SignInAdapter, ConnectionSignUp {

    @Autowired
    UserServiceBean userServiceBean;

    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        HttpServletRequest servletRequest = (HttpServletRequest) request.getNativeRequest();
        HttpServletResponse servletResponse = (HttpServletResponse) request.getNativeResponse();

        HttpSession session = servletRequest.getSession();
        String action = (String) session.getAttribute("oauth-action");
        UserProfile currentUser = userServiceBean.currentUser();
        org.springframework.social.connect.UserProfile oauthProfile = connection.fetchUserProfile();
        ConnectionData data = connection.createData();
        UserProfile user = userServiceBean.providerSignIn(oauthProfile.getUsername(), data.getProviderId(), data.getProviderUserId());

        if (user != null) {
            if (currentUser == null || "login".equals(action) || user.equals(currentUser)) {
                session.removeAttribute("oauth-merge");
                userServiceBean.login(user, servletRequest, servletResponse);
            } else {
                session.setAttribute("oauth-merge", new Long[]{currentUser.getId(), user.getId()});
            }
        }

        return null;
    }

    public String execute(Connection<?> connection) {
        UserProfile current = userServiceBean.currentUser();
        if (current == null) {
            org.springframework.social.connect.UserProfile profile = connection.fetchUserProfile();
            ConnectionData data = connection.createData();
            UserProfile user = userServiceBean.providerSignIn(profile.getUsername(), data.getProviderId(), data.getProviderUserId());

            return user.getUsername();
        } else {
            return current.getUsername();
        }
    }
}