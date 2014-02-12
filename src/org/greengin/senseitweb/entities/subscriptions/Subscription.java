package org.greengin.senseitweb.entities.subscriptions;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;

@Entity
public class Subscription {

	
	@Id
    @Column (name = "OPENID_ID", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
   	Long id;
	
	@ManyToOne
	UserProfile user;
	
	@ManyToOne
	Project project;
	
	@Basic
	SubscriptionType type;


	public Long getId() {
		return id;
	}
	
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
