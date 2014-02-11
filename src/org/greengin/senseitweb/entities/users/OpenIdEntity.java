package org.greengin.senseitweb.entities.users;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OpenIdEntity {

	@Id
    @Column (name = "OPENID_ID", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
   	Long id;
	
	@Basic
	String openId;
	
	@Basic
	String email;

	public Long getId() {
		return id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
