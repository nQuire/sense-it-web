package org.greengin.nquireit.json.mixins;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.rating.CommentThreadEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.json.Views;

public abstract class CommentMixIn {
	@JsonView(Views.User.class)
    UserProfile author;
	@JsonIgnore abstract Project getProject();
	@JsonIgnore
    abstract CommentThreadEntity getTarget();
}
