package org.greengin.senseitweb.entities.users;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Vector;

@Entity
public class UserProfile extends AbstractEntity {

    @Basic
    @Getter
    @Setter
    String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    @NonNull
    Collection<OpenIdEntity> openIds = new Vector<OpenIdEntity>();

    @OneToMany(mappedBy = "user", orphanRemoval = false, cascade = CascadeType.DETACH)
    @Getter
    @Setter
    @NonNull
    Collection<Role> roles = new Vector<Role>();
}
