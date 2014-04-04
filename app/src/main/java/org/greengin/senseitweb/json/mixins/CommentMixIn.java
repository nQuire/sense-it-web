package org.greengin.senseitweb.json.mixins;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.rating.CommentThreadEntity;
import org.greengin.senseitweb.entities.rating.VotableEntity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.json.Views;

public abstract class CommentMixIn {
	@JsonView(Views.User.class) UserProfile user;
	@JsonIgnore abstract Project getProject();
	@JsonIgnore
    abstract CommentThreadEntity getTarget();
}
