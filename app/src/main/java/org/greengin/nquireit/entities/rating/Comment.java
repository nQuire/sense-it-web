package org.greengin.nquireit.entities.rating;


import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.users.UserProfile;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;


@Entity
public class Comment extends VotableEntity {

    @Basic
    @Getter
    @Setter
    Date date;

	@Basic
    @Getter
    @Setter
	String comment;

	@ManyToOne
    @Getter
    @Setter
    UserProfile user;

	@ManyToOne
    @Getter
    @Setter
	CommentThreadEntity target;

}
