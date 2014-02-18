package org.greengin.senseitweb.entities.users;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.greengin.senseitweb.entities.AbstractEntity;

@Entity
public class UserProfile extends AbstractEntity {
	@Basic
	String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	Collection<OpenIdEntity> openIds;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<OpenIdEntity> getOpenIds() {
		return openIds;
	}

	public void setOpenIds(Collection<OpenIdEntity> openIds) {
		this.openIds = openIds;
	}
	

}
