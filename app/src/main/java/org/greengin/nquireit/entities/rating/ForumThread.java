package org.greengin.nquireit.entities.rating;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.nquireit.entities.users.UserProfile;

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
    UserProfile author;

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
}
