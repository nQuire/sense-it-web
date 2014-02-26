package org.greengin.senseitweb.entities.activities.senseit;


import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.greengin.senseitweb.entities.data.DataCollectionActivity;

@Entity
public class SenseItActivity extends DataCollectionActivity<SenseItSeries> {

	

	@OneToOne(orphanRemoval = true, cascade=CascadeType.ALL)
	SenseItProfile profile = new SenseItProfile();
	
	
	@OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
	Collection<SenseItSeries> data;
	

	
	@Override
	public Collection<SenseItSeries> getData() {
		return data;
	}

	public void setData(Collection<SenseItSeries> data) {
		this.data = data;
	}

	public SenseItProfile getProfile() {
		return profile;
	}



	public void setProfile(SenseItProfile profile) {
		this.profile = profile;
	}


}
