package org.greengin.senseitweb.entities.data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.rating.VotableEntity;
import org.greengin.senseitweb.entities.users.UserProfile;

@Entity
public abstract class AbstractDataProjectItem extends VotableEntity {
	
	
	@ManyToOne
    @Getter
    @Setter
    UserProfile author;

	@ManyToOne
    @Getter
    @Setter
	protected DataCollectionActivity<?, ?> dataStore;

}
