package org.greengin.senseitweb.entities.users;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.datanucleus.jpa.annotations.Extension;

@Entity
public class UserProfile {

	@Id
    @Column (nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Extension (vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
   	String id;
	
	@Basic
	String userId;
	
	@Basic
	String name;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

}
