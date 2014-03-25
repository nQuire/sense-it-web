package org.greengin.senseitweb.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;

public abstract class SensorInputMixIn extends AbstractDataProjectItemMixIn {
	@JsonIgnore abstract SenseItProfile getProfile();
}
