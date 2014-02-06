package org.greengin.senseitweb.entities.senseit;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.greengin.senseitweb.entities.AbstractActivity;

@Entity
public class SenseItActivity extends AbstractActivity {
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	Collection<Profile> profiles;
	
	
	
	public Collection<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Collection<Profile> profiles) {
		this.profiles = profiles;
	}
	
	
}
