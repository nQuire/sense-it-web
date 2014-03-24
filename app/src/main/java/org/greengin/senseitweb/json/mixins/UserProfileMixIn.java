package org.greengin.senseitweb.json.mixins;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.users.OpenIdEntity;

public abstract class UserProfileMixIn {
	@JsonView(Views.UserOpenIds.class) Collection<OpenIdEntity> openIds;
}
