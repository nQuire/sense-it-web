package org.greengin.senseitweb.logic.social;

import org.greengin.senseitweb.logic.permissions.UserServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;


public class AccountConnectionSignUp implements ConnectionSignUp {

    @Autowired
    UserServiceBean userServiceBean;

	public String execute(Connection<?> connection) {
		UserProfile profile = connection.fetchUserProfile();
        userServiceBean.createUser(profile.getUsername());
		return profile.getUsername();
	}
}
