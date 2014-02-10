package org.greengin.senseitweb.rs.users;

import org.greengin.senseitweb.entities.users.UserProfile;

public class UserResponse {
	boolean logged;
	boolean newUser;
	UserProfile profile;	
	String url;
	
	
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public UserProfile getProfile() {
		return profile;
	}
	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean isNewUser() {
		return newUser;
	}
	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}
}
