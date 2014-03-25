package org.greengin.senseitweb.entities.projects;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.subscriptions.Subscription;

import javax.persistence.*;
import java.util.Collection;
import java.util.Vector;


@Entity
public class Project extends AbstractEntity {

    public Project() {
        this.activity = null;
    }

    @Basic
    @Getter
    @Setter
    String title;

    @Lob
    @Basic
    @Getter
    @Setter
    String description;

    @Basic
    @Getter
    @Setter
    ProjectType type;

    @Basic
    @Getter
    @Setter
    Boolean open = false;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @Getter
    @Setter
    AbstractActivity activity;

    @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    @NonNull
    Collection<Subscription> subscriptions = new Vector<Subscription>();


}
