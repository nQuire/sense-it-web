package org.greengin.senseitweb.entities.users;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.greengin.senseitweb.entities.voting.Vote;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by evilfer on 3/31/14.
 */
@Entity
public class RoleContextEntity extends AbstractEntity {

    @OneToMany(mappedBy = "context")
    @Getter
    @Setter
    @NonNull
    List<Role> roles = new Vector<Role>();


}
