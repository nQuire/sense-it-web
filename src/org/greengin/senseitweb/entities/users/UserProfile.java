package org.greengin.senseitweb.entities.users;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class UserProfile {

	@Id
    @Column (name = "USER_ID", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
   	Long id;
	
	@Basic
	String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	Collection<OpenIdEntity> openIds;
	

	public Long getId() {
		return id;
	}

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
