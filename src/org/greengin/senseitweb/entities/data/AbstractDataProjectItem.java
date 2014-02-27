package org.greengin.senseitweb.entities.data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;

@Entity
public abstract class AbstractDataProjectItem extends VotableEntity {
	
	
	@ManyToOne
	UserProfile author;

	@ManyToOne
	DataCollectionActivity<?, ?> dataStore;
	
	
	public UserProfile getAuthor() {
		return author;
	}

	public void setAuthor(UserProfile author) {
		this.author = author;
	}

	public DataCollectionActivity<?, ?> getDataStore() {
		return dataStore;
	}

	public void setDataStore(DataCollectionActivity<?, ?> dataStore) {
		this.dataStore = dataStore;
	}
		
}
