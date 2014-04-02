package org.greengin.senseitweb.json.mixins;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.rating.VotableEntity;
import org.greengin.senseitweb.json.Views;

public abstract class VoteMixIn {
	@JsonView(Views.VotableNamedVotes.class) UserProfile user;
	@JsonIgnore abstract Project getProject();
	@JsonIgnore
    abstract VotableEntity getTarget();
}
