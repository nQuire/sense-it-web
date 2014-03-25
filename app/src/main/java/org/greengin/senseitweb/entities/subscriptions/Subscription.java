package org.greengin.senseitweb.entities.subscriptions;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;

@Entity
public class Subscription extends AbstractEntity {

	@ManyToOne
    @Getter
    @Setter
    UserProfile user;
	
	@ManyToOne
    @Getter
    @Setter
	Project project;
	
	@Basic
    @Getter
    @Setter
	SubscriptionType type;

}
