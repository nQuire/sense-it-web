package org.greengin.senseitweb.entities.rating;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.moderation.ModerationStatus;
import org.greengin.senseitweb.logic.voting.VoteCount;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

@Entity
public abstract class CommentThreadEntity extends VotableEntity {
	
	@OneToMany(mappedBy = "target", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    @NonNull
    List<Comment> comments = new Vector<Comment>();
}