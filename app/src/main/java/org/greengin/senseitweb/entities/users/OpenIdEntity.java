package org.greengin.senseitweb.entities.users;

import javax.persistence.Basic;
import javax.persistence.Entity;

import org.greengin.senseitweb.entities.AbstractEntity;

@Entity
public class OpenIdEntity extends AbstractEntity {

	@Basic
	String openId;
	
	@Basic
	String email;

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
