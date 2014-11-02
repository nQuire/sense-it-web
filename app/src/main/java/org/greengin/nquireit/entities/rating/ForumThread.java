package org.greengin.nquireit.entities.rating;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.admin.ReportedContent;

import javax.persistence.*;
import java.util.List;
import java.util.Vector;

@Entity
public class ForumThread extends CommentThreadEntity {

    @Basic
    @Getter
    @Setter
    String title;

    @ManyToOne
    @Getter
    @Setter
    ForumNode forum;

    @OneToOne
    @Getter
    @Setter
    Comment lastComment = null;

    @OneToOne
    @Getter
    @Setter
    Comment firstComment = null;

    @Basic
    @Getter
    @Setter
    int commentCount = 0;

    public void updateLastPost() {
        lastComment = comments.get(comments.size() - 1);
        this.commentCount = comments.size();
        this.forum.updateLastPost(this);
    }

    @Override
    public String getReportedType(ContextBean context) {
        return "forumThread";
    }

    @Override
    public String getReportedPath(ContextBean context) {
        return String.format("/forum/thread/%d", getId());
    }

    @Override
    public void createReportedInfo(ReportedContent info, ContextBean context) {
        info.setAuthor(author);
        info.addInfo("OP", firstComment.getComment());
        info.addInfo("Comments", String.valueOf(commentCount));
        info.setPath(getReportedPath(context));
    }
}
