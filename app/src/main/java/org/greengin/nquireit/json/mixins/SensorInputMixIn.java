package org.greengin.nquireit.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;

public abstract class SensorInputMixIn extends AbstractDataProjectItemMixIn {
	@JsonIgnore abstract SenseItProfile getProfile();
}
