package org.greengin.senseitweb.entities.users;

import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.hibernate.annotations.Loader;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

@Entity
public class UserProfile2 extends AbstractEntity implements UserDetails {

    @Basic
    @Setter
    String username;

    @Basic
    @Setter
    String password;

    @Lob
    Collection<? extends GrantedAuthority> authorities = new Vector<GrantedAuthority>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
}
