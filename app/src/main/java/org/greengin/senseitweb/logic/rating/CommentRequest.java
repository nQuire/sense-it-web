package org.greengin.senseitweb.logic.rating;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.rating.Comment;
import org.greengin.senseitweb.entities.rating.Vote;

public class CommentRequest {

    @Getter
    @Setter
    String comment;

    public void update(Comment comment) {
        comment.setComment(this.comment);
    }
}
