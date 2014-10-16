package org.greengin.nquireit.entities.users;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.utils.TimeValue;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

@Entity
public class UserProfile extends AbstractEntity implements UserDetails {

    @Basic
    @Setter
    String username;

    @Basic
    @Setter
    String password;

    @Basic
    @Setter
    @Getter
    String email;

    @Basic
    @Getter
    @Setter
    boolean emailConfirmed = false;

    @Basic
    @Setter
    @Getter
    boolean admin = false;

    @Basic
    @Setter
    @Getter
    String status = "";

    @Basic
    @Getter
    @Setter
    Date date;

    @Basic
    @Getter
    @Setter
    String image;

    @Lob
    @Getter
    @Setter
    HashMap<String, String> metadata = new HashMap<String, String>();

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isPasswordSet() {
        return password != null && password.length() > 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof UserProfile && getId().equals(((UserProfile) obj).getId());
    }

}
