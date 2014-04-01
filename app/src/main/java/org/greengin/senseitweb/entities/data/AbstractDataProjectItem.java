package org.greengin.senseitweb.entities.data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;

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
