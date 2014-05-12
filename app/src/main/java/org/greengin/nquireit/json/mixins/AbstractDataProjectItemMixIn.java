package org.greengin.nquireit.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.nquireit.entities.data.DataCollectionActivity;

public abstract class AbstractDataProjectItemMixIn extends VotableEntityMixIn {
	@JsonIgnore  DataCollectionActivity<?, ?> dataStore;
}
