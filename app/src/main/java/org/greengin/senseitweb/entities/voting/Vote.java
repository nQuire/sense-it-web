package org.greengin.senseitweb.entities.voting;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.users.UserProfile;


@Entity
public class Vote extends AbstractEntity {	

	@Basic
    @Getter
    @Setter
    Long value;

	@Basic
    @Getter
    @Setter
	String comment;

	@ManyToOne
    @Getter
    @Setter
	UserProfile user;

	@ManyToOne
    @Getter
    @Setter
	VotableEntity target;

}
