package org.greengin.senseitweb.entities.subscriptions;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;

@Entity
public class Subscription extends AbstractEntity {

	@ManyToOne
	UserProfile user;
	
	@ManyToOne
	Project project;
	
	@Basic
	SubscriptionType type;


	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public SubscriptionType getType() {
		return type;
	}

	public void setType(SubscriptionType type) {
		this.type = type;
	}
}
