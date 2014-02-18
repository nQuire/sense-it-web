package org.greengin.senseitweb.entities.voting;

import java.util.Collection;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.voting.VoteCount;

@Entity
public abstract class VotableEntity extends AbstractEntity {
	
	@OneToMany(mappedBy = "target", orphanRemoval = true, cascade = CascadeType.REMOVE)
    Collection<Vote> votes = new Vector<Vote>();

	private transient UserProfile selectedVoteAuthor = null;
	
	public Collection<Vote> getVotes() {
		return votes;
	}

	public void setVotes(Collection<Vote> votes) {
		this.votes = votes;
	}
	
	public VoteCount getVoteCount() {
		return new VoteCount(getVotes(), this.selectedVoteAuthor);
	}
	
	public void selectVoteAuthor(UserProfile selectedVoteAuthor) {
		this.selectedVoteAuthor = selectedVoteAuthor;
	}
	
	
	
}
