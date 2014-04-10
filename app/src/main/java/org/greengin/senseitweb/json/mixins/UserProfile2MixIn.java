package org.greengin.senseitweb.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.users.OpenIdEntity;
import org.greengin.senseitweb.entities.users.Role;
import org.greengin.senseitweb.json.Views;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public abstract class UserProfile2MixIn {
    @JsonIgnore Collection<Role> roles;
    @JsonIgnore String password;
    @JsonIgnore abstract Collection<? extends GrantedAuthority> getAuthorities();
    @JsonIgnore abstract boolean isAccountNonExpired();
    @JsonIgnore abstract boolean isAccountNonLocked();
    @JsonIgnore abstract boolean isCredentialsNonExpired();
    @JsonIgnore abstract boolean isEnabled();

}
