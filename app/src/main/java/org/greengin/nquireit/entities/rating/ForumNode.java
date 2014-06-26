package org.greengin.nquireit.entities.rating;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Vector;

@Entity
public class ForumNode extends CommentThreadEntity {

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    String description;

    @Getter
    @Setter
    boolean thread;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    @NonNull
    List<ForumNode> children = new Vector<ForumNode>();


}
