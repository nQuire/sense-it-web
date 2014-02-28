package org.greengin.senseitweb.logic.project.senseit;

import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;


public class SenseItProfileRequest {
	boolean geolocated;



	public boolean isGeolocated() {
		return geolocated;
	}


	public void setGeolocated(boolean geolocated) {
		this.geolocated = geolocated;
	}
	

	public void updateProfile(SenseItProfile profile) {
		profile.setGeolocated(geolocated);
	}

}
