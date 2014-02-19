package org.greengin.senseitweb.json.mixins;

import java.util.Collection;

import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.voting.Vote;
import org.greengin.senseitweb.logic.moderation.ModerationStatus;
import org.greengin.senseitweb.logic.voting.VoteCount;

public abstract class VotableEntityMixIn {
	
	@JsonView(Views.VotableNamedVotes.class) Collection<Vote> votes;
	@JsonView(Views.VotableCount.class) abstract VoteCount getVoteCount();
	@JsonView(Views.VotableCountModeration.class) abstract ModerationStatus getModerationStatus();
}
