package org.greengin.senseitweb.json.mixins;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;

public abstract class VoteMixIn {
	@JsonView(Views.VotableNamedVotes.class) UserProfile user;
	@JsonIgnore abstract Project getProject();
	@JsonIgnore abstract VotableEntity getTarget();
}
