package org.greengin.senseitweb.logic.project.senseit;

import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;
import org.greengin.senseitweb.entities.activities.senseit.SenseItTransformations;


public class SenseItProfileRequest {
	boolean geolocated;
	SenseItTransformations tx;



	public boolean isGeolocated() {
		return geolocated;
	}


	public void setGeolocated(boolean geolocated) {
		this.geolocated = geolocated;
	}
	

	public SenseItTransformations getTx() {
		return tx;
	}


	public void setTx(SenseItTransformations tx) {
		this.tx = tx;
	}
	


	public void updateProfile(SenseItProfile profile) {
		profile.setGeolocated(geolocated);
		profile.setTx(tx);
	}

}
