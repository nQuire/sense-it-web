package org.greengin.senseitweb.entities.activities.senseit;



import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.greengin.senseitweb.entities.data.DataCollectionActivity;

@Entity
public class SenseItActivity extends DataCollectionActivity<SenseItSeries, SenseItAnalysis> {

	

	@OneToOne(orphanRemoval = true, cascade=CascadeType.ALL)
	SenseItProfile profile = new SenseItProfile();
	
	

	public SenseItProfile getProfile() {
		return profile;
	}



	public void setProfile(SenseItProfile profile) {
		this.profile = profile;
	}


}
