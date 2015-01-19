package org.greengin.nquireit.logic.rating;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.rating.CommentThreadEntity;
import org.greengin.nquireit.utils.StringUtils;
import org.jsoup.Jsoup;

/**
 * Created by evilfer on 10/29/14.
 */
public class CommentFeedResponse {
    @Getter
    @Setter
    String comment;

    @Getter
    @Setter
    String threadTitle;

    @Getter
    @Setter
    Long threadId;

    public CommentFeedResponse(Comment comment) {
        CommentThreadEntity thread = comment.getTarget();
        this.comment = StringUtils.ellipsis(Jsoup.parse(comment.getComment()).text(), 20);
        this.threadTitle = thread.getTitle();
        this.threadId = thread.getId();
    }
}
