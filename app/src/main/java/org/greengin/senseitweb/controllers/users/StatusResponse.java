package org.greengin.senseitweb.controllers.users;

import org.greengin.senseitweb.entities.users.UserProfile;

public class StatusResponse {
	boolean logged;
	UserProfile profile;
	String token;
	
	
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
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
