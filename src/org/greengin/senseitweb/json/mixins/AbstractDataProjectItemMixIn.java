package org.greengin.senseitweb.json.mixins;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.greengin.senseitweb.entities.data.DataCollectionActivity;

public abstract class AbstractDataProjectItemMixIn {
	@JsonIgnore abstract DataCollectionActivity<?, ?> getDataStore();
}
