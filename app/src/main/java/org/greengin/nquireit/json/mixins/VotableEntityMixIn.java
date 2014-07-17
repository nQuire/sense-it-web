package org.greengin.nquireit.json.mixins;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.nquireit.entities.rating.Vote;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.moderation.ModerationStatus;
import org.greengin.nquireit.logic.rating.VoteCount;

public abstract class VotableEntityMixIn {

    @JsonView(Views.VotableNamedVotes.class) abstract Collection<Vote> getVotes();
	@JsonView(Views.VotableCount.class) abstract VoteCount getVoteCount();
	@JsonView(value = Views.VotableCountModeration.class) abstract ModerationStatus getModerationStatus();

}
