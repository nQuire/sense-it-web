package org.greengin.senseitweb.logic.project.senseit;

import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;

public class SenseItProfileRequest {
	String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void updateProfile(SenseItProfile profile) {
		profile.setTitle(title);
	}
}
