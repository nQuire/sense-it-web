package org.greengin.senseitweb.entities.users;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserProfile {

	@Id
    @Column (name = "USER_ID", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
   	Long id;
	
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

	public Long getId() {
		return id;
	}

}
