package org.greengin.senseitweb.logic.social;

import org.greengin.senseitweb.entities.users.UserProfile2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SocialAdapter implements SignInAdapter, ConnectionSignUp {

    @Autowired
    UserServiceBean userServiceBean;

    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        HttpServletRequest servletRequest = (HttpServletRequest) request.getNativeRequest();
        HttpServletResponse servletResponse = (HttpServletResponse) request.getNativeResponse();

        ConnectionData data = connection.createData();
        UserProfile2 user = userServiceBean.loadUserByProviderUserId(data.getProviderId(), data.getProviderUserId());
        if (user != null) {
            userServiceBean.login(user, servletRequest, servletResponse);
        }
        return null;
    }

    public String execute(Connection<?> connection) {
        UserProfile profile = connection.fetchUserProfile();
        ConnectionData data = connection.createData();
        userServiceBean.createUser(profile.getUsername(), data.getProviderId(), data.getProviderUserId());
        return profile.getUsername();
    }
}