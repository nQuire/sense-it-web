package org.greengin.nquireit.logic.rating;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.rating.Comment;

import java.util.Date;

public class CommentRequest {

    @Getter
    @Setter
    String comment;

    public void update(Comment comment) {
        comment.setDate(new Date());
        comment.setComment(this.comment);
    }
}
