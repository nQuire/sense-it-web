package org.greengin.senseitweb.entities.users;

import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.AbstractEntity;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
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

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter
    @NonNull
    List<UserGrantedAuthority> authorities = new Vector<UserGrantedAuthority>();

    @Override
    public List<? extends UserGrantedAuthority> getAuthorities() {
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


    public void addAuthority(@NonNull String providerId, @NonNull String providerUserId) {
        UserGrantedAuthority authority = new UserGrantedAuthority();
        authority.setProviderId(providerId);
        authority.setProviderUserId(providerUserId);
        if (!authorities.contains(authority)) {
            authorities.add(authority);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof UserProfile && getId().equals(((UserProfile) obj).getId());
    }

}
