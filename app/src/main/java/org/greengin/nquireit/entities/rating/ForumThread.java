package org.greengin.nquireit.entities.rating;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Vector;

@Entity
public class ForumThread extends CommentThreadEntity {

    @Getter
    @Setter
    String title;

    @ManyToOne
    @Getter
    @Setter
    ForumNode forum;
}
