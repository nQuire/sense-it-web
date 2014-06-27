package org.greengin.nquireit.entities.rating;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Vector;

@Entity
public class ForumNode extends VotableEntity {

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    String description;

    @ManyToOne
    @Getter
    @Setter
    ForumNode parent = null;

    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    List<ForumNode> children = new Vector<ForumNode>();

    @OneToMany(mappedBy = "forum", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    List<ForumThread> threads = new Vector<ForumThread>();
}
