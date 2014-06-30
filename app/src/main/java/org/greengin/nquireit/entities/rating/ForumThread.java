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
}
