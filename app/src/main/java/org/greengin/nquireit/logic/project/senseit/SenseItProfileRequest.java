package org.greengin.nquireit.logic.project.senseit;

import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;
import org.greengin.nquireit.entities.activities.senseit.SenseItTransformations;


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
        if (tx != null) {
            profile.setTx(tx);
        }
	}

}
