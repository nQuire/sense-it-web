package org.greengin.senseitweb.entities.activities.senseit;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.greengin.senseitweb.entities.projects.AbstractActivity;

@Entity
public class SenseItActivity extends AbstractActivity {
	
	@OneToMany(cascade = CascadeType.ALL)
	Collection<SenseItProfile> profiles;
	
	
	
	public Collection<SenseItProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Collection<SenseItProfile> profiles) {
		this.profiles = profiles;
	}
	
	
}
