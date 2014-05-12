package org.greengin.nquireit.entities.data;


import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

import org.greengin.nquireit.entities.activities.base.AbstractActivity;

@Entity
@DiscriminatorColumn(name = "DATA_ACTIVITY_TYPE")
public abstract class DataCollectionActivity<E extends AbstractDataProjectItem, T extends AbstractDataProjectItem> extends AbstractActivity {
	
	
}
