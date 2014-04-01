package org.greengin.senseitweb.entities.data;


import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

import org.greengin.senseitweb.entities.projects.AbstractActivity;

@Entity
@DiscriminatorColumn(name = "DATA_ACTIVITY_TYPE")
public abstract class DataCollectionActivity<E extends AbstractDataProjectItem, T extends AbstractDataProjectItem> extends AbstractActivity {
	
	
}
