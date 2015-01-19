package org.greengin.nquireit.entities.rating;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.nquireit.logic.ContextBean;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Vector;

@Entity
public abstract class CommentThreadEntity extends VotableEntity {
    @Basic
    @Getter
    @Setter
    protected String title;

	@OneToMany(mappedBy = "target", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    @NonNull
    List<Comment> comments = new Vector<Comment>();

    public int getCommentCount() {
        return comments.size();
    }
}
