package org.greengin.senseitweb.entities.users;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * Created by evilfer on 4/10/14.
 */
@Entity
public class UserGrantedAuthority extends AbstractEntity implements GrantedAuthority {

    @Basic
    @Setter
    @NonNull
    String authority;


    @Override
    @NonNull
    public String getAuthority() {
        return authority;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof UserGrantedAuthority && getAuthority().equals(((UserGrantedAuthority) obj).getAuthority());
    }
}
