package org.greengin.senseitweb.entities.projects;


import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.users.RoleContextEntity;

import javax.persistence.*;
import java.util.HashMap;



@Entity
public class Project extends RoleContextEntity {

    public Project() {
        this.activity = null;
    }

    @Basic
    @Getter
    @Setter
    String title;

    @Lob
    @Getter
    @Setter
    HashMap<String, String> description = new HashMap<String, String>();

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

}
