package org.greengin.nquireit.json.mixins;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.json.Views;

public abstract class VoteMixIn {
	@JsonView(Views.VotableNamedVotes.class)
    UserProfile user;
	@JsonIgnore abstract Project getProject();
	@JsonIgnore
    abstract VotableEntity getTarget();
}
