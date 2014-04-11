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
    @Getter
    @Setter
    @NonNull
    String providerId;

    @Basic
    @Getter
    @Setter
    @NonNull
    String providerUserId;


    @Override
    @NonNull
    public String getAuthority() {
        return String.format("%s:%s", providerId, providerUserId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserGrantedAuthority)) {
            return false;
        }

        UserGrantedAuthority a = (UserGrantedAuthority) obj;
        return providerId.equals(a.providerId) && providerUserId.equals(a.providerUserId);
    }
}
