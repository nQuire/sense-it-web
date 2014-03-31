package org.greengin.senseitweb.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.users.OpenIdEntity;
import org.greengin.senseitweb.entities.users.Role;

import java.util.Collection;

public abstract class RoleContextEntityMixIn {
    @JsonIgnore Collection<Role> roles;
}
