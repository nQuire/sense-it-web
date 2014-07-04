package org.greengin.nquireit.entities.projects;


import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.entities.activities.base.AbstractActivity;
import org.greengin.nquireit.entities.rating.CommentThreadEntity;
import org.greengin.nquireit.entities.users.UserProfile;

import javax.persistence.*;


@Entity
public class Project extends CommentThreadEntity {

    public Project() {
        this.activity = null;
    }

    @Basic
    @Getter
    @Setter
    String title;

    @ManyToOne
    @Getter
    @Setter
    UserProfile author;

    @Lob
    @Getter
    @Setter
    ProjectDescription description = new ProjectDescription();

    @Basic
    @Getter
    @Setter
    ProjectType type;

    @Basic
    @Getter
    @Setter
    Boolean open = false;

    @Basic
    @Getter
    @Setter
    boolean featured = false;


    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @Getter
    @Setter
    AbstractActivity activity;

}
