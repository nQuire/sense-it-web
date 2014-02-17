package org.greengin.senseitweb.json.mixins;


import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.users.UserProfile;

public abstract class VoteMixIn {
	@JsonView(Views.VotableIncludeNamedVotes.class) UserProfile user;
}
