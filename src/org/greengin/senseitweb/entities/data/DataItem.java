package org.greengin.senseitweb.entities.data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;

@Entity
public abstract class DataItem extends VotableEntity {
	
	
	@ManyToOne
	UserProfile author;

	
	public UserProfile getAuthor() {
		return author;
	}

	public void setAuthor(UserProfile author) {
		this.author = author;
	}
		
}
