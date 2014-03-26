package org.greengin.senseitweb.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.senseitweb.entities.data.DataCollectionActivity;

public abstract class AbstractDataProjectItemMixIn extends VotableEntityMixIn {
	@JsonIgnore  DataCollectionActivity<?, ?> dataStore;
}
