package org.greengin.senseitweb.rs.requestdata;

import org.greengin.senseitweb.entities.senseit.Profile;

public class ProfileMetadata {
	String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void updateProfile(Profile profile) {
		profile.setTitle(title);
	}
}
