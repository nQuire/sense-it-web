package org.greengin.senseitweb.logic.social;

import org.greengin.senseitweb.entities.users.UserProfile2;
import org.greengin.senseitweb.logic.permissions.UserServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

public class SocialSignInAdapter implements SignInAdapter {

    @Autowired
    UserServiceBean userServiceBean;

	public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        UserProfile2 user = userServiceBean.loadUserByUsername(localUserId);
  	
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities()));
       
		return null;
    }
}