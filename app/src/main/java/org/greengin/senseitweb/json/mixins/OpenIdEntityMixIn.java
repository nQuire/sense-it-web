package org.greengin.senseitweb.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.senseitweb.entities.users.UserProfile;

public abstract class OpenIdEntityMixIn {
	@JsonIgnore
    abstract UserProfile getUserProfile();
}
