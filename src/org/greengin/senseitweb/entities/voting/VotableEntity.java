package org.greengin.senseitweb.entities.voting;

import java.util.Collection;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.greengin.senseitweb.entities.IdEntity;

@Entity
public abstract class VotableEntity extends IdEntity {
	
	@OneToMany(mappedBy = "target", orphanRemoval = true, cascade = CascadeType.REMOVE)
    Collection<Vote> votes = new Vector<Vote>();

	
	public Collection<Vote> getVotes() {
		return votes;
	}

	public void setVotes(Collection<Vote> votes) {
		this.votes = votes;
	}
}
