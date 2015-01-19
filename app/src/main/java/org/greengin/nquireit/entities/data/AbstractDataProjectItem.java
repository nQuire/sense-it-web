package org.greengin.nquireit.entities.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.rating.CommentThreadEntity;
import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;

import java.util.Date;

@Entity
public abstract class AbstractDataProjectItem extends CommentThreadEntity {

    @Basic
    @Getter
    @Setter
    Date date;

	@ManyToOne
    @Getter
    @Setter
	protected DataCollectionActivity<?, ?> dataStore;

}
