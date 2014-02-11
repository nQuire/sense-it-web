package org.greengin.senseitweb.rs.users;

import org.greengin.senseitweb.entities.users.UserProfile;

public class StatusResponse {
	boolean logged;
	boolean newUser;
	UserProfile profile;
	
	
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
	
	public boolean isNewUser() {
		return newUser;
	}
	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}
}
