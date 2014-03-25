package org.greengin.senseitweb.entities.voting;

import java.util.Collection;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.moderation.ModerationStatus;
import org.greengin.senseitweb.logic.voting.VoteCount;

@Entity
public abstract class VotableEntity extends AbstractEntity {
	
	@OneToMany(mappedBy = "target", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    @NonNull
    Collection<Vote> votes = new Vector<Vote>();
	
	@Basic
    @Getter
    @Setter
	ModerationStatus moderationStatus = ModerationStatus.UNMODERATED;


    @Getter
    @Setter
	private transient UserProfile selectedVoteAuthor = null;


	public VoteCount getVoteCount() {
		return new VoteCount(getVotes(), this.selectedVoteAuthor);
	}
}
