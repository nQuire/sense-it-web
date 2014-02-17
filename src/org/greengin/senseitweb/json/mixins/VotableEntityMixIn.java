package org.greengin.senseitweb.json.mixins;

import java.util.Collection;

import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.voting.Vote;

public abstract class VotableEntityMixIn {
	@JsonView(Views.VotableIncludeAnonymousVotes.class) Collection<Vote> votes;
}
