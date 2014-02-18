package org.greengin.senseitweb.entities.voting;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.users.UserProfile;


@Entity
public class Vote extends AbstractEntity {	

	@Basic
	Long value;

	@Basic
	String comment;

	@ManyToOne
	UserProfile user;

	@ManyToOne
	VotableEntity target;

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public VotableEntity getTarget() {
		return target;
	}

	public void setTarget(VotableEntity target) {
		this.target = target;
	}


}
