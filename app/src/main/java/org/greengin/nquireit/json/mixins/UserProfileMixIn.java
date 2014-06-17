package org.greengin.nquireit.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.nquireit.entities.users.Role;
import org.greengin.nquireit.json.Views;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;

public abstract class UserProfileMixIn {
    @JsonIgnore Collection<Role> roles;
    @JsonIgnore abstract Collection<? extends GrantedAuthority> getAuthorities();
    @JsonView(Views.UserName.class) abstract String getUsername();
    @JsonView(Views.UserProfileData.class) abstract boolean isPasswordSet();
    @JsonView(Views.UserProfileData.class) abstract HashMap<String, String> getMetadata();

    @JsonIgnore String password;
    @JsonIgnore abstract boolean isAccountNonExpired();
    @JsonIgnore abstract boolean isAccountNonLocked();
    @JsonIgnore abstract boolean isCredentialsNonExpired();
    @JsonIgnore abstract boolean isEnabled();
}
