package org.greengin.senseitweb.json.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.greengin.senseitweb.entities.data.DataCollectionActivity;

public abstract class AbstractDataProjectItemMixIn {
	@JsonIgnore
    abstract DataCollectionActivity<?, ?> getDataStore();
}
