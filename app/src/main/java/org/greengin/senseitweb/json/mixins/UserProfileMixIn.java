package org.greengin.senseitweb.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.users.Role;
import org.greengin.senseitweb.json.Views;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public abstract class UserProfileMixIn {
    @JsonIgnore Collection<Role> roles;
    @JsonView(Views.UserOpenIds.class) abstract Collection<? extends GrantedAuthority> getAuthorities();

    @JsonIgnore String password;
    @JsonIgnore abstract boolean isAccountNonExpired();
    @JsonIgnore abstract boolean isAccountNonLocked();
    @JsonIgnore abstract boolean isCredentialsNonExpired();
    @JsonIgnore abstract boolean isEnabled();


}
